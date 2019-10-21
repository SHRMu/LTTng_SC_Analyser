package de.tu.darmstadt.utils;

import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class FileLoader {

    //
    public static final String CVE_RECORD_FOLDER = "D:\\Vulnerability\\CVE-2017-7494";

    private static Map<String, Integer> commMap;

    private static int sched_switch_ID;
    private static int kmem_free_ID;

    private static String LAYER_0 = "out.txt";
    private static String LAYER_1 = "layer1.txt";
    private static String LAYER_2 = "layer2.txt";

    /**
     * 将lttng list -k 保存的文件转化为Map<String, Integer>
     * txt文件保存的命令不全，需要后续追加
     */
    private void createMapper(String lttngFile){

        //读取resource文件夹下的lttng-k.txt
        String file = this.getClass().getClassLoader().getResource(lttngFile).getFile();
        int count = 1;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine())!=null){
                String comm = line.split(" ")[0]; //get command name
                if (!commMap.keySet().contains(comm)){
                    commMap.put(comm,count);
                    count++;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 将clean，dirty文件夹下的文件根据cmmMap转化为数字文件
     * @param folderPath
     * @param flag
     */
    private static void dataMapping(String folderPath, boolean flag) {

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
                    try{
                        commID = commMap.get(comm);
                        sb.append(commID+" ");
                    }catch (NullPointerException e){
                        //对于lttng-k缺失的命令进行弥补
                        commMap.put(comm, commMap.size()+1);
                        System.out.println(comm+" with added commID ::: "+commMap.size());
                        commID = commMap.get(comm);
                        sb.append(commID+" ");
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
     * 将clean和dirty文件夹中的数据合并编码保存为Layer0层
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
                dataMapping(file.getAbsolutePath(),true);
            }
            if (name.equalsIgnoreCase("dirty")) dirtyPath = file.getAbsolutePath();
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
        File outFile = new File(outPath);
        if (outFile.exists())
            outFile.delete();
        System.out.println(outPath);
        BufferedReader br = null;
        BufferedWriter bw = null;
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
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        commMap = new HashMap<String, Integer>();
//        createMapper("lttng-k.txt");  //利用ltttng-k的sc创建原始commMap
        dataEncoder(CVE_RECORD_FOLDER);
        sched_switch_ID = commMap.get("sched_switch");
        kmem_free_ID = commMap.get("kmem_kfree");
        dataSplitter("D:\\Vulnerability\\CVE-2017-7494\\clean\\out.txt");
    }
}
