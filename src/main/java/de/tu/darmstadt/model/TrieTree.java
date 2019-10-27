package de.tu.darmstadt.model;

public class TrieTree {

    private TrieNode root;

    //初始化
    public TrieTree(){
        this.root = new TrieNode();
        root.isLeaf = false;
    }


    public void insert(String line){
        String[] comms = line.split(" ");
        TrieNode cuurrPos = this.root;
        for (int i = 0; i < comms.length; i++) {
            int index = Integer.valueOf(comms[i]);
            if (!cuurrPos.children.containsKey(index)){
                cuurrPos.children.put(index, new TrieNode());
            }
            cuurrPos = cuurrPos.children.get(index);
        }
        cuurrPos.isLeaf = true;
    }

    public boolean search(String line){
        TrieNode node = root;
        boolean found = true;
        String[] comms = line.split(" ");
        for (int i = 0; i < comms.length; i++) {
            int index = Integer.valueOf(comms[i]);
            if (!node.children.containsKey(index)){
                return false;
            }
            node = node.children.get(index);
        }
        return found && node.isLeaf;
    }

}
