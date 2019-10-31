package de.tu.darmstadt;

import de.tu.darmstadt.Mapper;
import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.ParamsUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huanyingcool
 * 数据编码器，主要讲sc name的记录编码为数字，方便后期的处理和机器学习
 * 目前只考虑sc name，不考虑具体参数
 */
public class DataEncoder {

    //选择的data数
    private int selectedCount;
    //是否移除连续重复的sc
    private boolean removeRepeat;

    public DataEncoder(int selectedCount, boolean removeRepeat) {
        this.selectedCount = selectedCount;
        this.removeRepeat = removeRepeat;
    }

    /**
     * 遍历 clean/dirty 文件夹下的所有records,进行编码后保存为encode.txt
     * @param commMap
     * @param folderPath clean/dirty 文件夹路径
     * @return
     */
    public String encoding(Map<String,Integer> commMap, String folderPath){
        //如果输入的不是clean或者dirty文件夹，直接返回
        if (!FileUtils.checkFolderExist(folderPath)) {
            return "";
        }

        //输出文件在clean或者dirty文件夹下直接产生
        String outPath = folderPath+"\\"+ FileUtils.ENCODE_FILE_NAME;

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        int count = Math.min(files.length, selectedCount);
        for (int i=0; i<count; i++){
            try(BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
                String line ;
                StringBuilder sb = new StringBuilder();
                int commID;
                int preCommID =0;
                while ((line=br.readLine())!=null){
                    //提取lttng的sc名称
                    String scName = ParamsUtils.getScName(line);
                    if (!commMap.keySet().contains(scName)) { //对于可能不存在的sc name一律保存为-1
                        commMap.put(scName,-1);
                    }
                    commID = commMap.get(scName);
                    if (this.removeRepeat){
                        if (preCommID != commID){
                            sb.append(commID+" ");
                            preCommID = commID;
                        }
                    }else {
                        sb.append(commID+" ");
                    }
                }
                //追加写文件操作
                try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath,true)))){
                    bw.write(sb.toString()+"\n");
                    bw.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return outPath; //返回encode.txt的地址
    }

    /**
     * 只在sched_switch切换到指定service后编码数据
     * 用于保存指定进程的全部记录过程
     * @param commMap
     * @param folderPath clean/dirty 文件夹路径
     * @param param 指定的service进程名称
     * @return
     */
    public String encodingService(Map<String,Integer> commMap, String folderPath, String param){

        //如果输入的不是clean或者dirty文件夹，直接返回
        if (!FileUtils.checkFolderExist(folderPath)) {
            return null;
        }

        File folder = new File(folderPath);

        File[] files = folder.listFiles();
        List<String> list = new ArrayList<>();
        boolean flag = false;
        int preCommID = 0;
        int count = Math.min(files.length, selectedCount);
        for (int i = 0; i < count; i++) {
            try(BufferedReader br = new BufferedReader(new FileReader(files[i]))){
                String line;
                String scName;
                StringBuilder sb = new StringBuilder();
                while ((line=br.readLine())!=null){
                    scName = ParamsUtils.getScName(line);
                    if (scName.equals("sched_switch"))
                        flag = ParamsUtils.checkServiceParam(line,param);
                    if (flag){
                        int commID = commMap.containsKey(scName)? commMap.get(scName):-1;
                        if (removeRepeat ){
                            if (preCommID != commID){
                                sb.append(commID+" ");
                                preCommID = commID;
                            }
                        }else {
                            sb.append(commID+" ");
                        }
                    }
                }
                list.add(sb.toString());
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        //输出文件在clean或者dirty文件夹下直接产生
        String outPath = folderPath +"\\"+ FileUtils.ENCODE_FILE_NAME;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outPath)))){
            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i)+"\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return outPath;
    }

}
