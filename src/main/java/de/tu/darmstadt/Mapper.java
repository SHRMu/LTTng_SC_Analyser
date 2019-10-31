package de.tu.darmstadt;


import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;
import de.tu.darmstadt.utils.ParamsUtils;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huanyingcool
 * @desription: 从sc name到数字的Mapper
 */
public class Mapper {

    private String folderPath;

    public Mapper(String folderPath) {
        this.folderPath = folderPath;
    }

    /**
     * 遍历records folder下的clean文件，建立commMap映射，保存到folder下的lttng-map.txt
     * 改进：只保存sc name:int编号 没有太大意义，考虑后期的统计，改为保存sc name:count计数值
     */
    public void initMapper(){

        //调用clean folder下的records为commMap的基准
        String cleanFolder = FileUtils.getCleanFolder(folderPath);
        if (!FileUtils.checkFolderExist(cleanFolder)) {
            return ;
        }

        Map<String,Integer> commMap = createMap(cleanFolder);
        saveMap(commMap);
    }

    /**
     * 遍历cleanFolder下的所有records,尽力commMap
     * @param cleanFolder clean folder path
     * @return commMap<SC name, Count>
     */
    private Map<String,Integer> createMap(String cleanFolder){

        File folder = new File(cleanFolder);
        File[] files = folder.listFiles();
        Map<String,Integer> commMap = new HashMap<>();
        for (File f:
                files) {
            String line = null;
            try (BufferedReader br = new BufferedReader(new FileReader(f))){
                while ((line = br.readLine())!=null){
                    String scName = ParamsUtils.getScName(line);
                    if (commMap.containsKey(scName)){
                        commMap.replace(scName, commMap.get(scName), commMap.get(scName) + 1);
                    }else {
                        commMap.put(scName, 1);
                    }
                }
            }catch (IOException e){
                System.out.println(f.getName()+" with error exceptine line : " + line);
            }
        }

        return commMap;

    }

    /**
     *  将commMap保存到本地record folder下的lttng-map.txt中
     * @param commMap
     */
    private void saveMap(Map<String, Integer> commMap){
        String outPath = folderPath + "\\"+ "lttng-map.txt";
        commMap = MapUtils.sortByValue(commMap, true);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)))){
            commMap.entrySet().forEach(entry -> {
                try {
                    bw.write(entry.getKey() + " " + entry.getValue()+ "\n");
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 加载records folder path下的lttng-map.txt文件
     * @return commMap<SC name: Integer>
     */
    public Map<String,Integer> loadMap(){
        String mapPath = folderPath + "\\" + FileUtils.LTTNG_MAP_NAME;
        if (!FileUtils.checkFileExist(mapPath)) {
            return null;
        }
        Map<String, Integer> commMap = new HashMap<>();
        String line;
        int count = 1;
        try(BufferedReader br =  new BufferedReader(new FileReader(new File(mapPath)))){
            while ((line=br.readLine())!=null){
                String[] comm = line.split(" ");
                commMap.put(comm[0], count);
                count ++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return commMap;
    }

}
