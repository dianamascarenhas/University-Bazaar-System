package com.example.shreevidya.ubs_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;


public class Register extends AppCompatActivity {

    EditText mavidinput,firstname,lastname,phoneno;
    EditText password,repassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button Submit = (Button)findViewById(R.id.submitbutton);
        Button Reset = (Button) findViewById(R.id.resetbutton);

        mavidinput = (EditText)findViewById(R.id.mavsidinput);
        password = (EditText)findViewById(R.id.passwordinput);
        repassword = (EditText)findViewById(R.id.repasswordinput);
        firstname = (EditText)findViewById(R.id.firstnameinput);
        lastname = (EditText)findViewById(R.id.lastnameinput);
        phoneno = (EditText)findViewById(R.id.phonenoinput);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mavidinput.getText().toString().isEmpty() || password.getText().toString().isEmpty() || repassword.getText().toString().isEmpty() || lastname.getText().toString().isEmpty() || firstname.getText().toString().isEmpty() || phoneno.getText().toString().isEmpty() ){

                    Toast.makeText(Register.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();


                }


                else if (!password.getText().toString().equals(repassword.getText().toString()))
                {

                    Toast.makeText(Register.this, "Password do not match", Toast.LENGTH_LONG).show();
                    password.setText("");
                    repassword.setText("");
                }

                else if (password.getText().toString().length() < 10)
                {
                    Toast.makeText(Register.this, "Password must be atleast 10 characters", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(Register.this, Register.class));
                    password.setText("");
                    repassword.setText("");
                }

                else if(!mavidinput.getText().toString().endsWith("@mavs.uta.edu"))
                {
                    Toast.makeText(Register.this, "Provide valid email ID", Toast.LENGTH_LONG).show();
                    mavidinput.setText("");
                }




                else if(phoneno.getText().toString().length() != 10)
                {
                    Toast.makeText(Register.this, "Provide valid phone no", Toast.LENGTH_LONG).show();
                    phoneno.setText("");
                }
                else {


                    new Register.AsyncRegister().execute(firstname.getText().toString(),lastname.getText().toString(),mavidinput.getText().toString(), password.getText().toString(), repassword.getText().toString(), phoneno.getText().toString());

                }
            }
        });


        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname.setText("");
                lastname.setText("");
                mavidinput.setText("");
                phoneno.setText("");
                password.setText("");
                repassword.setText("");


            }
        });




    }



    private class AsyncRegister extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Register.this);
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
                url = new URL("https://utauniversitybazaar.000webhostapp.com/api/UserRegisterSubmit.php");

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
                        .appendQueryParameter("FirstName", params[0])
                        .appendQueryParameter("LastName", params[1])
                .appendQueryParameter("MavsID", params[2])
                .appendQueryParameter("PASSWORD", params[3])
                .appendQueryParameter("Phone_No", params[4]);
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

            if(result.equalsIgnoreCase("Registration Successfully"))
            {

                startActivity(new Intent(Register.this, Login.class));

                finish();


            }
            Toast.makeText(Register.this, ""+result, Toast.LENGTH_LONG).show();
        }

    }

}
