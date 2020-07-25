package com.dev_pd.pgptool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageKeysActivity extends AppCompatActivity {

    private Button btn_manageKeysAdd;
    private Button btn_manageKeysCreate;
    private Button btn_manageKeysAddOthersKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_keys);

        btn_manageKeysAdd = findViewById(R.id.btn_manageKeysAdd);
        btn_manageKeysCreate = findViewById(R.id.btn_manageKeysCreate);
        btn_manageKeysAddOthersKey = findViewById(R.id.btn_manageKeysAddOthersKey);


        btn_manageKeysAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btn_manageKeysCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btn_manageKeysAddOthersKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}