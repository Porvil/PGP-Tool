package com.dev_pd.pgptool;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.UI.HelperFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.dev_pd.pgptool.UI.SectionsPagerAdapter;

import java.security.Key;
import java.util.ArrayList;

public class ViewKeysActivity extends AppCompatActivity {

    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;
    TabLayout tabs;
    ArrayList<KeySerializable> myKeys;
    ArrayList<KeySerializable> othersKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_keys);
        myKeys = new ArrayList<>();

        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        LoadMyKeysTask loadMyKeysTask = new LoadMyKeysTask(this);
        loadMyKeysTask.execute();

    }

    class LoadMyKeysTask extends AsyncTask<Void, Integer, Void> {

        Context context;
        ProgressDialog progressDialog;

        public LoadMyKeysTask(Context context) {
            this.context = context;
            //            this.keySerializables = keySerializables;
        }

        @Override
        protected Void doInBackground(Void... integers) {

             myKeys = HelperFunctions.readKeys();
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

            sectionsPagerAdapter = new SectionsPagerAdapter(context, getSupportFragmentManager(), myKeys);
            viewPager.setAdapter(sectionsPagerAdapter);
            tabs.setupWithViewPager(viewPager);

        }


    }

}