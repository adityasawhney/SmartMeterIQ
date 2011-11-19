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
import java.io.IOException;

public abstract class SequenceOf extends ListOf {

	protected abstract void createElements(int length);

	public boolean decode(DataInputStream is) throws IOException {
		int tlLength = 1;
		byte typeLength = is.readByte();
		if (isOptional && (typeLength == 0x01)) {
			isSelected = false;
			return true;
		}

		if (!(((typeLength & 0x70) >> 4) == 7)) {
			return false;
		}
		int length = (typeLength & 0x0f);

		while ((typeLength & 0x80) == 0x80) {
			tlLength++;
			typeLength = is.readByte();
			if (!(((typeLength & 0x70) >> 4) == 0)) {
				return false;
			}
			length = ((length & 0xffffffff) << 4) | (typeLength & 0x0f);
		}

		createElements(length);
		isSelected = true;

		for (int i = 0; i < length; i++) {
			if (!seqArray[i].decode(is)) {
				return false;
			}
		}
		isSelected = true;
		return true;
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SequenceOf: ");
			for (int i = 0; i < seqArray.length; i++) {
				seqArray[i].print();
			}
		}
	}

}
