package de.tu.darmstadt.domain;

public class TrieTree {

    private TrieNode root;

    //初始化
    public TrieTree(){
        this.root = new TrieNode(119, false);
    }

    public void insert(String line){
        String[] comms = line.split(" ");
        TrieNode cuurrPos = this.root;
        for (int i = 0; i < comms.length; i++) {
            int index = Integer.getInteger(comms[i]);
            if (cuurrPos.children[index]==null){
                TrieNode newNode = new TrieNode(index,false);
                cuurrPos.children[index] = newNode;
            }
            if ( index == 119 ){
                cuurrPos.children[index].setLeaf(true);
            }
            cuurrPos = cuurrPos.children[index];
        }
    }




}
