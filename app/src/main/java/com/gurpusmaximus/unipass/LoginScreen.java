package com.gurpusmaximus.unipass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
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

/**
 * Activity LoginScreen will Load every time app is started.
 *
 * Continuously prompts user for a master key to encrypt entries until one is provided.
 * Master key must uphold certain AES standards and is best to be randomly chosen.
 */
public class LoginScreen extends AppCompatActivity {

    //Field to keep entered key.
    EditText masterPasswordField;
    //Place to store the byte array of the entered key.
    static byte[] masterKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //File path for where to store encrypted master key.
        final File file = getApplicationContext().getFileStreamPath("master");
        //Intent for password home screen.
        final Intent credentialsIntent = new Intent(this, CredentialsActivity.class);
        //Intent to restart LoginScreen on failed master key attempt.
        final Intent loginIntent = new Intent(this, LoginScreen.class);

        //Set up the layout...
        setContentView(R.layout.activity_login_screen);
        masterPasswordField = (EditText)findViewById(R.id.master_password);
        Button submit = (Button)findViewById(R.id.submit_password_button);

        //Change hint based on whether or not master key file exists.
        if (file.exists()) {
            masterPasswordField.setHint(R.string.enter_your_master_password);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Set masterKey to be byte array of EditText.
                masterKey = String.valueOf(masterPasswordField.getText()).getBytes();

                //Attempt to create encrypted master key file if one does not exist.
                if (!file.exists()) {

                    FileOutputStream outputStream;
                    try {
                        //Attempt to write an encrypted version of masterKey to file and close.
                        outputStream = openFileOutput("master", Context.MODE_PRIVATE);
                        outputStream.write(encryptMsg(String.valueOf(masterPasswordField.getText()),
                                generateKey()));
                        outputStream.close();
                        //If successfully (No exception thrown) then go to home screen activity.
                        credentialsIntent.putExtra("masterkey", masterKey);
                        startActivity(credentialsIntent);
                        finish();
                    } catch (Exception e) {
                        //If key is not valid then delete file and retry LoginScreen activity.
                        file.delete();
                        startActivity(loginIntent);
                        finish();
                    }

                } else {
                    //If master key file does exist then attempt to check user's input is the key.
                    try {
                        //File object which can easily be converted into bytes.
                        RandomAccessFile f = new RandomAccessFile(file, "r");
                        //Set up byte objects for encrypted master key file and encrypted input.
                        byte[] b = new byte[(int)f.length()];
                        byte[] bInput = encryptMsg(String.valueOf(masterPasswordField.getText()),
                                    generateKey());
                        f.readFully(b);

                        //Check if the encrypted cyphertext byte arrays are the same
                        //If so continue to the home screen.
                        if (Arrays.equals(b, bInput)) {
                            credentialsIntent.putExtra("masterkey", masterKey);
                            startActivity(credentialsIntent);
                            finish();
                        } else {
                            //Restart login if bytes are not the same.
                            startActivity(loginIntent);
                            finish();
                        }

                    }
                    catch (Exception e) {
                        //Restart login if inputted key is invalid (Not 16, 24, or 32 chars).
                        startActivity(loginIntent);
                        finish();
                    }
                }

            }
        });
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
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

}