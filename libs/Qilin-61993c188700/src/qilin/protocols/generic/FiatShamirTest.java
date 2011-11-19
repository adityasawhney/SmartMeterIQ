package qilin.protocols.generic;

import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import qilin.comm.Channel;
import qilin.comm.LocalChannelFactory;
import qilin.comm.Message;
import qilin.primitives.StreamingRandomOracle;
import qilin.primitives.concrete.DigestOracle;
import qilin.protocols.SigmaProtocol;
import qilin.util.Pair;


public class FiatShamirTest {

	StreamingRandomOracle oracle1;
	StreamingRandomOracle oracle2;
	LocalChannelFactory channelFactory;
	Pair<Channel,Channel> channels; 
	Random rand;
	
	public FiatShamirTest() {
		oracle1 = new DigestOracle();
		oracle2 = new DigestOracle();
		channelFactory = new LocalChannelFactory();
		channels = channelFactory.getChannelPair();
		rand = new Random(1);
	}
	
	public static class VerifierParams {
		int publicnum;
	}
	
	public static class ProverParams extends VerifierParams {
		int secretnum;
	}

	
	@Test
	public void testProof() throws IOException, InterruptedException {
		TestProver prover = new TestProver();
		TestVerifier verifier = new TestVerifier();

		FiatShamir.Prover<VerifierParams, ProverParams>  fiatShamirProver = new FiatShamir.Prover<VerifierParams, ProverParams>(prover, verifier, oracle1);
		FiatShamir.Verifier<VerifierParams>  fiatShamirVerifier = new FiatShamir.Verifier<VerifierParams> (verifier, oracle2);

		Random rand = new Random();
		
		fiatShamirProver.setParameters(channels.a, rand);
		fiatShamirVerifier.setParameters(channels.b, null); // FiatShamir verifiers are deterministic
		
		fiatShamirProver.init();
		fiatShamirVerifier.init();
		
		ProverParams params = new ProverParams();
		
		params.publicnum = 2;
		params.secretnum = 7;
		
		fiatShamirProver.prove(params);
		Assert.assertTrue(fiatShamirVerifier.verify(params));
	}
	
	public class TestProver extends ProtocolPartyBase implements SigmaProtocol.Prover<ProverParams> {

		@Override
		public void prove(ProverParams params) throws IOException {
			Message commit = toPeer.newMessage();
			
			commit.write(params.secretnum);
			toPeer.send(commit);
			
			Message challenge = toPeer.receive();
			
			int chal = challenge.read();
			
			Message resp = toPeer.newMessage();
			
			resp.write(chal + params.secretnum + params.publicnum);
			
			toPeer.send(resp);
		}
	}
	

	public class TestVerifier extends ProtocolPartyBase implements SigmaProtocol.Verifier<VerifierParams> {
		@Override
		public boolean verify(VerifierParams params) throws IOException {
			Message commit = toPeer.receive();
			int comm = commit.read();
			
			Message challenge = toPeer.newMessage();
			int chal = (comm + rand.nextInt()) & 0xff;
			
			challenge.write(chal);
			toPeer.send(challenge);
			
			Message response = toPeer.receive();
			int resp = response.read();
			
			return resp == comm + chal + params.publicnum;
		}
	}
}
