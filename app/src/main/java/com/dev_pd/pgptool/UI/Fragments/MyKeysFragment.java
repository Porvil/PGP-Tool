package com.dev_pd.pgptool.UI.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;
import com.dev_pd.pgptool.UI.Adapters.MyKeysAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyKeysFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<KeySerializable> keySerializables;
    private ArrayList<String> keysPath;

    private ExecutorService executorService;
    private Runnable runnable;

    public MyKeysFragment() {

    }

    public static MyKeysFragment newInstance() {
        MyKeysFragment fragment = new MyKeysFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_keys, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        executorService = Executors.newSingleThreadExecutor();

        keySerializables = new ArrayList<>();
        keysPath = new ArrayList<>();
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
        mAdapter = new MyKeysAdapter(context, keySerializables, keysPath, view, Constants.TYPE_VIEW);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

//        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(context);
//        loadMyKeysTask.execute();

        final Runnable refresh = new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        };


        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("----------------------------------------------------------");
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SELF_DIRECTORY;
                File directory = new File(path);
                File[] files = directory.listFiles();

                keysPath.clear();
                keySerializables.clear();
                if(files != null && files.length > 0) {
                    for (File curFile : files) {
                        String curPath = curFile.getAbsolutePath();
                        System.out.println(curFile.getName());
                        String name = curFile.getName();
                        if (HelperFunctions.isValidKeyFile(name)) {
                            KeySerializable keySerializable = HelperFunctions.readKey(curPath);
                            System.out.println(keySerializable);
                            if (!keySerializables.contains(keySerializable)) {
                                String keyNameInsideFile = keySerializable.getKeyName() + Constants.EXTENSION_KEY;
                                if (keyNameInsideFile.equals(curFile.getName())) {
                                    keySerializables.add(keySerializable);
                                    keysPath.add(curPath);
                                }
                            }
                            System.out.println(keySerializable != null);
                        }
                        else {
                            System.out.println("shit : " + name);
                        }

                    }
                }

                getActivity().runOnUiThread(refresh);
            }
        };

        executorService.execute(runnable);

    }

    @Override
    public void onRefresh() {
//        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(context);
//        loadMyKeysTask.execute();

        executorService.execute(runnable);

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

            keysPath.clear();
            keySerializables.clear();
            if(files != null && files.length > 0) {
                for (File curFile : files) {
                    String curPath = curFile.getAbsolutePath();
                    System.out.println(curFile.getName());
                    String name = curFile.getName();
                    if (HelperFunctions.isValidKeyFile(name)) {
                        KeySerializable keySerializable = HelperFunctions.readKey(curPath);
                        System.out.println(keySerializable);
                        if (!keySerializables.contains(keySerializable)) {
                            String keyNameInsideFile = keySerializable.getKeyName() + Constants.EXTENSION_KEY;
                            if (keyNameInsideFile.equals(curFile.getName())) {
                                keySerializables.add(keySerializable);
                                keysPath.add(curPath);
                            }
                        }
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