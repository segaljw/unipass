package com.gurpusmaximus.unipass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Activity CredentialActivity will Load every only after correct masterKey is given.
 *
 * Home screen for all account types.
 * Clicking on an account will move to a list view of the decrypted account credentials.
 */
public class CredentialsActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Toolbar toolbar;
    static final int REQUEST_CODE = 1;
    ListView listOfAccounts;
    File file;
    ArrayList<String> accounts = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String accountString = "";
    FileOutputStream outputStream;
    static Bundle extrasFromLogin;
    static byte[] masterKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get passed masterKey for encryption use.
        extrasFromLogin = getIntent().getExtras();
        masterKey = extrasFromLogin.getByteArray("masterkey");
        setContentView(R.layout.activity_credentials);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add all profile names to an ArrayList from the filenames in the current directory.
        accounts.addAll(getFileNames());
        //Do not add the master.
        accounts.remove("master");

        //Used to sync the ListView with accounts
        adapter = new ArrayAdapter<>(this,
                R.layout.activity_listview,
                accounts);

        //Set the ListView.
        listOfAccounts = (ListView)findViewById(R.id.account_list);
        listOfAccounts.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Add Item activity for adding account, username, password.
                Intent openPopUp = new Intent(view.getContext(), AddItem.class);
                startActivityForResult(openPopUp, REQUEST_CODE);
            }
        });

        //Start User activity after clicking on an Account in ListView
        listOfAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  Intent startUser = new Intent(view.getContext(), User.class);
                  //send bundle containing username and password
                  String temp = (String)parent.getItemAtPosition(position);
                  startUser.putExtra("account", temp);
                  startUser.putExtra("masterkey", masterKey);
                  startActivity(startUser);
              }
          }

        );

    }

    /**
     * Receive Account string for use in ListView and bundle of username and password
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle receive;
            receive = data.getExtras();
            accountString = receive.getString("account");
            Log.i("activityResult","creating files");
            file = getApplicationContext().getFileStreamPath(accountString);

            //If the file already contains an account.
            if (accounts.contains(accountString)) {
                try{

                    //Get all bytes of encrypted file.
                    RandomAccessFile f = new RandomAccessFile(file, "r");
                    byte[] b = new byte[(int)f.length()];
                    f.readFully(b);

                    outputStream = openFileOutput(accountString, Context.MODE_PRIVATE);
                    //Rewrite previous contents of encrypted file.
                    outputStream.write(b);
                    //Append newly received account info and encrypt before writing..
                    outputStream.write(encryptMsg(receive.getString("username")
                                    + " " +  receive.getString("password") + "\n",
                            generateKey()));
                    Log.i("Encrypted User+Pass", new String(encryptMsg(receive.getString("username")
                                    + " " +  receive.getString("password") + "\n",
                            generateKey())));
                    outputStream.close();

                    Log.i("adding file","data has been added to existing file");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //if file doesn't exist no need to write previous data.
                try {
                    outputStream = openFileOutput(accountString, Context.MODE_PRIVATE);
                    outputStream.write(encryptMsg(receive.getString("username")
                                    + " " +  receive.getString("password") + "\n",
                            generateKey()));
                    Log.i("Encrypted User+Pass", new String(encryptMsg(receive.getString("username")
                                    + " " +  receive.getString("password") + "\n",
                            generateKey())));
                    outputStream.close();
                    Log.i("adding file","file is being created and data has been added");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(!accounts.contains(accountString)){
                accounts.add(accountString);
                adapter.notifyDataSetChanged();
            }


        }

    }

    /**
     * Will return a list of all files in main working directory.
     */
    public ArrayList<String> getFileNames() {
        ArrayList<String> forReturn = new ArrayList<>();

        for(File f : getApplicationContext().getFilesDir().listFiles())
            forReturn.add(f.getName());

        return forReturn;
    }

    @Override
    public void onResume() {
        super.onResume();

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
    /**
     * Method will generate a secret key based on the given master key.
     */
    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(masterKey, "AES");
    }

    /**
     * Method will encrypt a given String with the generated secret key.
     */
    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {
        // Encrypt the message with AES.
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

}
