
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}


	public void testCharArea3() {
		char[][] grid = new char[][]{
				{'c', 'a', ' ', 'x'},
				{'d', 'e', ' ', ' '},
				{'b', ' ', 'b', 'c'},
				{' ', ' ', 'a', 'e'}
		};

		CharGrid cg = new CharGrid(grid);
		assertEquals(16, cg.charArea(' '));
        assertEquals(0, cg.charArea('z'));
        assertEquals(9, cg.charArea('e'));
	}

}
