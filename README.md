# Merkle Bucket Tree (MBT)
## ABSTRACT

The management of immutable data needs to address two major challenges: The ever-increasing volume of data caused by immutability and detecting it, if a piece of data is tampered with.

Merkle Bucket Tree (MBT) is employed to solve the problems mentioned. In fact, it is a Merkle tree built on top of a hash table. In order to implement the hash table, an array is used and data entries are hashed to its cells or buckets. Since each bucket is a Binary Search Tree, the entries within each bucket are arranged in sorted order, so binary search can be used to find the target key after the retrieval of the bucket. The bottom most level of MBT maintain the cryptographic hashes computed from the contents of hash table buckets and the internal nodes are formed by calculating the cryptographic hashes of their intermediate children. The cardinality of the bucket set is called capacity and the number of children an internal node of MBT has is called fanout. In our implementation of MBT, capacity can be set before running the program and fanout is two. These parameters are pre-defined and cannot be changed in MBT’s life cycle according to the main paper!

## IMPLEMENTATION

The project is implemented in Java programing language and consists of three packages which are explained in the following sections.

### MerkleTree package
This package consists of three classes:
#### HashAlgorithm class
This class only has a method called generateHash that takes a string and by utilizing FIPS202.HashFunction.SHA3_256.apply() and FIPS202.hexFromBytes() methods from uk.org.bobulous.java.crypto.keccak package returns the hash of input string.
#### Node class
This class models a node of MBT and has four properties:

private Node left;

private Node right;

private String hash;

private int bucketIndex;

The first three fields are self-explanatory and the last one maintains the largest bucketIndex of its children for a non-leaf node and the index of the relevant bucket of the hash table for a leaf node.

Also, it has a constructor and a pair of set and get methods for each property.

#### MerkleTree class
This class implements Merkle tree and has two fields:

private static int capacity = 5;

private static HashTable hashTable = new HashTable(capacity);

In addition to main method of the project, this class includes the bellow methods:

generateTree and buildTree are used to make the MBT.

printLevelOrderTraversal shows the hashes of each level of MBT with an empty line to distinguish consecutive levels.

copyOnWriteLookup searches for the key in the MBT by traversing from its root to the relevant bucket and then traversing the binary search tree in that bucket considering copyOnWrite restriction.

copyOnWriteInsert inserts a node in the MBT and then produces the required new hashes in the MBT considering copyOnWrite restriction.


![MBT](https://user-images.githubusercontent.com/14259973/216721589-03f0c57c-94c0-42a8-91a5-f6b87f90c002.png)
Fig 1: The structure of the MBT and the internal structure of each node

### HashTable package
This package is composed of two classes:
#### Entry class
This class models a node of the hash table and has four properties:

private String key;

private Object value;

private Entry left;

private Entry right;

Also, it has a constructor and a pair of set and get methods for each property.

#### HashTable class
This class implements Merkle tree and has two fields:

private int capacity;

public Entry[] buckets;

In addition to a constructor, this class includes the following methods:

bucketIndexFor returns an index for the given key based on its hash code.

put and insert are employed to insert a new entry in the binary search tree existing in the buckets of the hash table.

getValue takes a node as the root of a binary search tree and traverses it to find a node with the given key. It returns its value if such node exists, otherwise it returns null.

getHashes and getHashOfBucket are used to produce and return the hash values of the hash table buckets.

getSum takes the root of a binary search tree and build a string containing the values of all nodes existing in that tree.

showConcatenatedOfBucket with the help of getSum method prints the contents of all hash table buckets.

showTable with the help of inorder method show the entries of each bucket in the hash table in a sorted form based on the keys.


![BST](https://user-images.githubusercontent.com/14259973/216722963-39660378-a59b-4155-a738-9ef81513152f.png)
Fig 2: The internal structure of each bucket of the hash table

### uk.org.bobulous.java.crypto.keccak package
This package have been proposed by this website [2] and different open source implementations in various programing languages can be found on this website. We take advantage of a java version of it that can be downloaded here [3] . We only use two methods of this package called FIPS202.HashFunction.SHA3_256.apply() and FIPS202.hexFromBytes(). The first one takes a string (to be precise an array of bytes) as input and computes its hash as an array of bytes. The latter method takes the output of first method as input and converts it to a fixed-length string with 64 hex characters.


![Search](https://user-images.githubusercontent.com/14259973/216736351-f695be71-eacd-4123-a53b-d1f133db473f.png)
Fig 3: Looking up for a key in the MBT considering copyOnWrite restriction


![Insert](https://user-images.githubusercontent.com/14259973/216736545-108e8033-11f0-4fcc-838a-b3e793a0daf4.png)
Fig 4: Inserting a new node in the MBT considering copyOnWrite restriction

