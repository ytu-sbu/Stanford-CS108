import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	private Piece L11, L12, L13, L14;
    private Piece L21, L22, L23, L24;
    private Piece s2, s2Rtd;
    private Piece sq;
    private Piece stk, stkRtd;


	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);

		s = new Piece(Piece.S1_STR);

		L11 = new Piece(Piece.L1_STR);

        L21 = new Piece(Piece.L2_STR);

        s2 = new Piece(Piece.S2_STR);

        sq = new Piece(Piece.SQUARE_STR);

        stk = new Piece(Piece.STICK_STR);

        pyr2 = pyr1.computeNextRotation();
        pyr3 = pyr2.computeNextRotation();
        pyr4 = pyr3.computeNextRotation();
        sRotated = s.computeNextRotation();
        L12 = L11.computeNextRotation();
        L13 = L12.computeNextRotation();
        L14 = L13.computeNextRotation();
        L22 = L21.computeNextRotation();
        L23 = L22.computeNextRotation();
        L24 = L23.computeNextRotation();
        s2Rtd = s2.computeNextRotation();
        stkRtd = stk.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

		// Now try after rotation
		// Effectively we're testing size and rotation code here

		// Now try with some other piece, made a different way
		assertEquals(1, stk.getWidth());
		assertEquals(4, stk.getHeight());


        assertEquals(2, sq.getWidth());
        assertEquals(2, sq.getHeight());

        assertEquals(3, s.getWidth());
        assertEquals(2, s.getHeight());

        assertEquals(3, s2.getWidth());
        assertEquals(2, s2.getHeight());

        assertEquals(2, L11.getWidth());
        assertEquals(3, L11.getHeight());

        assertEquals(2, L21.getWidth());
        assertEquals(3, L21.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
//		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
//		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));

        assertTrue(Arrays.equals(new int[] {0}, stk.getSkirt()));

        assertTrue(Arrays.equals(new int[] {1, 0, 0}, s2.getSkirt()));

        assertTrue(Arrays.equals(new int[] {0, 0}, sq.getSkirt()));

        assertTrue(Arrays.equals(new int[] {0, 0}, L11.getSkirt()));

        assertTrue(Arrays.equals(new int[] {0, 0}, L21.getSkirt()));
	}

	public void testComputeNext() {
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
        assertTrue(Arrays.equals(new int[] {1, 0}, pyr2.getSkirt()));
        Piece ref = new Piece("0 1  1 0  1 1  1 2");
        assertEquals(ref, pyr2);

        assertEquals(3, pyr3.getWidth());
        assertEquals(2, pyr3.getHeight());
        assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
        ref = new Piece("0 1  1 0  1 1  2 1");
        assertEquals(ref, pyr3);

        assertEquals(2, pyr4.getWidth());
        assertEquals(3, pyr4.getHeight());
        assertTrue(Arrays.equals(new int[] {0, 1}, pyr4.getSkirt()));
        ref = new Piece("0 1  0 0  0 2  1 1");
        assertEquals(ref, pyr4);

        assertEquals(2, sRotated.getWidth());
        assertEquals(3, sRotated.getHeight());
        assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
        ref = new Piece("0 1  0 2  1 0  1 1");
        assertEquals(ref, sRotated);

        assertEquals(3, L12.getWidth());
        assertEquals(2, L12.getHeight());
        assertTrue(Arrays.equals(new int[] {0, 0, 0}, L12.getSkirt()));
        ref = new Piece("0 0  1 0  2 0  2 1");
        assertEquals(ref, L12);

        assertEquals(2, L13.getWidth());
        assertEquals(3, L13.getHeight());
        assertTrue(Arrays.equals(new int[] {2, 0}, L13.getSkirt()));
        ref = new Piece("0 2  1 0  1 1  1 2");
        assertEquals(ref, L13);

        assertEquals(3, L14.getWidth());
        assertEquals(2, L14.getHeight());
        assertTrue(Arrays.equals(new int[] {0, 1, 1}, L14.getSkirt()));
        ref = new Piece("0 1  0 0  1 1  2 1");
        assertEquals(ref, L14);

        assertEquals(3, L22.getWidth());
        assertEquals(2, L22.getHeight());
        assertTrue(Arrays.equals(new int[] {1, 1, 0}, L22.getSkirt()));
        ref = new Piece("0 1  1 1  2 0  2 1");
        assertEquals(ref, L22);

        assertEquals(2, L23.getWidth());
        assertEquals(3, L23.getHeight());
        assertTrue(Arrays.equals(new int[] {0, 2}, L23.getSkirt()));
        ref = new Piece("0 0  0 1  0 2  1 2");
        assertEquals(ref, L23);

        assertEquals(3, L24.getWidth());
        assertEquals(2, L24.getHeight());
        assertTrue(Arrays.equals(new int[] {0, 0, 0}, L24.getSkirt()));
        ref = new Piece("0 0  1 0  2 0  0 1");
        assertEquals(ref, L24);

        assertEquals(2, s2Rtd.getWidth());
        assertEquals(3, s2Rtd.getHeight());
        assertTrue(Arrays.equals(new int[] {0, 1}, s2Rtd.getSkirt()));
        ref = new Piece("0 1  0 0  1 2  1 1");
        assertEquals(ref, s2Rtd);

        assertEquals(4, stkRtd.getWidth());
        assertEquals(1, stkRtd.getHeight());
        assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stkRtd.getSkirt()));
        ref = new Piece("0 0  1 0  2 0  3 0");
        assertEquals(ref, stkRtd);
    }

    public void testRotation() {
	    assertEquals(pyr1, pyr4.computeNextRotation());
	    assertEquals(s, sRotated.computeNextRotation());
	    assertEquals(s2, s2Rtd.computeNextRotation());
	    assertEquals(L11, L14.computeNextRotation());
	    assertEquals(L21, L24.computeNextRotation());
	    assertEquals(stk, stkRtd.computeNextRotation());
	    assertEquals(sq, sq.computeNextRotation());
    }

    public void testFastRotation() {
	    Piece[] pieces = Piece.getPieces();

	    Piece stick = pieces[Piece.STICK];
	    assertEquals(stk, stick);
	    assertEquals(stkRtd, stick.fastRotation());
        assertEquals(stk, stick.fastRotation().fastRotation());

	    Piece l1 = pieces[Piece.L1];
	    assertEquals(L11, l1);
	    assertEquals(L12, l1.fastRotation());

	    Piece s1 = pieces[Piece.S1];
	    assertEquals(s, s1);
	    assertEquals(sRotated, s1.fastRotation());
        assertEquals(s, s1.fastRotation().fastRotation());
    }
}
