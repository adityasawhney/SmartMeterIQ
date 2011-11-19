package org.openmuc.jsml.structures;

/*
 * Copyright Fraunhofer ISE, 2009
 * Author(s): Stefan Feuerhahn
 *            Manuel Buehrer
 *    
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 * 
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Unsigned32 extends ASNObject {
	protected int val;

	public int getVal() {
		return val;
	}

	public Unsigned32(int i) {
		val = i & 0xffffffff;
		isSelected = true;
	}

	public Unsigned32() {
	}

	public void code(DataOutputStream os) throws IOException {
		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}
		os.writeByte(0x65);
		os.writeInt(val & 0xffffffff);
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		int typeLengthField = is.readByte();
		if (isOptional && (typeLengthField == 0x01)) {
			isSelected = false;
			return true;
		}
		if ((typeLengthField & 0x60) != 0x60) {
			return false;
		}
		// get length of unsigned with tl-field and subtract the tl-field
		int length = (typeLengthField & 0x0F) - 1;

		length--;
		for (; length >= 0; length--)
			val |= (is.readByte() & 0xFF) << (8 * length);

		// val = (is.readInt());
		isSelected = true;
		return true;
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("Unsigned32: " + val);
		}
	}
}
