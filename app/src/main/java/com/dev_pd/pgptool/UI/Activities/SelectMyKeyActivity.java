package com.dev_pd.pgptool.UI.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Others.Constants;
import com.dev_pd.pgptool.Others.HelperFunctions;
import com.dev_pd.pgptool.R;
import com.dev_pd.pgptool.UI.Adapters.MyKeysAdapter;
import com.dev_pd.pgptool.UI.Adapters.OthersKeyAdapter;
import com.dev_pd.pgptool.UI.Interfaces.OnKeySelectListener;

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
    private Button btn_confirm;
    private TextView textView;
    private ArrayList<KeySerializable> keySerializables;
    private ArrayList<String> keysPath;

    private ExecutorService executorService;
    private Runnable runnableMyKeys;
    private Runnable runnableOtherKeys;

    private String finalKeyPath;
    private KeySerializable finalKey;

    private int keyViewType = Constants.KEY_SELECT_SELF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_my_key);

        Intent intent = getIntent();
        if(intent != null){
            keyViewType = intent.getIntExtra(Constants.KEY_SELECT_TYPE, Constants.KEY_SELECT_SELF);
        }

        view = findViewById(R.id.linear_selectMyKey);
        executorService = Executors.newSingleThreadExecutor();

        keySerializables = new ArrayList<>();
        keysPath = new ArrayList<>();
        context = this;
        recyclerView = findViewById(R.id.rv_selectMyKeys);
        textView = findViewById(R.id.tv_selectKeySelectedKey);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        if(keyViewType == Constants.KEY_SELECT_SELF)
            mAdapter = new MyKeysAdapter(context, keySerializables, keysPath, view, Constants.TYPE_SELECT, this);
        else
            mAdapter = new OthersKeyAdapter(context, keySerializables, keysPath, view, Constants.TYPE_SELECT, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        btn_confirm = findViewById(R.id.btn_selectKeyReturn);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.KEY_SELECT_TYPE, keyViewType);
                returnIntent.putExtra(Constants.RETURN_PATH, finalKeyPath);
                returnIntent.putExtra(Constants.RETURN_KEY, finalKey);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        final Runnable refresh = new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        };


        runnableMyKeys = new Runnable() {
            @Override
            public void run() {
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
                        }
                    }
                }

                runOnUiThread(refresh);
            }
        };


        runnableOtherKeys = new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.OTHERS_DIRECTORY;
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
                        }
                    }
                }

                runOnUiThread(refresh);
            }
        };

        if(keyViewType == Constants.KEY_SELECT_SELF)
            executorService.execute(runnableMyKeys);
        else
            executorService.execute(runnableOtherKeys);

    }

    @Override
    public void onKeySelect(KeySerializable keySerializable, String keyPath) {
        textView.setText(keyPath);
        finalKey = keySerializable;
        finalKeyPath = keyPath;
    }

}