package de.tu.darmstadt;

import de.tu.darmstadt.analyser.TrieAnalyser;
import de.tu.darmstadt.graph.Graph;
import de.tu.darmstadt.graph.SCGraph;
import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.util.*;

public class Main {

    public static void main(String[] args){

        //records主目录，包括clean,dirty两个记录文件夹
        String folder = "D:\\Vulnerability\\CVE-2017-7494-G";
        //验证数据的数量
        int validCount = 190;

        //phase 1: 建立commMap，保存到lttng-map.txt, 用于将sc name映射为数字
        Mapper mapper = new Mapper(folder);
        mapper.run();
        Map<String, Integer> commMap = mapper.loadMap(folder);
        System.out.println("commMap size ::: " + commMap.size());
        System.out.println("---------------------- mapping END ---------------------------");

        //phase 2; 数据编码，将sc name映射到数字，减小存储，方便进一步分析和学习
        DataEncoder encoder = new DataEncoder(validCount,true);

        //method I: 将所有的records记录进行编码
//        String cleanEncode = encoder.encoding(commMap, FileUtils.getCleanFolder(folder));
//        String dirtyEncode = encoder.encoding(commMap, FileUtils.getDirtyFolder(folder));

        //method II: 只编码sched_switch到指定service的数据
//        String param = "\"ssh_server_fork\"";
        String param = "\"smbd\"";
        String cleanEncode = encoder.encodingService(FileUtils.getCleanFolder(folder), commMap, param);
        String dirtyEncode = encoder.encodingService(FileUtils.getDirtyFolder(folder), commMap, param);
        System.out.println("----------------------- coding END ------------------------");

        //phase 3: records数据流切分
        // method I： 通过指定的sc name完成数据切分
        List<Integer> selectComms = new ArrayList<>(Arrays.asList(commMap.get("kmem_kfree"),commMap.get("kmem_kmalloc")));
        selectComms.forEach(item -> System.out.println("selected commID is "+ item));
        String cleanSplitter = DataSplitter.splitByComms(cleanEncode, selectComms);
        String dirtySplitter = DataSplitter.splitByComms(dirtyEncode, selectComms);

        // method II: 通过指定行数切分数据,验证效果不理想
//        String cleanSplitter = LineSplitter.splitByLine(cleanEncode, 5);
//        String dirtySplitter = LineSplitter.splitByLine(dirtyEncode, 5);

//        //phase 4: 获取dirty中未在clean部分出现过的sc序列
        Map<String, Integer> countMap = TrieAnalyser.run(cleanSplitter, dirtySplitter);

        //反转commMap, 用于数字到sc名称的反向映射
        Map<Integer, String> reverseMap = MapUtils.reverseMap(commMap);
        //phase 5: 提取出现至少95%以上的重复案例
        List<String> scList = DataFilter.filterByCount(folder,countMap,(int)(validCount * 2.0f),(int)(validCount*0.95f));
        DataDecoder.decoding(folder,scList,reverseMap);

        //phase 6: 绘制图像
        Graph graph = new Graph(folder, scList, reverseMap);
        graph.run();


    }
}
