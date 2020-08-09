package com.dev_pd.pgptool.Others;

public class Constants {

    public static final String PGP_ENCRYPT = "Encrypt";
    public static final String PGP_DECRYPT = "Decrypt";
    public static final String KEY_SELECT_TYPE = "keySelectType";
    public static final String RETURN_PATH = "keyPath";
    public static final String RETURN_KEY = "key";
    public static final String PUBLICKEY = "PUBLIC";
    public static final String BOTHKEY = "BOTH";
    
    public static final String DIRECTORY = "/PGP Tool";
    public static final String SELF_DIRECTORY = DIRECTORY + "/My Keys";
    public static final String OTHERS_DIRECTORY = DIRECTORY + "/Others Keys";
    public static final String TEMP_DIRECTORY = DIRECTORY + "/Temp";
    public static final String ENC_DIRECTORY = DIRECTORY + "/Encrypted";
    public static final String DEC_DIRECTORY = DIRECTORY + "/Decrypted";
    public static final String SEPARATOR = "/";
    public static final String EXTENSION_KEY = ".key";
    public static final String EXTENSION_DATA = ".pgpdata";
    
    public static final String AUTHORITY = "com.dev_pd.pgptool.UI.Adapters.MyKeysAdapter";
    
    public static final int PERMISSION_ALL = 1000;
    public static final int ADD_OTHERS_KEY = 4000;
    public static final int SELECT_FILE = 5000;
    public static final int SELECT_SELF_KEY = 5001;
    public static final int SELECT_OTHER_KEY = 5002;
    public static final int SELECT_BROWSE_KEY = 5003;
    
    public static final int KEY_SELECT_SELF = 0;
    public static final int KEY_SELECT_OTHERS = 1;
    public static final int TYPE_SELECT = 0;
    public static final int TYPE_VIEW = 1;
    
    public static final int PASSWORD_LENGTH = 4;
    
    public static final int SPLASH_TIME = 500;

    public static final float MIN_X = 0.98f;
    public static final float MAX_X = 1f;

    public static final long SERIALVERSIONUID_KEYSERIALIZABLE = 40L;
    public static final long SERIALVERSIONUID_PUBLICKEYSERIALIZABLE = 41L;
    public static final long SERIALVERSIONUID_PRIVATEKEYSERIALIZABLE = 42L;
    public static final long SERIALVERSIONUID_ENCRYPTEDPGPOBJECT = 50L;

}
