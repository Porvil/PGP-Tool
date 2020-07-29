package com.dev_pd.pgptool;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyKeysFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyKeysFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<KeySerializable> keySerializables;

    public MyKeysFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyKeysFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyKeysFragment newInstance(Context context, ArrayList<KeySerializable> keys, String param1, String param2) {
        MyKeysFragment fragment = new MyKeysFragment();

        System.out.println("on new instance :" + keys.size());


//        ArrayList<KeySerializable> keySerializables = new ArrayList<>();
//        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(context);
//        for (KeySerializable keySerializable: keySerializables) {
//            System.out.println(keySerializable.toString());
//        }
        Bundle args = new Bundle();
        args.putSerializable("key", keys);
//        args.putParcelableArrayList("key", keySerializables);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            keySerializables = (ArrayList<KeySerializable>) getArguments().getSerializable("key");

            System.out.println("on create keysfragment :" + keySerializables.size());

        }

//        keySerializables = new ArrayList<>();
//        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(getContext());
//        loadMyKeysTask.execute();
//        for (KeySerializable keySerializable: keySerializables) {
//            System.out.println(keySerializable.toString());
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_keys, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_myKeys);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new KeyAdapter(keySerializables);
        recyclerView.setAdapter(mAdapter);


    }

    class LoadMyKeysTask extends AsyncTask<Void, Integer, Void>{
    
            Context context;
            ProgressDialog progressDialog;
    
            public LoadMyKeysTask(Context context) {
                this.context = context;
    //            this.keySerializables = keySerializables;
            }
    
            @Override
            protected Void doInBackground(Void... integers) {
                
                keySerializables = HelperFunctions.readKeys();
    //            return true;
                return null;
            }
    
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "","Generating Key. Please wait...", true);
            }
    
            @Override
            protected void onPostExecute(Void v) {
    //            super.onPostExecute(res);
                progressDialog.cancel();
    //            if(!res)
    //                Toast.makeText(context, ":(", Toast.LENGTH_SHORT).show();
    
    //        activity
    
            }
    
        
    }
}