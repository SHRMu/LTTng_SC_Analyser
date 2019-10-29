package de.tu.darmstadt.mapper;


import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Mapper {
    /**
     * 默认读取 records folder 路径下的lttng-k.txt文件，返回commMap映射
     * @param folderPath records folder path
     * @return lttng command 对应的Integer数值
     */
    public static Map<String, Integer> createMapper(String folderPath){
        //读取resource文件夹下的lttng-k.txt
        String lttngPath = folderPath + "\\" + FileUtils.LTTNG_FILE_NAME;
        if (!FileUtils.checkFileExist(lttngPath)) {
            return null;
        }
        Map<String, Integer> commMap = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(lttngPath)));
            String line;
            int count = 1;
            while ((line = br.readLine())!=null){
                String comm = line.split(" ")[0]; //sc name as map key
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
     * 将commMap保存到本地
     * @param commMap
     * @param folderPath
     */
    public static void saveMapper(Map<String, Integer> commMap, String folderPath){
        if (!FileUtils.checkFolderExist(folderPath)) {
            return;
        }
        String mapPath = folderPath +"\\"+ FileUtils.LTTNG_MAP_NAME;
        BufferedWriter bw;
        StringBuilder sb;
        try {
            bw = new BufferedWriter(new FileWriter(new File(mapPath)));
            sb = new StringBuilder();
            commMap = MapUtils.sortByValue(commMap); // 升序排列
            commMap.entrySet().forEach(entity -> sb.append(entity.getKey()+" "+entity.getValue()+"\n"));
            bw.write(sb.toString());
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 加载已经保存的lttng-map.txt文件
     * @param folderPath records folder path
     * @return commMap
     */
    public static Map<String, Integer> loadMapper(String folderPath){
        String mapPath = folderPath + "\\" + FileUtils.LTTNG_MAP_NAME;
        if (!FileUtils.checkFileExist(mapPath)) {
            return null;
        }
        BufferedReader  br;
        Map<String, Integer> commMap = new HashMap<>();
        try{
            br = new BufferedReader(new FileReader(new File(mapPath)));
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

    @Test
    public void test(){
        Map<String, Integer> map = loadMapper("D:\\Vulnerability\\CVE-2017-7494");
        map.entrySet().forEach(entity-> System.out.println(entity.getKey()+" "+entity.getValue()));
    }
}
