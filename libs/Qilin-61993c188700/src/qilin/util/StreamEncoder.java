package qilin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A generic serializer to/from a stream. 
 * @author talm
 *
 * @param <T> type of element to serialize.
 */
public interface StreamEncoder<T> {

	/**
	 * Encode an element into an OutputStream.
	 * @param input the element to encode.
	 * @param out the stream into which the encoding will be written.
	 * @return the number of bytes written to the stream.
	 */
	public int encode(T input, OutputStream out) throws IOException;
	
	/**
	 * Decode an element of the group from an InputStream. The
	 * element should have previously been encoded by 
	 * {@link #encode(Object, OutputStream)}.
	 *
	 * @param in the inputstream
	 * @return the decoded element
	 */
	public T decode(InputStream in) throws IOException;
}
