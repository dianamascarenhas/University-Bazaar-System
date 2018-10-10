package com.example.shreevidya.ubs_2;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Exchange extends AppCompatActivity {

    TextView nameTextView, descTextView, priceTextView, dateTextView;
    ImageView pathImageView;
    ArrayAdapter<String> adapter;
    String address = "https://utauniversitybazaar.000webhostapp.com/api/Exchange.php";
    InputStream is = null;
    String line = null;
    String result = null;
    String[] dates;
    String[] data;
//    String[] prices;
    String[] descriptions;
    String[] imagePaths;
    File fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        nameTextView = (TextView) findViewById(R.id.textView6);
        descTextView = (TextView) findViewById(R.id.textView7);
//        priceTextView = (TextView) findViewById(R.id.price);
        dateTextView = (TextView) findViewById(R.id.date1);
        pathImageView = (ImageView) findViewById(R.id.imageView1) ;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());


        getData();

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
//        priceTextView.setText("$" + prices[0]);
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

        ProgressDialog pdLoading = new ProgressDialog(Exchange.this);
        HttpURLConnection conn;
        URL url = null;

//
    }
//

    private void getData() {
        try {
            URL url = new URL(address);
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
            descriptions = new String[ja.length()];
//            prices = new String[ja.length()];
            dates = new String[ja.length()];
            imagePaths = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                data[i] = jo.getString("NAME");
                descriptions[i] = jo.getString("DESCRIPTION");
//                prices[i] = jo.getString("PRICE");
                dates[i] = jo.getString("POST_DATE");
                imagePaths[i] = jo.getString("IMAGE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
