package HashTable;

import java.util.ArrayList;

import MerkleTree.HashAlgorithm;

public class HashTable {
	private int capacity;
	public Entry[] buckets;
	
	public HashTable(int capacity) {
		if (capacity <= 0)
			throw new IllegalArgumentException(
					"Capacity is negative.");
		
		this.capacity = capacity;
		this.buckets = new Entry[capacity];
	}
	
	//Returns index for key based on its hash code.	
	public int bucketIndexFor(String key) {
		int h = key.hashCode() & 0x7fffffff;
		return h % this.capacity;
	}
	
	/*
	Associates the specified value with the specified key in this bucket of the hash table.
	If bucket previously contained a mapping for this key, the old value is replaced.
	*/
	public Entry insert(Entry node, String key, Object value)
	{
		if (node == null) {
			node = new Entry(key, value);
			//System.out.format("Inserted key:'%s' and value:'%s' \n", node.getKey(), node.value);
		}
		else
			{
			if (key.compareTo(node.getKey()) == 0) {
				node.setValue(value);
			}
			else if (key.compareTo(node.getKey())< 0) {
				Entry temp = insert(node.getLeft(), key, value);
				node.setLeft(temp);
				//System.out.format("\t insert into left child: %s:%s \n", key, value);
			}
			else
				{
				Entry temp = insert(node.getRight(), key, value );
				node.setRight(temp);
				//System.out.format("\t insert into right child: %s:%s \n", key, value);
			}
		}
		return node;
	}
	
	public void put(String key, Object value) {
		int i = bucketIndexFor(key);
		
		if (this.buckets[i] == null) {
			this.buckets[i] = new Entry(key, value);
			//System.out.format("Set the root of bucket %d  with '%s':'%s' \n", i, key, value );
		}
		else {
			//System.out.format("put into bucket %d :\n", i );
			insert(this.buckets[i], key, value );
		}
	}
	  
	/*Returns the value to which the specified key is mapped in this bucket of the hash table,
	or null if the current bucket contains no mapping for this key.
	*/
	public Object getValue(Entry node, String key)
	{
		if (node == null)
			return null;
		else
			{
			if ((key.compareTo(node.getKey()) == 0))
				return node.getValue();
			else if (key.compareTo(node.getKey())< 0)
				return getValue(node.getLeft(), key);
			else
				return getValue(node.getRight(), key);
			}
	}
	
	public void getHashes(ArrayList<String> blocksHashes) {
		for (int j = 0; j < this.capacity; j++) {
			String hash = getHashOfBucket(this.buckets[j]);
			blocksHashes.add(hash);
		}
	}
	
	public String getHashOfBucket(Entry node) {
		String sum = getSum(node);
		return (HashAlgorithm.generateHash(sum));
	}
	
	private String getSum(Entry r) {
		if (r != null)
			return getSum(r.getLeft()) + r.getValue().toString() + getSum(r.getRight());
		
		return "";
	}
	
	public void showConcatenatedOfBucket() {
		for (int j = 0; j < this.capacity; j++) {
			System.out.println("Bucket " + j + " :");
			String sum = getSum(this.buckets[j]);
			System.out.format("\t Concatenated : '%s' \n", sum);
			System.out.println();
		}
	}
	
	public void showTable() {
		for (int j = 0; j < this.capacity; j++) {
			System.out.println("Bucket " + j + " :");
			inorder(this.buckets[j]);
			System.out.println();
		}
	}
	
	private void inorder(Entry r) {
		if (r != null) {
			inorder(r.getLeft());
			System.out.println("\t (" + r.getKey() + ", " + r.getValue() + ")");
			inorder(r.getRight());
		}
	}
}

