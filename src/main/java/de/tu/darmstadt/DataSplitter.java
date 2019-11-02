package de.tu.darmstadt;

import de.tu.darmstadt.utils.FileUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DataSplitter {

    /**
     * 根据指定的comm sc name切分数据，方便建立tree结构
     * @param encodePath
     * @param commIDs
     * @return
     */
    public static String splitByComms(String encodePath, List<Integer> commIDs){

        if (!FileUtils.checkFileExist(encodePath)) {
            return "";
        }

        StringBuilder sb = null;
        try(BufferedReader br = new BufferedReader(new FileReader(new File(encodePath)))){
            String line ;
            sb = new StringBuilder();
            while ((line=br.readLine())!=null){
                String[] ids = line.split(" ");
                for (int i = 0; i < ids.length; i++) {
                    sb.append(ids[i]+" ");
                    if (commIDs.contains(Integer.valueOf(ids[i]))) {
                        sb.append("\n"); //根据选中的comms进行切分
                    }
                }
                sb.append("\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        String splitPath = encodePath.replaceAll(FileUtils.ENCODE_FILE_NAME,FileUtils.SPLITED_FILE_NAME);
        try(BufferedWriter bw =  new BufferedWriter(new FileWriter(new File(splitPath)))){
            bw.write(sb.toString().replaceAll("\n\n","\n"));
            bw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

        return splitPath;
    }

}
