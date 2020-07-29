package com.dev_pd.pgptool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev_pd.pgptool.Cryptography.KeySerializable;

import java.util.ArrayList;

class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.MyViewHolder> {
    private ArrayList<KeySerializable> keySerializables;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public KeyAdapter(ArrayList<KeySerializable> keySerializables) {
        this.keySerializables = keySerializables;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public KeyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        TextView viewById = holder.textView.findViewById(R.id.textView);
//        viewById.setText("pos = " + position+1);

        TextView tv_myKeysOwner = holder.view.findViewById(R.id.tv_myKeysOwner);
        TextView tv_myKeysKeyName = holder.view.findViewById(R.id.tv_myKeysKeyName);
        TextView tv_myKeysKeySize = holder.view.findViewById(R.id.tv_myKeysKeySize);

        Button btn_myKeysPrivateKey = holder.view.findViewById(R.id.btn_myKeysPrivateKey);
        Button btn_myKeysPublicKey = holder.view.findViewById(R.id.btn_myKeysPublicKey);

        KeySerializable keySerializable = keySerializables.get(position);
        if(keySerializable != null) {
            tv_myKeysOwner.setText(keySerializable.getOwner());
//        tv_myKeysKeyName.setText(keySerializable.ge);
            tv_myKeysKeySize.setText(keySerializable.getKeySize()+"");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return keySerializables.size();
    }
}