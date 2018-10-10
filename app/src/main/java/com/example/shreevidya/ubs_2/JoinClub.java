package com.example.shreevidya.ubs_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.password;


public class JoinClub extends AppCompatActivity {


    ListView lv;
    ArrayAdapter<String> adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/display_club.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String mavid ;
    String[] data;
    String[] description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_club);

        //Button Join = (Button)findViewById(R.id.joinbutton);
        lv = (ListView) findViewById(R.id.listView1);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());


        getData();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                mavid =  saved_values.getString("usernamekey" , "");


                //  EditText MavsID,Name;
                //  MavsID = (EditText)findViewById(R.id.mavsidinput);
                //  Name = (EditText)findViewById(R.id.name_create);
                final String item = ((TextView) view).getText().toString();
                String Desc = description[position];

                new AlertDialog.Builder(JoinClub.this)
                        .setTitle("Join " + item + " Club?")
                        .setMessage(Desc)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                new JoinClub.AsyncJoinClub().execute(mavid, item);
                            }
                            })
                                    .setNegativeButton(android.R.string.no, null).show();





            }
        });


    }

    private class AsyncJoinClub extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(JoinClub.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("https://utauniversitybazaar.000webhostapp.com/api/Joined_Club.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("MavsID", params[0])
                        .appendQueryParameter("clubName", params[1]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("Check Again"+response_code

                    );
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            if(result.equalsIgnoreCase("Club joined Successfully"))
            {

                Toast.makeText(getApplicationContext() , result , Toast.LENGTH_LONG).show();


            }else if(result.equalsIgnoreCase("You are Already Part of this Club"))
            {

                Toast.makeText(getApplicationContext() , result , Toast.LENGTH_LONG).show();


            }else if(result.equalsIgnoreCase("Something went wrong"))
            {

                Toast.makeText(getApplicationContext() , result , Toast.LENGTH_LONG).show();

            }





        }

    }






    private void getData()
    {
        try{
            URL url=new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line=br.readLine())!= null)
            {
                sb.append(line+"\n");
            }
            is.close();

            result = sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //PARSE JSON
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            data = new String[ja.length()];
            description = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                data[i] = jo.getString("Name");
                description[i] = jo.getString("Description");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}










