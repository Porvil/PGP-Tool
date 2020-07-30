package com.dev_pd.pgptool;

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
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        tabs = findViewById(R.id.tabs);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


}