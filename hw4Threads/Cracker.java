// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private final byte[] target;
	private final int len;
	private CountDownLatch countDown;

	public Cracker(byte[] t, int len, int numWorkers) {
	    target = t;
	    this.len = len;
        countDown = new CountDownLatch(numWorkers);
    }

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	/**
	 * An inner class implements Runnable, processing part of the job in its own thread
	 */
	private class worker implements Runnable {
		private MessageDigest md;
		private int from, to;
		private StringBuilder sb;

		/**
		 * @param from define the start of the range this worker need to do
		 * @param to define the end of the range
		 */
		public worker(int from, int to) {
		    try {
                md = MessageDigest.getInstance("SHA");
            }
            catch (java.security.NoSuchAlgorithmException noSuch) {
		        noSuch.printStackTrace();
            }
            this.from = from;
		    this.to = to;
		    sb = new StringBuilder();
        }

		@Override
		public void run() {
		    recursionHelper(0, from, to);
		    countDown.countDown();
		}

		/**
		 * @param s calculates the hash code and prints out the note
		 */
        private void hashAndPrint(String s) {
            md.update(s.getBytes());
            byte[] result = md.digest();
            // if the target String is "print"
            if (target == null) {
                System.out.println(s + " " +hexToString(result));
            }
            // else there do is a target and compare if it is the target
            else if (MessageDigest.isEqual(target, result)) {
                    System.out.println("match:" + s + " " + hexToString(result));
            }
        }

		/**
		 * use recursion to solve finds all the combination of characters, then do the calculation
		 * @param depth the length of the string to be processed
		 * @param from defines the start of the range
		 * @param to defines the end of the range
		 */
		private void recursionHelper(int depth, int from, int to) {
		    if (depth == len) {
		        return;
            }
            for (int i = from; i <= to; i++) {
            	// get the combination
                sb.append(getChar(i));
                // do the processing
                hashAndPrint(sb.toString());
                // go deeper
                recursionHelper(depth + 1, 0, 39);
                // finish the Character at this depth
                sb.deleteCharAt(sb.length() - 1);
            }

        }
	}

	// thread safe
	private synchronized char getChar(int i) {
	    return CHARS[i];
    }


	/**
	 * fork off threads of size of NumWorker and wait for them finish
	 * @param NumWorker
	 */
	private void forkoffAndWait(int NumWorker) {
	    int partSize = CHARS.length / NumWorker;
	    for (int i = 0; i < NumWorker; i++) {
	        int from = i * partSize;
	        int to;
	        if (i == NumWorker - 1) {
	            to = CHARS.length - 1;
            }
            else {
                to = from + partSize - 1;
            }

	        new Thread(new worker(from, to)).start();
        }
        try {
            countDown.await();
        }
        catch (InterruptedException ignored) { }
		System.out.println("all done!");
    }



	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Args: target length [workers]");
			System.exit(1);
		}
		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length>2) {
			num = Integer.parseInt(args[2]);
		}
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
		
		// YOUR CODE HERE
        Cracker c;
        if (targ.equals("print")) {
            c = new Cracker(null, len, num);
        }
        else {
            c = new Cracker(hexToArray(targ), len, num);
        }
        c.forkoffAndWait(num);
	}
}
