package com.dev_pd.pgptool.UI.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PrivateKeySerializable;
import com.dev_pd.pgptool.Cryptography.PublicKeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;

import java.security.KeyPair;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageKeysActivity extends AppCompatActivity {

    private Spinner spinner_keySize;
    private EditText et_manageKeysOwner;
    private EditText et_manageKeysKeyName;
    private EditText et_manageKeysPSWD;
    private Button btn_manageKeysCreate;
    private ProgressDialog show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_keys);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        spinner_keySize = findViewById(R.id.spinner_keySize);

        et_manageKeysOwner = findViewById(R.id.et_manageKeysOwner);
        et_manageKeysKeyName = findViewById(R.id.et_manageKeysKeyName);
        et_manageKeysPSWD = findViewById(R.id.et_manageKeysPSWD);

        btn_manageKeysCreate = findViewById(R.id.btn_manageKeysCreate);

        final Runnable success = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ManageKeysActivity.this, "Key Generated and Saved Successfully.", Toast.LENGTH_SHORT).show();
                show.cancel();
            }
        };

        final Runnable failure = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ManageKeysActivity.this, "Failed to create Key.", Toast.LENGTH_SHORT).show();
                show.cancel();
            }
        };


        btn_manageKeysCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int keySize = Integer.parseInt((String)spinner_keySize.getSelectedItem());
                final String owner = et_manageKeysOwner.getText().toString().trim();
                final String keyName = et_manageKeysKeyName.getText().toString().trim();
                final String password = et_manageKeysPSWD.getText().toString();

                boolean incomplete = false;
                if(TextUtils.isEmpty(owner)) {
                    et_manageKeysOwner.setError("Can't Be Empty");
                    incomplete = true;
                }

                if(TextUtils.isEmpty(keyName)) {
                    et_manageKeysKeyName.setError("Can't Be Empty");
                    incomplete = true;
                }

                if(TextUtils.isEmpty(password)) {
                    et_manageKeysPSWD.setError("Can't Be Empty");
                    incomplete = true;
                }

                if(incomplete)
                    return;

                show = ProgressDialog.show(ManageKeysActivity.this, "Generating Key. Please wait...", "Could Take upto 30seconds.", true);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        KeyPair keyPair = Utility.generateRSAKeyPair(keySize);
                        if(keyPair == null) {
                            runOnUiThread(failure);
                            return;
                        }

                        byte[] randomSalt = Utility.getRandomSalt();
                        byte[] hash = Utility.getHash(password, randomSalt);
                        if(hash == null) {
                            runOnUiThread(failure);
                            return;
                        }

                        PublicKeySerializable publicKeySerializable = new PublicKeySerializable(owner, keySize, keyPair.getPublic());
                        PrivateKeySerializable privateKeySerializable = new PrivateKeySerializable(owner, keySize, keyPair.getPrivate(), hash, randomSalt);

                        KeySerializable keySerializable = new KeySerializable(keyName, Constants.BOTHKEY, privateKeySerializable, publicKeySerializable);

                        boolean ret = HelperFunctions.writeKeySerializableSelf(keyName, Constants.EXTENSION_KEY, keySerializable);

                        if(ret)
                            runOnUiThread(success);
                        else
                            runOnUiThread(failure);

                    }
                };

                executorService.submit(runnable);

            }
        });

    }

}