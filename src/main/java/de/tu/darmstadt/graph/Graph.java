package de.tu.darmstadt.graph;

import java.util.List;
import java.util.Map;

public class Graph {
    private String folderPath;
    private List<String> scList;
    private Map<Integer, String> reverseMap;

    public Graph(String folderPath, List<String> scList, Map<Integer, String> reverseMap) {
        this.folderPath = folderPath;
        this.scList = scList;
        this.reverseMap = reverseMap;
    }

    public void run(){

        SCGraph scGraph = new SCGraph(folderPath);
        scGraph.createGraph(scList, reverseMap);

        BarGraph barGraph = new BarGraph(folderPath);
        barGraph.createGraph(scList);

    }
}
