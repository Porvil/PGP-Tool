package com.dev_pd.pgptool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dev_pd.pgptool.UI.FileUtilsMine;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    Button btn_encrypt;
    Button btn_decrypt;
    Button btn_addKeys;
    Button btn_addOthersKeys;
    Button btn_viewKeys;

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

                System.out.println(path);

                File file = new File(path);
                System.out.println();
                // Perform operations on the document using its URI.


//                PublicKeySerializable publicKeySerializable = HelperFunctions.readPublicKeySerializable(path);

//                System.out.println(publicKeySerializable.getPublicKey().);
//                try {
//                    System.out.println(Utility.getString(publicKeySerializable.getPublicKey().getEncoded()));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

            }
        }
    }

}