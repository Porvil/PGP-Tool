package com.dev_pd.pgptool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dev_pd.pgptool.Cryptography.PrivateKeySerializable;
import com.dev_pd.pgptool.Cryptography.PublicKeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.io.IOException;
import java.security.KeyPair;

public class LoadKeysTask extends AsyncTask<Integer, Integer, Boolean> {

    Context context;
    ProgressDialog progressDialog;
    int keySize;
    String owner;
    String keyName;
    String password;
    Activity activity;

    public LoadKeysTask(Activity activity) {
        this.context = context;
        this.keySize = keySize;
        this.owner = owner;
        this.keyName = keyName;
        this.password = password;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {

        KeyPair keyPair = Utility.generateRSAKeyPair(keySize);
        if(keyPair == null)
            return false;

        byte[] randomSalt = Utility.getRandomSalt();
        byte[] hash = Utility.getHash(password, randomSalt);
        if(hash == null)
            return false;

        PublicKeySerializable publicKeySerializable = new PublicKeySerializable(owner, keySize, keyPair.getPublic());
        PrivateKeySerializable privateKeySerializable = new PrivateKeySerializable(owner, keySize, keyPair.getPrivate(), hash, randomSalt);

        try {
            HelperFunctions.writeFileExternalStorage(keyName, Constants.EXTENSION_PUBLIC_KEY, 1, publicKeySerializable);
            HelperFunctions.writeFileExternalStorage(keyName, Constants.EXTENSION_PRIVATE_KEY, 1, privateKeySerializable);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "","Generating Key. Please wait...", true);
    }

    @Override
    protected void onPostExecute(Boolean res) {
        super.onPostExecute(res);
        progressDialog.cancel();
        if(!res)
            Toast.makeText(context, ":(", Toast.LENGTH_SHORT).show();

//        activity

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
