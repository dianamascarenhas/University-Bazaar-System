package com.example.shreevidya.ubs_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class MessageIndividualSend extends AppCompatActivity {


    String Firstnamedata,FromMavIdData,LastNameData,ToMavIdData;

    Button logoutBtn;

    public void init() {
        logoutBtn = (Button) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageIndividualSend.this,Login.class) );

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_individual_send);


        TextView Firstname = (TextView)findViewById(R.id.firstnametextview);
        TextView LastName = (TextView)findViewById(R.id.lastnametextview);
        TextView MavId = (TextView)findViewById(R.id.mavidtextview);
        final EditText messageedittext = (EditText)findViewById(R.id.messageedittext);


        Intent intentExtras = getIntent();
        Bundle extrabundle = intentExtras.getExtras();

        Button Submit = (Button)findViewById(R.id.send);

        if(!extrabundle.isEmpty())
        {
            Firstnamedata = extrabundle.getString("ToFirstName");
            FromMavIdData = extrabundle.getString("FromMavdId");
            LastNameData = extrabundle.getString("ToLastName");
            ToMavIdData = extrabundle.getString("ToMavdId");

                     Firstname.setText("FirstName : "+ Firstnamedata);
                     LastName.setText("LastName : "+ LastNameData);
                     MavId.setText("MavId : "+ ToMavIdData);

        }

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!messageedittext.getText().toString().isEmpty())
                new AsyncRegister().execute(ToMavIdData,FromMavIdData,messageedittext.getText().toString());
                else
                    Toast.makeText(MessageIndividualSend.this, "Type Something in the Message Field!!", Toast.LENGTH_LONG).show();
            }
        });

        init();

    }


    private class AsyncRegister extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(MessageIndividualSend.this);
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
                url = new URL("https://utauniversitybazaar.000webhostapp.com/api/message_indiviusal_send.php");

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
                        .appendQueryParameter("ToMavsID", params[0])
                        .appendQueryParameter("FromMavsID", params[1])
                        .appendQueryParameter("msg", params[2]);
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

                    return("Check Again");
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

            if(result.equalsIgnoreCase("Message Sent"))
            {

                startActivity(new Intent(MessageIndividualSend.this, MessageIndividual.class));

                finish();


            }
            Toast.makeText(MessageIndividualSend.this, "Message Sent", Toast.LENGTH_LONG).show();
        }

    }


}
