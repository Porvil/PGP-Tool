package com.dev_pd.pgptool.Cryptography;

public class PGPReturnData {

    private byte[] decryptedData;
    private EncryptedPGPObject encryptedPGPObject;
    private String pgpOperation;
    private boolean isError;
    private Exception exception;

    public PGPReturnData(String pgpOperation, boolean isError, Exception exception) {
        this.pgpOperation = pgpOperation;
        this.isError = isError;
        this.exception = exception;
    }

    public void setDecryptedData(byte[] decryptedData) {
        this.decryptedData = decryptedData;
    }

    public void setEncryptedPGPObject(EncryptedPGPObject encryptedPGPObject) {
        this.encryptedPGPObject = encryptedPGPObject;
    }

    public byte[] getDecryptedData() {
        return decryptedData;
    }

    public EncryptedPGPObject getEncryptedPGPObject() {
        return encryptedPGPObject;
    }

    public String getPgpOperation() {
        return pgpOperation;
    }

    public boolean isError() {
        return isError;
    }

    public Exception getException() {
        return exception;
    }

}
