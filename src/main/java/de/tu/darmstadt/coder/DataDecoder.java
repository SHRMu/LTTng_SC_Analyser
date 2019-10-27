package de.tu.darmstadt.coder;

import de.tu.darmstadt.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataDecoder {

    public static String decoding(Map<Integer, String> commMap, List<String> scList, String folderPath){

        String outPath = folderPath +"\\" + FileUtils.DECODE_FILE_NAME;
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)));
            for (String sc:
                    scList) {
                String[] s = sc.split(" ");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length; i++) {
                    if (commMap.containsKey(Integer.valueOf(s[i]))){
                        sb.append(commMap.get(Integer.valueOf(s[i]))+" ");
                    }else{
                        sb.append("null ");
                    }
                }
                sb.append("\n");
                bw.write(sb.toString());
            }
            bw.close();
        }catch (IOException e){

        }

        return outPath;

    }

}
