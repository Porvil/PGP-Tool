package com.dev_pd.pgptool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dev_pd.pgptool.Cryptography.EncryptedPGPObject;
import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PGP;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.UI.FileUtilsMine;
import com.dev_pd.pgptool.UI.HelperFunctions;

import org.w3c.dom.Text;

import java.io.File;
import java.security.PrivateKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private String fileName;
    private String password;
    private KeySerializable myKey;
    private KeySerializable othersKey;
    private ProgressDialog show;
    ExecutorService executorService;

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

        executorService = Executors.newSingleThreadExecutor();

        final Runnable success = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EncryptActivity.this, "Success", Toast.LENGTH_SHORT).show();
                show.cancel();
            }
        };

        final Runnable failure = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EncryptActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                show.cancel();
            }
        };


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
//                Uri pickerInitialUri = Uri.parse(HelperFunctions.getPGPDirectoryPath() + Constants.SELF_DIRECTORY);
//                System.out.println(pickerInitialUri.getPath());
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

                final String encFileName = et_setEncFileName.getText().toString().trim();

                if(TextUtils.isEmpty(fileName)){
                    et_setEncFileName.setError("Cant be empty");
                    return;
                }

                show = ProgressDialog.show(EncryptActivity.this, "Encrypting. Please wait...",
                        "Could Take several minutes if selected file is large.", true);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        PGP pgp = new PGP(myKey.getKeySize());
                        pgp.setMyPrivateKey(myKey.getPrivateKeySerializable().getPrivateKey(password));
                        pgp.setMyPublicKey(myKey.getPublicKeySerializable().getPublicKey());
                        pgp.setOthersPublicKey(othersKey.getPublicKeySerializable().getPublicKey());

                        System.out.println(filePath);
                        byte[] bytes = HelperFunctions.readFile(filePath);

                        EncryptedPGPObject encrypt = pgp.encrypt(bytes,fileName);

                        boolean b = HelperFunctions.writeFileExternalStorageEnc(encFileName, Constants.EXTENSION_DATA, encrypt);
                        if(b){
                            runOnUiThread(success);
                        }
                        else{
                            runOnUiThread(failure);
                        }
                    }
                };

                executorService.execute(runnable);

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
                String dataFileName = uri.getLastPathSegment();
                System.out.println(dataFileName);

                if(dataFileName != null){
                    fileName = dataFileName;
                }

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

                final KeySerializable keySerializable = HelperFunctions.readKey(path);
                if(keySerializable != null) {
                    if (keySerializable.getKeyType().equals(Constants.BOTHKEY)) {

                        View view = LayoutInflater.from(EncryptActivity.this).inflate(R.layout.dialog_enterpassword, null);

                        final EditText et_enterpswd = view.findViewById(R.id.et_enterpswd);
                        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
//                        progressBar.setVisibility(View.INVISIBLE);
                        final Button btn_confirmpswd = view.findViewById(R.id.btn_confirmpswd);

                        AlertDialog.Builder builder = new AlertDialog.Builder(EncryptActivity.this);
                        builder.setView(view);
                        builder.setTitle("Enter Password");
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EncryptActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        btn_confirmpswd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String pswd = et_enterpswd.getText().toString();

                                if(TextUtils.isEmpty(pswd)){
                                    et_enterpswd.setError("Cant be empty");
                                    return;
                                }

//                                ProgressDialog show1 = ProgressDialog.show(EncryptActivity.this, "Encrypting. Please wait...",
//                                        "Could Take several minutes if selected file is large.", true);

                                final Runnable wrongpswd = new Runnable() {
                                    @Override
                                    public void run() {
                                        et_enterpswd.setError("Wrong password");
                                    }
                                };

                                final Runnable correctpswd = new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                };


                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        PrivateKey privateKey = keySerializable.getPrivateKeySerializable().getPrivateKey(pswd);
//
                                        if(privateKey == null){
                                            //error here
                                            runOnUiThread(wrongpswd);
                                            return;
                                        }

                                        myKey = keySerializable;
                                        tv_enc_myKey.setText(myKey.getKeyName());
                                        password = pswd;
                                        runOnUiThread(correctpswd);

                                    }
                                });

//                                PrivateKey privateKey = keySerializable.getPrivateKeySerializable().getPrivateKey(pswd);
//
//                                if(privateKey == null){
//                                    et_enterpswd.setError("Wrong password");
//
//                                    return;
//                                }
//
//                                myKey = keySerializable;
//                                tv_enc_myKey.setText(myKey.getKeyName());
//                                password = pswd;
//                                dialog.dismiss();
                            }
                        });


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