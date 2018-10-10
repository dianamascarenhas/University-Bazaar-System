package com.example.shreevidya.ubs_2;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Homepage extends AppCompatActivity {

    Button announcementBtn, clubBtn, msgBtn, merchandiseBtn, logoutBtn,inboxbtn;

    public void init() {
        logoutBtn = (Button) findViewById(R.id.logout);
        inboxbtn = (Button)findViewById(R.id.inboxbtn);
        announcementBtn = (Button) findViewById(R.id.announcememt);
        //clubBtn=(Button)findViewById(R.id.club);
        // msgBtn = (Button) findViewById(R.id.msg);
        //merchandiseBtn=(Button)findViewById(R.id.merchandise);

        announcementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, Announcement.class);
                startActivity(i);
            }
        });

        //Inbox Button
        inboxbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this,inbox.class) );
            }
        });

      /*  msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Homepage.this, MessageIndividual.class);
                startActivity(in);
            }
        });*/

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this,Login.class) );
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        //Creating Spinner, and reflecting items from resources, into spinner
        //CREATED A SPINNER
        Spinner mySpinner = (Spinner) findViewById(R.id.s1);
        //NOW ADD DATA TO SPINNER BY GOING TO /RES/VALUES/STRINGS.XML AND NAME IT "C"
// ADAPTER IS A CONTAINER THAT WILL HOLD VALUES AND INTEGRATE THEM WITH THE SPINNER
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Homepage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.C));  // NOW WE HAVE CREATED AN ADAPTER

        // STATE THAT ADAPTER WILL HAVE A DROP DOWN LIST
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //SET SPINNER TO ADAPTER, AND DEPLOY APP, WE WILL GET A DROP DOWN BUTTON
        mySpinner.setAdapter(myAdapter);

        // This part of code, navigates to another activity based on the option selected
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    startActivity(new Intent(Homepage.this, CreateClub.class));

                } else if (i == 2) {
                    startActivity(new Intent(Homepage.this, JoinClub.class));
                } else if (i == 3) {
                    startActivity(new Intent(Homepage.this, ViewClub.class));
                } else if (i == 4) {
                    startActivity(new Intent(Homepage.this, LeaveClub.class));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Spinner mySpinner1 = (Spinner) findViewById(R.id.s2);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(Homepage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.M));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner1.setAdapter(myAdapter1);

        mySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    Intent merchant =new Intent(Homepage.this, BuyList.class);
                    merchant.putExtra("MERCHANDISE", "Sell");
                    startActivity(merchant);
                } else if (i == 2) {
                    startActivity(new Intent(Homepage.this, Sell.class));
                } else if (i == 3) {
                    Intent merchant =new Intent(Homepage.this, BuyList.class);
                    merchant.putExtra("MERCHANDISE", "Lend");
                    startActivity(merchant);
                } else if (i == 4) {
                    Intent merchant =new Intent(Homepage.this, BuyList.class);
                    merchant.putExtra("MERCHANDISE", "Exchange");
                    startActivity(merchant);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner mySpinner2 = (Spinner) findViewById(R.id.s4);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(Homepage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Message));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        mySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    startActivity(new Intent(Homepage.this, MessageIndividual.class));

                } else if (i == 2) {
                    startActivity(new Intent(Homepage.this, MessagetoMembers.class));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Spinner mySpinner3 = (Spinner) findViewById(R.id.s3);
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<String>(Homepage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Announce));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner3.setAdapter(myAdapter3);

        mySpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    startActivity(new Intent(Homepage.this, PostAnnouncement.class));

                } else if (i == 2) {
                    startActivity(new Intent(Homepage.this, ViewAnnouncement.class));
                }

                else if (i == 3) {

                    startActivity(new Intent(Homepage.this, DeleteAnnouncement.class));
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        init();

    }
}