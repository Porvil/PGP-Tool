package com.dev_pd.pgptool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btn_encrypt;
    Button btn_decrypt;
    Button btn_addKeys;
    Button btn_viewKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_encrypt = findViewById(R.id.btn_encrypt);
        btn_decrypt = findViewById(R.id.btn_decrypt);
        btn_addKeys = findViewById(R.id.btn_addKeys);
        btn_viewKeys = findViewById(R.id.btn_viewKeys);

        btn_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent encryptIntent = new Intent(HomeActivity.this, EncryptActivity.class);
                startActivity(encryptIntent);
            }
        });

        btn_decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent decryptIntent = new Intent(HomeActivity.this, DecryptActivity.class);
                startActivity(decryptIntent);
            }
        });

        btn_addKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageKeysIntent = new Intent(HomeActivity.this, ManageKeysActivity.class);
                startActivity(manageKeysIntent);
            }
        });

        btn_viewKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewKeysIntent = new Intent(HomeActivity.this, ViewKeysActivity.class);
                startActivity(viewKeysIntent);
            }
        });

    }
}