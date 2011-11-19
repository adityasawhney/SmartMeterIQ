package qilin.primitives;

import qilin.protocols.generic.FiatShamir;


/**
 * Represents a Random Oracle; this interface can be more efficient than {@link RandomOracle} when the oracle will be queried on different
 * points in a stream.
 * 
 * Note that implementations of this interface must maintain state between calls to {@link #update(byte[], int, int)} and {@link #digest(int, int)}.
 * 
 * @see RandomOracle
 * @see FiatShamir
 *
 */
public interface StreamingRandomOracle {
	
	/**
	 * Reset the oracle's input.
	 */
	public void reset();
	
	/**
	 * Update the hash with additional input 
	 * 
	 * @param input the input to be hashed
	 * @param offs offs the offset at which to begin hashing
	 * @param len the number of bytes to include in the hash.
	 */
	public void update(byte[] input, int offs, int len);
	
	/**
	 * Return a random output, depending on the hash input and the index. Since implementations must maintain state, a second call to
	 * this method without an intervening {@link #reset()} is not guaranteed to return valid results.
	 * 
	 * @param index The index of the random "stream" to return. The returned value should be "independent" for different index values.
	 * @param outlen the length of the output (in bytes) to return.    
	 */
	public byte[] digest(int index, int outlen);
	

	/**
	 * Get the "native" length  returned by a call to
	 * the digest function. Running {@link #digest(int, int)} 
	 * with this as the length value will be most efficient.
	 * @return the native length (in bytes).
	 */
	public int getNativeLength();
}
