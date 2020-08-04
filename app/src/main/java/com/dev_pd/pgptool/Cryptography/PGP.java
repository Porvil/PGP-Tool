package com.dev_pd.pgptool.Cryptography;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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

    private int keySize;
    private PublicKey myPublicKey;
    private PublicKey othersPublicKey;
    private PrivateKey myPrivateKey;

    public PGP(int keySize) {
        this.keySize = keySize;
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

    public boolean isKeysSet(){
        return !check();
    }

    private boolean check(){
        return (myPublicKey != null && myPrivateKey != null && othersPublicKey != null);            
    }

    public EncryptedPGPObject encrypt(String data, String fileName){
        if(!check()){
            System.out.println("KEYS NOT SET");
            return null;
        }
        try {
            byte[] dataBytes = Utility.getBytes(data);
            return __encrypt(dataBytes, fileName);
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
    
    public EncryptedPGPObject encrypt(byte[] dataBytes, String fileName){
        if(!check()){
            System.out.println("KEYS NOT SET");
            return null;
        }
        try {
            return __encrypt(dataBytes, fileName);
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
    
    private EncryptedPGPObject __encrypt(byte[] dataBytes, String fileName) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, SignatureException {
        
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
        
        
        EncryptedPGPObject encryptedPGPObject = new EncryptedPGPObject(fileName, keySize, digitalSignature, iv, cipherText, encryptedAESKey);
        System.out.println(encryptedPGPObject);
        
        return encryptedPGPObject;
    }

    public byte[] decrypt(EncryptedPGPObject encryptedPGPObject){
        if(encryptedPGPObject == null){
            System.out.println("Null object passed as pgp object");
            return null;
        }
        
        try {
            return __decrypt(encryptedPGPObject);
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
    
    private byte[] __decrypt(EncryptedPGPObject encryptedPGPObject) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, SignatureException {
        
        byte[] encryptedAESKey = encryptedPGPObject.getEncryptedAESKey();
        byte[] iv = encryptedPGPObject.getIv();
        byte[] cipherText = encryptedPGPObject.getCipherText();
        byte[] digitalSignature = encryptedPGPObject.getDigitalSignature();
        
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

        return decryptedDataBytes;
    }

}
