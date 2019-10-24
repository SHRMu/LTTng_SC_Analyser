package de.tu.darmstadt.utils;

import org.junit.Test;

import java.io.*;

/**
 *
 */
public class FileUtils {

    private static final String CLEAN_FOLDER_NAME = "clean";
    private static final String DIRTY_FOLDER_NAME = "dirty";


    public static String getCleanFolder(String folder){
        return folder+"\\"+CLEAN_FOLDER_NAME;
    }

    public static String getDirtyFolder(String folder){
        return folder+"\\"+DIRTY_FOLDER_NAME;
    }

    public static boolean checkIsFile(String filePath){
        File file = new File(filePath);
        if (file.exists() && file.isFile()) return true;
        return false;
    }

    public static boolean checkIsFolder(String folderPath){
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) return true;
        return false;
    }

}
