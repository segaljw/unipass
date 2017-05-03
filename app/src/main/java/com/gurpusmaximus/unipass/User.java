package com.gurpusmaximus.unipass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class User extends AppCompatActivity {

    String accountName;
    ListView listOfUsers;
    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bundle extras = getIntent().getExtras();
        accountName = extras.getString("account");
        adapter=new ArrayAdapter<>(this,
                R.layout.activity_listview,
                users);
        listOfUsers = (ListView)findViewById(R.id.user_list);
        listOfUsers.setAdapter(adapter);

        String yourFilePath = getApplicationContext().getFilesDir() + "/" + accountName;


        File yourFile = new File( yourFilePath );
        try{
            BufferedReader something = new BufferedReader(new FileReader(yourFile));
            String line;
            while((line = something.readLine()) != null){
                users.add(line);
                adapter.notifyDataSetChanged();
            }
            something.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
