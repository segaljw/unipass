package com.gurpusmaximus.unipass;

import android.graphics.Color;
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
    TextView tvMsg1, tvMsg2, tvMsg3;
    EditText editText1, editText2, editText3;
    boolean popupWasDisplaying = true;


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
        findViewById(R.id.full_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full_view).setBackground(new ColorDrawable(Color.BLUE));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpWindow.showAtLocation(findViewById(R.id.text_view), Gravity.CENTER, 0, 0);
                popUpWindow.update(0, 0, 1000, 1000);
                popupWasDisplaying = true;
            }
        });


        tvMsg1 = new TextView(this);
        tvMsg1.append("Enter your account credentials.\n");
        tvMsg1.append("   Account For:");
        tvMsg1.setWidth(1000);
        tvMsg1.setHeight(150);

        tvMsg2 = new TextView(this);
        tvMsg2.append("   Username:");
        tvMsg2.setWidth(1000);
        tvMsg2.setHeight(150);

        tvMsg3 = new TextView(this);
        tvMsg3.append("   Password:");
        tvMsg3.setWidth(1000);
        tvMsg3.setHeight(150);

        editText1 = new EditText(this);
        //editText1.setBackground(new ColorDrawable(Color.WHITE));

        editText2 = new EditText(this);
        //editText2.setBackground(new ColorDrawable(Color.WHITE));

        editText3 = new EditText(this);
        //editText3.setBackground(new ColorDrawable(Color.WHITE));

        layoutParams = new LinearLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setBackground(new ColorDrawable(android.graphics.Color.rgb(125, 169, 10)));
        containerLayout.addView(tvMsg1, layoutParams);
        containerLayout.addView(editText1, layoutParams);
        containerLayout.addView(tvMsg2, layoutParams);
        containerLayout.addView(editText2, layoutParams);
        containerLayout.addView(tvMsg3, layoutParams);
        containerLayout.addView(editText3, layoutParams);

        popUpWindow.setContentView(containerLayout);
        popUpWindow.setFocusable(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (popupWasDisplaying) {
            //popUpWindow.showAtLocation(findViewById(R.id.text_view), Gravity.CENTER, 0, 0);
            //popUpWindow.update(0, 0, 100, 100);
            //popupWasDisplaying = false;
        }
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
    public void onDestroy() {

        if (popUpWindow.isShowing()) {
            popupWasDisplaying = true;
        }
        super.onDestroy();

    }
}
