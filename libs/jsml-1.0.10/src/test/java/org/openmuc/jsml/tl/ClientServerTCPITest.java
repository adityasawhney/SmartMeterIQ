package org.openmuc.jsml.tl;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.openmuc.jsml.structures.Integer32;
import org.openmuc.jsml.structures.OctetString;
import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.structures.SML_GetListReq;
import org.openmuc.jsml.structures.SML_GetListRes;
import org.openmuc.jsml.structures.SML_List;
import org.openmuc.jsml.structures.SML_ListEntry;
import org.openmuc.jsml.structures.SML_Message;
import org.openmuc.jsml.structures.SML_MessageBody;
import org.openmuc.jsml.structures.SML_PublicCloseReq;
import org.openmuc.jsml.structures.SML_PublicOpenReq;
import org.openmuc.jsml.structures.SML_PublicOpenRes;
import org.openmuc.jsml.structures.SML_Status;
import org.openmuc.jsml.structures.SML_Unit;
import org.openmuc.jsml.structures.SML_Value;
import org.openmuc.jsml.structures.Unsigned8;

public class ClientServerTCPITest implements SML_TConnectionListener {

	public final static int serverPort = 53214;

	@Before
	public void startTCPServer() throws IOException {
		SML_TSAP sml_tSAP = null;

		sml_tSAP = new SML_TSAP(serverPort, this);

		System.out.println("Starting to listen on port: " + serverPort);
		sml_tSAP.startListening();

	}

	@Test
	public void testClient() throws UnknownHostException, IOException {

		/* Create client TSAP */
		SML_TSAP sml_tSAP = new SML_TSAP();

		SML_TConnection sml_tConnection = sml_tSAP.connectTo(InetAddress.getByName("localhost"), serverPort);

		System.out.println("Sending a GetProfileListRequest");
		byte[] getListRequest = createGetListRequest(0, 1);
		byte[] getListRequestCheck = new byte[] { 0x76, 0x02, 0x31, 0x62, 0x00, 0x62, 0x00, 0x72, 0x65, 0x00, 0x00,
				0x01, 0x00, 0x77, 0x01, 0x02, 0x31, 0x03, 0x31, 0x37, 0x01, 0x01, 0x01, 0x01, 0x63, (byte) 0xd2,
				(byte) 0xea, 0x00, 0x76, 0x02, 0x32, 0x62, 0x00, 0x62, 0x00, 0x72, 0x65, 0x00, 0x00, 0x07, 0x00, 0x75,
				0x07, 0x63, 0x6c, 0x69, 0x65, (byte) 0x6e, 0x74, 0x01, 0x01, 0x01, 0x01, 0x63, 0x7d, 0x04, 0x00, 0x76,
				0x02, 0x33, 0x62, 0x00, 0x62, 0x00, 0x72, 0x65, 0x00, 0x00, 0x02, 0x00, 0x71, 0x01, 0x63, (byte) 0x84,
				0x21, 0x00 };
		// Assert.assertTrue("getListRequest not coded correctly",
		// Arrays.equals(getListRequest, getListRequestCheck));
		sml_tConnection.send(getListRequest);
		SML_File response = sml_tConnection.receive();
		System.out.println("Finished!");
	}

	/************************************
	 * Client
	 */

	/**
	 * This sample builds a SML-File containing a PublicOpenRequest, a
	 * GetListRequest and a PublicCloseRequest
	 * 
	 * @return
	 */
	public static byte[] createGetListRequest(int groupNumber, int transactionIDCounter) {

		ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
		DataOutputStream os = new DataOutputStream(bs);

		// --- create a SML_Message with SML_PublicOpenRequest to start the
		// SML_File
		// make the transacionId and increment the transactionIDCounter
		OctetString transactionId = new OctetString(("" + transactionIDCounter++).getBytes());
		// make the group number and increment the group number
		Unsigned8 groupNum = new Unsigned8(groupNumber);
		// continue on error
		Unsigned8 abortOnError = new Unsigned8(0x00);
		// create clientId
		int iClientId = 0x1;
		OctetString clientId = new OctetString(("" + iClientId).getBytes());
		// create reqFileId
		int iReqFileId = 0x11;
		OctetString reqFileId = new OctetString(("" + iReqFileId).getBytes());
		// create the SML_Message
		SML_PublicOpenReq openRequest = new SML_PublicOpenReq(null, clientId, reqFileId, null, null, null, null);
		SML_MessageBody smlMessageBody = new SML_MessageBody(SML_MessageBody.OpenRequest, openRequest);
		SML_Message openRequestMessage = new SML_Message(transactionId, groupNum, abortOnError, smlMessageBody);

		// --- create a SML_Message with SML_GetProfileListReq with test values
		// make new transactionId
		transactionId = new OctetString(("" + transactionIDCounter++).getBytes());
		OctetString listName = new OctetString(new byte[] { 0x01, 0x00, 0x01, 0x08, 0x01, (byte) 0xff });
		SML_GetListReq getListRequest = new SML_GetListReq(clientId, null, null, null, listName);
		smlMessageBody = new SML_MessageBody(SML_MessageBody.GetListRequest, getListRequest);
		SML_Message getListRequestMessage = new SML_Message(transactionId, groupNum, abortOnError, smlMessageBody);

		// --- create a SML_Message with SML_PublicCloseRequest to end the
		// SML_File
		// make new transactionId
		transactionId = new OctetString(("" + transactionIDCounter++).getBytes());
		SML_PublicCloseReq closeRequest = new SML_PublicCloseReq(null);
		smlMessageBody = new SML_MessageBody(SML_MessageBody.CloseRequest, closeRequest);
		SML_Message closeRequestMessage = new SML_Message(transactionId, groupNum, abortOnError, smlMessageBody);

		// encode every SML_Messages and write the encoded data to the stream os
		try {
			openRequestMessage.code(os);
			getListRequestMessage.code(os);
			closeRequestMessage.code(os);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return bs.toByteArray();
	}

	/**
	 * This sample builds a SML-File containing a PublicOpenResponse, a
	 * GetListResponse and a PublicCloseResponse
	 * 
	 * @return
	 */
	public static byte[] createGetListResponse(int groupNumber, int transactionIDCounter) {

		ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
		DataOutputStream os = new DataOutputStream(bs);

		// --- create a SML_Message with SML_PublicOpenRequest to start the
		// SML_File
		// make the transacionId and increment the transactionIDCounter
		OctetString transactionId = new OctetString(("" + transactionIDCounter++).getBytes());
		// make the group number and increment the group number
		Unsigned8 groupNum = new Unsigned8(groupNumber);
		// continue on error
		Unsigned8 abortOnError = new Unsigned8(0x00);
		// create clientId
		int iServerId = 0x1;
		OctetString serverId = new OctetString(("" + iServerId).getBytes());
		// create reqFileId
		int iReqFileId = 0x11;
		OctetString reqFileId = new OctetString(("" + iReqFileId).getBytes());
		// create the SML_Message
		SML_PublicOpenRes openResponse = new SML_PublicOpenRes(null, null, reqFileId, serverId, null, null);
		SML_MessageBody smlMessageBody = new SML_MessageBody(SML_MessageBody.OpenResponse, openResponse);
		SML_Message openRequestMessage = new SML_Message(transactionId, groupNum, abortOnError, smlMessageBody);

		// --- create a SML_Message with SML_GetProfileListReq with test values
		// make new transactionId
		transactionId = new OctetString(("" + transactionIDCounter++).getBytes());

		SML_Status status = new SML_Status(new Unsigned8(0));
		SML_Unit unit = new SML_Unit(new Unsigned8(30));
		SML_Value value = new SML_Value(new Integer32(0));

		SML_ListEntry[] valListEntries = new SML_ListEntry[1];
		valListEntries[0] = new SML_ListEntry(
				new OctetString(new byte[] { 0x01, 0x00, 0x01, 0x08, 0x01, (byte) 0xff }), null, null, unit, null,
				value, null);
		SML_List valList = new SML_List(valListEntries);
		SML_GetListRes getListResponse = new SML_GetListRes(null, serverId, null, null, valList, null, null);

		smlMessageBody = new SML_MessageBody(SML_MessageBody.GetListResponse, getListResponse);
		SML_Message getListRequestMessage = new SML_Message(transactionId, groupNum, abortOnError, smlMessageBody);

		// --- create a SML_Message with SML_PublicCloseRequest to end the
		// SML_File
		// make new transactionId
		transactionId = new OctetString(("" + transactionIDCounter++).getBytes());
		SML_PublicCloseReq closeRequest = new SML_PublicCloseReq(null);
		smlMessageBody = new SML_MessageBody(SML_MessageBody.CloseResponse, closeRequest);
		SML_Message closeRequestMessage = new SML_Message(transactionId, groupNum, abortOnError, smlMessageBody);

		// encode every SML_Messages and write the encoded data to the stream os
		try {
			openRequestMessage.code(os);
			getListRequestMessage.code(os);
			closeRequestMessage.code(os);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return bs.toByteArray();
	}

	/************************************
	 * Server
	 * 
	 * @throws IOException
	 */

	public void connectionIndication(SML_TConnection sml_tConnection) {
		try {
			System.out.println("Connection indication!");
			sml_tConnection.receive();
			byte[] getListResponse = createGetListResponse(0, 1);
			sml_tConnection.send(getListResponse);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	public void serverStoppedListeningIndication(IOException e) {
		// TODO Auto-generated method stub

	}

}
