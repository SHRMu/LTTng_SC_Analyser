package de.tu.darmstadt.utils;

import org.junit.Test;

import javax.imageio.IIOException;
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

    //追加写文件的旧文件需要先删除
    public static void cleanFile(String filePath){
        File file = new File(filePath);
        if (file.exists() && file.isFile()){
            file.delete();
        }
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
