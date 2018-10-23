// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;
	private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
		for (int x = 0; x < grid.length; x++) {
		    for (int y = 0; y < grid[0].length; y++) {
		        if (grid[x][y] == ch) {
		            if (x < xMin) {
		                xMin = x;
                    }
                    if (x > xMax) {
                        xMax = x;
                    }
                    if (y < yMin) {
                        yMin = y;
                    }
                    if (y > yMax) {
                        yMax = y;
                    }
                }
            }
        }
        if (xMin == Integer.MAX_VALUE) {
            return 0;
        }
		return (xMax - xMin + 1) * (yMax - yMin + 1); // YOUR CODE HERE
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
	    int count = 0;
	    for (int i = 1; i < grid.length - 1; i++) {
	        for (int j = 1; j <grid[0].length - 1; j++) {
                int dir = 0;
	            int armLength = countArm(i, j, dir);
	            if (armLength < 2) {
	                continue;
                }
                while (dir < 3) {
                    int nextArmLength = countArm(i, j, dir + 1);
                    if (nextArmLength != armLength) {
                        break;
                    }
                    else {
                        dir += 1;
                    }
                }
                if (dir == 3) {
                    count += 1;
                }
            }
        }
		return count; // YOUR CODE HERE
	}

	private int countArm(int row, int col, int dir) {
	    char c = grid[row][col];
	    int count = 1;
	    int nextRow = nextRow(row, dir);
	    int nextCol = nextCol(col, dir);
	    while(true) {
            if (nextCol < 0 || nextCol >= grid[0].length || nextRow < 0 || nextRow >= grid.length
            || grid[nextRow][nextCol] != c) {
                break;
            }
            count += 1;
            nextRow = nextRow(nextRow, dir);
            nextCol = nextCol(nextCol, dir);
        }
        return count;
    }

	private int nextRow(int row, int dir) {
	    if (dir == UP) {
	        return row - 1;
        }
        else if (dir == DOWN) {
            return row + 1;
        }
        else {
            return row;
        }
    }

    private int nextCol(int col, int dir) {
        if (dir == LEFT) {
            return col - 1;
        }
        else if (dir == RIGHT) {
            return col + 1;
        }
        else {
            return col;
        }
    }
}
