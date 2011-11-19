package org.openmuc.jsml.structures;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.openmuc.jsml.structures.List_of_SML_Tree;
import org.openmuc.jsml.structures.OctetString;
import org.openmuc.jsml.structures.SML_Tree;

public class EncodingDecodingTest {

	// @Test
	// public void encodeEmptyUnsigned8() throws IOException {
	//
	// ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
	// DataOutputStream os = new DataOutputStream(bs);
	//
	// SML_Unit unit = null;
	//
	// try {
	// unit = new SML_Unit(new Unsigned8());
	// } catch (IllegalArgumentException e) {
	// }
	// Assert.assertTrue(false);
	//
	// try {
	// unit.code(os);
	// } catch (NullPointerException e) {
	// }
	// Assert.assertTrue(false);
	// }
	//
	// @Test
	// public void encodeEmptySequenceOf() {
	// ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
	// DataOutputStream os = new DataOutputStream(bs);
	// List_of_SML_ValueEntry valueEntryList = new List_of_SML_ValueEntry();
	// try {
	// valueEntryList.code(os);
	// } catch (IOException e) {
	// }
	// Assert.assertTrue(false);
	//
	// }

	@Test
	public void encodeListOfSMLTreeWithManyElements() {
		ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
		DataOutputStream os = new DataOutputStream(bs);
		SML_Tree[] sml_Trees = new SML_Tree[25];
		for (int i = 0; i < 25; i++) {
			sml_Trees[i] = new SML_Tree(new OctetString("test"), null, null);
		}
		List_of_SML_Tree list_of_SML_Tree = new List_of_SML_Tree(sml_Trees);

		try {
			list_of_SML_Tree.code(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Assert.assertEquals(bs.toByteArray()[0], (byte) 0xf1);
		Assert.assertEquals(bs.toByteArray()[1], (byte) 0x09);

	}

}
