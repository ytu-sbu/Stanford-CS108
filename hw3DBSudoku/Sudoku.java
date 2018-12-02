import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");

	// the full set of integers a grid could contains
	private static final List<Integer> fullSet = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	// the length of inner square
	public static final int PARTSIZE = SIZE / PART;

	private int[][] grids;
	private long duration;
	private Sudoku solution;
	private int soluNum;

    /**
     * encapsulates all the information and methods of an empty grid in grids
     */
	private class Spot implements Comparable<Spot> {
	    private final int row;
	    private final int col;
	    // the integers this grid could has
	    private HashSet<Integer> valueSet;

        /**
         * Spot constructor, takes two arguments
         * @param r row number
         * @param c column number
         */
	    public Spot(int r, int c) {
	        row = r;
	        col = c;
	        valueSet = new HashSet<>();
        }

        /**
         *set the grid to the specified value
         * @param v the specified value
         */
        public void setValue(int v) {
	        grids[row][col] = v;
        }

        /**
         * set the ValueSet to a collection of numbers which is valid on this grid
         */
        public void updateSelf() {
//            valueSet = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
            valueSet = new HashSet<>(fullSet);
            // exclude from same row
            for (int i = 0; i < SIZE; i++) {
                valueSet.remove(grids[row][i]);
            }
            // exclude from same col
            for (int i = 0; i < SIZE; i++) {
                valueSet.remove(grids[i][col]);
            }
            // exclude from group
            int deltai = row % PARTSIZE;
            int deltaj = col % PARTSIZE;
            int ibase = row - deltai;
            int jbase = col - deltaj;
            for (int i = 0; i < PARTSIZE; i++) {
                if (i == deltai) {
                    continue;
                }
                for (int j = 0; j < PARTSIZE; j++) {
                    if (j == deltaj) {
                        continue;
                    }
                    valueSet.remove(grids[ibase + i][jbase + j]);
                }
            }
        }

        /**
         * @return the value stored in this grid
         */
        public int getValue() {
	        return grids[row][col];
        }

        /**
         * after this grid has been set to some value, updates the related grid
         * @param relates the related grid
         */
        public void updateRelates(Spot relates) {
	        // same row or same col element
            if (relates.row == row || relates.col == col) {
                relates.valueSet.remove(getValue());
                return;
            }

            // part relative
            if (relates.row / PARTSIZE == row / PARTSIZE && relates.col / PARTSIZE == col / PARTSIZE) {
                relates.valueSet.remove(getValue());
            }
        }

        @Override
        public int compareTo(Spot other) {
	        return this.valueSet.size() - other.valueSet.size();
        }
    }

	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	

	/**
	 * Sets up based on the given ints.
	 */
//	public Sudoku(int[][] ints) {
	public Sudoku(int[][] grid) {
		// YOUR CODE HERE
        grids = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, grids[i], 0, SIZE);
        }
        duration = 0;
        soluNum = 0;
	}

	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
     * The original grid of the sudoku should not be changed by the solution
     * @return the number of solutions and sets the state for getSolutionText() and getElapsed().
	 */
	public int solve() {
	    long start = System.currentTimeMillis();
        // start processing
        ArrayList<Spot> blankSpots = readBlankAndSort();
        solveHelper(0, blankSpots);
        // finish processing
        duration = System.currentTimeMillis() - start;
		return soluNum; // YOUR CODE HERE
	}

    /**
     * read the int array, get all the blank grids, then sorting it based on the possible value numbers
     * @return the sorted Spot ArrayList
     */
	private ArrayList<Spot> readBlankAndSort() {
	    ArrayList<Spot> list = new ArrayList<>();
	    Spot tmp;
	    for (int i = 0; i < SIZE; i++) {
	        for (int j = 0; j < SIZE; j++) {
	            if (grids[i][j] == 0) {
	                tmp = new Spot(i, j);
	                tmp.updateSelf();
	                list.add(tmp);
                }
            }
        }
        Collections.sort(list);
	    return list;
    }

    /**
     * recursion helper
     * @param pos the index of current processing Spot
     * @param list the sorted Spot List read from grids
     */
	private void solveHelper(int pos, List<Spot> list) {
	    if (soluNum > MAX_SOLUTIONS) {
	        return;
        }
        // found a solution
        if (pos == list.size()) {
            soluNum += 1;
            if (solution == null) {
                solution = new Sudoku(grids);
            }
            return;
        }
        Spot current = list.get(pos);
        current.updateSelf();
        for (int v : list.get(pos).valueSet) {
            current.setValue(v);
//            updateSpots(pos, list);
            solveHelper(pos + 1, list);
            current.setValue(0);
//            restoreSpots(pos, list);
        }
	}

    /**
     * based on the new set value of current grid, updates all the related Spots
     * @param pos the index of current processing Spot
     * @param list the sorted Spot List read from grids
     */
	private void updateSpots(int pos, List<Spot> list) {
	    Spot master = list.get(pos);
	    for (int p = pos + 1; p < list.size(); p++) {
            master.updateRelates(list.get(p));
        }
    }

    /**
     * after the current grid restored to 0, restores all the related Spots
     * @param pos the index of current processing Spot
     * @param list the sorted Spot List read from grids
     */
    private void restoreSpots(int pos, List<Spot> list) {
	    for (int p = pos + 1; p < list.size(); p++) {
	        list.get(p).updateSelf();
        }
    }

    /**
     * @return the first found solution, otherwise the empty string
     */
	public String getSolutionText() {
		return solution.toString(); // YOUR CODE HERE
	}

    /**
     * after a solve
     * @return the elapsed time spent in the solve measured in milliseconds.
     */
	public long getElapsed() {
	    // System.currentTimeMillis()
		return duration; // YOUR CODE HERE
	}

    /**
     * @return a String made of 9 lines that shows the rows of the grid, with each number preceded by a space
     */
	@Override
	public String toString() {
	    StringBuilder buffer = new StringBuilder();
        for (int k = 0; k < SIZE + PARTSIZE + 1; k++) {
            buffer.append("- ");
        }
        buffer.append('\n');
        for (int i = 0; i < SIZE; i++) {
	        buffer.append('|');
	        for (int j = 0; j < SIZE; j++) {
	            buffer.append(' ');
	            buffer.append(grids[i][j]);
	            if (j % PARTSIZE == 2) {
	                buffer.append(" |");
                }
            }
            buffer.append('\n');
	        if (i % PARTSIZE == 2) {
	            for (int k = 0; k < SIZE + PARTSIZE + 1; k++) {
                    buffer.append("- ");
                }
                buffer.append('\n');
            }
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

}
