package com.dev_pd.pgptool.UI;

import android.content.Context;
import android.os.Environment;

import com.dev_pd.pgptool.Constants;
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
import java.security.KeyPair;

public class HelperFunctions {
    public static void writeFileExternalStorage(String fileName, String extension, Object object) throws IOException {

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println("Storage is not mounted, returning!!");
            //If it isn't mounted - we can't write into it.
            return;
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DIRECTORY;
        File rootFile = new File(path);
        if(!rootFile.exists()){
            if(!rootFile.mkdir()){
                System.out.println("Path/file doesn't exist");
                return;
            }
        }
        System.out.println(path);

        FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + fileName + extension);
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

        // Method for serialization of object
        out.writeObject(object);

        out.close();
        fileOutputStream.close();

    }

    public static void writeFileExternalStorage(String fileName, String extension, int type, Object object) throws IOException {

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println("Storage is not mounted, returning!!");
            //If it isn't mounted - we can't write into it.
            return;
        }

        String suffix = "";
        if(type == 1)
            suffix = Constants.SELF_DIRECTORY;
        else
            suffix = Constants.OTHERS_DIRECTORY;

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + suffix;
        File rootFile = new File(path);
        if(!rootFile.exists()){
            if(!rootFile.mkdir()){
                System.out.println("Path/file doesn't exist");
                return;
            }
        }
        System.out.println(path);

        FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + fileName + extension);
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

        // Method for serialization of object
        out.writeObject(object);

        out.close();
        fileOutputStream.close();

    }


    public static void writeFileExternalStorage(KeyPair keyPair) throws IOException {

        //Text of the Document
        String textToWrite = "bla bla bla";

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println("MOUNT -__-");
            //If it isn't mounted - we can't write into it.
            return;
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PGP Tool";
        File rootFile = new File(path);
        if(!rootFile.exists()){
            if(!rootFile.mkdir()){
                System.out.println("Path/file doesnt exist");
                return;
            }
        }
        System.out.println(path);
        //Create a new file that points to the root directory, with the given name:
//        File file = new File(rootFile, "filename.txt");

        //This point and below is responsible for the write operation


        FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + "enc.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

        // Method for serialization of object
        out.writeObject(keyPair);

        out.close();
        fileOutputStream.close();

//         try {
//            file.createNewFile();
//            //second argument of FileOutputStream constructor indicates whether
//            //to append or create new file if one exists
//            outputStream = new FileOutputStream(file, false);
//
//            outputStream.write(textToWrite.getBytes());
//            outputStream.flush();
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static PublicKeySerializable readPublicKeySerializable(String path){
        File file = new File(path);
        if(!file.exists()){
            System.out.println("file doesnt exist, returning null");
            return null;
        }

        return (PublicKeySerializable)readSerializedObject(path);
    }

    public static PrivateKeySerializable readPrivateKeySerializable(String path){
        File file = new File(path);
        if(!file.exists()){
            System.out.println("file doesnt exist, returning null");
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
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }

        return object1;
    }

    public static KeySerializable readKey(){
        KeySerializable keySerializable = null;

        System.out.println("DAMN");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DIRECTORY;
        File directory = new File(path);
        File[] files = directory.listFiles();
        
        for(File curFile : files){
            String curPath = curFile.getAbsolutePath();
            System.out.println(curFile.getName());
//            Object o = readSerializedObject(curPath);
        }

        return keySerializable;
    }

}
