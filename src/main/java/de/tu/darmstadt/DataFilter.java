package de.tu.darmstadt;

import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.io.*;
import java.util.*;


public class DataFilter {

    /**
     * 过滤出指定数量标准的sc chains
     * @param
     * @param max
     * @param min
     * @return
     */
    public static List<String> filterByCount(String folderPath, Map<String,Integer> countMap, int max, int min){

        List<String> scChains = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        countMap = MapUtils.sortByValue(countMap, true);
        countMap.entrySet().forEach(entry->{
            if (entry.getValue() > min && entry.getValue() < max){
                scChains.add(entry.getKey());
                sb.append(entry.getKey()+"\n");
            }

        });

        String outPath = folderPath + "\\" + "result\\sc.txt";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)))){
            bw.write(sb.toString());
            bw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

        return scChains;

    }

}
