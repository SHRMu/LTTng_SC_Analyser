package de.tu.darmstadt.utils;

import org.junit.Test;

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


    public static <K,V> Map<V,K> reverseMap(Map<K,V> map){
        Map<V,K> reverseMap = new HashMap<>();
        map.entrySet().forEach(entry->{
            reverseMap.put(entry.getValue(),entry.getKey());
        });
        return reverseMap;
    }

    @Test
    public void test(){
        Map<String, Integer> commMap = new HashMap<>();
        commMap.put("sc",1);
        Map<Integer, String> integerStringMap = reverseMap(commMap);
        integerStringMap.entrySet().forEach(entry->{
            System.out.println(entry.getKey()+":"+entry.getValue());
        });
    }

}
