import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import org.openmuc.jsml.structures.OctetString;
import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.structures.SML_GetProfileListReq;
import org.openmuc.jsml.structures.SML_Message;
import org.openmuc.jsml.structures.SML_MessageBody;
import org.openmuc.jsml.structures.SML_PublicCloseReq;
import org.openmuc.jsml.structures.SML_PublicOpenReq;
import org.openmuc.jsml.structures.SML_TreePath;
import org.openmuc.jsml.structures.Unsigned8;
import org.openmuc.jsml.tl.SML_TConnection;
import org.openmuc.jsml.tl.SML_TSAP;

public class SampleSMLClient {

	public static void main(String[] args) throws UnknownHostException, IOException {

		if (args.length == 0) {
			System.out.println("Usage: SampleSMLClient portnum [-s]");
			System.exit(1);
		}

		int port = -1;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Invalid port.");
			System.out.println("Usage: SampleSMLClient portnum [-s]");
			System.exit(1);
		}

		boolean useSSL = false;

		if (args.length > 1) {
			if (args[1].equals("-s")) {
				useSSL = true;
			}
			else {
				System.out.println("Usage: SampleSMLClient portnum [-s]");
				System.exit(1);
			}
		}

		SML_TSAP sml_tSAP = null;
		if (useSSL == false) {
			sml_tSAP = new SML_TSAP();
		}
		else {

			System.out.println("using ssl");

			// System.setProperty("javax.net.debug", "ssl");
			XTrustProvider.install();
			SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sml_tSAP = new SML_TSAP(sslSocketFactory);
		}

		SML_TConnection sml_tConnection = null;

		System.out.println("Trying to connect to port: " + port);
		sml_tConnection = sml_tSAP.connectTo(InetAddress.getByName("localhost"), port);

		System.out.println("Sending a GetProfileListRequest");
		sml_tConnection.send(createGetProfileListRequest(1, 1));

		System.out.println("Listening for an SML response from the server");
		SML_File sml_File = sml_tConnection.receive();

		System.out.println("Got a response from the server.");

		List<SML_Message> sml_messages = sml_File.getMessages();
		for (SML_Message sml_message : sml_messages) {
			int tag = sml_message.getMessageBody().getTag().getVal();
			switch (tag) {
			case SML_MessageBody.OpenRequest:
				SML_PublicOpenReq sml_PublicOpenReq = (SML_PublicOpenReq) sml_message.getMessageBody().getChoice();
				System.out.println("Got OpenRequest");
				System.out.println("ClientID: " + sml_PublicOpenReq.getClientId().toString());
				break;
			case SML_MessageBody.OpenResponse:
				System.out.println("Got OpenResponse");
				break;
			case SML_MessageBody.CloseRequest:
				System.out.println("Got CloseRequest");
				break;
			case SML_MessageBody.CloseResponse:
				System.out.println("Got CloseResponse");
				break;
			case SML_MessageBody.GetProfilePackRequest:
				System.out.println("Got GetProfilePackRequest");
				break;
			case SML_MessageBody.GetProfilePackResponse:
				System.out.println("Got GetProfilePackResponse");
				break;
			case SML_MessageBody.GetProfileListRequest:
				System.out.println("Got GetProfileListRequest");
				break;
			case SML_MessageBody.GetProfileListResponse:
				System.out.println("Got GetProfileListResponse");
				break;
			case SML_MessageBody.GetProcParameterRequest:
				System.out.println("Got GetProcParameterRequest");
				break;
			case SML_MessageBody.GetProcParameterResponse:
				System.out.println("Got GetProcParameterResponse");
				break;
			case SML_MessageBody.SetProcParameterRequest:
				System.out.println("Got SetProcParameterRequest");
				break;
			case SML_MessageBody.GetListRequest:
				System.out.println("Got GetListRequest");
				break;
			case SML_MessageBody.GetListResponse:
				System.out.println("Got GetListResponse");
				break;
			case SML_MessageBody.AttentionResponse:
				System.out.println("Got AttentionResponse");
				break;
			default:
				System.out.println("type not found");
			}
		}

		System.out.println("Disconnecting");
		sml_tConnection.disconnect();

		System.out.println("Client is done and quits");
	}

	/**
	 * This sample builds a SML-File containing a PublicOpenRequest, a
	 * GetProfileListRequest and a PublicCloseRequest
	 * 
	 * @return
	 */
	public static byte[] createGetProfileListRequest(int groupNumber, int transactionIDCounter) {

		// The size of the ByteArray for the ByteArrayOutputStream should be
		// high enough so that expensive resizing is not needed. The
		// ByteArrayOutputStream can be reused by using the reset() function.
		ByteArrayOutputStream bs = new ByteArrayOutputStream(50);
		DataOutputStream os = new DataOutputStream(bs);

		// --- create a SML_Message with SML_PublicOpenRequest to start the
		// SML_File
		// make the transacionId and increment the transactionIDCounter
		OctetString transactionId = new OctetString(("" + transactionIDCounter++).getBytes());
		// make the group number and increment the group number
		Unsigned8 groupNum = new Unsigned8(groupNumber++);
		// continue on error
		Unsigned8 abortOnError = new Unsigned8(0x00);
		// create clientId
		int iClientId = 0x10;
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
		// make new group number
		groupNum = new Unsigned8(groupNumber++);
		SML_TreePath treePath = new SML_TreePath(new OctetString[] { new OctetString("testtreepath".getBytes()) });
		SML_GetProfileListReq getProfileListRequest = new SML_GetProfileListReq(null, null, null, null, null, null,
				treePath, null, null);
		smlMessageBody = new SML_MessageBody(SML_MessageBody.GetProfileListRequest, getProfileListRequest);
		SML_Message getProfileListRequestMessage = new SML_Message(transactionId, groupNum, abortOnError,
				smlMessageBody);

		// --- create a SML_Message with SML_PublicCloseRequest to end the
		// SML_File
		// make new transactionId
		transactionId = new OctetString(("" + transactionIDCounter++).getBytes());
		// make new group number
		groupNum = new Unsigned8(groupNumber++);
		SML_PublicCloseReq closeRequest = new SML_PublicCloseReq(null);
		smlMessageBody = new SML_MessageBody(SML_MessageBody.CloseRequest, closeRequest);
		SML_Message closeRequestMessage = new SML_Message(transactionId, groupNum, abortOnError, smlMessageBody);

		// encode every SML_Messages and write the encoded data to the stream os
		try {
			openRequestMessage.code(os);
			getProfileListRequestMessage.code(os);
			closeRequestMessage.code(os);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return bs.toByteArray();
	}

}
