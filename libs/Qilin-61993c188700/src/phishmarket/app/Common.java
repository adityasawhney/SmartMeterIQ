package phishmarket.app;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.bouncycastle.math.ec.ECPoint;

import phishmarket.concrete.ECObliviousInformationPurchase;
import phishmarket.concrete.ECObliviousInformationPurchaseWithFiatShamir;
import phishmarket.generic.ObliviousInformationPurchase;
import qilin.comm.TCPChannelFactory;
import qilin.comm.TCPChannelFactory.TCPChannel;
import qilin.primitives.concrete.ECGroup;
import qilin.protocols.CheatingPeerException;
import qilin.util.ByteEncoder;
import qilin.util.CountingChannel;
import qilin.util.EncodingUtils;
import qilin.util.JDBCMap;
import qilin.util.Pair;



/**
 * An abstract class containing the common code for both
 * the Buyer and the Seller.
 * 
 * @author talm
 *
 */
abstract public class Common {
	public final String DONETAG = "done";
	protected Logger logger;
	protected Options opts;
	protected CommandLine line;
	protected TCPChannelFactory tcpChannelFactory;
	protected CountingChannel toPeer;
	protected String appName;
	protected String appVersion;
	protected Connection dbConn;
	protected JDBCMap<byte[], Pair<ECPoint,BigInteger>> commitCache;
	protected ECPoint h;
	
	Random rand; 
	ECGroup grp;
	ObliviousInformationPurchase<ECPoint> oip; 

	public class XFormatter extends Formatter {
		@Override
		public String format(LogRecord record) {
			String rxTx;
			if (toPeer != null) {
				rxTx = ";Rx: " + toPeer.getReceivedBytes() 
					 + ";Tx: " + toPeer.getSentBytes(); 
			} else {
				rxTx = "";
			}
			
			Throwable thrown = record.getThrown();
			
			String thrownMessage;
			if (thrown != null) {
				StringWriter stackTrace = new StringWriter();
				thrown.printStackTrace(new PrintWriter(stackTrace));
				
				thrownMessage = stackTrace.toString();
			} else
				thrownMessage = "";
			
			return "["+record.getMillis() + ";"+record.getLoggerName()+rxTx+"] "+
				record.getMessage() + "\n" 
				+ thrownMessage;
		}
	}
	
	public Common(String appName) {
		this.appName = appName;
		this.appVersion = null;
		InputStream verStream = getClass().getResourceAsStream("/version");
		if (verStream != null) try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(verStream));
			appVersion = reader.readLine();
		} catch (IOException e) { }
		this.h = null;
		XFormatter formatter = new XFormatter();
		for (Handler handler : Logger.getLogger("").getHandlers()) {
			handler.setLevel(Level.ALL);
			handler.setFormatter(formatter);
		}
		logger =  Logger.getLogger(appName);
		opts = getOptions();
	}
	
	@SuppressWarnings(value="static-access")
	Options getOptions() {
		Options opts = new Options();
		
		opts.addOption("h", "help", false, "Show this help message");
		opts.addOption("v", "verbose", false, "Be more verbose");
		opts.addOption("version", "version", false, "Output version and exit");
		opts.addOption("n", "no-cache", false, "Don't use commitment caching");
		opts.addOption("f", "fiat-shamir", false, "Use non-interactive proofs (Fiat-Shamir heursitic)");
		opts.addOption("x", "clear-cache", false, "Clear commitment cache on startup");
		opts.addOption( OptionBuilder.hasArg()
									 .withArgName("local port")
									 .withLongOpt("port")
									 .withDescription("Local port on which server will be run")
									 .create("p") );
		opts.addOption( OptionBuilder.hasArg()
				 .withArgName("dbname")
				 .withLongOpt("database")
				 .withDescription("Source database")
				 .create("d") );
		opts.addOption(OptionBuilder.hasArg()
				 .withArgName("tablename")
				 .withLongOpt("cache-table")
				 .withDescription("Table to use for commitment caching (will be created if necessary)")
				 .create("c") );
		return opts;
	}
	
	void printHelp() {
		HelpFormatter formatter = new HelpFormatter(); 
		formatter.printHelp("Buyer [options] [peer_ip:port]", 
				"If peer_ip:port is not given, operates in server mode\n"+
				"Options: ", opts, "");
	}

	abstract protected void run() throws IOException, InterruptedException, SQLException;
	
	protected void initializeDatabase(String dbname) throws ClassNotFoundException, SQLException {
		assert dbname != null;
		logger.info("Initializing database");

		Class.forName("org.sqlite.JDBC");
		dbConn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
		Statement st = dbConn.createStatement();
		st.execute("PRAGMA read_uncommitted=true");
		st.close();
	}
	
	protected void runMain(String[] args) {
		logger.setLevel(Level.INFO);
		
		CommandLineParser parser = new PosixParser();
		
		try {
			line = parser.parse(opts, args);
		} catch (ParseException pe) {
			System.err.println("Syntax error (" + pe.getMessage() + ")");
			printHelp();
			return;
		}

		if (line.hasOption("h")) {
			printHelp();
			return;
		} else if (line.hasOption("version")) {
			System.out.println(appVersion);
			return;
		} else if (!line.hasOption("d")) {
			System.err.println("Database option (-d) must be present!");
			printHelp();
			return;
		}
		
		if (appVersion != null) {
			logger.info(appName +", version " + appVersion);
		}
		
		String[] argsLeft = line.getArgs();
		
		if (line.hasOption("v")) {
			logger.setLevel(Level.ALL);
			logger.log(Level.FINE, "Turned on verbose logging");
		}
		
		String peerAddr = null;
		if (argsLeft.length >= 1)
			peerAddr = argsLeft[0];
		
		
		try {		
			initializeDatabase(line.getOptionValue("d"));

			rand = new SecureRandom(); 
			grp = new ECGroup("P-256");

			if (line.hasOption("n")) {
				// No commitment caching
				logger.fine("Not using commitment cache");
				if (line.hasOption("f")) {
					logger.info("Using non-interactive proofs");
					oip = new ECObliviousInformationPurchaseWithFiatShamir(grp);
				} else {
					logger.info("Using interactive proofs");
					oip = new ECObliviousInformationPurchase(grp);
				}
			} else {
				// Use commitment cache.
				String table = line.getOptionValue("c", "commitCache");
				boolean dropTable = line.hasOption("x");
				ByteEncoder<byte[]> keyEncoder = new EncodingUtils.ByteArrayEncoder(0);
				ByteEncoder<Pair<ECPoint,BigInteger>> valEncoder = new ByteEncoder<Pair<ECPoint,BigInteger>>() {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					@Override
					public byte[] encode(Pair<ECPoint, BigInteger> input) {
						try {
							out.reset();
							grp.encode(input.a, out);
							EncodingUtils.encode(input.b, out);
							return out.toByteArray();
						} catch (IOException e) {
							throw new Error("Unexpected IO exception on ByteArray!");
						}
					}

					@Override
					public Pair<ECPoint, BigInteger> decode(byte[] input) {
						ByteArrayInputStream inp = new ByteArrayInputStream(input);
						try {
							ECPoint p = grp.decode(inp);
							BigInteger r = EncodingUtils.decodeBigInteger(inp);
							return new Pair<ECPoint, BigInteger>(p, r);
						} catch (IOException e) {
							throw new Error("Unexpected IO exception on ByteArray!");
						}
					}

					@Override
					public Pair<ECPoint, BigInteger> denseDecode(byte[] input) {
						return null;
					}
					@Override
					public int getMinLength() {
						return 0;
					}
				};
				
				commitCache =
					new JDBCMap<byte[], Pair<ECPoint,BigInteger>>(dbConn, table, "key", "val", 
							keyEncoder, valEncoder, dropTable);

				logger.info("Using commitment cache in table " + table + " (current size: "+commitCache.size()+")");
				if (!dropTable) {
					Pair<ECPoint,BigInteger> comStarPk = commitCache.get(new byte[0]);
					if (comStarPk != null) {
						h = comStarPk.a;
						logger.info("Loaded commitment public-key from cache");
					}
				}

				if (line.hasOption("f")) {
					logger.info("Using non-interactive proofs");
					oip = new ECObliviousInformationPurchaseWithFiatShamir(grp, commitCache);
				} else {
					logger.info("Using interactive proofs");
					oip = new ECObliviousInformationPurchase(grp, commitCache);
				}
			}

			int localport = Integer.valueOf(line.getOptionValue("p", "0"));

			tcpChannelFactory = new TCPChannelFactory(localport);
			logger.log(Level.INFO, appName + " started, serving on address " + tcpChannelFactory.getLocalname());
			
			Thread tcpHandler = new Thread(tcpChannelFactory, "Tcp Handler");
			tcpHandler.start();
			
			if (peerAddr != null) {
				logger.log(Level.FINE, "Connecting to peer: " + peerAddr);
				TCPChannel chan = tcpChannelFactory.getChannel(peerAddr);
				logger.log(Level.INFO, "Connected to peer: " + chan.getName());
				toPeer = new CountingChannel(chan);
			} else {
				logger.log(Level.FINE, "Waiting for peer to connect");
				TCPChannel chan = tcpChannelFactory.getChannel();
				logger.log(Level.INFO, "Connected to peer: " + chan.getName());
				toPeer = new CountingChannel(chan);
			}
			
			run();

			logger.log(Level.INFO, "Stopping Tcp Handler.");
			tcpChannelFactory.stop();
			logger.log(Level.FINE, "Waiting for Tcp Handler to exit...");
			tcpHandler.join();
			logger.log(Level.FINE, "TCP Handler has exited");
		
		} catch (CheatingPeerException e) {
			logger.log(Level.SEVERE, "Cheating peer detected: ", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Couldn't load database driver: ", e);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Database error: ", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "I/O Error", e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Interrupted!", e);
		}
	}
}
