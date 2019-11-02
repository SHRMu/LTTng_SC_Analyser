package de.tu.darmstadt;

import de.tu.darmstadt.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataDecoder {

    public static void decoding(String folderPath, List<String> scList, Map<Integer, String> commMap){

        String outPath = folderPath + "\\" + FileUtils.DECODE_FILE_NAME;
        try( BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)))){
            StringBuilder sb = new StringBuilder();
            scList.forEach(sc->{
                String[] ids = sc.split(" ");
                for (int i = 0; i < ids.length; i++) {
                    if (commMap.containsKey(Integer.valueOf(ids[i]))){
                        sb.append(commMap.get(Integer.valueOf(ids[i]))+" ");
                    }else{
                        sb.append("null ");
                    }
                }
                sb.append("\n");
            });
            bw.write(sb.toString());
            bw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
