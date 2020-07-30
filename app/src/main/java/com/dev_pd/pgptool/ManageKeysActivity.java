package com.dev_pd.pgptool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.security.KeyPair;

public class ManageKeysActivity extends AppCompatActivity {

    private Spinner spinner_keySize;
    private EditText et_manageKeysOwner;
    private EditText et_manageKeysKeyName;
    private EditText et_manageKeysPSWD;

    private Button btn_manageKeysCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_keys);

        spinner_keySize = findViewById(R.id.spinner_keySize);

        et_manageKeysOwner = findViewById(R.id.et_manageKeysOwner);
        et_manageKeysKeyName = findViewById(R.id.et_manageKeysKeyName);
        et_manageKeysPSWD = findViewById(R.id.et_manageKeysPSWD);

        btn_manageKeysCreate = findViewById(R.id.btn_manageKeysCreate);


        btn_manageKeysCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int keySize = Integer.parseInt((String)spinner_keySize.getSelectedItem());
                String owner = et_manageKeysOwner.getText().toString().trim();
                String keyName = et_manageKeysKeyName.getText().toString().trim();
                String password = et_manageKeysPSWD.getText().toString();

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

                RSAKeyGeneratorTask rsaKeyGeneratorTask = new RSAKeyGeneratorTask(
                        ManageKeysActivity.this,
                        keySize,
                        owner,
                        keyName,
                        password);
                AsyncTask<Integer, Integer, Boolean> execute = rsaKeyGeneratorTask.execute();


            }
        });


    }
}