package de.tu.darmstadt;

import de.tu.darmstadt.analyser.TrieAnalyser;
import de.tu.darmstadt.domain.TrieTree;
import de.tu.darmstadt.filter.Filter;
import de.tu.darmstadt.graph.GraphUtils;
import de.tu.darmstadt.splitter.CommSplitter;
import de.tu.darmstadt.utils.EncoderUtils;
import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.util.*;

public class Main {

    public static void main(String[] args){

        //records 主目录
        String folder = "D:\\Vulnerability\\CVE-2018-10933";

        //phase 1: 建立commMap，用于将sc名称映射为数字
        Map<String, Integer> commMap = new HashMap<>();
        //可选：lttng-k导出后的结果与实际records中sc的情况差距较大
//        commMap = MapUtils.createMapper(path);

        //phase 2; 数据编码
        String cleanEncode = EncoderUtils.dataEncoder(commMap, FileUtils.getCleanFolder(folder));
        //保存clean的commMap数据，用于最后的reverseMap
        MapUtils.saveMapper(commMap,folder);
        String dirtyEncode = EncoderUtils.dataEncoder(commMap, FileUtils.getDirtyFolder(folder));

        //phase 3: records数据流切分
        List<Integer> selectComms = new ArrayList<>();
        selectComms.add(commMap.get("sched_switch"));
        selectComms.add(commMap.get("kmem_kfree"));
        selectComms.forEach(item -> System.out.println("selected splitter commands is "+ item));
        String cleanSplitter = CommSplitter.splitByComms(cleanEncode, selectComms,true);
        String dirtySplitter = CommSplitter.splitByComms(dirtyEncode, selectComms,true);

        //phase 4: 获取dirty 中未在clean部分出现过的sc序列
        TrieTree trieTree = TrieAnalyser.buildTree(cleanSplitter);
        String differ = TrieAnalyser.searchTree(trieTree, dirtySplitter);

        //加载保存的commMap, 用于数字到sc名称的反向映射
        Map<Integer, String> reverseMap = MapUtils.loadMapper(folder);
//        String differ = "D:\\Vulnerability\\CVE-2017-7494\\dirty\\differ.txt";
        //phase 5: 提取出现至少95%以上的重复案例
        List<String> list = Filter.filterByCount(differ, 200,95);
        EncoderUtils.dataDecoder(reverseMap, list, folder);

        //绘制图像
        Set<String> lns = GraphUtils.convertToGraph(folder);
        GraphUtils.createGraph(folder,lns);

    }
}
