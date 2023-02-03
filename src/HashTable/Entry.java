package HashTable;

//Each entry stores a (key, value) pair and two references to its children */
public class Entry {
	private String key;
	private Object value;
	private Entry left, right;
	
	//Create new entry.
	public Entry(String k, Object v ){
		this.key = k;
		this.value = v;
		this.left = null;
		this.right = null;
	}
	
	public String getKey() {
		return this.key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public Object getValue() {
		return this.value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Entry getLeft() {
		return this.left;
	}
	public void setLeft(Entry node) {
		this.left = node;
	}
	
	public Entry getRight() {
		return this.right;
	}
	public void setRight(Entry node) {
		this.right= node;
	}
}