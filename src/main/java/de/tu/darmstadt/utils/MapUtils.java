package de.tu.darmstadt.utils;

import org.junit.Test;

import javax.imageio.IIOException;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class MapUtils {


    /**
     *
     * @param folderPath records folder的路径
     * @return lttng command 对应的Integer数值
     */
    public static Map<String, Integer> createMapper(String folderPath){
        Map<String, Integer> commMap = new HashMap<>();
        //读取resource文件夹下的lttng-k.txt
        String lttngFile = folderPath + "\\" + FileUtils.LTTNG_FILE_NAME;
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

    public static void saveMapper(Map<String, Integer> commMap, String folderPath){
        File file = new File(folderPath);
        if (!file.exists()||!file.isDirectory())
            return;
        String out = folderPath +"\\"+ FileUtils.LTTNG_MAP_NAME;
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

    public static Map<Integer,String> loadMapper(String folderPath){
        String filePath = folderPath + "\\" + FileUtils.LTTNG_MAP_NAME;
        if (!FileUtils.checkIsFile(filePath)) {
            return null;
        }
        Map<Integer, String> commMap = new HashMap<>();
        BufferedReader  br;
        try{
            br = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            while ((line=br.readLine())!=null){
                String[] comm = line.split(" ");
                commMap.put(Integer.valueOf(comm[1]),comm[0]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return commMap;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean reverse){
        List<Entry<K,V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        if (reverse)
            Collections.reverse(list);
        Map<K,V> result = new LinkedHashMap<>();
        list.forEach(item -> result.put(item.getKey(), item.getValue()));
        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return sortByValue(map,false);
    }

}
