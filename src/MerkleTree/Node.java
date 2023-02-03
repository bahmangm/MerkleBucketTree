package MerkleTree;

//Each node of the MBT is an instance of this class
public class Node {

    private Node left;
    private Node right;
    private String hash;
    /*The largest bucketIndex of its children for a non leaf and the index of the 
    relevant bucket of the hash table for a leaf*/  
    private int bucketIndex;

    public Node(Node left, Node right, String hash, int bucketIndex) {
        this.left = left;
        this.right = right;
        this.hash = hash;
        this.bucketIndex = bucketIndex;
    }

    public int getBucketIndex(){
    	return this.bucketIndex;
    }
    
    public void setBucketIndex(int bucketIndex) {
    	this.bucketIndex = bucketIndex;
    }
    
    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}