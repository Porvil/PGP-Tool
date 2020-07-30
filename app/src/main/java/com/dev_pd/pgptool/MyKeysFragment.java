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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.UI.HelperFunctions;

import java.io.File;
import java.util.ArrayList;

public class MyKeysFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<KeySerializable> keySerializables;

    public MyKeysFragment() {
        // Required empty public constructor
    }

    public static MyKeysFragment newInstance(Context context) {
        MyKeysFragment fragment = new MyKeysFragment();

//        Bundle args = new Bundle();
//        args.putSerializable("key", keys);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            keySerializables = (ArrayList<KeySerializable>) getArguments().getSerializable("key");
        }

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

        keySerializables = new ArrayList<>();
        context = getContext();
        recyclerView = view.findViewById(R.id.rv_myKeys);

        swipeRefreshLayout = view.findViewById(R.id.srl_myKeys);
        swipeRefreshLayout.setOnRefreshListener(MyKeysFragment.this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyKeysAdapter(keySerializables);
        recyclerView.setAdapter(mAdapter);

        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(context);
        loadMyKeysTask.execute();

    }

    @Override
    public void onRefresh() {
        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(context);
        loadMyKeysTask.execute();
    }

    class LoadMyKeysTask extends AsyncTask<Void, Integer, Void> {

        Context context;

        public LoadMyKeysTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... integers) {

            System.out.println("----------------------------------------------------------");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SELF_DIRECTORY;
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