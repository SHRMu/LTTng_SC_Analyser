package de.tu.darmstadt.analyser;

import de.tu.darmstadt.domain.TrieTree;
import de.tu.darmstadt.utils.FileUtils;

import java.io.*;

public class TrieAnalyser {

    //基于clean splitter的结果构建trie
    public static TrieTree buildTree(String cleanPath){
        if (!FileUtils.checkIsFile(cleanPath)) {
            return null;
        }
        TrieTree trieTree = new TrieTree();
        BufferedReader br;
        String line;
        try{
            br  = new BufferedReader(new FileReader(cleanPath));
            while ((line = br.readLine())!=null){
                trieTree.insert(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return trieTree;
    }

    //基于构建的trie来识别dirty中出现的sensitive sc序列
    public static String searchTree(TrieTree trieTree, String dirtyPath){
        if (!FileUtils.checkIsFile(dirtyPath)) {
            return "";
        }
        String outPath = dirtyPath.replaceAll(FileUtils.SPLITED_FILE_NAME,FileUtils.DIFFER_FILE_NAME);
        BufferedReader br;
        BufferedWriter bw;
        try{
            br = new BufferedReader(new FileReader(dirtyPath));
            bw = new BufferedWriter(new FileWriter(new File(outPath)));
            String line;
            while ((line = br.readLine())!=null){
                boolean found = trieTree.search(line);
                if (!found){
                    bw.write(line+"\n");
                }
            }
            br.close();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return outPath;
    }
}
