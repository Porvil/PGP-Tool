package com.dev_pd.pgptool.Cryptography;

import java.io.Serializable;
import java.security.PublicKey;

public class PublicKeySerializable implements Serializable {

    private static final long serialVersionUID = 41L;
    private String owner;
    private int keySize;
    private PublicKey publicKey;

    public PublicKeySerializable(String owner, int keySize, PublicKey publicKey) {
        this.owner = owner;
        this.keySize = keySize;
        this.publicKey = publicKey;
    }

    public PublicKeySerializable(){

    }

    public String getOwner() {
        return owner;
    }

    public int getKeySize() {
        return keySize;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
