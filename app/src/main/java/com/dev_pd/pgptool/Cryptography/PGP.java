package com.dev_pd.pgptool.Cryptography;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PGP {

    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    private PublicKey myPublicKey;
    private PublicKey othersPublicKey;
    private PrivateKey myPrivateKey;

    public PGP() throws NoSuchAlgorithmException {
//        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(1024);
//
//        // Generate the KeyPair
//        keyPair = keyPairGenerator.generateKeyPair();
//
//        // Get the public and private key
//        myPublicKey = keyPair.getPublic();
//        othersPublicKey = keyPair.getPublic();
//        myPrivateKey = keyPair.getPrivate();
    }

    public void setMyPublicKey(PublicKey myPublicKey) {
        this.myPublicKey = myPublicKey;
    }

    public void setOthersPublicKey(PublicKey othersPublicKey) {
        this.othersPublicKey = othersPublicKey;
    }

    public void setMyPrivateKey(PrivateKey myPrivateKey) {
        this.myPrivateKey = myPrivateKey;
    }
    
    
    
    boolean check(){
        return (myPublicKey != null && myPrivateKey != null && othersPublicKey != null);            
    }

    public EncrpytedPGPObject encrypt(String data){
        if(!check()){
            System.out.println("KEYS NOT SET");
            return null;
        }
        try {
            byte[] dataBytes = Utility.getBytes(data);
            return __encrypt(dataBytes);
        } catch (UnsupportedEncodingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException |
                NoSuchProviderException |
                InvalidAlgorithmParameterException |
                SignatureException ex) {
            System.out.println("Exception = " + ex.toString());
            Logger.getLogger(PGP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public EncrpytedPGPObject encrypt(byte[] dataBytes){
        if(!check()){
            System.out.println("KEYS NOT SET");
            return null;
        }
        try {
            return __encrypt(dataBytes);
        } catch (UnsupportedEncodingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException |
                NoSuchProviderException |
                InvalidAlgorithmParameterException |
                SignatureException ex) {
            System.out.println("Exception = " + ex.toString());
            Logger.getLogger(PGP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private EncrpytedPGPObject __encrypt(byte[] dataBytes) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, SignatureException {
        
        // Random Session Key for AES Encryption
        KeyGenerator aesKeyGenerator = KeyGenerator.getInstance("AES");
        SecretKey aesSessionKey = aesKeyGenerator.generateKey();
        
        // Digital Signature Using SHA1withRSA using myPrivate Key
        Signature sha1WithRSASigner = Signature.getInstance("SHA1withRSA");
        sha1WithRSASigner.initSign(myPrivateKey);
        sha1WithRSASigner.update(dataBytes);
        byte[] digitalSignature = sha1WithRSASigner.sign();

        // Encrypting Data using Session Key
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesSessionKey);
        byte[] iv = aesCipher.getIV();
        byte[] cipherText = aesCipher.doFinal(dataBytes);
        
        // Encrypting Session Key using Recipient's Public Key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, othersPublicKey);
        byte[] encryptedAESKey = rsaCipher.doFinal(aesSessionKey.getEncoded());
        
        
        EncrpytedPGPObject encrpytedPGPObject = new EncrpytedPGPObject("PD.txt", 1024, digitalSignature, iv, cipherText, encryptedAESKey);
        System.out.println(encrpytedPGPObject);
        
        return encrpytedPGPObject ;       
    }

    public void decrypt(EncrpytedPGPObject encrpytedPGPObject){
        if(encrpytedPGPObject == null){
            System.out.println("Null object passed as pgp object");
            return;
        }
        
        try {
            __decrypt(encrpytedPGPObject);
        } catch (UnsupportedEncodingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException |
                NoSuchProviderException |
                InvalidAlgorithmParameterException |
                SignatureException ex) {
            System.out.println("Exception = " + ex.toString());
            Logger.getLogger(PGP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void __decrypt(EncrpytedPGPObject encrpytedPGPObject) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, SignatureException {
        
        byte[] encryptedAESKey = encrpytedPGPObject.getEncryptedAESKey();
        byte[] iv = encrpytedPGPObject.getIv();
        byte[] cipherText = encrpytedPGPObject.getCipherText();
        byte[] digitalSignature = encrpytedPGPObject.getDigitalSignature();
        
        // Decrypting Session Key using myPrivate Key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, myPrivateKey);
        byte[] decryptedAESKey = rsaCipher.doFinal(encryptedAESKey);
        SecretKey aesSessionKey = new SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.length, "AES");
        
        // Decrypting Cipher Text using Session Key
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, aesSessionKey, new IvParameterSpec(iv));
        byte[] decryptedDataBytes = aesCipher.doFinal(cipherText);
        String decryptedData = Utility.getString(decryptedDataBytes);
        
        // Digital signature SHA1withRSA using other's Public Key
        Signature sha1WithRSASigner = Signature.getInstance("SHA1withRSA");
        sha1WithRSASigner.initVerify(othersPublicKey);
        sha1WithRSASigner.update(decryptedDataBytes);
        boolean isCorrect = sha1WithRSASigner.verify(digitalSignature);
        
        System.out.println("SIG = " + isCorrect);
        System.out.println("====");
        System.out.println(decryptedData);
        
    }

}
