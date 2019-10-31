package de.tu.darmstadt.utils;

import de.tu.darmstadt.exception.ParamsExtractException;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sc line参数分析和提取工具
 */
public class ParamsUtils {

    /**
     * 提取每一行记录的sc name
     * @param line record readline
     * @return sc name
     */
    public static String getScName(String line){
        String scName;

        String[] prefix  = line.split(": ")[0].split(" ");
        scName = prefix[prefix.length-1];

        return scName;
    }

    /**
     * 验证是否为指定参数的sc记录
     * @param line
     * @param param
     * @return
     */
    public static boolean checkServiceParam(String line, String param){
        if (getScName(line).equalsIgnoreCase("sched_switch")){
            String[] split = line.split("}, ");
            String s = split[split.length-1];
            Pattern p = Pattern.compile("\\{(.*?)\\}");
            Matcher m = p.matcher(s);
            while (m.find()){
                String[] params = m.group(1).split(",");
                for (int i = 0; i < params.length; i++) {
                    String[] items = params[i].split("=");
                    if (items[0].trim().equalsIgnoreCase("next_comm")&&items[1].trim().equalsIgnoreCase(param)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Test
    public void test(){
        String line = "?";
        String abd = getScName(line);
        System.out.println(abd);
    }


}
