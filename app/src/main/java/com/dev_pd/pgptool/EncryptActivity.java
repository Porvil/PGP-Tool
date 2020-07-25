package com.dev_pd.pgptool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EncryptActivity extends AppCompatActivity {

    private Button btn_encSelectFile;
    private Button btn_encChooseMyKey;
    private Button btn_encChooseOthersKey;
    private Button btn_encryptFile;
    private EditText et_setEncFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        btn_encSelectFile = findViewById(R.id.btn_encSelectFile);
        btn_encChooseMyKey = findViewById(R.id.btn_encChooseMyKey);
        btn_encChooseOthersKey = findViewById(R.id.btn_encChooseOthersKey);
        btn_encryptFile = findViewById(R.id.btn_encryptFile);
        et_setEncFileName = findViewById(R.id.et_setEncFileName);

    }
}