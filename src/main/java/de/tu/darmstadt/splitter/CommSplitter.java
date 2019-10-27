package de.tu.darmstadt.splitter;

import de.tu.darmstadt.utils.FileUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class CommSplitter {

    public static String splitByComms(String encodeFile, List<Integer> comms){

        if (!FileUtils.checkIsFile(encodeFile))
            return "";

        String outPath = encodeFile.replaceAll(FileUtils.ENCODE_FILE_NAME,FileUtils.SPLITED_FILE_NAME);

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

}
