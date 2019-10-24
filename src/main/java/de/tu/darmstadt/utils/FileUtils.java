package de.tu.darmstadt.utils;

import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class FileUtils {

    public static final Map commMap = new HashMap();

    public static final String CVE_RECORD_FOLDER = "D:\\Vulnerability\\CVE-2017-7494";

    private static int sched_switch_ID;
    private static int kmem_free_ID;

    private static String LAYER_0 = "out.txt"; //
    private static String LAYER_1 = "layer1.txt";

    /**
     * 将clean，dirty文件夹下的文件根据cmmMap转化为数字文件
     * @param folderPath
     * @param flag
     */
    private static void dataMapping(Map<String,Integer> commMap, String folderPath, boolean flag) {

        File dataFolder = new File(folderPath);
        //如果输入的不是clean或者dirty文件夹地址，直接返回
        if (!dataFolder.isDirectory()){
            System.out.println(folderPath);
            System.out.println("please enter folder path");
            return;
        }

        //输出文件在clean或者dirty文件夹下直接产生
        String outPath = dataFolder.getAbsolutePath()+"\\"+LAYER_0;
        File out = new File(outPath);
        if (out.exists())
            out.delete();

        BufferedReader br = null;
        BufferedWriter bw = null;
        File[] files = dataFolder.listFiles();
        for (File f:
                files) {
            try {
                br = new BufferedReader(new FileReader(f));
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath,true)));
                StringBuilder sb = new StringBuilder();
                String line ;
                int commID;
                while ((line=br.readLine())!=null){
                    String[] split = line.split(": ")[0].split(" ");
                    String comm = split[split.length-1];
                    if (commMap.keySet().contains(comm)){
                        commID = commMap.get(comm);
                        sb.append(commID+" ");
                    }else {
                        if (flag){
                            //对于clean中新出现的，添加入commMap
                            commMap.put(comm, commMap.size()+1);
//                            System.out.println(comm+" with added commID ::: "+commMap.size());
                            commID = commMap.get(comm);
                            sb.append(commID+" ");
                        }else {
                            //对于只在dirty中出现的comm标识为-1
                            commMap.put(comm, -1);
                            System.out.println(comm+" with negative commID !!! " );
                            commID = commMap.get(comm);
                            sb.append(commID+" ");
                        }
                    }
                }
                sb.append("\n");
                bw.write(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    br.close();
                    bw.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将clean和dirty文件夹.中的数据合并编码保存为Layer0层
     */
    private static void dataEncoder(String folderPath){
        File folder = new File(folderPath);
        String cleanPath="";
        String dirtyPath="";
        if (!folder.isDirectory()){
            System.out.println("please enter CVE records folder path");
        }
        File[] files = folder.listFiles();
        for (File file:
             files) {
            String name = file.getName();
            if (name.equalsIgnoreCase("clean")) {
                dataMapping(commMap, file.getAbsolutePath(),true);
            }
            if (name.equalsIgnoreCase("dirty")) {
                dataMapping(commMap, file.getAbsolutePath(), false);
            }
        }
        if (cleanPath.equals("")||dirtyPath.equals(""))
            return;
        System.out.println(cleanPath);
        System.out.println(dirtyPath);

    }

    /**
     * 数据分层
     * @param filePath
     */
    private static void dataSplitter(String filePath){
        File file = new File(filePath);
        if (!file.exists()){
            System.out.println("dataSplitting file doesn't exist !!! ");
            return;
        }
        String outPath = file.getAbsolutePath().replaceAll(LAYER_0,LAYER_1);
        File out = new File(outPath);
        if (out.exists())
            out.delete();
        System.out.println(outPath);
        BufferedReader br ;
        BufferedWriter bw ;
        try{
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath,true)));
            String line ;
            while ((line = br.readLine()) != null){
                StringBuilder sb = new StringBuilder();
                //数据切分
                String[] commIDs = line.split(" ");
                int preCommID = -1;
                int commID;
                for (String comm:
                        commIDs) {
                    commID = Integer.valueOf(comm);
                    //去除连续重复的commID
                    if (commID!=preCommID){
                        sb.append(commID+" ");
                        preCommID = commID;
                        if (commID == sched_switch_ID || commID == kmem_free_ID)
                            sb.append("\n");
                    }
                }
                bw.write(sb.toString());
            }
            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
//        commMap = new HashMap<String, Integer>();
////        createMapper("lttng-k.txt");  //利用ltttng-k的sc创建原始commMap
//        dataEncoder(CVE_RECORD_FOLDER);
//        sched_switch_ID = commMap.get("sched_switch");
//        kmem_free_ID = commMap.get("kmem_kfree");
//        dataSplitter("D:\\Vulnerability\\CVE-2017-7494\\clean\\out.txt");
//        dataSplitter("D:\\Vulnerability\\CVE-2017-7494\\dirty\\out.txt");

    }
}
