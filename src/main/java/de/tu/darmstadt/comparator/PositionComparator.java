package de.tu.darmstadt.comparator;

import de.tu.darmstadt.utils.FileUtils;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PositionComparator {

    public static String getPosition(List<String> sensCodes, String dirtyEncodePath){
        return null;
    }

    public static List<Float> getPosition(String sensCode, String dirtyEncode){
        if (!FileUtils.checkFileExist(dirtyEncode)) {
            return null;
        }
        BufferedReader br;
        List<Float> list = new ArrayList<>();
        try{
            br = new BufferedReader(new FileReader(new File(dirtyEncode)));
            String line;
            while ((line = br.readLine())!= null ){
                String[] split = line.split(sensCode);
                float total = line.split(" ").length;
                float prefix = split[0].split(" ").length;
                list.add((prefix/total));
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    @Test
    public void test(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\Vulnerability\\CVE-2017-7494\\result\\10.txt")));
            String line;
            while ((line = br.readLine())!=null){
                List<Float> position = getPosition(line, "D:\\Vulnerability\\CVE-2017-7494\\dirty\\encode.txt");
                position.forEach(item-> System.out.print((int)(item*100f)+" "));
                System.out.println();
                System.out.println("-----------------------------------------------------");
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
