package com.dev_pd.pgptool.UI.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.FileUtilsMine;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    private String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private Button btn_encrypt;
    private Button btn_decrypt;
    private Button btn_addKeys;
    private Button btn_addOthersKeys;
    private Button btn_viewKeys;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        view = findViewById(R.id.main);
        //checkPermissions() should be called only after assigning root View;
        checkPermissions();

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
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                startActivityForResult(intent, Constants.ADD_OTHERS_KEY);
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
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == Constants.ADD_OTHERS_KEY && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                System.out.println(uri.getPath());

                FileUtilsMine fileUtilsMine = new FileUtilsMine(this);
                String path = fileUtilsMine.getPath(uri);
                File file = new File(path);
                if(!HelperFunctions.isValidKeyFile(file.getName())){
                    Snackbar.make(view, "The Selected File is not Key File", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                KeySerializable keySerializable = HelperFunctions.readKey(path);
                if(keySerializable != null) {
                    if (keySerializable.getKeyType().equals(Constants.PUBLICKEY)) {
                        HelperFunctions.writeKeySerializableOther(keySerializable.getKeyName(), Constants.EXTENSION_KEY, keySerializable);
                        Snackbar.make(view, "The Key is successfully added.", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        Snackbar.make(view, "The Selected File is not a Public Key File", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(view, "The Key is NULL", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkPermissions(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!hasPermissions(PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.PERMISSION_ALL);
            }
        }
    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.PERMISSION_ALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(view, "Permissions Granted.",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                final Snackbar snackbar = Snackbar.make(view, "App may not work properly.",
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Give Permissions", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermissions();
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}