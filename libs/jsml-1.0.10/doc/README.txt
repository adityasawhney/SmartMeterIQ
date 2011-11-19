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


jSML is Java library implementing the Smart Message Language (SML).

SML is communication protocol for transmitting meter data for so called smart meters. 
SML is similar to XML with regard to its ability to nest information tags within other tags.
But the standard way of coding SML packets is in a more efficient binary format that is similar 
but not equal to BER encoding. Also the specification comes in a form that is similar to ASN.1 
but not completely standards compliant.

The jSML library can be used to easily construct SML messages, encode them and then send them. 
Just as easily received messages can be decoded. jSML also implements the SML Transport Layer 
which sits on top of TCP/IP. View the sample files for more details.

For the latest release of this software visit http://www.openmuc.org .

For the newest version of the SML specification visit: 
http://www.sym2.org/eng/index_eng.html

Build jsml:
------------

To build jsml from source code you need maven 2.2 installed on your computer.

To build the library type

mvn install

After the build process the library can be found in the target directory.

To create the API docs type

mvn javadoc:javadoc

The Javadoc documentation will then be created in the target/site/apidoc directory.



Using the examples:
--------------------

For an example on how to use jSML see the sample/ folder.
To compile and execute the example on Linux use something like this:
javac *.java -cp "../jsml-<version>.jar:../lib/RXTXcomm-2.1-7.jar"
java -cp "../jsml-<version>.jar:../lib/RXTXcomm-2.1-7.jar:./" SampleSerialRead

To execute the server and client in ssl mode:
java -Djavax.net.ssl.keyStore=/path/to/serverKeystore -Djavax.net.ssl.keyStorePassword="storepass" -cp "../jsml-<version>.jar:../lib/RXTXcomm-2.1-7.jar:./" SampleSMLServer <port> -s
java -cp "../jsml-<version>.jar:../lib/RXTXcomm-2.1-7.jar:./" SampleSMLClient <port> -s


