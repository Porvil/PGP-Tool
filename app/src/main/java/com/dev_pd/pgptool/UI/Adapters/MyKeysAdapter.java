package com.dev_pd.pgptool.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PrivateKeySerializable;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MyKeysAdapter extends RecyclerView.Adapter<MyKeysAdapter.MyViewHolder> {

    private View parentView;
    private Context context;
    private ArrayList<KeySerializable> keySerializables;
    private ArrayList<String> keysPath;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public MyKeysAdapter(Context context, ArrayList<KeySerializable> keySerializables, ArrayList<String> keysPath, View parentView) {
        this.context = context;
        this.keysPath = keysPath;
        this.keySerializables = keySerializables;
        this.parentView = parentView;
    }

    @Override
    public MyKeysAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mykeys, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        TextView tv_myKeysOwner = holder.view.findViewById(R.id.tv_myKeysOwner);
        final TextView tv_myKeysKeyName = holder.view.findViewById(R.id.tv_myKeysKeyName);
        TextView tv_myKeysKeySize = holder.view.findViewById(R.id.tv_myKeysKeySize);

        Button btn_myKeysPrivateKey = holder.view.findViewById(R.id.btn_myKeysPrivateKey);
        Button btn_myKeysPublicKey = holder.view.findViewById(R.id.btn_myKeysPublicKey);
        Button btn_myKeysDelete = holder.view.findViewById(R.id.btn_myKeysDelete);

        final KeySerializable keySerializable = keySerializables.get(position);
        if(keySerializable != null) {
            tv_myKeysOwner.setText(keySerializable.getOwner());
            tv_myKeysKeyName.setText(keySerializable.getKeyName());
            tv_myKeysKeySize.setText(keySerializable.getKeySize()+"");
        }


        btn_myKeysPublicKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KeySerializable publicKey = keySerializable.getShareableKey();

                String tempPath = HelperFunctions.getExternalStoragePath() +
                        Constants.TEMP_DIRECTORY +
                        Constants.SEPARATOR +
                        keySerializable.getKeyName() +
                        Constants.EXTENSION_KEY;

                Boolean aBoolean = HelperFunctions.writeTempKeyForSharing(tempPath, publicKey);

                final File file = new File(tempPath);
                System.out.println(file.getAbsolutePath());

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(context, Constants.AUTHORITY, file);
                intent.setDataAndType(uri, "*/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                context.startActivity(Intent.createChooser(intent, "Sending key..."));
            }
        });

        btn_myKeysPrivateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = LayoutInflater.from(context).inflate(R.layout.dialog_changepassword, null);
                final EditText et_oldpswd = view.findViewById(R.id.et_oldpswd);
                final EditText et_newpswd = view.findViewById(R.id.et_newpswd);
                final EditText et_confirmnewpswd = view.findViewById(R.id.et_confirmnewpswd);

                Button btn_cancelPSWD = view.findViewById(R.id.btn_dialog_changepassword_cancelKey);
                Button btn_changePSWD = view.findViewById(R.id.btn_dialog_changepassword_changePSWD);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                btn_cancelPSWD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_changePSWD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldP = et_oldpswd.getText().toString();
                        String newP = et_newpswd.getText().toString();
                        String newCP = et_confirmnewpswd.getText().toString();

                        boolean incomplete = false;
                        if(TextUtils.isEmpty(oldP)) {
                            et_oldpswd.setError("Can't Be Empty");
                            incomplete = true;
                        }

                        if(TextUtils.isEmpty(newP)) {
                            et_newpswd.setError("Can't Be Empty");
                            incomplete = true;
                        }

                        if(TextUtils.isEmpty(newCP)) {
                            et_confirmnewpswd.setError("Can't Be Empty");
                            incomplete = true;
                        }

                        PrivateKeySerializable privateKeySerializable = keySerializable.getPrivateKeySerializable();

                        if(privateKeySerializable == null) {
                            System.out.println("PRIVATE KEY EMPTY");
                            return;
                        }

                        if(!newP.equals(newCP)){
                            et_confirmnewpswd.setError("Doesn't match");
                            incomplete = true;
                        }

                        if(oldP.equals(newP)){
                            et_newpswd.setError("Cant be same as old password");
                            incomplete = true;
                        }

                        if(!incomplete){
                            boolean b = privateKeySerializable.changePassword(oldP, newP);
                            if(b){
                                HelperFunctions.writeKeySerializableSelf(keySerializable.getKeyName(), Constants.EXTENSION_KEY, keySerializable);
                                notifyItemChanged(position);
                                Snackbar.make(parentView, "Password Changed Successfully.", Snackbar.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Snackbar.make(view, "Wrong Password", Snackbar.LENGTH_SHORT).show();
                                et_oldpswd.setText("");
                                et_newpswd.setText("");
                                et_confirmnewpswd.setText("");
                            }
                        }
                    }
                });
            }
        });

        btn_myKeysDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view_dialogDelete = LayoutInflater.from(context).inflate(R.layout.dialog_deletekey, null);

                Button btn_cancelKey = view_dialogDelete.findViewById(R.id.btn_dialog_deletekey_cancelKey);
                Button btn_deleteKey = view_dialogDelete.findViewById(R.id.btn_dialog_deletekey_deleteKey);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view_dialogDelete);
                final AlertDialog dialog = builder.create();
                dialog.show();

                btn_cancelKey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_deleteKey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final KeySerializable keySerializable = keySerializables.get(position);
                        //Delete from directory
                        String tempPath = HelperFunctions.getExternalStoragePath() +
                                Constants.SELF_DIRECTORY +
                                "/" +
                                keySerializable.getKeyName() +
                                Constants.EXTENSION_KEY;
                        File file = new File(tempPath);
                        if (file.exists()) {
                            boolean delete = file.delete();
                            if (delete) {
                                keySerializables.remove(position);
                                keysPath.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, keySerializables.size());
                                Snackbar.make(parentView, "Key successfully Deleted.", Snackbar.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Snackbar.make(parentView, "Failed to delete Key.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Snackbar.make(parentView, "Key doesn't exist", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return keySerializables.size();
    }
}