package de.tu.darmstadt.compators;

import de.tu.darmstadt.domain.TrieTree;

import java.io.*;

public class TrieTreeComp {

    private static TrieTree buildTree(String filePath){
        TrieTree trieTree = new TrieTree();
        File file = new File(filePath);
        if (!file.exists())
            return null;
        BufferedReader br;
        try{
            br  = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine())!=null){
                trieTree.insert(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return trieTree;
    }

    private static void searchTree(TrieTree trieTree, String filePath, String outPath){
        File file = new File(filePath);
        if (!file.exists())
            return;

        File out = new File(outPath);
        if (out.exists())
            out.delete();
        BufferedReader br;
        BufferedWriter bw;
        try{
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath, true)));
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
    }
}
