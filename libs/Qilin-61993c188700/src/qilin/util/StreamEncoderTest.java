package qilin.util;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;




abstract public class StreamEncoderTest<G> implements GlobalTestParams {
	abstract protected StreamEncoder<G> getEncoder();
	/**
	 * Return a collection of "special" elements that may need
	 * to be handled differently in encoding an decoding.
	 */
	abstract protected List<G> getSpecialElements();
	
	
	abstract protected G getRandomElement();
	
	/**
	 * Test that encoding and decoding zero works. 
	 */
	@Test
	public void testEncodeDecodeSpecial() throws IOException {
		testEncodeDecodeCollection(getSpecialElements());
	}
	
	/**
	 * Test that encoding and decoding multiple elements works. 
	 */
	@Test
	public void testEncodeDecodeRandom() throws IOException {
		List<G> testElements = new ArrayList<G>(CONFIDENCE);
		for (int i = 0; i < CONFIDENCE; ++i) {
			testElements.add(getRandomElement());
		}
		testEncodeDecodeCollection(testElements);
	}
	
	/**
	 * Test that encoding and decoding multiple elements works. 
	 */
	public void testEncodeDecodeCollection(List<G> elements) throws IOException {
		StreamEncoder<G> encoder = getEncoder();
		ByteArrayOutputStream outbuf = new ByteArrayOutputStream();
		ByteArrayInputStream inbuf;
		
		int[] enclens = new int[elements.size()];
		int i = 0;
		
		// Encode all the elements
		for (G r : elements) {
			int cursize = outbuf.size();
			int n = encoder.encode(r, outbuf);
			// Check that the number of bytes written matches:
			assertTrue("Encoding wrote " + (outbuf.size() - cursize) + 
					", claimed "+ n, n == outbuf.size() - cursize);
			enclens[i++] = n;
		}
		

		inbuf = new ByteArrayInputStream(outbuf.toByteArray());
		
		// Decode all elements
		i = 0;
		for (G rTest : elements) {
			int curleft = inbuf.available();
			G r = encoder.decode(inbuf);
			int n = curleft - inbuf.available();
			assertTrue("Incorrect number of bytes read for decoding (expected "+
					enclens[i] + ", got " + n, enclens[i] == n);
			assertTrue(GenericsUtils.deepEquals(rTest, r));
		}
	}
}
