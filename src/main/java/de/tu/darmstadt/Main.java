package de.tu.darmstadt;

import de.tu.darmstadt.analyser.TrieAnalyser;
import de.tu.darmstadt.coder.DataDecoder;
import de.tu.darmstadt.coder.DataEncoder;
import de.tu.darmstadt.domain.TrieTree;
import de.tu.darmstadt.filter.Filter;
import de.tu.darmstadt.graph.GraphUtils;
import de.tu.darmstadt.splitter.CommSplitter;
import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.util.*;

public class Main {

    public static void main(String[] args){

        //records 主目录
        String folder = "D:\\Vulnerability\\CVE-2017-7494";
        int selectedCount = 90;

        //phase 1: 建立commMap，用于将sc名称映射为数字
        Map<String, Integer> commMap = new HashMap<>();
        //可选：lttng-k导出后的结果与实际records中sc的情况差距较大
//        commMap = MapUtils.createMapper(path);

        //phase 2; 数据编码
        String cleanEncode = DataEncoder.encoding(commMap, FileUtils.getCleanFolder(folder), selectedCount,true);
        //保存clean的commMap数据，用于最后的reverseMap
        MapUtils.saveMapper(commMap,folder);
        String dirtyEncode = DataEncoder.encoding(commMap, FileUtils.getDirtyFolder(folder), selectedCount, true);

        //phase 3: records数据流切分
        List<Integer> selectComms = new ArrayList<>();
        selectComms.add(commMap.get("sched_switch"));
        selectComms.add(commMap.get("kmem_kfree"));
        selectComms.forEach(item -> System.out.println("selected splitter commands is "+ item));
        String cleanSplitter = CommSplitter.splitByComms(cleanEncode, selectComms);
        String dirtySplitter = CommSplitter.splitByComms(dirtyEncode, selectComms);

        //phase 4: 获取dirty 中未在clean部分出现过的sc序列
        TrieTree trieTree = TrieAnalyser.buildTree(cleanSplitter);
        String differ = TrieAnalyser.searchTree(trieTree, dirtySplitter);

        //加载保存的commMap, 用于数字到sc名称的反向映射
        Map<Integer, String> reverseMap = MapUtils.loadMapper(folder);
        //phase 5: 提取出现至少95%以上的重复案例
        List<String> list = Filter.filterByCount(differ,(int)(selectedCount * 1.05f),(int)(selectedCount*0.95f));
        DataDecoder.decoding(reverseMap, list, folder);

        //绘制图像
        Set<String> lns = GraphUtils.convertToGraph(folder);
        GraphUtils.createGraph(folder,lns);

    }
}
