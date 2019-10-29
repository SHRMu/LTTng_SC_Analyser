package de.tu.darmstadt.analyser;

import de.tu.darmstadt.model.TrieTree;
import de.tu.darmstadt.utils.FileUtils;

import java.io.*;

public class TrieAnalyser {

    public static String run(String cleanSplitPath, String dirtySplitPath){
        TrieAnalyser trieAnalyser = new TrieAnalyser();
        TrieTree trieTree = trieAnalyser.buildTree(cleanSplitPath);
        String differPath = trieAnalyser.searchTree(trieTree, dirtySplitPath);
        return differPath;
    }

    //基于clean splitter的结果构建trie
    private TrieTree buildTree(String cleanPath){
        if (!FileUtils.checkFileExist(cleanPath)) {
            return null;
        }
        TrieTree trieTree = new TrieTree();
        BufferedReader br;
        String line;
        try{
            br  = new BufferedReader(new FileReader(cleanPath));
            while ((line = br.readLine())!=null){
                if (!line.isEmpty())
                    trieTree.insert(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return trieTree;
    }

    //基于构建的trie来识别dirty中出现的sensitive sc序列
    private String searchTree(TrieTree trieTree, String dirtyPath){
        if (!FileUtils.checkFileExist(dirtyPath)) {
            return "";
        }
        String outPath = dirtyPath.replaceAll(FileUtils.SPLITED_FILE_NAME,FileUtils.DIFFER_FILE_NAME);
        BufferedReader br;
        BufferedWriter bw;
        try{
            br = new BufferedReader(new FileReader(dirtyPath));
            bw = new BufferedWriter(new FileWriter(new File(outPath)));
            String line = br.readLine();
            while (line!=null && !line.isEmpty()){
                boolean found = trieTree.search(line);
                if (!found){
                    bw.write(line+"\n");
                }
                line = br.readLine();
            }
            br.close();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return outPath;
    }
}
