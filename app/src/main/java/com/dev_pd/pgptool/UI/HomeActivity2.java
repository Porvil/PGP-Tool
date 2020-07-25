package com.dev_pd.pgptool.UI;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dev_pd.pgptool.Cryptography.EncrpytedPGPObject;
import com.dev_pd.pgptool.Cryptography.PGP;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.pgptool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public class HomeActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainhost);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//        bottomNavigationView.se

//        Button button = findViewById(R.id.button);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PGP pgp = null;
//                try {
//                    pgp = new PGP();
//                    KeyPair keyPair = Utility.generateRSAKeyPair();
//                    pgp.setMyPrivateKey(keyPair.getPrivate());
//                    pgp.setMyPublicKey(keyPair.getPublic());
//                    //remove these later
//                    pgp.setOthersPublicKey(keyPair.getPublic());
//
//                    String data = "PD IS TESTING PGP :p";
//                    EncrpytedPGPObject encrpytedPGPObject = pgp.encrypt(data);
//                    pgp.decrypt(encrpytedPGPObject);
//
//
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//
//        Button button1 = findViewById(R.id.button2);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Navigation.findNavController(R.layout.activity_home2);
//            }
//        });
    }
}