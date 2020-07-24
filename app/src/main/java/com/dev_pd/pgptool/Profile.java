package com.dev_pd.pgptool;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.dev_pd.pgptool.Cryptography.PublicKeySerializable;
import com.dev_pd.pgptool.Cryptography.Utility;
import com.dev_pd.*;
import com.dev_pd.pgptool.UI.HelperFunctions;
import com.dev_pd.pgptool.UI.FileUtilsMine;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_generateKey = view.findViewById(R.id.btn_generateKey);
        final Spinner spinner_keySize = view.findViewById(R.id.spinner_keySize);

        btn_generateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int keySize = 1024;
                String keySizeString = (String)spinner_keySize.getSelectedItem();
                int keySize = Integer.parseInt(keySizeString);
                System.out.println(keySize);
                try {
                    KeyPair keyPair = Utility.generateRSAKeyPair(keySize);
                    System.out.println(keyPair.getPrivate());
                    System.out.println(keyPair.getPublic());

                    System.out.println(Utility.getString(keyPair.getPublic().getEncoded()));
                    System.out.println(keyPair.getPublic().getFormat());
//                    HelperFunctions.writeFileExternalStorage(keyPair);

                    PublicKeySerializable publicKeySerializable = new PublicKeySerializable("pd",keySize, keyPair.getPublic());

                    HelperFunctions.writeFileExternalStorage(" mykey.txt", publicKeySerializable);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        Button button = view.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
//                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

                startActivityForResult(intent, 5000);
            }
        });


        Button button1 = view.findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == 5000
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
//                File file = new File(uri)
                System.out.println(uri.getPath());

                FileUtilsMine fileUtilsMine = new FileUtilsMine(getContext());
                String path = fileUtilsMine.getPath(uri);


                System.out.println(path);

                File file = new File(path);
                System.out.println();
                // Perform operations on the document using its URI.


                PublicKeySerializable publicKeySerializable = HelperFunctions.readPublicKeySerializable(path);

//                System.out.println(publicKeySerializable.getPublicKey().);
                try {
                    System.out.println(Utility.getString(publicKeySerializable.getPublicKey().getEncoded()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}