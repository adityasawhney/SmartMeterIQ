package phishmarket.app;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.bouncycastle.math.ec.ECPoint;


import phishmarket.concrete.ECObliviousInformationPurchase;
import phishmarket.generic.ObliviousInformationPurchase.BuyerInfo;
import phishmarket.generic.ObliviousInformationPurchase.BuyerInterest;
import qilin.comm.Message;
import qilin.util.Pair;

/**
 * The executable class that is run by the Buyer in the 
 * Information-purchase transaction.
 * @author talm
 *
 */

public class Buyer extends Common {
	final static String defaultUrlTABLE = "buyer";
	final static String defaultTagTABLE = "buyer-tags";
	final static String tagCol = "tag";
	final static String urlCol = "url";
	
	ECObliviousInformationPurchase.Buyer buyer;	
	Set<String> tags;
	
	public Buyer() {
		super(Buyer.class.getName());
		tags = new HashSet<String>();
	}
	
	public static void main(String args[]) {
		Buyer me = new Buyer();
		
		me.runMain(args);
	}

	@SuppressWarnings(value="static-access")
	@Override
	Options getOptions() {
		Options opts = super.getOptions();
		opts.addOption(OptionBuilder.hasArg()
				 .withArgName("tablename")
				 .withLongOpt("url-table")
				 .withDescription("Table containing '"+tagCol+"' and '"+urlCol+"' columns")
				 .create("u") );
		opts.addOption(OptionBuilder.hasArg()
				 .withArgName("tablename")
				 .withLongOpt("tags-table")
				 .withDescription("Table with '"+tagCol+"' column (containing interesting tags)")
				 .create("t") );
		opts.addOption(OptionBuilder.hasArg()
				 .withArgName("number")
				 .withLongOpt("limit-rows")
				 .withDescription("Maximum number of rows to read from the URL table")
				 .create("l") );
		return opts;
	}
	
	void loadDatabase() throws SQLException {

		String urlTABLE = line.getOptionValue("u", defaultUrlTABLE);
		String tagTABLE = line.getOptionValue("t", defaultTagTABLE);
		int maxRows = Integer.valueOf(line.getOptionValue("l", "0"));
		
		logger.info("Loading URL database from table " + urlTABLE);

		dbConn.setAutoCommit(false);
		Statement st = dbConn.createStatement();
		ResultSet rs = st.executeQuery("SELECT "+tagCol+","+urlCol+" FROM "+ urlTABLE);

		int count = 0;
		while (rs.next()) {
			String tag = rs.getString(1).toUpperCase().trim();
			String url = rs.getString(2).trim();
			logger.fine("Inserting ["+tag+"]["+url+"] into Merkle tree");

			buyer.addInfo(url.getBytes());
			++count;
			if (maxRows > 0 && count >= maxRows)
				break;
		}
		rs.close();		
		logger.info("URL Database loaded (" + count + " rows)");

		logger.fine("Loading Tags");
		rs = st.executeQuery("SELECT "+tagCol+" FROM "+ tagTABLE);

		count = 0;
		while (rs.next()) {
			String tag = rs.getString(1).toUpperCase().trim();
			logger.fine("Adding tag: " + tag);
			tags.add(tag);
			++count;
		}
		rs.close();
		st.close();
		dbConn.setAutoCommit(true);
		logger.info("Tags loaded from table "+tagTABLE+" (" + count + " rows)");	
	}

	@Override
	protected void run() throws IOException, InterruptedException, SQLException {
		buyer = oip.newBuyer();
		buyer.setParameters(toPeer, rand);
		logger.log(Level.INFO, "Running Initialization");
		if (h != null)
			buyer.init(h);
		else {
			buyer.init();
			h = buyer.getCommitPK();
			if (!line.hasOption("n"))
				commitCache.put(new byte[0], new Pair<ECPoint, BigInteger>(h, BigInteger.ZERO));
		}
		logger.log(Level.INFO, "Initialization complete");
		

		loadDatabase();
		Message msg = toPeer.newMessage();
		msg.write(0);
		toPeer.send(msg);
		
		boolean done = false;
	
		BuyerInterest interest = new BuyerInterest() {
			@Override
			public boolean isInterested(byte[] tag) {
				return tags.contains(new String(tag).toUpperCase().trim());
			}
		};

		int countFake = 0;
		int countKnown = 0;
		int countBought = 0;
		
		logger.info("Starting buy loop");
		while (!done) {
			logger.finer("Waiting to buy");
			BuyerInfo buyerInfo = buyer.buy(interest);
			String tag = new String(buyerInfo.tag);
			
			if (buyerInfo.didPay) {
				String url = new String(buyerInfo.info);

				++countBought;
				logger.fine("Bought ["+tag+"] "+url);
				logger.finer("Adding url to database");
				buyer.addInfo(buyerInfo.info);
			} else if (buyerInfo.info != null) {
				String url = new String(buyerInfo.info);

				++countKnown;
				logger.fine("Already knew ["+tag+"] "+url);
			} else {
				++countFake;
				logger.fine("Not interested in ["+tag+"]");
			}
			if (tag.equals(DONETAG))
					done = true;
		}
		logger.info("End of buy loop (bought "+countBought+", knew "+countKnown+
				" ignored "+countFake+")");
		
		logger.info("Proving payment value: "+buyer.getPaymentValue());
		buyer.provePaymentValue();
		logger.info("All done");
	}
}
