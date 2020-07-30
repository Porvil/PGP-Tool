package com.dev_pd.pgptool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TextView tv_myKeysOwner = holder.view.findViewById(R.id.tv_myKeysOwner);
        TextView tv_myKeysKeyName = holder.view.findViewById(R.id.tv_myKeysKeyName);
        TextView tv_myKeysKeySize = holder.view.findViewById(R.id.tv_myKeysKeySize);

        Button btn_myKeysPrivateKey = holder.view.findViewById(R.id.btn_myKeysPrivateKey);
        Button btn_myKeysPublicKey = holder.view.findViewById(R.id.btn_myKeysPublicKey);

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

                HelperFunctions.writeTempFileExternalStorage(tempPath, publicKey);

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

            }
        });
    }

    @Override
    public int getItemCount() {
        return keySerializables.size();
    }
}