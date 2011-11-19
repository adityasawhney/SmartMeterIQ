package phishmarket.app;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.logging.Level;


import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.bouncycastle.math.ec.ECPoint;

import phishmarket.concrete.ECObliviousInformationPurchase;
import qilin.util.Pair;

/**
 * The executable class that is run by the Seller in the 
 * Information-purchase transaction.
 * @author talm
 *
 */
public class Seller extends Common {
	/**
	 * Maximum delay to allow. 
	 * Delays over this number of ms are shortened to this.
	 */
	long MAX_DELAY = 3000;
	final static String defaultSimTABLE = "seller";
	final static String tsCol = "timestamp";
	final static String tagCol = "tag";
	final static String urlCol = "url";
	
	ECObliviousInformationPurchase.Seller seller;
	
	/**
	 * Represents a single database entry (containing a timestamp,
	 * tag and URL).
	 * @author talm
	 *
	 */
	public static class Entry implements Comparable<Entry> {
		long timestamp;
		String tag;
		String url;
		
		@Override
		public int compareTo(Entry o) {
			if (timestamp != o.timestamp)
				return timestamp < o.timestamp ? -1 : 1;
			int urlcomp = url.compareTo(o.url);
			if (urlcomp != 0)
				return urlcomp;
			return tag.compareTo(o.tag);
		}
	}
	
	TreeSet<Entry> simEntries;
	
	public Seller() {
		super(Seller.class.getName());
		simEntries = new TreeSet<Entry>();
	}
	

	public static void main(String args[]) {
		Seller me = new Seller();
		
		me.runMain(args);
	}


	@SuppressWarnings(value="static-access")
	@Override
	Options getOptions() {
		Options opts = super.getOptions();
		opts.addOption("i", "ignore-timestamps", false, "Sell as fast as possible");
		opts.addOption(OptionBuilder.hasArg()
				 .withArgName("tablename")
				 .withLongOpt("simulation-table")
				 .withDescription("Table containing '"+tsCol+"', '"+tagCol+"' and '"+urlCol+"' columns")
				 .create("s") );
		opts.addOption(OptionBuilder.hasArg()
				 .withArgName("number")
				 .withLongOpt("limit-rows")
				 .withDescription("Maximum number of urls to attempt to sell")
				 .create("l") );
		return opts;
	}
	void loadDatabase() throws SQLException {
		String simTABLE = line.getOptionValue("s", defaultSimTABLE);
		int maxRows = Integer.valueOf(line.getOptionValue("l", "0"));
		
		logger.info("Loading Simulation database from table " + simTABLE);
		dbConn.setAutoCommit(false);

		Statement st = dbConn.createStatement();
		ResultSet rs = st.executeQuery("SELECT "+tsCol+","+tagCol+","+urlCol+" FROM "+ simTABLE);

		int count = 0;
		while (rs.next()) {
			Entry ent = new Entry();
			ent.timestamp = rs.getLong(1);
			ent.tag = rs.getString(2).toUpperCase().trim();
			ent.url = rs.getString(3).trim();
 
			logger.fine("Loaded ["+ent.timestamp+"]["+ent.tag+"]["+ent.url+"]");
			if (ent.url.length() > 0)
				simEntries.add(ent);
			++count;
			if (maxRows > 0 && count >= maxRows)
				break;
		}
		rs.close();
		st.close();

		dbConn.setAutoCommit(true);
		logger.info("Simulation Database loaded (" + count + " rows)");
	}

	@Override
	protected void run() throws IOException, InterruptedException, SQLException {
		seller = oip.newSeller();
		seller.setParameters(toPeer, rand);
		logger.log(Level.INFO, "Running Initialization");
		if (h != null)
			seller.init(h);
		else {
			seller.init();
			h = seller.getCommitPK();
			if (!line.hasOption("n"))
				commitCache.put(new byte[0], new Pair<ECPoint, BigInteger>(h, BigInteger.ZERO));
		}

		logger.log(Level.INFO, "Initialization complete");
		
		loadDatabase();

		logger.info("Waiting for Buyer to load database");
		toPeer.receive();

		if (line.hasOption("i"))
			// Shorten all timestamp delays to 0
			MAX_DELAY = 0;
		
		logger.info("Starting sell loop");
		long offs = -Long.MIN_VALUE;
		long maxDelay = 0;
		for (Entry ent : simEntries) {
			// Wait until time has elapsed (if necessary)
			long curTime = System.currentTimeMillis();

			long delay = curTime + offs - ent.timestamp;
			if (delay < -MAX_DELAY) {
				// The delay to the next time is too long, shorten it.
				logger.finer("Shortening sleep of " + (-delay));
				offs =  ent.timestamp - curTime - MAX_DELAY;
				delay = -MAX_DELAY;
			}
			if (delay < 0) {
				logger.finer("Sleeping " + (-delay) + " ms");
				Thread.sleep(-delay);
			} else if (delay > 0) { 
				maxDelay = Math.max(maxDelay, delay);
				logger.finer("Delay is " + delay);
			}
			logger.finer("Starting to sell ["+ent.tag+"] "+ent.url);
			seller.sell(ent.url.getBytes(), ent.tag.getBytes());
			logger.fine("Sold ["+ent.tag+"] "+ent.url);
		}
		logger.finer("Starting to sell final tag");
		seller.sell(new byte[0], DONETAG.getBytes());
		logger.info("End of sell loop (sold "+simEntries.size()+"; max delay: "+maxDelay+")");
		
		logger.info("Verifying payment value");
		BigInteger payment = seller.getPaymentValue();
		logger.info("All done (payment was "+payment+")");
	}

}
