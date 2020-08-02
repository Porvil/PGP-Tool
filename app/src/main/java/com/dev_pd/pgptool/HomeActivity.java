package com.dev_pd.pgptool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.UI.FileUtilsMine;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    private Button btn_encrypt;
    private Button btn_decrypt;
    private Button btn_addKeys;
    private Button btn_addOthersKeys;
    private Button btn_viewKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_encrypt = findViewById(R.id.btn_encrypt);
        btn_decrypt = findViewById(R.id.btn_decrypt);
        btn_addKeys = findViewById(R.id.btn_addKeys);
        btn_addOthersKeys = findViewById(R.id.btn_addOthersKeys);
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

        btn_addOthersKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Select Key to add
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
//                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

                startActivityForResult(intent, 5000);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 5000 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
//                File file = new File(uri)
                System.out.println(uri.getPath());

                FileUtilsMine fileUtilsMine = new FileUtilsMine(this);
                String path = fileUtilsMine.getPath(uri);
//                gpath = path;

                File file = new File(path);
                if(!HelperFunctions.isValidKeyFile(file.getName())){
                    System.out.println("not a key file");
                    return;
                }

                System.out.println(path);

                KeySerializable keySerializable = HelperFunctions.readKey(path);
                if(keySerializable != null) {
                    if (keySerializable.getKeyType().equals(Constants.PUBLICKEY)) {
                        HelperFunctions.writeFileExternalStorageOther(keySerializable.getKeyName(), Constants.EXTENSION_KEY, keySerializable);
                    }
                    else {
                        System.out.println("Not a public key");
                    }
                }
                else {
                    System.out.println("NUll key");
                }

            }
        }
    }

}