package de.tu.darmstadt.utils;

import java.io.*;

public class FileUtils {

    public static final String CLEAN_FOLDER_NAME = "clean";
    public static final String DIRTY_FOLDER_NAME = "dirty";

    public static final String LTTNG_FILE_NAME = "lttng-k.txt";
    public static final String LTTNG_MAP_NAME = "lttng-map.txt";

    public static final String ENCODE_FILE_NAME = "encode.txt";
    public static final String SPLITED_FILE_NAME = "split.txt";
    public static final String DIFFER_FILE_NAME = "differ.txt";
    public static final String DECODE_FILE_NAME = "decode.txt";


    public static String getCleanFolder(String folder){
        return folder+"\\"+CLEAN_FOLDER_NAME;
    }

    public static String getDirtyFolder(String folder){
        return folder+"\\"+DIRTY_FOLDER_NAME;
    }

    public static boolean checkFileExist(String filePath){
        File file = new File(filePath);
        if (file.exists() && file.isFile()) return true;
        return false;
    }

    public static boolean checkFolderExist(String folderPath){
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) return true;
        return false;
    }

}
