package com.gurpusmaximus.unipass;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CredentialsActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Toolbar toolbar;
    PopupWindow popUpWindow;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout mainLayout;
    LinearLayout containerLayout;
    TextView tvMsg;
    EditText editText;
    boolean popupWasDisplaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        containerLayout = new LinearLayout(this);
        mainLayout = new LinearLayout(this);
        popUpWindow = new PopupWindow(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                popUpWindow.update(0, 0, 1000, 900);
            }
        });

        tvMsg = new TextView(this);
        editText = new EditText(this);

        tvMsg.setText("Enter your account credentials. \n\n Username: \n\n");

        layoutParams = new LinearLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
        containerLayout.setBackground(new ColorDrawable(android.graphics.Color.rgb(125, 169, 10)));
        containerLayout.addView(tvMsg, layoutParams);
        containerLayout.addView(editText, layoutParams);
        popUpWindow.setContentView(containerLayout);
        popUpWindow.setFocusable(true);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && popupWasDisplaying) {
            popUpWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            popUpWindow.update(0, 0, 1500, 700);
            popupWasDisplaying = false;
        }
        else {
            popUpWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            popUpWindow.update(0, 0, 1000, 900);
            popupWasDisplaying = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (popUpWindow.isShowing()) {
            popupWasDisplaying = true;
        }

    }
}
