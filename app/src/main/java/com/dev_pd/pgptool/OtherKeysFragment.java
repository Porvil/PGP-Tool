package com.dev_pd.pgptool;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.io.File;
import java.util.ArrayList;

public class OtherKeysFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<KeySerializable> keySerializables;

    public OtherKeysFragment() {
        // Required empty public constructor
    }

    public static OtherKeysFragment newInstance() {
        OtherKeysFragment fragment = new OtherKeysFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_keys, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        keySerializables = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_othersKeys);
        swipeRefreshLayout = view.findViewById(R.id.srl_othersKeys);
        swipeRefreshLayout.setOnRefreshListener(OtherKeysFragment.this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new OthersKeyAdapter(keySerializables);
        recyclerView.setAdapter(mAdapter);

        LoadOthersKeysTask loadOthersKeysTask = new LoadOthersKeysTask(context);
        loadOthersKeysTask.execute();
    }

    @Override
    public void onRefresh() {
        LoadOthersKeysTask loadOthersKeysTask = new LoadOthersKeysTask(context);
        loadOthersKeysTask.execute();
    }

    class LoadOthersKeysTask extends AsyncTask<Void, Integer, Void> {

        Context context;

        public LoadOthersKeysTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... integers) {

            System.out.println("----------------------------------------------------------");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.OTHERS_DIRECTORY;
            File directory = new File(path);
            File[] files = directory.listFiles();

            keySerializables.clear();
            if(files != null && files.length > 0) {
                for (File curFile : files) {
                    String curPath = curFile.getAbsolutePath();
                    System.out.println(curFile.getName());
                    String name = curFile.getName();
                    if (HelperFunctions.isValidKeyFile(name)) {
                        KeySerializable keySerializable = HelperFunctions.readKey(curPath);
                        System.out.println(keySerializable);
                        if (!keySerializables.contains(keySerializable))
                            keySerializables.add(keySerializable);
                        System.out.println(keySerializable != null);
                    } else {
                        System.out.println("shit : " + name);
                    }

                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

}