package com.dev_pd.pgptool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PrivateKeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.io.File;
import java.util.ArrayList;

class MyKeysAdapter extends RecyclerView.Adapter<MyKeysAdapter.MyViewHolder> {

    Context context;
    private ArrayList<KeySerializable> keySerializables;
    private ArrayList<String> keysPath;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public MyKeysAdapter(Context context, ArrayList<KeySerializable> keySerializables, ArrayList<String> keysPath) {
        this.context = context;
        this.keysPath = keysPath;
        this.keySerializables = keySerializables;
    }

    @Override
    public MyKeysAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

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

                String tempPath = HelperFunctions.getPGPDirectoryPath() +
                        Constants.TEMP_DIRECTORY +
                        "/" +
                        keySerializable.getKeyName() +
                        Constants.EXTENSION_KEY;

                Boolean aBoolean = HelperFunctions.writeTempFileExternalStorage(tempPath, publicKey);

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

                View view = LayoutInflater.from(context).inflate(R.layout.changepswd, null);
                final EditText et_oldpswd = view.findViewById(R.id.et_oldpswd);
                final EditText et_newpswd = view.findViewById(R.id.et_newpswd);
                final EditText et_confirmnewpswd = view.findViewById(R.id.et_confirmnewpswd);

                Button btn_changePSWD = view.findViewById(R.id.btn_changePSWD);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("are you sure, you want to delete this key?");
                builder.setView(view);
                builder.setTitle("Change Password");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.findViewById(R.id.et_oldpswd);
                dialog.show();



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
                            et_confirmnewpswd.setError("Doesnt match");
                            incomplete = true;
                        }

                        if(oldP.equals(newP)){
                            et_newpswd.setError("Cant be same as old password");
                            incomplete = true;
                        }

                        if(!incomplete){
                            boolean b = privateKeySerializable.changePassword(oldP, newP);
                            if(b){
                                // change file
                                HelperFunctions.writeFileExternalStorage(keySerializable.getKeyName(), Constants.EXTENSION_KEY, keySerializable);
                                notifyItemChanged(position);
                                Toast.makeText(context, "succesfully changed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                System.out.println(keySerializable);

            }
        });

        btn_myKeysDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("are you sure, you want to delete this key?\nThis action cant be reversed");
                builder.setTitle("Confirm");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final KeySerializable keySerializable = keySerializables.get(position);
                        //Delete from directory
                        String tempPath = HelperFunctions.getPGPDirectoryPath() +
                                Constants.SELF_DIRECTORY +
                                "/" +
                                keySerializable.getKeyName() +
                                Constants.EXTENSION_KEY;
                        File file = new File(tempPath);
                        System.out.println("name " + file.getName());
                        System.out.println("exist " + file.exists());
                        System.out.println("abspath " + file.getAbsolutePath());
                        System.out.println("file " + file.isFile());
                        System.out.println("directory " + file.isDirectory());
                        System.out.println("abs " + file.isAbsolute());
                        System.out.println("hidden " + file.isHidden());
                        System.out.println(tempPath);
                        if (file.exists()) {
                            boolean delete = file.delete();
//                                boolean b = context.deleteFile(tempPath);
                            if (delete) {
                                Toast.makeText(context, "dleeted", Toast.LENGTH_SHORT).show();
                                keySerializables.remove(position);
                                keysPath.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, keySerializables.size());
//                                holder.view.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(context, " not dleeted", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, " doesnt exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return keySerializables.size();
    }
}