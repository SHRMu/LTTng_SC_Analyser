package de.tu.darmstadt.graphs;

import org.junit.Test;

import java.util.List;

public class GraphUtils {

    private static final String TARGET_FOLDER = "\\graph";
    private static final String GRAPH_DOT_PATH = "D:\\Software\\Graphviz2.38\\bin\\dot.exe";

    public static void createGraph(String folderPath){
        GraphViz gViz=new GraphViz(folderPath.concat(TARGET_FOLDER), GRAPH_DOT_PATH);
        gViz.start_graph();
//        gViz.addln("A->B;");
//        gViz.addln("A->C;");
//        gViz.addln("C->B;");
//        gViz.addln("B->D;");
//        gViz.addln("C->E;");
        gViz.end_graph();
        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
