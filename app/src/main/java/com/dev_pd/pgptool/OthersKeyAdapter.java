package com.dev_pd.pgptool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev_pd.pgptool.Cryptography.KeySerializable;

import java.util.ArrayList;

class OthersKeyAdapter extends RecyclerView.Adapter<OthersKeyAdapter.MyViewHolder> {

    private ArrayList<KeySerializable> keySerializables;
    private ArrayList<String> keysPath;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public OthersKeyAdapter(ArrayList<KeySerializable> keySerializables, ArrayList<String> keysPath) {
        this.keySerializables = keySerializables;
        this.keysPath = keysPath;
    }

    @Override
    public OthersKeyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mykeys, parent, false);

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

        KeySerializable keySerializable = keySerializables.get(position);
        if(keySerializable != null) {
            tv_myKeysOwner.setText(keySerializable.getOwner());
            tv_myKeysKeyName.setText(keySerializable.getKeyName());
            tv_myKeysKeySize.setText(keySerializable.getKeySize()+"");
        }

        btn_myKeysPublicKey.setOnClickListener(new View.OnClickListener() {
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