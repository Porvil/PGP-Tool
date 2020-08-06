package com.dev_pd.pgptool.UI.Activities;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private RadioGroup rg_keySize;
    private RadioButton rb_1024;
    private RadioButton rb_2048;
    private RadioButton rb_4096;

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

        rg_keySize = findViewById(R.id.rg_keySize);
        rb_1024 = findViewById(R.id.rb_1024);
        rb_2048 = findViewById(R.id.rb_2048);
        rb_4096 = findViewById(R.id.rb_4096);

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

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, //disabled
                        new int[]{android.R.attr.state_checked} //enabled
                },
                new int[] {
                        Color.BLACK, //disabled
                        Color.BLUE //enabled
                }
        );

        rb_1024.setTextColor(colorStateList);
        rb_2048.setTextColor(colorStateList);
        rb_4096.setTextColor(colorStateList);

        btn_manageKeysCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkedRadioButtonId = rg_keySize.getCheckedRadioButtonId();
                RadioButton curRadioButton = findViewById(checkedRadioButtonId);
                final int keySize = Integer.parseInt(curRadioButton.getText().toString());
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