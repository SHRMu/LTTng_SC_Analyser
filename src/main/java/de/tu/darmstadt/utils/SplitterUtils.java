package de.tu.darmstadt.utils;

import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SplitterUtils {

    private static final String ENCODER_FILE_NAME = "encoder";
    private static final String SPLITED_FILE_NAME = "split";

    //移除连续重复出现的命令
    private static String[] removeRepeat(String[] ids){
        List<String> result = new LinkedList<>();
        String preid = ids[0];
        result.add(preid);
        for (int i = 1; i < ids.length; i++) {
            if (!ids[i].equals(preid)){
                result.add(ids[i]);
                preid = ids[i];
            }
        }
        return result.toArray(new String[result.size()]);
    }

    public static String splitByComms(String encodeFile, List<Integer> comms, boolean norepeat){

        if (!FileUtils.checkIsFile(encodeFile))
            return "";

        String outPath = encodeFile.replaceAll(ENCODER_FILE_NAME,SPLITED_FILE_NAME);

        BufferedReader br;
        BufferedWriter bw;
        try{
            br = new BufferedReader(new FileReader(new File(encodeFile)));
            bw = new BufferedWriter(new FileWriter(new File(outPath)));
            String line ;
            StringBuilder sb;
            while ((line=br.readLine())!=null){
                sb = new StringBuilder();
                String[] ids = line.split(" ");
                if (norepeat) {
                    ids = removeRepeat(ids);
                }
                for (int i = 0; i < ids.length; i++) {
                    sb.append(ids[i]+" ");
                    if (comms.contains(Integer.valueOf(ids[i]))) {
                        sb.append("\n"); //根据选中的comms进行切分
                    }
                }
                sb.append("\n");
                bw.write(sb.toString());
            }
            br.close();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return outPath;
    }

    public static void splitByComms(String encodeFile, List<Integer> comms){
        splitByComms(encodeFile,comms,false);
    }

    public static void splitByLines(){

    }



}
