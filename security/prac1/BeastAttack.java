import java.io.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.util.Arrays;

public class BeastAttack
{

    public static void main(String[] args) throws Exception
    {
	byte[] ciphertext=new byte[1024]; // will be plenty big enough

	// e.g. this prints out the result of an encryption with no prefix	

	//=================== TASK 1 ======================//
	for (int j = 0; j < 100; j++) {

		int length=callEncrypt(null,0,ciphertext);			
		for(int i=0; i < 8; i++) {
			System.out.print(String.format("0x2 ",ciphertext[i]));
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
