package de.tu.darmstadt.utils;

import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class MapUtils {

    private static final String LTTNG_FILE_NAME = "lttng-k.txt";

    private static final String LTTNG_MAP_NAME = "lttng-map.txt";

    /**
     *
     * @param folderPath records folder的路径
     * @return lttng command 对应的Integer数值
     */
    public static Map<String, Integer> createMapper(String folderPath){
        Map<String, Integer> commMap = new HashMap<>();
        //读取resource文件夹下的lttng-k.txt
        String lttngFile = folderPath + "\\" + LTTNG_FILE_NAME;
        int count = 1;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(lttngFile)));
            String line;
            while ((line = br.readLine())!=null){
                String comm = line.split(" ")[0]; //get command name
                if (!commMap.keySet().contains(comm)){
                    commMap.put(comm,count);
                    count++;
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return commMap;
    }

    /**
     *
     * @param commMap
     * @param folderPath
     */
    public static void saveMapper(Map<String, Integer> commMap, String folderPath){
        File file = new File(folderPath);
        if (!file.exists()||!file.isDirectory())
            return;
        String out = folderPath +"\\"+ LTTNG_MAP_NAME;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(out)));
            Map<String, Integer> map = sortByValue(commMap);
            map.entrySet().forEach(entity -> sb.append(entity.getKey()+" "+entity.getValue()+"\n"));
            bw.write(sb.toString());
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K,V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Map<K,V> result = new LinkedHashMap<>();
        list.forEach(item -> result.put(item.getKey(), item.getValue()));
        return result;
    }

    @Test
    public void test(){
        String recordPath = "D:\\Vulnerability\\CVE-2017-7494";
        Map<String, Integer> commMap = createMapper(recordPath);
        saveMapper(commMap,recordPath);
    }

}
