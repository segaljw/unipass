package com.gurpusmaximus.unipass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
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
 * User Activity decrypts the file account information after correct master key is inputted.
 *
 * Data is decrypted with files as the cypher-text.
 */
public class User extends AppCompatActivity {

    String accountName;
    ListView listOfUsers;
    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter<String> adapter;
    static byte[] masterKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        masterKey = getIntent().getExtras().getByteArray("masterkey");
        Bundle extras = getIntent().getExtras();
        accountName = extras.getString("account");
        adapter=new ArrayAdapter<>(this,
                R.layout.activity_listview,
                users);
        listOfUsers = (ListView)findViewById(R.id.user_list);
        listOfUsers.setAdapter(adapter);

        String accountFilePath = getApplicationContext().getFilesDir() + "/" + accountName;
        File accountFile = new File(accountFilePath);

        try{
            //Read the full file as a byte array...
            RandomAccessFile f = new RandomAccessFile(accountFile, "r");
            byte[] b = new byte[(int)f.length()];
            f.readFully(b);

            //Give input stream the bytes and decrypt with masterKey.
            InputStream is = new ByteArrayInputStream(b);
            String fileContent = decryptMsg(b, generateKey());

            //Accounts are separated with regex \n so split on that.
            String[] lines = fileContent.split("\n");
            for (String line: lines) {
                Log.i("Decrypted Line", line);
                users.add("Username/Password:" + line);
                //update the adapter to change the ListView.
                adapter.notifyDataSetChanged();
            }

            is.close();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    /**
     * Method will generate a secret key based on the given master key.
     */
    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(masterKey, "AES");
    }

    /**
     * Method will decrypt a given String with the generated secret key.
     */
    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}
