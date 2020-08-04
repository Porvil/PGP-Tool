package com.dev_pd.pgptool.UI;

import android.os.Environment;

import com.dev_pd.pgptool.Constants;
import com.dev_pd.pgptool.Cryptography.EncryptedPGPObject;
import com.dev_pd.pgptool.Cryptography.KeySerializable;
import com.dev_pd.pgptool.Cryptography.PrivateKeySerializable;
import com.dev_pd.pgptool.Cryptography.PublicKeySerializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class HelperFunctions {

    public static boolean writeFileExternalStorage(String fileName, String extension, Object object){

                    //Checking the availability state of the External Storage.
                    String state = Environment.getExternalStorageState();
                    if (!Environment.MEDIA_MOUNTED.equals(state)) {
                        System.out.println("Storage is not mounted, returning!!");
                        //If it isn't mounted - we can't write into it.
                        return false;
                    }

                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SELF_DIRECTORY;
                    File rootFile = new File(path);
                    if(!rootFile.exists()){
                        boolean mkdir = rootFile.mkdirs();
                        if(!mkdir){
                            System.out.println("Path/file couldn't be created");
                return false;
            }
                    }
        System.out.println(path);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path + "/" + fileName + extension);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            // Method for serialization of object
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

    public static boolean writeFileExternalStorageEnc(String fileName, String extension, Object object){

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println("Storage is not mounted, returning!!");
            //If it isn't mounted - we can't write into it.
            return false;
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.ENC_DIRECTORY;
        File rootFile = new File(path);
        if(!rootFile.exists()){
            boolean mkdir = rootFile.mkdirs();
            if(!mkdir){
                System.out.println("Path/file couldn't be created");
                return false;
            }
        }
        System.out.println(path);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path + "/" + fileName + extension);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            // Method for serialization of object
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

    public static boolean writeFileExternalStorageOther(String fileName, String extension, Object object){

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println("Storage is not mounted, returning!!");
            //If it isn't mounted - we can't write into it.
            return false;
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.OTHERS_DIRECTORY;
        File rootFile = new File(path);
        if(!rootFile.exists()){
            boolean mkdir = rootFile.mkdirs();
            if(!mkdir){
                System.out.println("Path/file couldn't be created");
                return false;
            }
        }
        System.out.println(path);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path + "/" + fileName + extension);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            // Method for serialization of object
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

    public static Boolean writeTempFileExternalStorage(String path, Object object){

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println("Storage is not mounted, returning!!");
            //If it isn't mounted - we can't write into it.
            return false;
        }

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.TEMP_DIRECTORY;
        File rootFile = new File(rootPath);
        if(!rootFile.exists()){
            boolean mkdir = rootFile.mkdirs();
            if(!mkdir){
                System.out.println("Path/file couldn't be created");
                return false;
            }
        }
        System.out.println(path);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

            // Method for serialization of object
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

    public static String getPGPDirectoryPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static PublicKeySerializable readPublicKeySerializable(String path){
        File file = new File(path);
        if(!file.exists()){
            System.out.println("file doesn't exist, returning null");
            return null;
        }

        return (PublicKeySerializable)readSerializedObject(path);
    }

    public static PrivateKeySerializable readPrivateKeySerializable(String path){
        File file = new File(path);
        if(!file.exists()){
            System.out.println("file doesn't exist, returning null");
            return null;
        }

        return (PrivateKeySerializable) readSerializedObject(path);
    }

    private static Object readSerializedObject(String path){
        Object object1 = null;

        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
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


    public static ArrayList<KeySerializable> readKeys(){
        ArrayList<KeySerializable> keySerializables = new ArrayList<>();

        System.out.println("DAMN");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SELF_DIRECTORY;
        File directory = new File(path);
        File[] files = directory.listFiles();
        
        for(File curFile : files){
            String curPath = curFile.getAbsolutePath();
            System.out.println(curFile.getName());
            String name = curFile.getName();
            if(isValidKeyFile(name)){
                KeySerializable keySerializable = readKey(curPath);
                System.out.println(keySerializable);
                keySerializables.add(keySerializable);
                System.out.println(keySerializable!=null);
            }
            else{
                System.out.println("shit : " + name);
            }

        }

        return keySerializables;
    }

    public static boolean isValidKeyFile(String name){
        if(name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0)
            if (name.substring(name.lastIndexOf(".")).equals(Constants.EXTENSION_KEY))
                return true;

        return false;
    }

    public static byte[] readFile(String path){
        byte fileContent[] = null;
        File file = new File(path);
        FileInputStream fin = null;
        try {
            // create FileInputStream object
            fin = new FileInputStream(file);

            fileContent = new byte[(int)file.length()];

            // Reads up to certain bytes of data from this input stream into an array of bytes.
            fin.read(fileContent);
            //create string from byte array
            String s = new String(fileContent);
            System.out.println("File content: " + s);

        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }

        return fileContent;
    }

    public static boolean writeByte(byte[] bytes, String fileName)
    {
        try {

            // Initialize a pointer
            // in file using OutputStream
            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DEC_DIRECTORY;
            File rootFile = new File(rootPath);
            if(!rootFile.exists()){
                boolean mkdir = rootFile.mkdirs();
                if(!mkdir){
                    System.out.println("Path/file couldn't be created");
                    return false;
                }
            }

            String path = rootPath + "/" + fileName;
            System.out.println(path);
            File file = new File(path);
            boolean newFile = file.createNewFile();
            System.out.println(newFile);
            OutputStream os = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);
            System.out.println("Successfully"
                    + " byte inserted");

            // Close the file
            os.close();
            return true;
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        return false;
    }

}
