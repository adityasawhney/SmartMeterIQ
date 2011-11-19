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

public class SML_Status extends ImplicitChoice {

	
	public SML_Status(ASNObject choice) {
		if (!(choice instanceof Unsigned8 || choice instanceof Unsigned16 || choice instanceof Unsigned32 || choice instanceof Unsigned64))
			throw new IllegalArgumentException("SML_Status: Wrong ASNObject! " + choice.getClass().getName()
					+ " is not allowed.");

		this.choice = choice;
		isSelected = true;

	}

	public SML_Status() {
	}
}
