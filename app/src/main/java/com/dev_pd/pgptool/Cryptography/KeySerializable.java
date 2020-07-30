package com.dev_pd.pgptool.Cryptography;

import com.dev_pd.pgptool.Constants;

import java.io.Serializable;
import java.util.Objects;

public class KeySerializable implements Serializable {

    private static final long serialVersionUID = 40L;
    private String keyName;
    private String keyType;
    private PrivateKeySerializable privateKeySerializable;
    private PublicKeySerializable publicKeySerializable;

    public KeySerializable(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeySerializable that = (KeySerializable) o;
        return keyName.equals(that.keyName) &&
                keyType.equals(that.keyType) &&
                Objects.equals(privateKeySerializable, that.privateKeySerializable) &&
                publicKeySerializable.equals(that.publicKeySerializable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyName, keyType, privateKeySerializable, publicKeySerializable);
    }

    public KeySerializable(String keyName, String keyType, PublicKeySerializable publicKeySerializable) {
        this.keyName = keyName;
        this.keyType = keyType;
        this.publicKeySerializable = publicKeySerializable;
    }

    public KeySerializable(String keyName, String keyType, PrivateKeySerializable privateKeySerializable, PublicKeySerializable publicKeySerializable) {
        this.keyName = keyName;
        this.keyType = keyType;
        this.privateKeySerializable = privateKeySerializable;
        this.publicKeySerializable = publicKeySerializable;
    }

    public String getKeyType() {
        return keyType;
    }

    public PrivateKeySerializable getPrivateKeySerializable() {
         return keyType.equals(Constants.BOTHKEY) ? privateKeySerializable : null;
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
                "keyName='" + keyName + '\'' +
                ", keyType='" + keyType + '\'' +
                ", privateKeySerializable=" + privateKeySerializable +
                ", publicKeySerializable=" + publicKeySerializable +
                '}';
    }

    public String getKeyName() {
        return keyName;
    }

    public KeySerializable getShareableKey(){
        return new KeySerializable(keyName, Constants.PUBLICKEY, publicKeySerializable);
    }

}


