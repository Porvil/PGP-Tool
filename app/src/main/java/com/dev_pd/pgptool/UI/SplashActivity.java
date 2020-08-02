package com.dev_pd.pgptool.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.dev_pd.pgptool.Constants;
import com.dev_pd.pgptool.HomeActivity;
import com.dev_pd.pgptool.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        int SPLASH_TIME = Constants.SPLASH_TIME;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentHomeScreen = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intentHomeScreen);
                finish();
            }
        }, SPLASH_TIME);

    }
}