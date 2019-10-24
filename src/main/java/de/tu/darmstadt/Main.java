package de.tu.darmstadt;

import de.tu.darmstadt.utils.EncoderUtils;
import de.tu.darmstadt.utils.MapUtils;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String path = "D:\\Vulnerability\\CVE-2017-7494";
        String cleanPath = "D:\\Vulnerability\\CVE-2017-7494\\clean";
        String dirtyPath = "D:\\Vulnerability\\CVE-2017-7494\\dirty";
        Map<String, Integer> mapper = MapUtils.createMapper(path);
        EncoderUtils.dataEncoder(mapper, cleanPath);
        EncoderUtils.dataEncoder(mapper, dirtyPath);
    }
}
