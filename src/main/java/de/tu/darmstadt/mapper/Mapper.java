package de.tu.darmstadt.mapper;

import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Mapper {
    /**
     *
     * @param folderPath records folder的路径
     * @return lttng command 对应的Integer数值
     */
    public static Map<String, Integer> createMapper(String folderPath){
        Map<String, Integer> commMap = new HashMap<>();
        //读取resource文件夹下的lttng-k.txt
        String lttngPath = folderPath + "\\" + FileUtils.LTTNG_FILE_NAME;
        if (!FileUtils.checkFileExist(lttngPath)) {
            return null;
        }
        int count = 1;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(lttngPath)));
            String line;
            while ((line = br.readLine())!=null){
                String comm = line.split(" ")[0]; //get sc name
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
     * 将clean数据中出现过的sc name map保存到本地
     * @param commMap
     * @param folderPath
     */
    public static void saveMapper(Map<String, Integer> commMap, String folderPath){
        if (!FileUtils.checkFolderExist(folderPath)) {
            return;
        }
        String lttngMapPath = folderPath +"\\"+ FileUtils.LTTNG_MAP_NAME;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(lttngMapPath)));
            Map<String, Integer> map = MapUtils.sortByValue(commMap);
            map.entrySet().forEach(entity -> sb.append(entity.getKey()+" "+entity.getValue()+"\n"));
            bw.write(sb.toString());
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //
    public static Map<String, Integer> loadMapper(String folderPath){
        String filePath = folderPath + "\\" + FileUtils.LTTNG_MAP_NAME;
        if (!FileUtils.checkFileExist(filePath)) {
            return null;
        }
        Map<String, Integer> commMap = new HashMap<>();
        BufferedReader  br;
        try{
            br = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            while ((line=br.readLine())!=null){
                String[] comm = line.split(" ");
                commMap.put(comm[0], Integer.valueOf(comm[1]));
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return commMap;
    }

    //加载数字到sc name的反向映射
    public static Map<Integer,String> loadReverseMapper(String folderPath){
        String filePath = folderPath + "\\" + FileUtils.LTTNG_MAP_NAME;
        if (!FileUtils.checkFileExist(filePath)) {
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
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return commMap;
    }
}
