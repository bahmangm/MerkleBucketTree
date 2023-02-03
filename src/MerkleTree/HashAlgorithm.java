package MerkleTree;

import uk.org.bobulous.java.crypto.keccak.FIPS202;

public class HashAlgorithm {
	
	/*Takes a string and computes a fixed-size hash from it that is composed of 
	64 hex chars. This package is produced in https://keccak.team and the current 
	implementation is borrowed from 
	https://github.com/Bobulous/Cryptography/tree/master/src/uk/org/bobulous/java/crypto/keccak */
    public static String generateHash(String originalText) {
		byte[] hashBytes = FIPS202.HashFunction.SHA3_256.apply(originalText.getBytes());
		return new String(FIPS202.hexFromBytes(hashBytes));
    }

}