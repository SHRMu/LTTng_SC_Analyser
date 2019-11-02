package de.tu.darmstadt.graph;

import de.tu.darmstadt.model.GraphViz;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SCGraph {

    private static final String GRAPH_DOT_PATH = "D:\\Software\\Graphviz2.38\\bin\\dot.exe";

    private String folderPath;

    public SCGraph(String folderPath) {
        this.folderPath = folderPath;
    }

    public void createGraph(List<String> scList, Map<Integer, String> reverseMap){
        String graphFolder = folderPath +"\\graph";
        for (int i = 0; i < scList.size(); i++) {
            GraphViz gViz=new GraphViz(graphFolder, GRAPH_DOT_PATH);
            gViz.setDotCodeFile((i+1)+".txt");
            gViz.setResultGif(String.valueOf(i+1));
            gViz.start_graph();
            Set<String> lns = getGraphLn(scList.get(i), reverseMap);
            lns.forEach(ln -> gViz.addln(ln));
            gViz.end_graph();
            try {
                gViz.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Set<String> getGraphLn(String line, Map<Integer, String> reverseMap){
        if (line == null || line.equals(""))
            return null;
        Set<String> lns = new HashSet<>();
        String[] ids = line.split(" ");
        for (int i = 0; i < ids.length-1; i++) {
            String start = reverseMap.containsKey(Integer.valueOf(ids[i])) ? reverseMap.get(Integer.valueOf(ids[i])): null;
            String end = reverseMap.containsKey(Integer.valueOf(ids[i+1])) ? reverseMap.get(Integer.valueOf(ids[i+1])):null;
            lns.add(start+"->"+end+";");
        }
        return lns;
    }

    @Test
    public void test(){
//        try(BufferedReader br = new BufferedReader(new FileReader(new File("D:\\Vulnerability\\CVE-2017-7494-G\\decode.txt")))){
//            String line;
//            List<String> list = new ArrayList<>();
//            while ((line = br.readLine()) != null){
//                list.add(line);
//            }
//            createGraph("D:\\Vulnerability\\CVE-2017-7494-G\\graph", list);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
    }

}
