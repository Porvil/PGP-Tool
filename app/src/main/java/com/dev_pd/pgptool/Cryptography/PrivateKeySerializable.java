package com.dev_pd.pgptool.Cryptography;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Objects;

public class PrivateKeySerializable implements Serializable {

    private static final long serialVersionUID = 42L;
    private String owner;
    private int keySize;
    private PrivateKey privateKey;
    private byte[] hashPassword;
    private byte[] salt;

    public PrivateKeySerializable(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateKeySerializable that = (PrivateKeySerializable) o;
        return keySize == that.keySize &&
                owner.equals(that.owner) &&
                privateKey.equals(that.privateKey) &&
                Arrays.equals(hashPassword, that.hashPassword) &&
                Arrays.equals(salt, that.salt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(owner, keySize, privateKey);
        result = 31 * result + Arrays.hashCode(hashPassword);
        result = 31 * result + Arrays.hashCode(salt);
        return result;
    }

    public PrivateKeySerializable(String owner, int keySize, PrivateKey privateKey, byte[] hashPassword, byte[] salt) {
        this.owner = owner;
        this.keySize = keySize;
        this.privateKey = privateKey;
        this.hashPassword = hashPassword;
        this.salt = salt;
    }

    public String getOwner() {
        return owner;
    }

    public int getKeySize() {
        return keySize;
    }

    public PrivateKey getPrivateKey(String password) {
        try {
            if(checkIsValidUser(password))
                return privateKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        System.out.println("Exception or wrong pswd");
        return null;
    }

    private boolean checkIsValidUser(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(salt == null){
            System.out.println("SALT IS NULL");
            return false;
        }

        byte[] hash = Utility.getHash(password, salt);

        return Arrays.equals(hash, hashPassword);
    }
}
