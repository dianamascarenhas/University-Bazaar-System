package com.example.shreevidya.ubs_2;

import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;
import java.net.HttpURLConnection;
import java.net.URL;
import 	java.util.ArrayList;
import java.util.List;

public class SendEmailActivity extends Activity {

    InputStream is = null;
    String line = null;
    String result = null;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/Message_group.php?Name=";
    String[] data;
    Button buttonSend;
    EditText textTo;
    EditText textSubject;
    EditText textMessage;
    String Name;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Intent intent = new Intent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        Name = extras.getString("MY_kEY", "");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        buttonSend = findViewById(R.id.buttonSend);
        textTo = findViewById(R.id.editTextTo);
        textSubject = findViewById(R.id.editTextSubject);
        textMessage = findViewById(R.id.editTextMessage);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        List<String> jadu = new ArrayList<String>();;

//        for (int i = 0; i < 4; i++) {
//            jadu.add("inhgdra@mail.com" + i) ;
//        }
            try {
                URL url = new URL(address + Name);
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
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                data = new String[ja.length()];


                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    data[i] = jo.getString("MavsID");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        Gson gson = new Gson();
        String j = gson.toJson(data);
        textTo.setText(j);

        buttonSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String to = textTo.getText().toString();
                String subject = textSubject.getText().toString();
                String message = textMessage.getText().toString();

                if (message.length() > 1) {
                    Intent email = new Intent(Intent.ACTION_SENDTO);
                    email.setData(Uri.parse("mailto:"));
                    to = to.substring(1, to.length()-1);
                    String uriText = "mailto:" + to +
                            "?subject=" + subject +
                            "&body=" + message;
                    Uri uri = Uri.parse(uriText);
                    email.setData(uri);
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                } else {
                    Toast.makeText(SendEmailActivity.this, "Please enter a message", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}