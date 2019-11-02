package de.tu.darmstadt.analyser;

import de.tu.darmstadt.model.TrieTree;
import de.tu.darmstadt.utils.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TrieAnalyser {

    public static Map<String,Integer> run(String cleanSplitPath, String dirtySplitPath){
        TrieAnalyser trieAnalyser = new TrieAnalyser();
        TrieTree trieTree = trieAnalyser.buildTree(cleanSplitPath);
        Map<String,Integer> countMap = trieAnalyser.searchTree(trieTree, dirtySplitPath);
        return countMap;
    }

    //基于clean split的结果构建trieTree
    private TrieTree buildTree(String cleanPath){
        if (!FileUtils.checkFileExist(cleanPath)) {
            return null;
        }
        TrieTree trieTree = new TrieTree();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(cleanPath)))){
            String line;
            while ((line = br.readLine())!=null){
                trieTree.insert(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return trieTree;
    }

    //基于构建的clean trie来识别dirty中出现的sensitive sc序列
    private Map<String, Integer> searchTree(TrieTree trieTree, String dirtyPath){
        if (!FileUtils.checkFileExist(dirtyPath)) {
            return null;
        }

        Map<String, Integer> countMap = null;

        try(BufferedReader br = new BufferedReader(new FileReader(new File(dirtyPath)))){
            countMap = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null ){
                boolean found = trieTree.search(line);
                if (!found){ //统计只在dirty split中出现的序列
                    if (!countMap.containsKey(line)){
                        countMap.put(line,1);
                    }else {
                        int temp = countMap.get(line);
                        countMap.replace(line,temp,temp+1);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return countMap;
    }
}
