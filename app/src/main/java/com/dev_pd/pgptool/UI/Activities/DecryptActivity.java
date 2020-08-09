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

import com.dev_pd.pgptool.Cryptography.EncryptedPGPObject;
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

public class DecryptActivity extends AppCompatActivity {

    private View view;
    private Button btn_decSelectFile;
    private Button btn_decChooseMyKey;
    private Button btn_dec_myKeyUnselect;
    private Button btn_dec_myKeyView;
    private Button btn_decChooseMyKeyBrowse;
    private Button btn_decChooseOthersKey;
    private Button btn_dec_otherKeyUnselect;
    private Button btn_dec_otherKeyView;
    private Button btn_decChooseOthersKeyBrowse;
    private Button btn_decryptFile;
    private TextView tv_dec_FileName;
    private TextView tv_dec_myKey;
    private TextView tv_dec_othersKey;

    private String filePath;
    private String fileName;
    private String password;
    private KeySerializable myKey;
    private KeySerializable othersKey;
    private ProgressDialog show;
    ExecutorService executorService;

    private boolean isErrorInDecryption = false;
    private String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        view = findViewById(R.id.linear_decMain);
        btn_decSelectFile = findViewById(R.id.btn_decSelectFile);
        btn_dec_myKeyUnselect = findViewById(R.id.btn_dec_myKeyUnselect);
        btn_dec_myKeyView = findViewById(R.id.btn_dec_myKeyView);
        btn_decChooseMyKeyBrowse = findViewById(R.id.btn_decChooseMyKeyBrowse);
        btn_decChooseMyKey = findViewById(R.id.btn_decChooseMyKey);
        btn_decChooseOthersKey = findViewById(R.id.btn_decChooseOthersKey);
        btn_dec_otherKeyUnselect = findViewById(R.id.btn_dec_otherKeyUnselect);
        btn_dec_otherKeyView = findViewById(R.id.btn_dec_otherKeyView);
        btn_decChooseOthersKeyBrowse = findViewById(R.id.btn_decChooseOthersKeyBrowse);
        btn_decryptFile = findViewById(R.id.btn_decryptFile);
        tv_dec_FileName = findViewById(R.id.tv_dec_FileName);
        tv_dec_myKey = findViewById(R.id.tv_dec_myKey);
        tv_dec_othersKey = findViewById(R.id.tv_dec_othersKey);

        executorService = Executors.newSingleThreadExecutor();

        final Runnable success = new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, "File Decrypted Successfully.", Snackbar.LENGTH_SHORT).show();
                show.cancel();

                isErrorInDecryption = false;
                error = "";

            }
        };

        final Runnable failure = new Runnable() {
            @Override
            public void run() {
                String snack = "File Decryption Failed.";
                if(isErrorInDecryption == true){
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

                isErrorInDecryption = false;
                error = "";
            }
        };

        btn_dec_myKeyUnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myKey = null;
                tv_dec_myKey.setText("No Key Selected");
            }
        });

        btn_dec_otherKeyUnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othersKey = null;
                tv_dec_othersKey.setText("No Other Key Selected");
            }
        });

        btn_decSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.SELECT_FILE);
            }
        });

        btn_decChooseMyKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DecryptActivity.this, SelectMyKeyActivity.class);
                i.putExtra(Constants.KEY_SELECT_TYPE, Constants.KEY_SELECT_SELF);
                startActivityForResult(i, Constants.SELECT_BROWSE_KEY);
            }
        });

        btn_decChooseMyKeyBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.SELECT_SELF_KEY);
            }
        });

        btn_decChooseOthersKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DecryptActivity.this, SelectMyKeyActivity.class);
                i.putExtra(Constants.KEY_SELECT_TYPE, Constants.KEY_SELECT_OTHERS);
                startActivityForResult(i, Constants.SELECT_BROWSE_KEY);
            }
        });

        btn_decChooseOthersKeyBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.SELECT_OTHER_KEY);
            }
        });

        btn_decryptFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(fileName)) {
                    Snackbar.make(view, "No File Selected", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (myKey != null && othersKey != null) {

                    if (myKey.getKeySize() != othersKey.getKeySize()) {
                        Snackbar.make(view, "Keys are of different Size", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    show = ProgressDialog.show(DecryptActivity.this, "Decrypting. Please wait...",
                            "Could Take several minutes if selected file is large.", true);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            PGP pgp = new PGP(myKey.getKeySize());
                            pgp.setMyPrivateKey(myKey.getPrivateKeySerializable().getPrivateKey(password));
                            pgp.setMyPublicKey(myKey.getPublicKeySerializable().getPublicKey());
                            pgp.setOthersPublicKey(othersKey.getPublicKeySerializable().getPublicKey());

                            System.out.println(filePath);

                            EncryptedPGPObject encryptedPGPObject = HelperFunctions.readEncryptedFile(filePath);

                            if(encryptedPGPObject == null){
                                isErrorInDecryption = true;
                                error = "Selected File is not a \"" + Constants.EXTENSION_DATA + "\" file.";
                                runOnUiThread(failure);
                                return;
                            }

                            if(encryptedPGPObject.getRsaKeyLength() != myKey.getKeySize()){
                                isErrorInDecryption = true;
                                error = "The selected file is encrypted with key size of " + encryptedPGPObject.getRsaKeyLength() +
                                        ", but the selected keys are of key size " + myKey.getKeySize();
                                runOnUiThread(failure);
                                return;
                            }

                            String fileName = encryptedPGPObject.getFileName();
                            String decPath = HelperFunctions.getExternalStoragePath() + Constants.DEC_DIRECTORY;
                            String path = decPath + Constants.SEPARATOR + fileName;
                            PGPReturnData decryptedData = pgp.decrypt(encryptedPGPObject);

                            if(!decryptedData.isError()){
                                boolean b = HelperFunctions.writeOriginalFileFromBytesData(decryptedData.getDecryptedData(), fileName);

                                if (b) {
                                    runOnUiThread(success);
                                }
                                else {
                                    runOnUiThread(failure);
                                }
                            }
                            else{
                                //SOME ERROR
                                isErrorInDecryption = true;
                                error = decryptedData.getException().getMessage();
                                runOnUiThread(failure);
                            }
                        }
                    };

                    executorService.execute(runnable);
                }
                else{
                    Snackbar.make(view, "Please Select Keys", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        btn_dec_myKeyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myKey != null) {
                    View dialView = LayoutInflater.from(DecryptActivity.this).inflate(R.layout.item_keys_for_select, null);
                    TextView tv_myKeysOwner = dialView.findViewById(R.id.tv_keysForSelectOwner);
                    final TextView tv_myKeysKeyName = dialView.findViewById(R.id.tv_keysForSelectKeyName);
                    TextView tv_myKeysKeySize = dialView.findViewById(R.id.tv_keysForSelectKeySize);

                    if (myKey != null) {
                        tv_myKeysOwner.setText(myKey.getOwner());
                        tv_myKeysKeyName.setText(myKey.getKeyName());
                        tv_myKeysKeySize.setText(myKey.getKeySize() + "");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(DecryptActivity.this);
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
                    View view = findViewById(R.id.linear_decMyKeyContainer);
                    Snackbar.make(view, "No Key Selected", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btn_dec_otherKeyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (othersKey != null) {
                    View dialView = LayoutInflater.from(DecryptActivity.this).inflate(R.layout.item_keys_for_select, null);
                    TextView tv_myKeysOwner = dialView.findViewById(R.id.tv_keysForSelectOwner);
                    final TextView tv_myKeysKeyName = dialView.findViewById(R.id.tv_keysForSelectKeyName);
                    TextView tv_myKeysKeySize = dialView.findViewById(R.id.tv_keysForSelectKeySize);

                    if (othersKey != null) {
                        tv_myKeysOwner.setText(othersKey.getOwner());
                        tv_myKeysKeyName.setText(othersKey.getKeyName());
                        tv_myKeysKeySize.setText(othersKey.getKeySize() + "");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(DecryptActivity.this);
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
                    View view = findViewById(R.id.linear_decOthersKeyContainer);
                    Snackbar.make(view, "No Other's Key Selected", Snackbar.LENGTH_SHORT).show();
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

                if(!HelperFunctions.isValidPGPDataFile(fileName)){
                    Snackbar.make(view, "The selected file is not a PGP Data File, Please change.", Snackbar.LENGTH_LONG).show();
                }
                filePath = path;
                tv_dec_FileName.setText(filePath);

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

                        View view = LayoutInflater.from(DecryptActivity.this).inflate(R.layout.dialog_enterpassword, null);

                        final EditText et_enterpswd = view.findViewById(R.id.et_enterpswd);
                        final Button btn_confirmpswd = view.findViewById(R.id.btn_confirmpswd);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DecryptActivity.this);
                        builder.setView(view);
                        builder.setTitle("Enter Password");
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
                                        tv_dec_myKey.setText(myKey.getKeyName());
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
                        tv_dec_othersKey.setText(othersKey.getKeyName());
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
            if (resultCode == Activity.RESULT_OK) {
                int type = resultData.getIntExtra(Constants.KEY_SELECT_TYPE, Constants.KEY_SELECT_SELF);
                String returnKeyPath = resultData.getStringExtra(Constants.RETURN_PATH);
                final KeySerializable keySerializable = (KeySerializable) resultData.getSerializableExtra(Constants.RETURN_KEY);
                System.out.println("==============" + keySerializable);

                if (type == Constants.KEY_SELECT_SELF) {
                    if (keySerializable.getKeyType().equals(Constants.BOTHKEY)) {

                        View view = LayoutInflater.from(DecryptActivity.this).inflate(R.layout.dialog_enterpassword, null);

                        final EditText et_enterpswd = view.findViewById(R.id.et_enterpswd);
                        final Button btn_confirmpswd = view.findViewById(R.id.btn_confirmpswd);
                        final Button btn_cancelpswd = view.findViewById(R.id.btn_cancelpswd);
                        AlertDialog.Builder builder = new AlertDialog.Builder(DecryptActivity.this);
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

                                if (TextUtils.isEmpty(pswd)) {
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

                                        if (privateKey == null) {
                                            //error here
                                            runOnUiThread(wrongpswd);
                                            return;
                                        }

                                        myKey = keySerializable;
                                        tv_dec_myKey.setText(myKey.getKeyName());
                                        password = pswd;
                                        runOnUiThread(correctpswd);

                                    }
                                });
                            }
                        });
                    } else {
                        System.out.println("Not a Both key");
                    }
                } else {
                    if (keySerializable.getKeyType().equals(Constants.PUBLICKEY)) {
                        othersKey = keySerializable;
                        tv_dec_othersKey.setText(othersKey.getKeyName());
                    } else {
                        System.out.println("Not a public key");
                    }
                }

            }
        }
    }

}