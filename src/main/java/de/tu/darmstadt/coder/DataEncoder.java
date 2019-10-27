package de.tu.darmstadt.coder;

import de.tu.darmstadt.utils.FileUtils;

import java.io.*;
import java.util.Map;

public class DataEncoder {

    public static String encoding(Map<String,Integer> commMap, String folderPath, int selectedCount, boolean removeRepeat){

        //如果输入的不是clean或者dirty文件夹，直接返回
        if (!FileUtils.checkIsFolder(folderPath)){
            return "";
        }

        File folder = new File(folderPath);
        String name = folder.getName();

        //输出文件在clean或者dirty文件夹下直接产生
        String encodePath = folder.getAbsolutePath()+"\\"+ FileUtils.ENCODE_FILE_NAME;

        BufferedReader br = null;
        BufferedWriter bw = null;

        File[] files = folder.listFiles();

        for (int i=0; i<selectedCount; i++){
            try {
                br = new BufferedReader(new FileReader(files[i]));
                //追加写文件操作
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(encodePath,true)));
                String line ;
                StringBuilder sb = new StringBuilder();
                int commID;
                int preCommID =0;
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
                    if (removeRepeat){
                        if (preCommID != commID){
                            sb.append(commID+" ");
                            preCommID = commID;
                        }
                    }else {
                        sb.append(commID+" ");
                    }
                }
                bw.write(sb.toString()+"\n");
                bw.flush();
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
        return encodePath; //返回写入文件的地址
    }

}
