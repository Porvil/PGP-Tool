package com.dev_pd.pgptool.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;
import com.dev_pd.pgptool.UI.Adapters.MyKeysAdapter;
import com.dev_pd.pgptool.UI.Fragments.MyKeysFragment;
import com.dev_pd.pgptool.UI.OnKeySelectListener;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectMyKeyActivity extends AppCompatActivity implements OnKeySelectListener {

    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textView;
    private ArrayList<KeySerializable> keySerializables;
    private ArrayList<String> keysPath;

    private ExecutorService executorService;
    private Runnable runnable;

    private String finalKeyPath;
    private KeySerializable finalKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_my_key);


        view = findViewById(R.id.linear_selectMyKey);
        executorService = Executors.newSingleThreadExecutor();

        keySerializables = new ArrayList<>();
        keysPath = new ArrayList<>();
        context = this;
        recyclerView = findViewById(R.id.rv_selectMyKeys);

//        swipeRefreshLayout = findViewById(R.id.srl_myKeys);
//        swipeRefreshLayout.setOnRefreshListener(MyKeysFragment.this);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyKeysAdapter(context, keySerializables, keysPath, view, Constants.TYPE_SELECT, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

//        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(context);
//        loadMyKeysTask.execute();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", finalKeyPath);
                returnIntent.putExtra("key", finalKey);
                System.out.println("-----------" + finalKeyPath);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        textView = findViewById(R.id.textView);
        final MyKeysAdapter my = (MyKeysAdapter) mAdapter;

        final Runnable refresh = new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
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

                runOnUiThread(refresh);
            }
        };

        executorService.execute(runnable);


    }

    @Override
    public void onKeySelect(KeySerializable keySerializable, String keyPath) {
        Toast.makeText(this, keySerializable.toString(), Toast.LENGTH_LONG).show();
        textView.setText(keyPath);
        finalKey = keySerializable;
        finalKeyPath = keyPath;
    }
}