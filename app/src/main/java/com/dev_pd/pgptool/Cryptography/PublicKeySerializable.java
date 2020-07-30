package com.dev_pd.pgptool.Cryptography;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicKeySerializable that = (PublicKeySerializable) o;
        return keySize == that.keySize &&
                owner.equals(that.owner) &&
                publicKey.equals(that.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, keySize, publicKey);
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
