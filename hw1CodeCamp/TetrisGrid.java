//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.
import java.util.Set;
import java.util.HashSet;

public class TetrisGrid {
    private boolean[][] grid;
	
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
	    /*
	    int width = grid.length;
	    int height = grid[0].length;
		this.grid = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, height);
        }
        */
	    this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
        int width = grid.length;
        int height = grid[0].length;
        Set<Integer> filledRow = new HashSet<>();
        // find all rows to be removed
        for (int i = 0; i < height; i++) {
            // check row i
            int j;
            for (j = 0; j < width; j++) {
                if (!grid[j][i]) {
                    break;
                }
            }
            if (j == width) {
                filledRow.add(i);
            }
        }

        int i = 0;
        for (int j = 0; j < height; j++) {
            if (filledRow.contains(j)) {
                continue;
            }
            if (j == i) {
                i += 1;
                continue;
            }
            copyOneRow(j, i);
            i += 1;
        }
        // fill the upper part of the grid
        fillWithFalse(i);
	}

	private void copyOneRow(int from, int to) {
        int width = grid.length;
	    for (int i = 0; i < width; i++) {
	        grid[i][to] = grid[i][from];
        }
    }

    private void fillWithFalse(int from) {
        int width = grid.length;
        int height = grid[0].length;
	    for (int i = 0; i < width; i++) {
	        for (int j = from; j < height; j++) {
	            grid[i][j] = false;
            }
        }
    }
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
	    // list (array) is mutable, so make a copy and return
        int width = grid.length;
        int height = grid[0].length;
        boolean[][] gridCp = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            System.arraycopy(this.grid[i], 0, gridCp[i], 0, height);
        }
		return gridCp; // YOUR CODE HERE
	}
}
