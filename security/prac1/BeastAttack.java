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
		byte[] prefix = new byte[8];
		
		//===================//
		//		 Task 1      //
		//===================//

		// (a) determine the plaintext's length
		int ptextlen = findLength();
		System.out.println("plaintext length = "+ptextlen);

		// (b) determine an IV function
        compareInitVectors(10);

        //===================//
        //       Task 2      //
        //===================//

        // (a) decrypt m1
        

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

    // compares our getInitVector function with the first 8
    // bytes of the actual ciphertext (i.e. the actual IV)
    static void compareInitVectors(int n) {
        int length = callEncrypt(null,0,ciphertext);
            long time = new Date().getTime();
            byte[] current_iv = getInitVector(Arrays.copyOf(ciphertext,8),time);
            
            for (int i = 0; i < n; i++) {
                length = callEncrypt(null,0,ciphertext);
                time = new Date().getTime();
                current_iv = getInitVector(Arrays.copyOf(ciphertext,8),time);
                
                for (int k = 0; k < 8; k++) {
                    System.out.print(String.format("%02X ",current_iv[k]));
                }
                System.out.print('\t');
                
                for (int j = 0; j<8; j++) {
                    System.out.print(String.format("%02X ",ciphertext[j]));
                }
                System.out.println("");
            }
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
