package de.tu.darmstadt.domain;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {

    Map<Integer, TrieNode> children;
    boolean isLeaf;

    public TrieNode(){
        children = new HashMap<Integer, TrieNode>();
        isLeaf = false;
    }

}
