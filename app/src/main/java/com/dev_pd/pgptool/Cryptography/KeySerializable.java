package com.dev_pd.pgptool.Cryptography;

import android.os.Parcelable;

import java.io.Serializable;

public class KeySerializable implements Serializable {

    private static final long serialVersionUID = 40L;
    private String keyType;
    private PrivateKeySerializable privateKeySerializable;
    private PublicKeySerializable publicKeySerializable;

    public KeySerializable(){

    }

    public KeySerializable(String keyType, PublicKeySerializable publicKeySerializable) {
        this.keyType = keyType;
        this.publicKeySerializable = publicKeySerializable;
    }

    public KeySerializable(String keyType, PrivateKeySerializable privateKeySerializable, PublicKeySerializable publicKeySerializable) {
        this.keyType = keyType;
        this.privateKeySerializable = privateKeySerializable;
        this.publicKeySerializable = publicKeySerializable;
    }

    public String getKeyType() {
        return keyType;
    }

    public PrivateKeySerializable getPrivateKeySerializable() {
         return keyType.equals("BOTH") ? privateKeySerializable : null;
    }

    public PublicKeySerializable getPublicKeySerializable() {
        return publicKeySerializable;
    }

    public int getKeySize(){
        return publicKeySerializable.getKeySize();
    }

    public String getOwner(){
        return publicKeySerializable.getOwner();
    }

    @Override
    public String toString() {
        return "KeySerializable{" +
                "keyType='" + keyType + '\'' +
                ", privateKeySerializable=" + privateKeySerializable +
                ", publicKeySerializable=" + publicKeySerializable +
                '}';
    }
}

