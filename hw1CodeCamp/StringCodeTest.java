// StringCodeTest
// Some test code is provided for the early HW1 problems,
// and much is left for you to add.

import junit.framework.TestCase;

public class StringCodeTest extends TestCase {
	//
	// blowup
	//
	public void testBlowup1() {
		// basic cases
		assertEquals("xxaaaabb", StringCode.blowup("xx3abb"));
		assertEquals("xxxZZZZ", StringCode.blowup("2x3Z"));
        System.out.println("run testBlowup1");
	}
	
	public void testBlowup2() {
		// things with digits
		
		// digit at end
		assertEquals("axxx", StringCode.blowup("a2x3"));
		
		// digits next to each other
		assertEquals("a33111", StringCode.blowup("a231"));
		
		// try a 0
		assertEquals("aabb", StringCode.blowup("aa0bb"));

		System.out.println("run testBlowup2");
	}
	
	public void testBlowup3() {
		// weird chars, empty string
		assertEquals("AB&&,- ab", StringCode.blowup("AB&&,- ab"));
		assertEquals("", StringCode.blowup(""));
		
		// string with only digits
		assertEquals("", StringCode.blowup("2"));
		assertEquals("33", StringCode.blowup("23"));
	}
	
	
	//
	// maxRun
	//
	public void testRun1() {
		assertEquals(2, StringCode.maxRun("hoopla"));
		assertEquals(3, StringCode.maxRun("hoopllla"));
	}
	
	public void testRun2() {
		assertEquals(3, StringCode.maxRun("abbcccddbbbxx"));
		assertEquals(0, StringCode.maxRun(""));
		assertEquals(3, StringCode.maxRun("hhhooppoo"));
	}
	
	public void testRun3() {
		// "evolve" technique -- make a series of test cases
		// where each is change from the one above.
		assertEquals(1, StringCode.maxRun("123"));
		assertEquals(2, StringCode.maxRun("1223"));
		assertEquals(2, StringCode.maxRun("112233"));
		assertEquals(3, StringCode.maxRun("1112233"));
	}

	// Need test cases for stringIntersect
    public void testStrIntersect1() {
	    // assume all strings are of length 1 or more
	    assertFalse(StringCode.stringIntersect("abc", "defg", 1));
        assertTrue(StringCode.stringIntersect("abc", "dbefg", 1));
    }

    public void testStrIntersect2() {
        assertFalse(StringCode.stringIntersect("junittestcase", "framework", 4));
        assertTrue(StringCode.stringIntersect("junittestcase", "frametestwork", 4));
    }

    public void testStrIntersect3() {
	    assertFalse(StringCode.stringIntersect("junitframework", "testcase", 8));
        assertTrue(StringCode.stringIntersect("testcase", "testcase", 8));
    }

    public void testStrIntersect4() {
        assertFalse(StringCode.stringIntersect("junitframework", "testcase", 18));
    }

}
