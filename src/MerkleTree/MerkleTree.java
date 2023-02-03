package MerkleTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import HashTable.HashTable;

public class MerkleTree {
	
	private static int capacity = 5; // The capacity of the hash table
	private static HashTable hashTable = new HashTable(capacity);

	/*Takes a list of buckets hashes and make a list of MBT nodes from them to 
	feed to buildTree method*/ 
	private static Node generateTree(ArrayList<String> blocksHashes) {
		ArrayList<Node> childNodes = new ArrayList<>();
		
		for (String message : blocksHashes)
			childNodes.add(new Node(null, null, message, -1)); // -1 is replaced with a correct bucket's index in the buildTree method.
		
		return buildTree(childNodes);
	}

	/*Takes a list of Nodes and converts them to leaves and then 
	constructs the higher levels of the MBT to its root*/
    private static Node buildTree(ArrayList<Node> children) {
        ArrayList<Node> parents = new ArrayList<>();

        boolean leafFlag = true; // To start working with leaf nodes.
        
        while (children.size() != 1) { // children.size() becomes 1 once the tree has been built. 
            int index = 0, length = children.size();
            
            while (index < length) {
                Node leftChild = children.get(index);
                if (leafFlag)
                	leftChild.setBucketIndex(index);
                
                Node rightChild = null;
                
                if ((index + 1) < length) {
                    rightChild = children.get(index + 1);
                    if (leafFlag)
                    	rightChild.setBucketIndex(index + 1);
                }
                else // To keep the tree balanced, left child is duplicated to create a right child when there is no more nodes.
                	rightChild = new Node(null, null, leftChild.getHash(), -2);
                
                String parentHash = HashAlgorithm.generateHash(leftChild.getHash() + rightChild.getHash());
                parents.add(new Node(leftChild, rightChild, parentHash, Math.max(leftChild.getBucketIndex(), rightChild.getBucketIndex())));
                index += 2;
            }
            children = parents;
            parents = new ArrayList<>();
            leafFlag = false; // To start working with non-leaf nodes.
        }
        return children.get(0);
    }

    /*Shows the hashes of each level of MBT with an empty line to distinguish 
    consecutive levels*/
    private static void printLevelOrderTraversal(Node root) {
        if (root == null)
        	return;
        
        if ((root.getLeft() == null && root.getRight() == null))
        	System.out.format("Hash: '%s'--->Bucket's Index: '%d' \n", root.getHash(), root.getBucketIndex());
        
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null); // To put a delimiter between two consecutive levels of the MBT

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            
            if (node != null)
            	System.out.format("Hash: '%s'--->Bucket's Index: '%d' \n", node.getHash(),node.getBucketIndex());
            else
            {
            	System.out.println();
                if (!queue.isEmpty())
                	queue.add(null); // To put a delimiter between two consecutive levels of the MBT
            }

            if (node != null && node.getLeft() != null)
            	queue.add(node.getLeft());
            
            if (node != null && node.getRight() != null)
            	queue.add(node.getRight());
        }
    }
    
    /*Searches for the key in the MBT by traversing from its root to 
    the relevant bucket and then traversing the binary search tree in that bucket 
    considering copyOnWrite restriction*/
    private static Object copyOnWriteLookup(Node root, String key) {
		if (root.getLeft() == null && root.getRight() == null)
			return hashTable.getValue(hashTable.buckets[root.getBucketIndex()], key);
		
		int bucketIndex = hashTable.bucketIndexFor(key);
    	
		if(bucketIndex <= root.getLeft().getBucketIndex())
			return copyOnWriteLookup(root.getLeft(), key);
		else
			return copyOnWriteLookup(root.getRight(), key);	
    }
    
    /*Inserts a node in the MBT and then produces the required new hashes in the MBT
    considering copyOnWrite restriction*/
    private static void copyOnWriteInsert(Node root, String key, Object value) {
		System.out.println("befor----"+ root.getHash() +" Bucketindex: " +root.getBucketIndex());
		
    	if (root.getLeft() == null && root.getRight() == null) {
    		System.out.println("\nThe index of selected bucket to insert is : " + root.getBucketIndex());
    		hashTable.buckets[root.getBucketIndex()] = hashTable.insert(hashTable.buckets[root.getBucketIndex()], key, value);
			System.out.format("Insert done!!! \nkey:'%s' and value:'%s' \n\n", key, value);
			String newHash = hashTable.getHashOfBucket(hashTable.buckets[root.getBucketIndex()]);
			root.setHash(newHash);
		}
		else 
		{
			int bucketIndex = hashTable.bucketIndexFor(key);
	    	if(bucketIndex <= root.getLeft().getBucketIndex()) {
				copyOnWriteInsert(root.getLeft(), key, value);
				String parentHash = HashAlgorithm.generateHash(root.getLeft().getHash() + root.getRight().getHash());
				root.setHash(parentHash);
	    	}
			else {
				copyOnWriteInsert(root.getRight(), key, value);
				String parentHash = HashAlgorithm.generateHash(root.getLeft().getHash() + root.getRight().getHash());
				root.setHash(parentHash);
			}
		}
    	
    	System.out.println("after----"+ root.getHash() +" Bucketindex: " +root.getBucketIndex());
    }
    
    public static void main(String[] args) {
    	
    	try {
    		File myObj = new File("input.txt");
    		Scanner myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			String line = myReader.nextLine();
    			String[] fields = line.split(",");
    			hashTable.put(fields[0], fields[1]);
    		}
    		myReader.close();
    	} catch (FileNotFoundException e) {
    		System.out.println("An error occurred.");
    		e.printStackTrace();
    	}
    	
    	System.out.println("=============== Contents of hash table's buckets ===============");
    	hashTable.showTable();
        
        System.out.println("========== Concatenated values of hash table's buckets =========");
        hashTable.showConcatenatedOfBucket();
        
        ArrayList<String> blocksHashes = new ArrayList<>();
        hashTable.getHashes(blocksHashes);
        Node root = generateTree(blocksHashes);
        
        System.out.println("===================== Merkle tree's levels =====================");
        printLevelOrderTraversal(root);
        
        System.out.println("================== Lookup for an existing key ==================");
        String key = "Brian2";
        Object result = copyOnWriteLookup(root, key);
        if(result == null)
        	System.out.format("'%s' does not exist in the data. \n\n", key);
        else
        	System.out.format("'%s's value is : %s \n\n", key, result.toString());
        
        System.out.println("================= Lookup for a non-existing key =================");
        key = "Bahman";
        result = copyOnWriteLookup(root, key);
        if(result == null)
        	System.out.format("'%s' does not exist in the data. \n\n", key);
        else
        	System.out.format("'%s's value is : %s \n\n", key, result.toString());

        System.out.println("===================== Inserting a new node =====================");
        
        copyOnWriteInsert(root, "Bahman0", "000-0000");
        
        System.out.println("\n=================== New Merkle tree's levels ===================");
        printLevelOrderTraversal(root);
        
        System.out.println("=============== Contents of hash table's buckets ===============");
    	hashTable.showTable();
    	
        System.out.println("============================= Bye! =============================");
    }
}
