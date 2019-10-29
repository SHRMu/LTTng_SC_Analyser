package de.tu.darmstadt.graph;

import de.tu.darmstadt.utils.MapUtils;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BarGraph {

    public static void createBarGraph(String path, String titleName, String aTitleName, String uTitleName,CategoryDataset ds,  int width,int height) throws IOException {
//	        CategoryDataset ds = getDataSet();
//	        JFreeChart chart = ChartFactory.createBarChart3D(
        JFreeChart chart = ChartFactory.createBarChart(
                titleName, //图表标题
                aTitleName, //目录轴的显示标签
                uTitleName, //数值轴的显示标签
                ds, //数据集
                PlotOrientation.VERTICAL, //图表方向 HORIZONTAL(水平的)
                false, //是否显示图例，对于简单的柱状图必须为false
                false, //是否生成提示工具
                false);         //是否生成url链接

        // 设置总的背景颜色
        chart.setBackgroundPaint(ChartColor.white);
        // 获得图表对象
        CategoryPlot p = chart.getCategoryPlot();
        // 设置图的背景颜色
        p.setBackgroundPaint(ChartColor.WHITE);
        //设置图的边框
        p.setOutlinePaint(ChartColor.white);

        BarRenderer customBarRenderer = (BarRenderer) p.getRenderer();
        //取消柱子上的渐变色
//	        customBarRenderer.setBarPainter( new StandardBarPainter() );
//	        customBarRenderer.setItemMargin(-0.01);
        //设置柱子的颜色
//	        customBarRenderer.setBaseOutlinePaint(ChartColor.red);
        Color c=new Color(0,97,183);
        customBarRenderer.setSeriesPaint(0, c);
        //设置柱子宽度
//	        customBarRenderer.setMaximumBarWidth(0.015);
//	        customBarRenderer.setMinimumBarLength(0.1);
        //设置柱子间距
        customBarRenderer.setItemMargin(1);

        //

        //设置阴影,false代表没有阴影
        customBarRenderer.setShadowVisible(true);
        // 设置柱状图的顶端显示数字
        customBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值
        customBarRenderer.setBaseItemLabelsVisible(true);
        customBarRenderer.setItemLabelAnchorOffset(0);

        //注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
//	        customBarRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
//	                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
//	          customBarRenderer.setItemLabelAnchorOffset(1D);// 设置柱形图上的文字偏离值
//	        customBarRenderer.setItemLabelsVisible(true);


        NumberAxis numberaxis = (NumberAxis) p.getRangeAxis();
//	        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        CategoryAxis axis = p.getDomainAxis(); //x轴
//	        /设置最高的一个柱与图片顶端的距离(最高柱的10%)
        numberaxis.setUpperMargin(0.3);
        // axis.setTickLabelPaint(ChartColor.red);
//	        axis.setMaximumCategoryLabelLines(10);  //标题行数，每个字显示一行
//	        axis.setMaximumCategoryLabelWidthRatio(0.5f);  //每个标题宽度，控制为1个字的宽度
//	        axis.setLabelInsets();
//	        axis.setLabelPaint(ChartColor.red);
        /*------设置X轴坐标上的文字-----------*/
        axis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 20));
        /*------设置X轴的标题文字------------*/
        axis.setLabelFont(new Font("宋体", Font.PLAIN, 20));

        /*------设置Y轴坐标上的文字-----------*/
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 20));

        // 设置显示位置
//	        p.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
//	        p.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        /*------设置Y轴的标题文字------------*/
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN,40));
        //设置y轴显示整数数据
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //设置Y轴的标题文字颜色
//	        numberaxis.setLabelPaint(ChartColor.red);
        //设置y轴文字横方向
        numberaxis.setLabelAngle(1.5);
        //设置 y轴刻度尺为隐藏
//	        numberaxis.setTickLabelsVisible(false);


        /*------这句代码解决了底部汉字乱码的问题-----------*/
        // chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));

        /*******这句代码解决了标题汉字乱码的问题********/
        chart.getTitle().setFont(new Font("宋体", Font.PLAIN, 20));

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);//"F://1.jpg"
//	            File ou=new File("F://1.jpg");

            ChartUtilities.saveChartAsPNG(new File(path), chart, width, height);
//	            ChartUtilities.writeChartAsJPEG(out, 0.5f, chart, 320, 140, null);
        } finally {
            try {
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static CategoryDataset getDataList(String demoPath) {

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        Map<String,Integer> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(demoPath)));
            String line;

            while ((line = br.readLine())!=null){
                String[] values = line.split(" ");
                for (int i = 0; i < values.length; i++) {
                    if (!map.containsKey(values[i])){
                        map.put(values[i],1);
                    }else {
                        Integer num = map.get(values[i]);
                        map.replace(values[i],num,num+1);
                    }
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        for (int i = 0; i < 101; i++) {
            if (map.keySet().contains(Integer.toString(i))){
                ds.addValue(map.get(Integer.toString(i)),"",Integer.toString(i));
            }else {
                ds.addValue(0,"",Integer.toString(i));
            }
        }
        return ds;
    }

    public static void main(String[] args) {
        try {
            BarGraph.createBarGraph("D:\\Vulnerability\\CVE-2017-7494\\result\\10.png", "sc落点位置柱状图", "pos", "nums",BarGraph.getDataList("D:\\Vulnerability\\CVE-2017-7494\\result\\10.txt"),1000,500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
