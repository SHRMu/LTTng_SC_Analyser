package de.tu.darmstadt;

import de.tu.darmstadt.filters.Filter;
import de.tu.darmstadt.utils.EncoderUtils;
import de.tu.darmstadt.utils.MapUtils;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        //records 主目录
        String folder = "D:\\Vulnerability\\CVE-2017-7494";

        //phase 1: 建立mapper，用于将lttng命令映射为数字
        Map<String, Integer> commMap = new HashMap<>();
//        commMap = MapUtils.createMapper(path);

        //phase 2; 数据编码，data encoding
//        String cleanEncoder = EncoderUtils.dataEncoder(commMap, FileUtils.getCleanFolder(folder));
//        MapUtils.saveMapper(commMap,folder);
//        String dirtyEncoder = EncoderUtils.dataEncoder(commMap, FileUtils.getDirtyFolder(folder));
//
//        //phase 3: records数据流切分
//        List<Integer> selectComms = new ArrayList<>();
//        selectComms.add(commMap.get("sched_switch"));
//        selectComms.add(commMap.get("kmem_kfree"));
//        selectComms.forEach(item -> System.out.println("selected splitter commands is "+ item));
//        String cleanSplitter = SplitterUtils.splitByComms(cleanEncoder, selectComms,true);
//        String dirtySplitter = SplitterUtils.splitByComms(dirtyEncoder, selectComms,true);
//
//        //phase 4: 获取dirty records中未在clean部分出现过的sc序列
//        TrieTree trieTree = TrieAnalyser.buildTree(cleanSplitter);
//        String differ = TrieAnalyser.searchTree(trieTree, dirtySplitter);

        Map<Integer, String> reverseMap = MapUtils.loadMapper("D:\\Vulnerability\\CVE-2017-7494\\lttng-map.txt");
        String differ = "D:\\Vulnerability\\CVE-2017-7494\\dirty\\differ.txt";
        //phase 5: 提取出现至少90%以上的重复案例
        List<String> list = Filter.filterByCount(differ, 95);
        EncoderUtils.dataDecoder(reverseMap, list, folder);


    }
}
