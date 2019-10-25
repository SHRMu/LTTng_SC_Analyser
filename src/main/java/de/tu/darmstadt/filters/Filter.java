package de.tu.darmstadt.filters;

import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.io.*;
import java.util.*;

public class Filter {

    private static final String DIFFER_FILE_NAME = "differ";
    private static final String RESULT_FILE_NAME = "result";

    public static List<String> filterByCount(String differPath, int count){
        if (!FileUtils.checkIsFile(differPath)) {
            return null;
        }

        String outPath = differPath.replaceAll(DIFFER_FILE_NAME,RESULT_FILE_NAME);

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

        System.out.println(countMap.get("120 12 46 119 7 6 40 8 "));
        Set<String> strings = countMap.keySet();
        for (String s:
             strings) {
            if (countMap.get(s)<count)
                break;
            result.add(s);
        }

        return result;

    }
}
