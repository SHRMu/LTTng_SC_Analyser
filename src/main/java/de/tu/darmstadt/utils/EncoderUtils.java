package de.tu.darmstadt.utils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class EncoderUtils {

    private static final String DATA_ENCODER = "encoder.txt";
    private static final String DATA_DECODER = "result.txt";

    public static String dataEncoder(Map<String,Integer> commMap, String folderPath) throws FileNotFoundException{

        File folder = new File(folderPath);
        String name = folder.getName();

        //如果输入的不是clean或者dirty文件夹，直接返回
        if (!FileUtils.checkIsFolder(folderPath)){
            return "";
        }

        //输出文件在clean或者dirty文件夹下直接产生
        String outPath = folder.getAbsolutePath()+"\\"+ DATA_ENCODER;
        FileUtils.cleanFile(outPath);

        BufferedReader br = null;
        BufferedWriter bw = null;

        File[] files = folder.listFiles();
        for (File f:
                files) {
            try {
                br = new BufferedReader(new FileReader(f));
                //追加写文件操作
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath,true)));
                String line ;
                StringBuilder sb = new StringBuilder();
                int commID;
                while ((line=br.readLine())!=null){
                    //提取lttng的sc名称
                    String[] split = line.split(": ")[0].split(" ");
                    String comm = split[split.length-1];
                    if (!commMap.keySet().contains(comm)) { //默认导出的lttng-k本身不全，需要通过clean records进行补充
                        if (name.equalsIgnoreCase("clean")) {
                            //对于clean中新出现的，添加入commMap
                            commMap.put(comm, commMap.size() + 1);
                        } else if (name.equalsIgnoreCase("dirty")) {
                            //对于只在dirty中出现的comm标识为-1
                            commMap.put(comm, -1);
                            System.out.println(comm + " with negative commID !!! ");
                        } else {
                            break;
                        }
                    }
                    commID = commMap.get(comm);
                    sb.append(commID+" ");
                }
                sb.append("\n");
                bw.write(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //关闭流
        try{
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return outPath; //返回写入文件的地址
    }

    public static void dataDecoder(Map<Integer, String> commMap, List<String> scList, String folderPath){

        String outPath = folderPath +"\\" + DATA_DECODER;

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)));
            for (String sc:
                    scList) {
                String[] s = sc.split(" ");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length; i++) {
                    sb.append(commMap.get(Integer.valueOf(s[i]))+" ");
                }
                sb.append("\n");
                bw.write(sb.toString());
            }
            bw.close();
        }catch (IOException e){

        }

    }
}
