package de.tu.darmstadt.splitter;

import de.tu.darmstadt.utils.FileUtils;

import java.io.*;
import java.util.List;

public class CommSplitter {

    public static String splitByComms(String encodePath, List<Integer> comms){

        if (!FileUtils.checkFileExist(encodePath)) {
            return "";
        }

        String splitPath = encodePath.replaceAll(FileUtils.ENCODE_FILE_NAME,FileUtils.SPLITED_FILE_NAME);

        BufferedReader br;
        BufferedWriter bw;
        try{
            br = new BufferedReader(new FileReader(new File(encodePath)));
            bw = new BufferedWriter(new FileWriter(new File(splitPath)));
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
                bw.write(sb.toString().replaceAll("\n\n","\n"));
            }
            br.close();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return splitPath;
    }

}
