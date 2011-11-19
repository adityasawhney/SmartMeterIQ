package qilin.primitives;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

abstract public class CipherSKTest<C, P, R> extends CipherPKTest<C, P, R> {
	abstract public Cipher.SK<C,P,R> getSK();
	
	/**
	 * Verify that decrypting an encrypted value gives the same value.
	 * 
	 * Note that this only tests functionality, not security. 
	 */
	@Test
	public void testEncryptDecrypt() {
		for (int i = 0; i < CONFIDENCE; ++i) {
			P plain = getRandomPlaintext();
			R rnd = getPK().getRandom(getRand());
			C cipher = getPK().encrypt(plain, rnd);
			
			P plain2 = getSK().decrypt(cipher);
			
			assertTrue("Decryption didn't work", plain.equals(plain2));
		}
	}
}
