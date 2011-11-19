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

public class SML_List extends SequenceOf {

	protected SML_ListEntry[] valListEntry;

	public SML_ListEntry[] getValListEntry() {
		return valListEntry;
	}

	public SML_List(SML_ListEntry[] valListEntry) {
		this.valListEntry = valListEntry;
		seqArray = valListEntry;
		isSelected = true;
	}

	public SML_List() {
	}

	protected void createElements(int length) {
		valListEntry = new SML_ListEntry[length];
		for (int i = 0; i < length; i++)
			valListEntry[i] = new SML_ListEntry();
		seqArray = valListEntry;
	}

}
