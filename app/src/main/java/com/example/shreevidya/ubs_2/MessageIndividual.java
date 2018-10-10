package com.example.shreevidya.ubs_2;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class MessageIndividual extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<String> adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/message_individual.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String mavid;
    String FirstName;
    String[] Firstnamedata,IdData,LastNameData,MavIdData,CombineNameData;


//    Button logoutBtn;
//
//    public void init() {
//        logoutBtn = (Button) findViewById(R.id.logout);
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MessageIndividual.this,Login.class) );
//
//            }
//        });
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_individual);


        //Button Join = (Button)findViewById(R.id.joinbutton);
        lv = (ListView) findViewById(R.id.listView1);



        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        getData();

        for(int i = 0; i < Firstnamedata.length ; i++)
        {
            CombineNameData[i] = Firstnamedata[i] + " " + LastNameData[i];
        }


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CombineNameData);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                mavid =  saved_values.getString("usernamekey" , "");

                // Toast.makeText(getApplicationContext() , Firstnamedata[position] +  MavIdData[position] + LastNameData[position] + mavid , Toast.LENGTH_LONG ).show();


                Intent IntentData = new Intent(MessageIndividual.this,MessageIndividualSend.class);
                IntentData.putExtra("ToMavdId" ,  MavIdData[position]);
                IntentData.putExtra("ToFirstName" , Firstnamedata[position]);
                IntentData.putExtra("ToLastName" ,  LastNameData[position]);
                IntentData.putExtra("FromMavdId" ,  mavid);
                startActivity(IntentData);



            }
        });
//        init();

    }


    private void getData()
    {
        try{
            URL url=new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
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

            Firstnamedata = new String[ja.length()];
            LastNameData = new String[ja.length()];
            IdData = new String[ja.length()];
            MavIdData = new String[ja.length()];
            CombineNameData = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                Firstnamedata[i] = jo.getString("FirstName");
                LastNameData[i] = jo.getString("LastName");
                IdData[i] = jo.getString("ID");
                MavIdData[i] = jo.getString("MavsID");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}














