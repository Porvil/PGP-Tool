package com.dev_pd.pgptool.Cryptography;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Utility {
    
    static public KeyPair generateRSAKeyPair(int keySize){
        if( !(keySize == 1024 || keySize == 2048 || keySize == 4096)){
            System.out.println("BAD KEY SIZE PROVIDED");
            return null;
        }

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Exception : " + e);
            return null;
        }

    }
    
    static public String getString(byte[] data){
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("Exception : " + e);
            return null;
        }
    }
    
    static public byte[] getBytes(String data){
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("Exception : " + e);
            return null;
        }
    }

    public static byte[] getHash(String password, byte[] salt){
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return hash;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            System.out.println("Exception : " + e);
            return null;
        }

    }

    public static byte[] getRandomSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

}
