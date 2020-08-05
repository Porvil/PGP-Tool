package com.dev_pd.pgptool.UI;

import android.os.Environment;

import com.dev_pd.pgptool.Constants;
import com.dev_pd.pgptool.Cryptography.EncryptedPGPObject;
import com.dev_pd.pgptool.Cryptography.KeySerializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class HelperFunctions {

    public static String getExternalStoragePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static boolean isValidKeyFile(String name){
        if(name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0)
            if (name.substring(name.lastIndexOf(".")).equals(Constants.EXTENSION_KEY))
                return true;

        return false;
    }

    private static boolean writeSerializableObject(String fileName, String extension, Object object, String directory){
        //Checking the availability state of the External Storage.
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            System.out.println("Storage is not mounted, returning!!");
            return false;
        }

        String path = getExternalStoragePath() + directory;
        File rootFile = new File(path);
        if(!rootFile.exists()){
            boolean mkdir = rootFile.mkdirs();
            if(!mkdir){
                System.out.println("Path/file couldn't be created");
                return false;
            }
        }

        String filePath = path + Constants.SEPARATOR + fileName + extension;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            out.writeObject(object);

            out.close();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean writeKeySerializableSelf(String fileName, String extension, Object object){
        return writeSerializableObject(fileName, extension, object, Constants.SELF_DIRECTORY);
    }

    public static boolean writeEncryptedData(String fileName, String extension, Object object){
        return writeSerializableObject(fileName, extension, object, Constants.ENC_DIRECTORY);
    }

    public static boolean writeKeySerializableOther(String fileName, String extension, Object object){
        return writeSerializableObject(fileName, extension, object, Constants.OTHERS_DIRECTORY);
    }

    public static Boolean writeTempKeyForSharing(String path, Object object){

        //Checking the availability state of the External Storage.
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            System.out.println("Storage is not mounted, returning!!");
            return false;
        }

        String rootPath = getExternalStoragePath() + Constants.TEMP_DIRECTORY;
        File rootFile = new File(rootPath);
        if(!rootFile.exists()){
            boolean mkdir = rootFile.mkdirs();
            if(!mkdir){
                System.out.println("Path/file couldn't be created");
                return false;
            }
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            out.writeObject(object);

            out.close();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static Object readSerializedObject(String path){
        Object object1 = null;

        try{
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);

            object1 = (Object) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
            System.out.println(ex.getMessage());
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }

        return object1;
    }

    public static KeySerializable readKey(String path){
        KeySerializable object;
        try {
            object = (KeySerializable) readSerializedObject(path);
        }
        catch (ClassCastException e){
            System.out.println("Exception = " + e);
            return null;
        }

        if(object == null)
            return null;

        return object;
    }

    public static EncryptedPGPObject readEncryptedFile(String path){
        EncryptedPGPObject object;
        try {
            object = (EncryptedPGPObject) readSerializedObject(path);
        }
        catch (ClassCastException e){
            System.out.println("Exception = " + e);
            return null;
        }

        if(object == null)
            return null;

        return object;
    }

    public static byte[] readFileToBytes(String path){
        byte fileContent[] = null;

        File file = new File(path);
        FileInputStream fin = null;

        try {
            fin = new FileInputStream(file);
            fileContent = new byte[(int)file.length()];

            fin.read(fileContent);
            String s = new String(fileContent);
            System.out.println("File content: " + s);
            fin.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }

        return fileContent;
    }

    public static boolean writeOriginalFileFromBytesData(byte[] bytes, String fileName){
        try {
            String rootPath =getExternalStoragePath() + Constants.DEC_DIRECTORY;
            File rootFile = new File(rootPath);
            if(!rootFile.exists()){
                boolean mkdir = rootFile.mkdirs();
                if(!mkdir){
                    System.out.println("Path/file couldn't be created");
                    return false;
                }
            }

            String path = rootPath + Constants.SEPARATOR + fileName;
            File file = new File(path);
            boolean newFile = file.createNewFile();
            System.out.println(newFile);
            OutputStream os = new FileOutputStream(file);

            os.write(bytes);
            System.out.println("bytes data written in file successfully.");

            os.close();
            return true;
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        return false;
    }

}
