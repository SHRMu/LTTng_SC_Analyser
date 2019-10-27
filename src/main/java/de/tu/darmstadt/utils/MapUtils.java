package de.tu.darmstadt.utils;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class MapUtils {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean reverse){
        List<Entry<K,V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        if (reverse)
            Collections.reverse(list);
        Map<K,V> result = new LinkedHashMap<>();
        list.forEach(item -> result.put(item.getKey(), item.getValue()));
        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return sortByValue(map,false);
    }

}
