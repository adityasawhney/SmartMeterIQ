import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.List;

import org.openmuc.jsml.structures.ASNObject;
import org.openmuc.jsml.structures.Integer32;
import org.openmuc.jsml.structures.Integer64;
import org.openmuc.jsml.structures.OctetString;
import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.structures.SML_GetListRes;
import org.openmuc.jsml.structures.SML_List;
import org.openmuc.jsml.structures.SML_ListEntry;
import org.openmuc.jsml.structures.SML_Message;
import org.openmuc.jsml.structures.SML_MessageBody;
import org.openmuc.jsml.structures.SML_PublicOpenReq;
import org.openmuc.jsml.structures.SML_Value;
import org.openmuc.jsml.tl.SML_SerialReceiver;

public class SampleSerialRead {

	public static void main(String[] args) throws IOException, PortInUseException, UnsupportedCommOperationException {
		SML_SerialReceiver receiver = new SML_SerialReceiver();
		receiver.setupComPort("/dev/ttyS0");

		for (int j = 0; j < 5; j++) {

			SML_File smlFile = receiver.getSMLFile();
			System.out.println("Got SML_File");

			List<SML_Message> smlMessages = smlFile.getMessages();
			System.out.println("The SML_File contains " + smlMessages.size() + " messages");

			for (int i = 0; i < smlMessages.size(); i++) {
				System.out.println("Got SMLMessage with tag: " + smlMessages.get(i).getMessageBody().getTag().getVal());

				SML_Message sml_message = smlMessages.get(i);

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
					SML_GetListRes resp = (SML_GetListRes) sml_message.getMessageBody().getChoice();
					SML_List smlList = resp.getValList();

					System.out.println("Server-ID: " + resp.getServerId().toString());

					SML_ListEntry[] list = smlList.getValListEntry();

					for (SML_ListEntry entry : list) {
						entry.getObjName().print();
						System.out.println("Unit: " + entry.getUnit().getVal());
						SML_Value value = entry.getValue();
						ASNObject obj = value.getChoice();

						if (obj.getClass().equals(Integer32.class)) {
							Integer32 val = (Integer32) obj;
							System.out.println(val.getVal());
						}
						else if (obj.getClass().equals(Integer64.class)) {
							Integer64 val = (Integer64) obj;
							System.out.println(val.getVal());
						}
						else if (obj.getClass().equals(OctetString.class)) {
							OctetString strVal = (OctetString) obj;
							strVal.print();
						}
						else {
							System.out.println(obj.getClass());
						}
					}
					System.out.println("Got GetListResponse");
					break;
				case SML_MessageBody.AttentionResponse:
					System.out.println("Got AttentionResponse");
					break;
				default:
					System.out.println("type not found");
				}
			}

		}

		receiver.close();
	}
}
