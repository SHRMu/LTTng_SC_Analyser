package de.tu.darmstadt.utils;

import java.io.*;
import java.util.Date;
import java.util.Map;

public class EncoderUtils {

    private static final String DATA_ENCODER = "encoder.txt";

    /**
     *
     * @param commMap
     * @param folderPath clean/dirty folder path
     */
    public static void dataEncoder(Map<String,Integer> commMap, String folderPath){

        File folder = new File(folderPath);
        String name = folder.getName();

        //如果输入的不是clean或者dirty文件夹地址，直接返回
        if (!folder.exists() || !folder.isDirectory()){
            return;
        }

        //输出文件在clean或者dirty文件夹下直接产生
        String outPath = folder.getAbsolutePath()+"\\"+ DATA_ENCODER;
        File out = new File(outPath);
        if (out.exists())
            out.delete();

        BufferedReader br = null;
        BufferedWriter bw = null;

        File[] files = folder.listFiles();
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
                        if (name.equalsIgnoreCase("clean")){
                            //对于clean中新出现的，添加入commMap
                            commMap.put(comm, commMap.size()+1);
//                            System.out.println(comm+" with added commID ::: "+commMap.size());
                            commID = commMap.get(comm);
                            sb.append(commID+" ");
                        }else if (name.equalsIgnoreCase("dirty")){
                            //对于只在dirty中出现的comm标识为-1
                            commMap.put(comm, -1);
                            System.out.println(comm+" with negative commID !!! " );
                            commID = commMap.get(comm);
                            sb.append(commID+" ");
                        }else {
                            break;
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

    public static void dataDecoder(){

    }
}
