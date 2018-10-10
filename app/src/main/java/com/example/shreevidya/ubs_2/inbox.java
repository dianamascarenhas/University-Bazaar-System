package com.example.shreevidya.ubs_2;

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
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class inbox extends AppCompatActivity {

    ListView lv;
    SimpleAdapter adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/View_message_individual.php?FromMavsID=";
    InputStream is = null;
    String line = null;
    String result = null;
    String mavid;
    String[] Messagedata,FromMavIddata = null;

    Button logoutBtn;

    public void init() {
        logoutBtn = (Button) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(inbox.this,Login.class) );

            }
        });

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        //Button Join = (Button)findViewById(R.id.joinbutton);
        lv = (ListView) findViewById(R.id.listView1);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mavid = saved_values.getString("usernamekey", "");

        getMessageData(mavid);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for (int i = 0 ; i < FromMavIddata.length ; i++)
        {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", "From : " + FromMavIddata[i]);
            datum.put("message", "Message : " + Messagedata[i] );
            data.add(datum);
        }

        if (data != null) {
            adapter = new SimpleAdapter(this, data ,android.R.layout.simple_list_item_2, new String[] {"title", "message"} , new int[] {android.R.id.text1,
                    android.R.id.text2});
            lv.setAdapter(adapter);
        }

        init();

    }



    private void getMessageData(String mavid) {
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
            if (result.equalsIgnoreCase("You have not received any Messages!")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            } else {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                FromMavIddata = new String[ja.length()];
                Messagedata = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    FromMavIddata[i] = jo.getString("FromMavId");
                    Messagedata[i] = jo.getString("Message");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
