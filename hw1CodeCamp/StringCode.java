import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {
    private static final int ZERO = 48;

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
	    if (str.length() == 0) {
	        return 0;
        }
	    int run = 1;
	    int maxRun = 1;
	    char preChar = str.charAt(0);
	    for (int i = 1; i < str.length(); i++) {
	        // same character with last position
	        if (str.charAt(i) == preChar) {
	            run += 1;
            }
            // different character
            else {
                // if current run larger than maxRun, update
                if (run > maxRun) {
                    maxRun = run;
                }
                // reset the flags
                run = 1;
                preChar = str.charAt(i);
            }
            if (i == str.length() - 1 && run > maxRun) {
                maxRun = run;
            }
        }
        return maxRun; // YOUR CODE HERE
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
	    StringBuilder newString = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
		    char aChar = str.charAt(i);
		    if (aChar < 48 || aChar > 57) {
		        newString.append(str.charAt(i));
		        continue;
            }
            if (i == str.length() - 1) {
                break;
            }
            int repeat = aChar - ZERO;
		    for (int j = 0; j < repeat; j++) {
		        newString.append(str.charAt(i + 1));
            }
        }
		return newString.toString(); // YOUR CODE HERE
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
	    Set<String> bag = new HashSet<>();
	    for (int i = 0; i <= a.length() - len; i++) {
            bag.add(a.substring(i, i + len));
        }
        for (int i = 0; i <= b.length() - len; i++) {
            if (bag.contains(b.substring(i, i + len))) {
                return true;
            }
        }
		return false; // YOUR CODE HERE
	}
}
