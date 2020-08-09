package com.dev_pd.pgptool.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PGP;
import com.dev_pd.pgptool.Cryptography.PGPReturnData;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.FileUtilsMine;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.security.PrivateKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EncryptActivity extends AppCompatActivity {

    private View view;
    private Button btn_encSelectFile;
    private Button btn_encChooseMyKey;
    private Button btn_enc_myKeyView;
    private Button btn_enc_myKeyUnselect;
    private Button btn_encChooseMyKeyBrowse;
    private Button btn_enc_otherKeyUnselect;
    private Button btn_enc_otherKeyView;
    private Button btn_encChooseOthersKey;
    private Button btn_encChooseOthersKeyBrowse;
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
    private ExecutorService executorService;

    private boolean isErrorInEncryption = false;
    private String error = "";
    private String encryptedFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        view = findViewById(R.id.linear_encMain);
        btn_encSelectFile = findViewById(R.id.btn_encSelectFile);
        btn_enc_myKeyUnselect = findViewById(R.id.btn_enc_myKeyUnselect);
        btn_enc_myKeyView = findViewById(R.id.btn_enc_myKeyView);
        btn_encChooseMyKey = findViewById(R.id.btn_encChooseMyKey);
        btn_encChooseMyKeyBrowse = findViewById(R.id.btn_encChooseMyKeyBrowse);
        btn_enc_otherKeyUnselect = findViewById(R.id.btn_enc_otherKeyUnselect);
        btn_enc_otherKeyView = findViewById(R.id.btn_enc_otherKeyView);
        btn_encChooseOthersKey = findViewById(R.id.btn_encChooseOthersKey);
        btn_encChooseOthersKeyBrowse = findViewById(R.id.btn_encChooseOthersKeyBrowse);
        btn_encryptFile = findViewById(R.id.btn_encryptFile);
        et_setEncFileName = findViewById(R.id.et_setEncFileName);
        tv_enc_FileName = findViewById(R.id.tv_enc_FileName);
        tv_enc_myKey = findViewById(R.id.tv_enc_myKey);
        tv_enc_othersKey = findViewById(R.id.tv_enc_othersKey);

        executorService = Executors.newSingleThreadExecutor();

        final Runnable success = new Runnable() {
            @Override
            public void run() {
                String fileLoc = HelperFunctions.getExternalStoragePath() +
                        Constants.ENC_DIRECTORY +
                        Constants.SEPARATOR +
                        encryptedFileName;
                Snackbar.make(view, "File Encrypted at \"" + fileLoc + "\"", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                show.cancel();

                isErrorInEncryption = false;
                error = "";
                encryptedFileName = "";
            }
        };

        final Runnable failure = new Runnable() {
            @Override
            public void run() {
                String snack = "File Encryption Failed.";
                if(isErrorInEncryption == true){
                    snack = error;
                }

                Snackbar.make(view, snack, Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                show.cancel();

                isErrorInEncryption = false;
                error = "";
                encryptedFileName = "";
            }
        };

        btn_enc_myKeyUnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myKey = null;
                tv_enc_myKey.setText("No Key Selected");
            }
        });

        btn_enc_otherKeyUnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othersKey = null;
                tv_enc_othersKey.setText("No Other Key Selected");
            }
        });


        btn_encSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.SELECT_FILE);
            }
        });

        btn_encChooseMyKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EncryptActivity.this, SelectMyKeyActivity.class);
                i.putExtra(Constants.KEY_SELECT_TYPE, Constants.KEY_SELECT_SELF);
                startActivityForResult(i, Constants.SELECT_BROWSE_KEY);
            }
        });

        btn_encChooseMyKeyBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.SELECT_SELF_KEY);
            }
        });

        btn_encChooseOthersKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EncryptActivity.this, SelectMyKeyActivity.class);
                i.putExtra(Constants.KEY_SELECT_TYPE, Constants.KEY_SELECT_OTHERS);
                startActivityForResult(i, Constants.SELECT_BROWSE_KEY);
            }
        });

        btn_encChooseOthersKeyBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.SELECT_OTHER_KEY);
            }
        });

        btn_encryptFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fileName)) {
                    Snackbar.make(view, "No File Selected", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (myKey != null && othersKey != null) {

                    if(myKey.getKeySize() != othersKey.getKeySize()){
                        Snackbar.make(view, "Keys are of different Size", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    final String encFileName = et_setEncFileName.getText().toString().trim();

                    if (TextUtils.isEmpty(encFileName)) {
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
                            byte[] bytes = HelperFunctions.readFileToBytes(filePath);

                            PGPReturnData encrypt = pgp.encrypt(bytes, fileName);
                            if(!encrypt.isError()){
                                boolean b = HelperFunctions.writeEncryptedData(encFileName, Constants.EXTENSION_DATA, encrypt.getEncryptedPGPObject());
                                if (b) {
                                    encryptedFileName = encFileName + Constants.EXTENSION_DATA;
                                    runOnUiThread(success);
                                }
                                else {
                                    runOnUiThread(failure);
                                }
                            }
                            else{
                                //SOME ERROR
                                isErrorInEncryption = true;
                                error = encrypt.getException().getMessage();
                                runOnUiThread(failure);
                            }
                        }
                    };

                    executorService.execute(runnable);
                }
                else{
                    Snackbar.make(view, "Please Select Keys", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        btn_enc_myKeyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myKey != null) {
                    View dialView = LayoutInflater.from(EncryptActivity.this).inflate(R.layout.item_keys_for_select, null);
                    TextView tv_myKeysOwner = dialView.findViewById(R.id.tv_keysForSelectOwner);
                    final TextView tv_myKeysKeyName = dialView.findViewById(R.id.tv_keysForSelectKeyName);
                    TextView tv_myKeysKeySize = dialView.findViewById(R.id.tv_keysForSelectKeySize);

                    if (myKey != null) {
                        tv_myKeysOwner.setText(myKey.getOwner());
                        tv_myKeysKeyName.setText(myKey.getKeyName());
                        tv_myKeysKeySize.setText(myKey.getKeySize() + "");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(EncryptActivity.this);
                    builder.setView(dialView);
                    builder.setTitle("Key Details");
                    builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else{
                    View view = findViewById(R.id.linear_encMyKeyContainer);
                    Snackbar.make(view, "No Key Selected", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        btn_enc_otherKeyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (othersKey != null) {
                    View dialView = LayoutInflater.from(EncryptActivity.this).inflate(R.layout.item_keys_for_select, null);
                    TextView tv_myKeysOwner = dialView.findViewById(R.id.tv_keysForSelectOwner);
                    final TextView tv_myKeysKeyName = dialView.findViewById(R.id.tv_keysForSelectKeyName);
                    TextView tv_myKeysKeySize = dialView.findViewById(R.id.tv_keysForSelectKeySize);

                    if (othersKey != null) {
                        tv_myKeysOwner.setText(othersKey.getOwner());
                        tv_myKeysKeyName.setText(othersKey.getKeyName());
                        tv_myKeysKeySize.setText(othersKey.getKeySize() + "");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(EncryptActivity.this);
                    builder.setView(dialView);
                    builder.setTitle("Key Details");
                    builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else{
                    View view = findViewById(R.id.linear_encOthersKeyContainer);
                    Snackbar.make(view, "No Other's Key Selected", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        //File
        if (requestCode == Constants.SELECT_FILE && resultCode == Activity.RESULT_OK) {
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
        else if (requestCode == Constants.SELECT_SELF_KEY && resultCode == Activity.RESULT_OK) {
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
                        final Button btn_confirmpswd = view.findViewById(R.id.btn_confirmpswd);
                        final Button btn_cancelpswd = view.findViewById(R.id.btn_cancelpswd);

                        AlertDialog.Builder builder = new AlertDialog.Builder(EncryptActivity.this);
                        builder.setView(view);
                        builder.setTitle("Enter Password");
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        btn_cancelpswd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        btn_confirmpswd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String pswd = et_enterpswd.getText().toString();

                                if(TextUtils.isEmpty(pswd)){
                                    et_enterpswd.setError("Cant be empty");
                                    return;
                                }

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

                                        if(privateKey == null){
                                            runOnUiThread(wrongpswd);
                                            return;
                                        }

                                        myKey = keySerializable;
                                        tv_enc_myKey.setText(myKey.getKeyName());
                                        password = pswd;
                                        runOnUiThread(correctpswd);

                                    }
                                });

                            }
                        });
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
        else if (requestCode == Constants.SELECT_OTHER_KEY && resultCode == Activity.RESULT_OK) {
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
        else if (requestCode == Constants.SELECT_BROWSE_KEY) {
            if(resultCode == Activity.RESULT_OK){
                int type = resultData.getIntExtra(Constants.KEY_SELECT_TYPE, Constants.KEY_SELECT_SELF);
                String returnKeyPath = resultData.getStringExtra(Constants.RETURN_PATH);
                final KeySerializable keySerializable = (KeySerializable) resultData.getSerializableExtra(Constants.RETURN_KEY);
                System.out.println("==============" + keySerializable);

                if(type == Constants.KEY_SELECT_SELF){
                    if (keySerializable.getKeyType().equals(Constants.BOTHKEY)) {

                        View view = LayoutInflater.from(EncryptActivity.this).inflate(R.layout.dialog_enterpassword, null);

                        final EditText et_enterpswd = view.findViewById(R.id.et_enterpswd);
                        final Button btn_confirmpswd = view.findViewById(R.id.btn_confirmpswd);
                        final Button btn_cancelpswd = view.findViewById(R.id.btn_cancelpswd);
                        AlertDialog.Builder builder = new AlertDialog.Builder(EncryptActivity.this);
                        builder.setView(view);
                        builder.setTitle("Enter Password");

                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        btn_cancelpswd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        btn_confirmpswd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String pswd = et_enterpswd.getText().toString();

                                if(TextUtils.isEmpty(pswd)){
                                    et_enterpswd.setError("Cant be empty");
                                    return;
                                }

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

                                        if(privateKey == null){
                                            runOnUiThread(wrongpswd);
                                            return;
                                        }

                                        myKey = keySerializable;
                                        tv_enc_myKey.setText(myKey.getKeyName());
                                        password = pswd;
                                        runOnUiThread(correctpswd);

                                    }
                                });
                            }
                        });
                    }
                    else {
                        System.out.println("Not a Both key");
                    }
                }
                else{
                    if (keySerializable.getKeyType().equals(Constants.PUBLICKEY)) {
                        othersKey = keySerializable;
                        tv_enc_othersKey.setText(othersKey.getKeyName());
                    }
                    else {
                        System.out.println("Not a public key");
                    }
                }

            }
        }
    }

}