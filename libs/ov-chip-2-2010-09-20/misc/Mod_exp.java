// compute modular powers that are too big for bc
//
// usage: java Mod_exp base exponent modulus

import java.math.BigInteger;

public class Mod_exp {

    public static String to_byte_hex_string(byte[] data) {
        int i = data.length;

        if(i == 0)
            return "";
        if(i == 1)
            return String.format("%02X", data[0]);
        else {
            String res = String.format("%02X%02X", data[i-2], data[i-1]);
            i -= 2;

            while(i > 0) {
                if(i == 1)
                    res = String.format("%02X.", data[0]) + res;
                else
                    res = String.format("%02X%02X.", data[i-2], data[i-1]) + 
                        res;
                i -= 2;
            }
            return res;
        }
    }

    public static String to_byte_hex_string(BigInteger bi) {
        return to_byte_hex_string(bi.toByteArray());
    }


    public static void main(String[] args) {
        if(args.length != 3) {
            System.err.println("Usage: java Mod_exp base exp mod");
            System.exit(1);
        }

        BigInteger base = new BigInteger(args[0]);
        BigInteger exp = new BigInteger(args[1]);
        BigInteger mod = new BigInteger(args[2]);

        BigInteger res = base.modPow(exp, mod);

        System.out.format("base = %s\n" +
                          "exp  = %s\n" +
                          "mod  = %s\n" +
                          "res  = %s\n" +
                          "     = %s\n",
                          to_byte_hex_string(base),
                          to_byte_hex_string(exp),
                          to_byte_hex_string(mod),
                          res,
                          to_byte_hex_string(res));
    }
}

/// Local Variables:
/// compile-command: "javac Mod_exp.java"
/// End:
