package de.tu.darmstadt.comparator;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CodeComparator {

    /**
     * 比较两个不同的decode文件所记录的sensitive sc chains的共同点
     */
    public static void twoComparator(String decodePath1, String decodePath2){
        BufferedReader br;
        try{
           br = new BufferedReader(new FileReader(new File(decodePath1)));
           String line;
            Set<String> set = new HashSet<>();
           while ((line=br.readLine())!=null){
                set.add(line.trim());
           }
           br = new BufferedReader(new FileReader(new File(decodePath2)));
           while ((line=br.readLine()) !=null){
               if (set.contains(line.trim()));
               System.out.println(line);
           }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        twoComparator("D:\\Vulnerability\\CVE-2017-7494-G\\result\\decode_160.txt","D:\\Vulnerability\\CVE-2017-7494\\decode.txt");
    }

}
