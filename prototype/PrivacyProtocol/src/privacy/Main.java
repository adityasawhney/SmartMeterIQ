/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package privacy;

import java.math.BigInteger;
import java.util.Random;
import qilin.primitives.concrete.ZpPedersen;
import qilin.primitives.concrete.Zpsafe;

/**
 *
 * @author sawhneya
 */
public class Main {

    static ZpPedersen createPedersen() {
        final int BITS = 256;
        Random rand = new Random(1);
        Zpsafe grp = new Zpsafe(Zpsafe.randomSafePrime(BITS, 50, rand));
        BigInteger h = grp.sample(rand);
        return new ZpPedersen(grp, h);
    }

    public class SmartMeterData {
        private BigInteger V[];
        private BigInteger R[];
        private BigInteger C[];

        public SmartMeterData(BigInteger[] V, BigInteger[] R, BigInteger[] C) {
            this.V = V;
            this.R = R;
            this.C = C;
        }

        public BigInteger[] getReadings() {
            return this.V;
        }

        public BigInteger[] getRandoms() {
            return this.R;
        }

        public BigInteger[] getCommitments() {
            return this.C;
        }
    }

    public class SmartMeterBill {
        private BigInteger C[];
        private BigInteger Pt;
        private BigInteger Rt;

        public SmartMeterBill(BigInteger[] C, BigInteger Pt, BigInteger Rt) {
            this.C = C;
            this.Pt = Pt;
            this.Rt = Rt;
        }
        
        public BigInteger[] getCommitments() {
            return this.C;
        }

        public BigInteger getTotalBillAmount() {
            return this.Pt;
        }

        public BigInteger getTotalRandomness() {
            return this.Rt;
        }
    }

    public class SmartMeter {
        private ZpPedersen perdersen;
        private BigInteger T[];
        
        public SmartMeter() {
           perdersen = Main.createPedersen();
        }

        public void setTarrif(BigInteger tarrif[]) {
            this.T = tarrif;
        }

        public SmartMeterData getData() {
            Random rand = new Random();
            BigInteger V[] = { new BigInteger("10"), new BigInteger("20") };
            BigInteger R[] = { this.perdersen.getRandom(rand), this.perdersen.getRandom(rand) };
            BigInteger C[] = { this.perdersen.commit(V[0], R[0]), this.perdersen.commit(V[1], R[1]) };

            return new SmartMeterData(V, R, C);
        }
    }

    public class PrivacyPlugin {
        private SmartMeter meter;
        private BigInteger T[];
        private boolean tamper;

        public PrivacyPlugin(SmartMeter meter) {
           this.meter = meter;
           this.tamper = false;
        }

        public void setTarrif(BigInteger tarrif[]) {
            this.T = tarrif;
        }

        public void toggleTamper() {
            this.tamper = !this.tamper;
        }
        
        public SmartMeterBill retrieveBill() {
            SmartMeterData data = meter.getData();
            BigInteger Pt = new BigInteger("0");
            BigInteger Rt = new BigInteger("0");

            for (int i = 0; i < data.V.length; i++) {
                Pt = Pt.add(data.V[i].multiply(T[i]));
                Rt = Rt.add(data.R[i].multiply(T[i]));
            }

            if (this.tamper) {
                Pt = Pt.subtract(new BigInteger("10"));
            }
            return new SmartMeterBill(data.C, Pt, Rt);
        }
    }

    public class Supplier {
        private ZpPedersen perdersen;
        private BigInteger T[];

        public Supplier() {
           perdersen = Main.createPedersen();
        }

        public void setTarrif(BigInteger tarrif[]) {
            this.T = tarrif;
        }

        public boolean verifyBill(SmartMeterBill bill) {
            BigInteger Ct = this.perdersen.multiply(bill.C[0], T[0]);
            for (int i = 1; i < bill.C.length; i++) {
                BigInteger c = this.perdersen.multiply(bill.C[i], T[i]);
                Ct = this.perdersen.add(Ct, c);
            }

            return this.perdersen.verify(Ct, bill.Pt, bill.Rt);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main m = new Main();
        m.run();
    }

    public void run() {
        BigInteger T[] = { new BigInteger("5"), new BigInteger("7") };

        SmartMeter meter = new SmartMeter();
        PrivacyPlugin plugin = new PrivacyPlugin(meter);
        Supplier supplier = new Supplier();

        meter.setTarrif(T);
        plugin.setTarrif(T);
        supplier.setTarrif(T);

        SmartMeterBill bill = plugin.retrieveBill();
        boolean isValid = supplier.verifyBill(bill);
        System.out.print(isValid);

        plugin.toggleTamper();
        
        bill = plugin.retrieveBill();
        isValid = supplier.verifyBill(bill);
        System.out.print(isValid);
    }
}
