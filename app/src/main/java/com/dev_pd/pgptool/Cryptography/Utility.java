package com.dev_pd.pgptool.Cryptography;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Utility {
    
    static public KeyPair generateRSAKeyPair(int keySize) throws NoSuchAlgorithmException{
        if( !(keySize == 1024 || keySize == 2048 || keySize == 4096)){
            System.out.println("BAD KEY SIZE PROVIDED");
            return  null;
        }

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);

        // Generate the KeyPair
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        return keyPair;
    }
    
    static public String getString(byte[] data) throws UnsupportedEncodingException{
        return new String(data, "UTF-8");
    }
    
    static public byte[] getBytes(String data) throws UnsupportedEncodingException{
        return data.getBytes("UTF-8");
    }
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    static public byte[] readFile(String fileName){
        try {
            File file = new File(fileName);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return fileContent;
        } catch (IOException ex) {
            System.out.println("Exception = " + ex.toString());
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public static byte[] getHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return hash;
    }

    public static byte[] getRandomSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

}
