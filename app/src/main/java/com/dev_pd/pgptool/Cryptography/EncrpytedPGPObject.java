package com.dev_pd.pgptool.Cryptography;

import java.io.Serializable;
import javax.crypto.Cipher;

public class EncrpytedPGPObject implements  Serializable{
    
    private String fileName;
    private int rsaKeyLength;
    private byte[] digitalSignature;
    private byte[] iv;
    private byte[] cipherText;
    private byte[] encryptedAESKey;

    public EncrpytedPGPObject(String fileName, int rsaKeyLength, byte[] digitalSignature, byte[] iv, byte[] cipherText, byte[] encryptedAESKey) {
        this.fileName = fileName;
        this.rsaKeyLength = rsaKeyLength;
        this.digitalSignature = digitalSignature;
        this.iv = iv;
        this.cipherText = cipherText;
        this.encryptedAESKey = encryptedAESKey;
    }

    @Override
    public String toString() {
        return "EncrpytedPGPObject{" + "fileName=" + fileName + ", rsaKeyLength=" + rsaKeyLength + ", digitalSignature=" + digitalSignature + ", iv=" + iv + ", cipherText=" + cipherText + ", encryptedAESKey=" + encryptedAESKey + '}';
    }

    public String getFileName() {
        return fileName;
    }

    public int getRsaKeyLength() {
        return rsaKeyLength;
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public byte[] getEncryptedAESKey() {
        return encryptedAESKey;
    }
    
    
    
}
