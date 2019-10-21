package de.tu.darmstadt.domain;

public class TrieNode {

    private int curr;
    TrieNode[] children;
    private boolean isLeaf;

    public TrieNode(int curr, boolean isLeaf) {
        this.curr = curr;
        this.isLeaf = isLeaf;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
