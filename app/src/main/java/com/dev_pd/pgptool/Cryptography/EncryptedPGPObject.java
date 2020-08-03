package com.dev_pd.pgptool.Cryptography;

import java.io.Serializable;
import java.util.Arrays;

public class EncryptedPGPObject implements Serializable{

    private static final long serialVersionUID = 50L;
    private String fileName;
    private int rsaKeyLength;
    private byte[] digitalSignature;
    private byte[] iv;
    private byte[] cipherText;
    private byte[] encryptedAESKey;

    public EncryptedPGPObject(String fileName, int rsaKeyLength, byte[] digitalSignature, byte[] iv, byte[] cipherText, byte[] encryptedAESKey) {
        this.fileName = fileName;
        this.rsaKeyLength = rsaKeyLength;
        this.digitalSignature = digitalSignature;
        this.iv = iv;
        this.cipherText = cipherText;
        this.encryptedAESKey = encryptedAESKey;
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

    @Override
    public String toString() {
        return "EncrpytedPGPObject{" +
                "fileName='" + fileName + '\'' +
                ", rsaKeyLength=" + rsaKeyLength +
                ", digitalSignature=" + Arrays.toString(digitalSignature) +
                ", iv=" + Arrays.toString(iv) +
                ", cipherText=" + Arrays.toString(cipherText) +
                ", encryptedAESKey=" + Arrays.toString(encryptedAESKey) +
                '}';
    }

}
