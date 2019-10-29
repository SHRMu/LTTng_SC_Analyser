package de.tu.darmstadt.coder;

import de.tu.darmstadt.mapper.Mapper;
import de.tu.darmstadt.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataEncoder {

    //选择的data数
    private int selectedCount;
    //是否移除连续重复的sc
    private boolean removeRepeat;

    public DataEncoder(int selectedCount, boolean removeRepeat) {
        this.selectedCount = selectedCount;
        this.removeRepeat = removeRepeat;
    }

    public String encoding(Map<String,Integer> commMap, String folderPath){

        //如果输入的不是clean或者dirty文件夹，直接返回
        if (!FileUtils.checkFolderExist(folderPath)) {
            return "";
        }

        File folder = new File(folderPath);
        String name = folder.getName();

        //输出文件在clean或者dirty文件夹下直接产生
        String encodePath = folder.getAbsolutePath()+"\\"+ FileUtils.ENCODE_FILE_NAME;

        BufferedReader br = null;
        BufferedWriter bw = null;

        File[] files = folder.listFiles();

        for (int i=0; i<this.selectedCount; i++){
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
                    if (this.removeRepeat){
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
        if (name.equalsIgnoreCase("clean"))
            Mapper.saveMapper(commMap,folder.getParent());
        return encodePath; //返回写入文件的地址
    }

    // 只针对制定的comm应用进行编码
    public String encodingSchedSwitch(Map<String,Integer> commMap, String folderPath, String param){

        //如果输入的不是clean或者dirty文件夹，直接返回
        if (!FileUtils.checkFolderExist(folderPath)) {
            return null;
        }

        File folder = new File(folderPath);
        String name = folder.getName();

        //输出文件在clean或者dirty文件夹下直接产生
        String encodePath = folder.getAbsolutePath()+"\\"+ FileUtils.ENCODE_FILE_NAME;

        BufferedReader br = null;
        File[] files = folder.listFiles();
        List<String> list = new ArrayList<>();
        boolean flag = false;
        int preCommID = 0;
        for (File f:
                files) {
            try{
                br = new BufferedReader(new FileReader(f));
                String line;
                String commName;
                StringBuilder sb = new StringBuilder();
                while ((line=br.readLine())!=null){
                    String[] split = line.split(": ")[0].split(" ");
                    commName = split[split.length-1];
                    if (commName.equalsIgnoreCase("sched_switch")){
                        String[] split1 = line.split("}, ");
                        String s = split1[split1.length-1];
                        Pattern p = Pattern.compile("\\{(.*?)\\}");
                        Matcher m = p.matcher(s);
                        while (m.find()){
                            String[] params = m.group(1).split(",");
                            for (int i = 0; i < params.length; i++) {
                                String[] items = params[i].split("=");
                                if (items[0].trim().equalsIgnoreCase("next_comm")&&items[1].trim().equalsIgnoreCase(param)){
                                    flag = true;
                                    break;
                                }
                                flag = false;
                            }
                        }
                    }
                    if (flag){
                        int commID = commMap.containsKey(commName)? commMap.get(commName):-1;
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
        try {
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(encodePath)));
            for (int i = 0; i < list.size(); i++) {
                bw.write(list.get(i)+"\n");
            }
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return encodePath;
    }

}
