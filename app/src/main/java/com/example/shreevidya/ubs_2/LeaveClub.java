package com.example.shreevidya.ubs_2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class LeaveClub extends AppCompatActivity {


    ListView lv;
    ArrayAdapter<String> adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/displayclub_mavid.php?MavsID=";
    InputStream is = null;
    String line = null;
    String result = null;
    String mavid;
    String[] data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_club);


        //Button Join = (Button)findViewById(R.id.joinbutton);
        lv = (ListView) findViewById(R.id.listView1);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mavid = saved_values.getString("usernamekey", "");


        getData(mavid);
        if (data != null)
        {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
            lv.setAdapter(adapter);
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                final String item = ((TextView) view).getText().toString();

                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(LeaveClub.this)
                        .setTitle("Delete Club")
                        .setMessage("Do you really want to Delete "+ item + " club?")
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
//                                HttpURLConnection con = null;
//                                Toast.makeText(ViewClub.this, item, Toast.LENGTH_SHORT).show();
                                try {
                                    URL url = new URL("https://utauniversitybazaar.000webhostapp.com/api/leave_club.php?MavsID=" + mavid + "&Name=" + item);

                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setRequestMethod("GET");
                                    is = new BufferedInputStream(con.getInputStream());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {

                                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                                    StringBuilder sb = new StringBuilder();

                                    while ((line = br.readLine()) != null) {
                                        sb.append(line + "\n");
                                    }
                                    is.close();

                                    result = sb.toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });


    }


    private void getData(String mavid) {
        try {
            URL url = new URL(address + mavid);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //PARSE JSON
        try {
            if (result.equalsIgnoreCase("You have not Joined any Club yet!")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            } else {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                data = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    data[i] = jo.getString("Name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}