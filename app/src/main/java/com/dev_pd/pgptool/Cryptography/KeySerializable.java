package com.dev_pd.pgptool.Cryptography;

import java.io.Serializable;

public class KeySerializable implements Serializable {

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
}

