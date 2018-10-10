package com.example.shreevidya.ubs_2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.password;


public class Buy extends AppCompatActivity {


    //ListView lv;
    TextView nameTextView, descTextView, priceTextView, dateTextView, textView2;
    ImageView pathImageView;
    Button buyBtn;
    ArrayAdapter<String> adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/BuyOneItem.php?NAME=";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] dates;
    String[] data;
    String[] prices;
    String[] descriptions;
    String[] imagePaths;
    String[] email;
    String[] ltill;
    String[] firstName;
    String[] lastName;
    String Product_Name, Product_Owner, MERCHANDISE, mavid;
    File fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = new Intent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Product_Name = extras.getString("Product", "");
            Product_Owner = extras.getString("OwnerEmail", "");
            MERCHANDISE = extras.getString("MERCHANDISE", "");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        //Button Join = (Button)findViewById(R.id.joinbutton);
        //lv = (ListView) findViewById(R.id.listView1);
        nameTextView = (TextView) findViewById(R.id.textView6);
        textView2 = (TextView) findViewById(R.id.textView2);
        descTextView = (TextView) findViewById(R.id.textView7);
        priceTextView = (TextView) findViewById(R.id.price);
        dateTextView = (TextView) findViewById(R.id.date1);
        pathImageView = (ImageView) findViewById(R.id.imageView1) ;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());


        getData();

        buyBtn = (Button) findViewById(R.id.button);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {

                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                mavid = saved_values.getString("usernamekey", "");


                Intent IntentData = new Intent(Buy.this,MessageIndividualSend.class);
                IntentData.putExtra("ToMavdId" ,  email[0]);
                IntentData.putExtra("ToFirstName" , firstName[0]);
                IntentData.putExtra("ToLastName" ,  lastName[0]);
                IntentData.putExtra("FromMavdId" ,  mavid);
                startActivity(IntentData);
            }
        });

//        File imgFile = new  File(imagePaths[0]);
//
//        if(imgFile.exists()){
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//           // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
//            pathImageView = (ImageView) findViewById(R.id.imageView1) ;
//            //myImage.setImageBitmap(myBitmap);
//            pathImageView.setImageBitmap(myBitmap);
//
//        }

                try {
                    URL url = new URL("https://utauniversitybazaar.000webhostapp.com/api/Book.jpg");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    pathImageView = (ImageView) findViewById(R.id.imageView1) ;
                    pathImageView.setImageBitmap(myBitmap);
                }catch (Exception e) {
                    e.printStackTrace();
                }
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data, descriptions);
        nameTextView.setText(data[0]);
        descTextView.setText(descriptions[0]);
        if ("Sell".equals(MERCHANDISE)) {
            priceTextView.setText("$" + prices[0]);
        }else if("Lend".equals(MERCHANDISE)){
            priceTextView.setText("Available till " + ltill[0]);
            textView2.setText("Item for Lend!");
        }else if("Exchange".equals(MERCHANDISE)){
            priceTextView.setText("Exchange with- " + ltill[0]);
            textView2.setText("Item for Exchange!");
        }
        dateTextView.setText("Posted on " + dates[0]);


//        nameTextView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//
//
//                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//            }
//        });

    ProgressDialog pdLoading = new ProgressDialog(Buy.this);
    HttpURLConnection conn;
    URL url = null;

//
    }
//

    private void getData() {
        try {
            URL url = new URL(address+Product_Name+"&EMAIL="+Product_Owner+"&MERCHANDISE="+MERCHANDISE);
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
            JSONObject je = null;

            data = new String[ja.length()];//ja[1] , ja[2]
            descriptions = new String[ja.length()];
            prices = new String[ja.length()];
            dates = new String[ja.length()];
            imagePaths = new String[ja.length()];
            email = new String[ja.length()];
            ltill = new String[ja.length()];
            firstName = new String[ja.length()];
            lastName = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                je = ja.getJSONObject(i+1);
                data[i] = jo.getString("NAME");
                descriptions[i] = jo.getString("DESCRIPTION");
                prices[i] = jo.getString("PRICE");
                dates[i] = jo.getString("POST_DATE");
                imagePaths[i] = jo.getString("IMAGE");
                email[i] = jo.getString("EMAIL");
                ltill[i] = jo.getString("LTILL");
                firstName[i] = je.getString("FirstName");
                lastName[i] = je.getString("LastName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}











