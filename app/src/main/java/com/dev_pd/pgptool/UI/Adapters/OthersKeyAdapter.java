package com.dev_pd.pgptool.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;
import com.dev_pd.pgptool.UI.Interfaces.OnKeySelectListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class OthersKeyAdapter extends RecyclerView.Adapter<OthersKeyAdapter.MyViewHolder> {

    private View parentView;
    private Context context;
    private ArrayList<KeySerializable> keySerializables;
    private ArrayList<String> keysPath;
    private int type;
    private OnKeySelectListener onKeySelectListener;
    public int selectedItem = 0;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public OthersKeyAdapter(Context context, ArrayList<KeySerializable> keySerializables, ArrayList<String> keysPath, View parentView, int type) {
        this.keySerializables = keySerializables;
        this.keysPath = keysPath;
        this.context = context;
        this.parentView = parentView;
        this.type = type;
    }

    public OthersKeyAdapter(Context context,ArrayList<KeySerializable> keySerializables, ArrayList<String> keysPath, View parentView, int type, OnKeySelectListener onKeySelectListener) {
        this.keySerializables = keySerializables;
        this.keysPath = keysPath;
        this.context = context;
        this.parentView = parentView;
        this.type = type;
        this.onKeySelectListener = onKeySelectListener;
    }

    @Override
    public OthersKeyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;
        if(type == Constants.TYPE_VIEW){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_otherskeys, parent, false);
        }
        else if(type == Constants.TYPE_SELECT){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_keys_for_select, parent, false);
        }

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(type == Constants.TYPE_SELECT) {
            if (selectedItem == position) {
                holder.view.setScaleX(Constants.MIN_X);
                holder.view.setSelected(true);
                onKeySelectListener.onKeySelect(keySerializables.get(position), keysPath.get(position));
            }
            else {
                holder.view.setScaleX(Constants.MAX_X);
                holder.view.setSelected(false);
            }

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedItem);
                    selectedItem = position;
                    notifyItemChanged(selectedItem);
                }
            });

            TextView tv_myKeysOwner = holder.view.findViewById(R.id.tv_keysForSelectOwner);
            TextView tv_myKeysKeyName = holder.view.findViewById(R.id.tv_keysForSelectKeyName);
            TextView tv_myKeysKeySize = holder.view.findViewById(R.id.tv_keysForSelectKeySize);

            final KeySerializable keySerializable = keySerializables.get(position);
            if(keySerializable != null) {
                tv_myKeysOwner.setText(keySerializable.getOwner());
                tv_myKeysKeyName.setText(keySerializable.getKeyName());
                tv_myKeysKeySize.setText(keySerializable.getKeySize()+"");
            }

        }

        else if(type == Constants.TYPE_VIEW) {
            TextView tv_myKeysOwner = holder.view.findViewById(R.id.tv_othersKeysOwner);
            TextView tv_myKeysKeyName = holder.view.findViewById(R.id.tv_othersKeysKeyName);
            TextView tv_myKeysKeySize = holder.view.findViewById(R.id.tv_othersKeysKeySize);

            Button btn_othersKeysDelete = holder.view.findViewById(R.id.btn_othersKeysDelete);

            KeySerializable keySerializable = keySerializables.get(position);
            if (keySerializable != null) {
                tv_myKeysOwner.setText(keySerializable.getOwner());
                tv_myKeysKeyName.setText(keySerializable.getKeyName());
                tv_myKeysKeySize.setText(keySerializable.getKeySize() + "");
            }

            btn_othersKeysDelete.setOnClickListener(new View.OnClickListener() {
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
                                    Constants.OTHERS_DIRECTORY +
                                    Constants.SEPARATOR +
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
                                } else {
                                    Snackbar.make(parentView, "Failed to delete Key.", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(parentView, "Key doesn't exist", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return keySerializables.size();
    }
}