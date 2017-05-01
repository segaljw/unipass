package com.gurpusmaximus.unipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class CredentialsActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Toolbar toolbar;
    static final int REQUST_CODE = 1;
    ListView listOfAccounts;
    Bundle stuff;
    ArrayList<String> accounts = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public static String accountString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        adapter=new ArrayAdapter<>(this,
                R.layout.activity_listview,
                accounts);
        listOfAccounts = (ListView)findViewById(R.id.account_list);
        listOfAccounts.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Add Item activity for adding account, username, password.
                Intent openPopUp = new Intent(view.getContext(), AddItem.class);
                startActivityForResult(openPopUp, REQUST_CODE);
            }
        });

    }

    // Receive Account string for use in ListView and bundle of username and password
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //Bundle receive;
            //receive = data.getExtras();
            //stuff = receive;
            Log.i("something", "this method is executing");
            accountString = data.getStringExtra("account");
            accounts.add(data.getStringExtra("account"));
            adapter.notifyDataSetChanged();
        }

    }



    @Override
    public void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_credentials, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
