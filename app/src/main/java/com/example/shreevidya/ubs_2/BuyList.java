package com.example.shreevidya.ubs_2;

/**
 * Created by Diana on 04/27/2018.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

public class BuyList extends AppCompatActivity {


    ListView lv;
    ArrayAdapter<String> adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/Buy.php?MERCHANDISE=";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data = null;
    String[] email;
    String MERCHANDISE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buylist);

        Intent intent = new Intent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MERCHANDISE = extras.getString("MERCHANDISE", "");
        }

        //Button Join = (Button)findViewById(R.id.joinbutton);
        lv = (ListView) findViewById(R.id.listView1);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        getData();
        if (data != null)
        {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
            lv.setAdapter(adapter);
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                String item = ((TextView) view).getText().toString();
                String Email = email[position];;

                Intent i = new Intent(BuyList.this, Buy.class);
                i.putExtra("Product", item);
                i.putExtra("OwnerEmail", Email);
                i.putExtra("MERCHANDISE", MERCHANDISE);
                startActivity(i);


            }
        });


    }


    private void getData() {
        try {
            URL url = new URL(address + MERCHANDISE);
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
            if (result.equalsIgnoreCase("No item available to buy!")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            } else {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                data = new String[ja.length()];
                email = new String[ja.length()];
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    data[i] = jo.getString("NAME");
                    email[i] = jo.getString("EMAIL");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
