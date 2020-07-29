package com.dev_pd.pgptool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PrivateKeySerializable;
import com.dev_pd.pgptool.Cryptography.PublicKeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;

public class LoadKeysTask extends AsyncTask<Integer, Integer, Boolean> {

    Context context;
    ProgressDialog progressDialog;
    ArrayList<KeySerializable> keySerializables;

    public LoadKeysTask(Context context, ArrayList<KeySerializable> keySerializables) {
        this.context = context;
        this.keySerializables = keySerializables;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        keySerializables = HelperFunctions.readKeys();
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
