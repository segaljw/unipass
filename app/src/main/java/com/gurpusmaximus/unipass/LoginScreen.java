package com.gurpusmaximus.unipass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class LoginScreen extends AppCompatActivity {

    EditText masterPasswordField;
    static byte[] masterKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final File file = getApplicationContext().getFileStreamPath("master");
        final Intent credentialsIntent = new Intent(this, CredentialsActivity.class);
        final Intent loginIntent = new Intent(this, LoginScreen.class);

        setContentView(R.layout.activity_login_screen);
        masterPasswordField = (EditText)findViewById(R.id.master_password);
        Button submit = (Button)findViewById(R.id.submit_password_button);

        if (file.exists()) {
            masterPasswordField.setHint(R.string.enter_your_master_password);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                masterKey = String.valueOf(masterPasswordField.getText()).getBytes();

                if (!file.exists()) {

                    FileOutputStream outputStream;

                    try {
                        outputStream = openFileOutput("master", Context.MODE_PRIVATE);
                        outputStream.write(encryptMsg(String.valueOf(masterPasswordField.getText()),
                                generateKey()));

                        outputStream.close();
                        credentialsIntent.putExtra("masterkey", masterKey);
                        startActivity(credentialsIntent);
                        finish();
                    } catch (Exception e) {
                        file.delete();
                        startActivity(loginIntent);
                        finish();
                    }

                } else {

                    try {
                        RandomAccessFile f = new RandomAccessFile(file, "r");
                        byte[] b = new byte[(int)f.length()];
                        byte[] bInput = new byte[(int)f.length()];
                        try{
                            bInput = encryptMsg(String.valueOf(masterPasswordField.getText()),
                                    generateKey());
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }
                        f.readFully(b);

                        if (Arrays.equals(b, bInput)) {
                            credentialsIntent.putExtra("masterkey", masterKey);
                            startActivity(credentialsIntent);
                            finish();
                        } else {
                            startActivity(loginIntent);
                            finish();
                        }

                    }
                    catch (Exception e) {
                        startActivity(loginIntent);
                        finish();
                    }
                }

            }
        });
    }

    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return new SecretKeySpec(masterKey, "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException
    {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

}