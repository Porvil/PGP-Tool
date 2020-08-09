package com.dev_pd.pgptool.Cryptography;

import com.dev_pd.pgptool.Others.Constants;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Objects;

public class PrivateKeySerializable implements Serializable {

    private static final long serialVersionUID = Constants.SERIALVERSIONUID_PRIVATEKEYSERIALIZABLE;
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
        if(checkIsValidUser(password))
            return privateKey;

        System.out.println("Exception or wrong Password");
        return null;
    }

    private boolean checkIsValidUser(String password) {
        if(salt == null){
            System.out.println("SALT IS NULL");
            return false;
        }

        byte[] hash = Utility.getHash(password, salt);

        return Arrays.equals(hash, hashPassword);
    }

    public boolean changePassword(String oldPswd, String newPswd){
        byte[] hash = Utility.getHash(oldPswd, salt);

        if(Arrays.equals(hash, hashPassword)){
            byte[] newRandomSalt = Utility.getRandomSalt();
            byte[] newHash = Utility.getHash(newPswd, newRandomSalt);
            this.hashPassword = newHash;
            this.salt = newRandomSalt;
            return true;
        }

        return false;
    }

}
