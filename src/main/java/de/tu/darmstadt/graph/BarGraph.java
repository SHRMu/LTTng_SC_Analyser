package de.tu.darmstadt.graph;

import de.tu.darmstadt.model.BarChart;
import de.tu.darmstadt.utils.FileUtils;
import org.jfree.chart.*;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarGraph {

    private int width = 1000;
    private int height = 1000;

    private String folderPath;

    public BarGraph(String folderPath) {
        this.folderPath = folderPath;
    }

    public void createGraph(List<String> scList) {
        List<DefaultCategoryDataset> dataList = getDataList(scList);

        for (int i = 0; i < dataList.size(); i++) {
            String filePath = folderPath + "\\graph\\" + (i+1)+ ".png";
            try(FileOutputStream out = new FileOutputStream(filePath)) {
                ChartUtilities.saveChartAsPNG(new File(filePath), new BarChart().create(dataList.get(i)), width, height);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }


    private List<DefaultCategoryDataset> getDataList(List<String> scList) {

        List<DefaultCategoryDataset> dsList = new ArrayList<>();

        List<Map> posList = getPosList(scList);
        posList.forEach(pos->{
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            for (int i = 1; i < 101; i++) {
                if (pos.containsKey(i)){
                    ds.addValue((Number) pos.get(i),"",Integer.toString(i));
                }else {
                    ds.addValue(0,"",Integer.toString(i));
                }
            }
            dsList.add(ds);
        });

        return dsList;
    }


    private List<Map> getPosList(List<String> scList){

        String dirtyEncode = this.folderPath +"\\dirty\\"+"encode.txt";

        List<Map> posList = new ArrayList();
        scList.forEach(sc->{
            Map<Integer, Integer> posMap = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(new File(dirtyEncode)))){
                String line;
                while ((line = br.readLine()) != null){
                    int prefix = line.split(sc)[0].split(" ").length;
                    int total = line.split(" ").length;
                    int pos = 100*prefix/total;
                    if (!posMap.containsKey(pos)){
                        posMap.put(pos,1);
                    }else {
                        int temp = posMap.get(pos);
                        posMap.replace(pos,temp,temp+1);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            posList.add(posMap);
        });

        return posList;
    }

}
