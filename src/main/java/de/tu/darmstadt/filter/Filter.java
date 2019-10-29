package de.tu.darmstadt.filter;

import de.tu.darmstadt.mapper.Mapper;
import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {

    public static List<String> filterByCount(String differPath, int max, int min){
        if (!FileUtils.checkFileExist(differPath)) {
            return null;
        }
        Map<String,Integer> countMap = new HashMap<>();
        List<String> result = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(differPath)));
            String line;
            while ((line=br.readLine())!=null){
                if (!countMap.containsKey(line)){
                    countMap.put(line,1);
                }else {
                    int num = countMap.get(line);
                    countMap.replace(line, num, num+1);
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        countMap = MapUtils.sortByValue(countMap, true);

        Set<String> strings = countMap.keySet();
        for (String s:
             strings) {
            if (countMap.get(s)<min)
                break;
            if (countMap.get(s)<max)
                result.add(s);
        }

        String resultFolder = "D:\\Vulnerability\\CVE-2017-7494-G\\result";
        BufferedWriter bw =null;
        for (int i = 0; i < result.size(); i++) {
            String outPath = resultFolder +"\\"+ String.valueOf(i)+".txt";
            try {
                bw = new BufferedWriter(new FileWriter(new File(outPath)));
                bw.write(result.get(i));
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }



//    @Test
//    public void test(){
//        List<String> list = filterBySchedSwitch("D:\\Vulnerability\\CVE-2018-10933", Mapper.loadMapper("D:\\Vulnerability\\CVE-2018-10933"), "\"ssh_server_fork\"");
//        try{
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\Vulnerability\\CVE-2018-10933"+"\\sched_switch.txt")));
//            for (int i = 0; i < list.size(); i++) {
//                bw.write(list.get(i));
//                bw.write("\n");
//            }
//            bw.close();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
}
