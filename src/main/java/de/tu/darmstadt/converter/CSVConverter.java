package de.tu.darmstadt.converter;

import com.sun.corba.se.impl.orbutil.ObjectUtility;
import de.tu.darmstadt.model.SCRecord;
import de.tu.darmstadt.utils.FileUtils;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVConverter {

    public void convertToCSV(String folderPath){
        if (!FileUtils.checkFolderExist(folderPath)) {
            return;
        }

        String outfolder = folderPath + "\\csv";
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        BufferedReader br = null;
        List<SCRecord> recordList;
        for (int i = 0; i < files.length; i++) {
            try {
                br = new BufferedReader(new FileReader(files[i]));
                recordList = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null){
                    SCRecord scRecord = new SCRecord();
                    String[] split = line.split(": ");
                    String[] prefix = split[0].split(" ");
                    if (prefix.length<4){
                        System.out.println(line);
                        break;
                    }
                    Pattern p = Pattern.compile("\\[(.*?)\\]");
                    Matcher m = p.matcher(prefix[0]);
                    while (m.find()) scRecord.setTime_Stampe(m.group(1));
                    p = Pattern.compile("\\((.*?)\\)");
                    m = p.matcher(prefix[1]);
                    while (m.find()) {
                        try {
                            scRecord.setTime(Double.valueOf(m.group(1)));
                        }catch (NumberFormatException e){
                            ;
                        }
                    }
                    scRecord.setOs_sys(prefix[2]);
                    scRecord.setSc_name(prefix[3]);
                    p = Pattern.compile("\\{(.*?)\\}");
                    m = p.matcher(split[1]);
                    while (m.find()){
                        String s = m.group(1);
                        if (s.contains("cpu_id")){
                            try {
                                scRecord.setCpu_id(s.split("=")[1]);
                            }catch (ArrayIndexOutOfBoundsException e){
                                System.out.println(line);
                            }
                        } else if (s.contains(",")){
                            scRecord.setParams(s);
                        }
                    }
                    recordList.add(scRecord);
                }
                writeCSV(recordList, outfolder+"\\"+files[i].getName().replace("txt","csv"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try {
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void writeCSV(List<SCRecord> list, String csvFilePath){
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try{
            File csvFile = new File(csvFilePath);
            fos = new FileOutputStream(csvFile);
            osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
            bw = new BufferedWriter(osw);
            String title = "time_Stampe, time, os_sys, sc_name, cpu_id, params";
            bw.append(title).append("\n");
            if (list!=null && !list.isEmpty()){
                for (SCRecord data:
                     list) {
                    bw.append(data.getTime_Stampe() + ",");
                    bw.append(data.getTime()+",");
                    bw.append(data.getOs_sys()+",");
                    bw.append(data.getSc_name()+",");
                    bw.append(data.getCpu_id()+",");
                    bw.append(data.getParams()+"\n");
                }
            }
            bw.close();
            osw.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        convertToCSV("D:\\Vulnerability\\CVE-2017-7494\\clean");
    }

}
