package com.dev_pd.pgptool;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PrivateKeySerializable;
import com.dev_pd.pgptool.Cryptography.PublicKeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.security.KeyPair;

public class RSAKeyGeneratorTask extends AsyncTask<Integer, Integer, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;
    private int keySize;
    private String owner;
    private String keyName;
    private String password;

    public RSAKeyGeneratorTask(Context context, int keySize, String owner, String keyName, String password) {
        this.context = context;
        this.keySize = keySize;
        this.owner = owner;
        this.keyName = keyName;
        this.password = password;
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

        KeySerializable keySerializable = new KeySerializable(keyName,Constants.BOTHKEY, privateKeySerializable, publicKeySerializable);

        boolean ret = HelperFunctions.writeFileExternalStorage(keyName, Constants.EXTENSION_KEY, keySerializable);

        return ret;
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
        else
            Toast.makeText(context, "Key Created Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
