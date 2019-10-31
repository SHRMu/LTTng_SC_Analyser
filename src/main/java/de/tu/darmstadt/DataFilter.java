package de.tu.darmstadt;

import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.io.*;
import java.util.*;


public class DataFilter {

    /**
     * 过滤出指定数量标准的sc chains
     * @param differPath
     * @param max
     * @param min
     * @return
     */
    public static List<String> filterByCount(String differPath, int max, int min){

        if (!FileUtils.checkFileExist(differPath)) {
            return null;
        }

        Map<String,Integer> countMap = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(new File(differPath)))) {
            String line;
            while ((line=br.readLine())!=null){
                if (!countMap.containsKey(line)){
                    countMap.put(line,1);
                }else {
                    int num = countMap.get(line);
                    countMap.replace(line, num, num+1);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        List<String> result = new ArrayList<>();
        countMap = MapUtils.sortByValue(countMap, true);
        countMap.entrySet().forEach(entry->{
            if (entry.getValue() > min && entry.getValue() < max)
                result.add(entry.getKey());
        });

//        String resultFolder = "D:\\Vulnerability\\CVE-2017-7494-G\\result";
//        BufferedWriter bw =null;
//        for (int i = 0; i < result.size(); i++) {
//            String outPath = resultFolder +"\\"+ String.valueOf(i)+".txt";
//            try {
//                bw = new BufferedWriter(new FileWriter(new File(outPath)));
//                bw.write(result.get(i));
//                bw.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return result;

    }

}
