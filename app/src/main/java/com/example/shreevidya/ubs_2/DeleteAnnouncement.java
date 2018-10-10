package com.example.shreevidya.ubs_2;

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
import android.widget.Button;
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


public class DeleteAnnouncement extends AppCompatActivity {


    ListView lv;
    ArrayAdapter<String> adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/view_announcement.php?MavsID=";
    InputStream is = null;
    String line = null;
    String result = null;
    String mavid;
    String[] data;
    String[] description;

    Button logoutBtn;

    public void init() {
        logoutBtn = (Button) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeleteAnnouncement.this, Login.class));
            }


        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcement);

        lv = (ListView) findViewById(R.id.listView2);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mavid = saved_values.getString("usernamekey", "");


        getData();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                final String item = ((TextView) view).getText().toString();
                String Desc = description[position];

              new AlertDialog.Builder(DeleteAnnouncement.this)
                        .setTitle("Delete " + item + " Announcement?")
                        .setMessage(Desc)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    URL url = new URL("https://utauniversitybazaar.000webhostapp.com/api/deleteAnnouncement.php?TITLE=" + item);

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
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();


            }


        });
    }




    private void getData() {
        try {
            URL url = new URL(address+mavid);
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
            description = new String[ja.length()];
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                data[i] = jo.getString("TITLE");
                description[i] = jo.getString("DESCRIPTION");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
