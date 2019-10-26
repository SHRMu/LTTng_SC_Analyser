package de.tu.darmstadt.filter;

import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.io.*;
import java.util.*;

public class Filter {

    public static List<String> filterByCount(String differPath, int max, int min){
        if (!FileUtils.checkIsFile(differPath)) {
            return null;
        }
        String outPath = differPath.replaceAll(FileUtils.DIFFER_FILE_NAME, FileUtils.DECODE_FILE_NAME);

        BufferedReader br;
        Map<String,Integer> countMap = new HashMap<>();
        List<String> result = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(new File(differPath)));
            String line;
            while ((line=br.readLine())!=null){
                if (!countMap.keySet().contains(line)){
                    countMap.put(line,1);
                }else {
                    int num = countMap.get(line);
                    countMap.replace(line, num, num+1);
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        countMap = MapUtils.sortByValue(countMap, true);

        Set<String> strings = countMap.keySet();
        for (String s:
             strings) {
            if (countMap.get(s)<min)
                break;
            if (countMap.get(s)<max)
                result.add(s);
        }

        return result;

    }
}
