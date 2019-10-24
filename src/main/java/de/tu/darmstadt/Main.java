package de.tu.darmstadt;

import de.tu.darmstadt.utils.EncoderUtils;
import de.tu.darmstadt.utils.FileUtils;
import de.tu.darmstadt.utils.SplitterUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String path = "D:\\Vulnerability\\CVE-2017-7494";

        //phase 1: 建立mapper
        Map<String, Integer> commMap = new HashMap<>();
//        commMap = MapUtils.createMapper(path);

        //phase 2; data encoding
        String clean = EncoderUtils.dataEncoder(commMap, FileUtils.getCleanFolder(path));
        String dirty = EncoderUtils.dataEncoder(commMap, FileUtils.getDirtyFolder(path));

        //phase 3: data splitting
        List<Integer> comms = new ArrayList<>();
        comms.add(commMap.get("sched_switch"));
        comms.add(commMap.get("kmem_kfree"));
        comms.forEach(item -> System.out.println(item+" "));
        SplitterUtils.splitByComms(clean, comms,true);
        SplitterUtils.splitByComms(dirty, comms,true);

        //phase 4: data Comparator

    }
}
