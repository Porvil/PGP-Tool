package com.dev_pd.pgptool;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev_pd.pgptool.Cryptography.EncryptedPGPObject;
import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PGP;
import com.dev_pd.pgptool.UI.FileUtilsMine;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.io.File;

public class EncryptActivity extends AppCompatActivity {

    private Button btn_encSelectFile;
    private Button btn_encChooseMyKey;
    private Button btn_encChooseOthersKey;
    private Button btn_encryptFile;
    private EditText et_setEncFileName;
    private TextView tv_enc_FileName;
    private TextView tv_enc_myKey;
    private TextView tv_enc_othersKey;

    private String filePath;
    private KeySerializable myKey;
    private KeySerializable othersKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        btn_encSelectFile = findViewById(R.id.btn_encSelectFile);
        btn_encChooseMyKey = findViewById(R.id.btn_encChooseMyKey);
        btn_encChooseOthersKey = findViewById(R.id.btn_encChooseOthersKey);
        btn_encryptFile = findViewById(R.id.btn_encryptFile);
        et_setEncFileName = findViewById(R.id.et_setEncFileName);
        tv_enc_FileName = findViewById(R.id.tv_enc_FileName);
        tv_enc_myKey = findViewById(R.id.tv_enc_myKey);
        tv_enc_othersKey = findViewById(R.id.tv_enc_othersKey);

        btn_encSelectFile.setOnClickListener(new View.OnClickListener() {
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

        btn_encChooseMyKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Select Key to add
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
//                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

                startActivityForResult(intent, 5001);
            }
        });

        btn_encChooseOthersKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Select Key to add
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
//                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

                startActivityForResult(intent, 5002);
            }
        });

        btn_encryptFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileName = et_setEncFileName.getText().toString().trim();

                if(TextUtils.isEmpty(fileName)){
                    et_setEncFileName.setError("Cant be empty");
                    return;
                }

                PGP pgp = new PGP();
                String pswd = "m";
                String path = filePath;
                pgp.setMyPrivateKey(myKey.getPrivateKeySerializable().getPrivateKey(pswd));
                pgp.setMyPublicKey(myKey.getPublicKeySerializable().getPublicKey());
                pgp.setOthersPublicKey(othersKey.getPublicKeySerializable().getPublicKey());

                byte[] bytes = HelperFunctions.readFile(path);

                EncryptedPGPObject encrypt = pgp.encrypt(bytes);

                boolean b = HelperFunctions.writeFileExternalStorageEnc(fileName, Constants.EXTENSION_DATA, encrypt);

                if(b){
                    Toast.makeText(EncryptActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EncryptActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        //File
        if (requestCode == 5000 && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                System.out.println(uri.getPath());

                FileUtilsMine fileUtilsMine = new FileUtilsMine(this);
                String path = fileUtilsMine.getPath(uri);

                filePath = path;
                tv_enc_FileName.setText(filePath);

            }
        }
        //My key
        else if (requestCode == 5001 && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                System.out.println(uri.getPath());

                FileUtilsMine fileUtilsMine = new FileUtilsMine(this);
                String path = fileUtilsMine.getPath(uri);

                File file = new File(path);
                if(!HelperFunctions.isValidKeyFile(file.getName())){
                    System.out.println("not a key file");
                    return;
                }

                System.out.println(path);

                KeySerializable keySerializable = HelperFunctions.readKey(path);
                if(keySerializable != null) {
                    if (keySerializable.getKeyType().equals(Constants.BOTHKEY)) {
                        myKey = keySerializable;
                        tv_enc_myKey.setText(myKey.getKeyName());
//                        HelperFunctions.writeFileExternalStorageOther(keySerializable.getKeyName(), Constants.EXTENSION_KEY, keySerializable);
                    }
                    else {
                        System.out.println("Not a Both key");
                    }
                }
                else {
                    System.out.println("NUll key");
                }

            }
        }
        //Others Key
        else if (requestCode == 5002 && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                System.out.println(uri.getPath());

                FileUtilsMine fileUtilsMine = new FileUtilsMine(this);
                String path = fileUtilsMine.getPath(uri);

                File file = new File(path);
                if(!HelperFunctions.isValidKeyFile(file.getName())){
                    System.out.println("not a key file");
                    return;
                }

                System.out.println(path);

                KeySerializable keySerializable = HelperFunctions.readKey(path);
                if(keySerializable != null) {
                    if (keySerializable.getKeyType().equals(Constants.PUBLICKEY)) {
                        othersKey = keySerializable;
                        tv_enc_othersKey.setText(othersKey.getKeyName());
//                        HelperFunctions.writeFileExternalStorageOther(keySerializable.getKeyName(), Constants.EXTENSION_KEY, keySerializable);
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