import java.io.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.util.Date;

public class BeastAttack
{

    public static void main(String[] args) throws Exception
    {
		byte[] ciphertext = new byte[1024]; // will be plenty big enough
		
		//===================//
		//		 Task 1      //
		//===================//

		// (a) determine the plaintext's length
		int ptextlen = findLength();
		System.out.println("plaintext length = "+ptextlen);

        //===================//
        //       Task 2      //
        //===================//

        // (a) decrypt the first block
        
        byte x = (byte) -127;
        byte[] foundChars = new byte[8];
        int ms = 0; // number of ms we've found in this block
        for (int j = 7; j > -1; j--) {
            boolean found = false;
            long prevIVtime = new Date().getTime();
            if (j > -1) { int len = callEncrypt(new byte[j],j,ciphertext); }
            byte[] ivReal = getBlock(1,ciphertext);
            byte[] ivPrev = Arrays.copyOfRange(ivReal,0,8);
            byte[] targetBlock = getBlock(2,ciphertext); 
            x = (byte) -127;

            while (!found && j >= 0) {
                byte[] prefix = new byte[8];
                // add the known chars
                int i = 7 - ms;
                int f = 0;  // 0 <= f <= ms 
                while (i < 7) {
                    prefix[i] = foundChars[f];
                    i++; f++;
                }
                // this is the varying byte
                prefix[7] = (byte) x;
                // recalculate the IV
                byte[] ivGuess = getInitVector(ivReal,prevIVtime);
                // ex-or with the prefix
                for (int k = 0; k<8; k++) {prefix[k]= (byte) (ivGuess[k] ^ ivPrev[k] ^ prefix[k]); } 
                //printArr(prefix);

                // encrypt and update IV details
                prevIVtime = new Date().getTime();
                callEncrypt(prefix,8,ciphertext);
                ivReal = getBlock(1,ciphertext);
                // was our IV guess correct? if so, we can compare blocks
                if (Arrays.equals(ivGuess,ivReal)) {
                    byte[] block2 = getBlock(2,ciphertext);
                    if (Arrays.equals(targetBlock,block2)) {
                        System.out.println("m"+(7-j)+" = "+((char) x));
                        found = true;
                        foundChars[ms] = x;
                        ms++;
                        //System.exit(0);
                    } else {
                        // System.out.print("\tm1 != "+((char) x));
                        // try another character
                        x = (byte) (x + 1);
                    }
                } else {
                    // System.out.println("[iv guess did not match]");
                    // try another IV
                }
            }
        }

    /* ========== Output ============= /
    
    $ java BeastAttack 
    adding prefix of size 0 yields ciphertext length 56
    adding prefix of size 1 yields ciphertext length 56
    adding prefix of size 2 yields ciphertext length 56
    adding prefix of size 3 yields ciphertext length 64
    plaintext length = 54
    m0 = T
    m1 = h
    m2 = e
    m3 =  
    m4 = n
    m5 = a
    m6 = m
    m7 = e

    / ================================ */
    }

    // prints out a an array of bytes
    static void printArr(byte[] arr) {
        for (int i = 0; i<arr.length; i++) {
            System.out.print(String.format("%02x ", arr[i]));
        }
        System.out.println("");
    }

    // determines the length of the plaintext by gradually increasing
    // the size of an additional prefix we add, until a new block is 
    // created in the ciphertext
    static int findLength() throws Exception {
    	byte[] ciphertext = new byte[1024];
    	byte[] prefix = new byte[8];
    	int i = 0;
    	boolean same = true;
    	int prevLen = callEncrypt(prefix,0,ciphertext); // init length without any prefix 
    	while (i < 8 && same) {
    		int len = callEncrypt(prefix,i,ciphertext);
    		System.out.println("adding prefix of size "+i+" yields ciphertext length "+len);
    		if (len > prevLen) {
    			same = false;
    		} else {
    			prevLen = len;
    			i++;
    		}
    	}
    	if (!same) {
    		return (callEncrypt(null,0,ciphertext)-(i-1));
    	} else {
    		return -1;
    	}
    }

    // returns an initial vector based on the current system time
    // where the returned vector is 8 bytes long
    static byte[] getInitVector(byte[] iv0, long time0) {
        long timeDiff = (new Date().getTime() - time0) * 5;
        long iv0_long = ByteBuffer.wrap(iv0).getLong();
        return ByteBuffer.allocate(8).putLong(iv0_long + timeDiff).array();
    }

    /** returns arr[8(i-1)..8i) == ith block of 8 in arr 
    * precondition: 1 <= i <= N/8 where N = arr.length
    */
    static byte[] getBlock(int i , byte[] arr) {
        return Arrays.copyOfRange(arr,8*(i-1),8*i);
    } 


    // a helper method to call the external programme "encrypt" in the current directory
    // the parameters are the plaintext, length of plaintext, and ciphertext; returns length of ciphertext
    static int callEncrypt(byte[] prefix, int prefix_len, byte[] ciphertext) throws IOException
    {
	HexBinaryAdapter adapter = new HexBinaryAdapter();
	Process process;
	
	// run the external process (don't bother to catch exceptions)
	if(prefix != null)
	{
	    // turn prefix byte array into hex string
	    byte[] p=Arrays.copyOfRange(prefix, 0, prefix_len);
	    String PString=adapter.marshal(p);
	    process = Runtime.getRuntime().exec("./encrypt "+PString);
	}
	else
	{
	    process = Runtime.getRuntime().exec("./encrypt");
	}

    // process the resulting hex string
	String CString = (new BufferedReader(new InputStreamReader(process.getInputStream()))).readLine();
	byte[] c=adapter.unmarshal(CString);
	System.arraycopy(c, 0, ciphertext, 0, c.length); 
	return(c.length);
    }
}
