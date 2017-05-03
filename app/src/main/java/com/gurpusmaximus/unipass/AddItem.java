package com.gurpusmaximus.unipass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItem extends AppCompatActivity {

    private EditText account, username, password;
    private String accountString, usernameString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        Button submit = (Button)findViewById(R.id.submit_button);
        account = (EditText)findViewById(R.id.account_name);
        username = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.pass_word);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountString = account.getText().toString();
                usernameString = username.getText().toString();
                passwordString = password.getText().toString();

                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameString);
                bundle.putString("password", passwordString);
                bundle.putString("account", accountString);
                returnIntent.putExtras(bundle);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
