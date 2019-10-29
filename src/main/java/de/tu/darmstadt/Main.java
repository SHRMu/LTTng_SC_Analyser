package de.tu.darmstadt;

import de.tu.darmstadt.analyser.TrieAnalyser;
import de.tu.darmstadt.coder.DataDecoder;
import de.tu.darmstadt.coder.DataEncoder;
import de.tu.darmstadt.mapper.Mapper;
import de.tu.darmstadt.model.TrieTree;
import de.tu.darmstadt.filter.Filter;
import de.tu.darmstadt.graph.SCGraph;
import de.tu.darmstadt.splitter.CommSplitter;
import de.tu.darmstadt.utils.FileUtils;

import java.util.*;

public class Main {

    public static void main(String[] args){

        //records主目录，包括clean,dirty两个记录文件夹
        String folder = "D:\\Vulnerability\\CVE-2017-7494-G";
        //验证数据的数量
        int validCount = 150;

        //phase 1: 建立commMap，用于将sc名称映射为数字
        Map<String, Integer> commMap = new HashMap<>();
        //结论：可不用，lttng-k导出后的结果与实际records中sc name的情况差距较大
//        commMap = MapUtils.createMapper(path);

        //phase 2; 数据编码，将sc name映射到数字
        DataEncoder encoder = new DataEncoder(validCount,true);

        //method I: 将所有的records记录进行编码
//        String cleanEncode = encoder.encoding(commMap, FileUtils.getCleanFolder(folder));
//        String dirtyEncode = encoder.encoding(commMap, FileUtils.getDirtyFolder(folder));

        //method II: 只编码sched_switch到指定service的records
        commMap = Mapper.loadMapper(folder);
        String cleanEncode = encoder.encodingSchedSwitch(commMap, FileUtils.getCleanFolder(folder),"\"smbd\"");
        String dirtyEncode = encoder.encodingSchedSwitch(commMap, FileUtils.getDirtyFolder(folder), "\"smbd\"");

        //phase 3: records数据流切分
        // method I： 通过指定的sc name完成数据切分
        List<Integer> selectComms = new ArrayList<>(Arrays.asList(commMap.get("sched_switch"),commMap.get("kmem_kfree")));
        selectComms.forEach(item -> System.out.println("selected commID is "+ item));
        String cleanSplitter = CommSplitter.splitByComms(cleanEncode, selectComms);
        String dirtySplitter = CommSplitter.splitByComms(dirtyEncode, selectComms);

        // method II: 通过指定行数切分数据,验证效果不理想
//        String cleanSplitter = LineSplitter.splitByLine(cleanEncode, 5);
//        String dirtySplitter = LineSplitter.splitByLine(dirtyEncode, 5);

        //phase 4: 获取dirty中未在clean部分出现过的sc序列
        String differ = TrieAnalyser.run(cleanSplitter, dirtySplitter);

        //加载保存的commMap, 用于数字到sc名称的反向映射
        Map<Integer, String> reverseMap = Mapper.loadReverseMapper(folder);
        //phase 5: 提取出现至少95%以上的重复案例
        List<String> list = Filter.filterByCount(differ,(int)(validCount * 1.05f),(int)(validCount*0.95f));
        list.forEach(item-> System.out.println(item));
        DataDecoder.decoding(reverseMap, list, folder);

        //phase 6: 绘制图像
        Set<String> lns = SCGraph.convertToGraph(folder);
        SCGraph.createGraph(folder,lns);

        //phase 7: 验证测试

    }
}
