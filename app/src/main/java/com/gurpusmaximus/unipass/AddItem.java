package com.gurpusmaximus.unipass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static java.lang.System.out;

public class AddItem extends AppCompatActivity {

    private EditText account, username, password;
    public static String accountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        /*if(savedInstanceState != null){
            username = (EditText) savedInstanceState.get(String.valueOf("username"));
            account = (EditText) savedInstanceState.get(String.valueOf("account"));
        }*/

        Button submit = (Button)findViewById(R.id.submit_button);
        account = (EditText)findViewById(R.id.account_name);
        username = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.pass_word);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountString = account.getText().toString();
                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("username", String.valueOf(username));
                bundle.putString("password", String.valueOf(password));
                returnIntent.putExtra("account", accountString);
                returnIntent.putExtras(bundle);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
    /*@Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("username", String.valueOf(username));
        outState.putString("account", String.valueOf(account));

        super.onSaveInstanceState(outState);
    }*/
}
