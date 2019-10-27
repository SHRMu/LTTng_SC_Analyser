package de.tu.darmstadt.splitter;

import de.tu.darmstadt.utils.FileUtils;
import org.junit.Test;

import java.io.*;

public class LineSplitter {

    /**
     * 按照一定数量，e.g.10 来切分records的数据
     */
    public static String splitByLine(String encodePath, int setCount){

        if (!FileUtils.checkFileExist(encodePath)) {
            return "";
        }

        String splitPath = encodePath.replaceAll(FileUtils.ENCODE_FILE_NAME,FileUtils.SPLITED_FILE_NAME);

        BufferedReader br;
        BufferedWriter bw;
        try{
            br = new BufferedReader(new FileReader(new File(encodePath)));
            bw = new BufferedWriter(new FileWriter(new File(splitPath)));
            String line ;
            StringBuilder sb;
            while ((line=br.readLine())!=null){
                sb = new StringBuilder();
                String[] ids = line.split(" ");
                for (int i = 0; i < ids.length; i++) {
                    sb.append(ids[i]+" ");
                    if ((i+1)%setCount == 0)
                        sb.append("\n");
                }
                sb.append("\n");
                bw.write(sb.toString().replaceAll("\n\n","\n"));
            }
            br.close();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return splitPath;
    }

    @Test
    public void test(){
        splitByLine("D:\\Vulnerability\\CVE-2017-7494-G\\clean\\encode.txt",10);
    }
}
