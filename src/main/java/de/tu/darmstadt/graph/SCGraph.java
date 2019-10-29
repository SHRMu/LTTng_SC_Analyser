package de.tu.darmstadt.graph;

import de.tu.darmstadt.model.GraphViz;
import de.tu.darmstadt.utils.FileUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SCGraph {

    private static final String TARGET_FOLDER = "\\graph";
    private static final String GRAPH_DOT_PATH = "D:\\Software\\Graphviz2.38\\bin\\dot.exe";

    public static void createGraph(String folderPath, Set<String> lns){
        GraphViz gViz=new GraphViz(folderPath.concat(TARGET_FOLDER), GRAPH_DOT_PATH);
        gViz.start_graph();
        lns.forEach(ln -> gViz.addln(ln));
        gViz.end_graph();
        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将 folder 下的decode.txt转化为graph的格式
    public static Set<String> convertToGraph(String folderPath){
        String decodePath = folderPath + "\\" + FileUtils.DECODE_FILE_NAME;
        BufferedReader br;
        Set<String> list = new HashSet<>();
        try{
            br =new BufferedReader(new FileReader(new File(decodePath)));
            String line;
            while ((line = br.readLine()) != null){
                String[] scs = line.trim().split(" ");
                for (int i = 0; i < scs.length-1; i++) {
                    list.add(scs[i]+"->"+scs[i+1]+";");
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    @Test
    public void test(){
        createGraph("D:\\Vulnerability\\CVE-2017-7494",SCGraph.convertToGraph("D:\\Vulnerability\\CVE-2017-7494"));
    }

}
