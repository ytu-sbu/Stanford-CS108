// Board.java
import java.util.*;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private final int width;
	private final int height;
	// stores how many filled spots there are in each row
	private int widths[];
	// stores the height to which each column has been filled
    // the height will be the index of the open spot which is just above the top filled spot in that col
    private int heights[];
    private int xwidths[];
    private int xheights[];
    private int maxHeight;
    private int xmaxHeight;
	private boolean[][] grid;
    private boolean[][] xgrid;
	private boolean DEBUG = true;
	private boolean committed;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		
		// YOUR CODE HERE
        xgrid = new boolean[width][height];
        widths = new int[height];
        xwidths = new int[height];
        heights = new int[width];
        xheights = new int[width];
        maxHeight = 0;
        xmaxHeight = 0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// YOUR CODE HERE
            checkGrid(grid, heights, widths, maxHeight);
//            checkGrid(xgrid, xheights, xwidths, xmaxHeight);
		}
	}

	private void checkGrid(boolean[][] grid, int[] heights, int[] widths, int maxHeight) {
	    int[] checkHeights = new int[width];
	    int[] checkWidths = new int[height];
	    int checkMaxHeight = 0;
	    for (int i = 0; i < width; i++) {
	        for (int j = 0; j < height; j++) {
	            if (grid[i][j]) {
	                checkWidths[j] += 1;
	                if (j >= checkHeights[i]) {
	                    checkHeights[i] = j + 1;
                    }
                }
            }
        }
        // check heights
        for (int i = 0; i < width; i++) {
            if (checkHeights[i] != heights[i]) {
//                System.out.println("original heights[" + i + "] = " + heights[i]);
//                System.out.println("checked heights[" + i + "] = " + checkHeights[i]);
                throw new java.lang.RuntimeException("heights[" + i + "] incorrect!");
            }
            if (checkHeights[i] > checkMaxHeight) {
                checkMaxHeight = checkHeights[i];
            }
        }

        // check widths
        for (int i = 0; i < height; i++) {
            if (checkWidths[i] != widths[i]) {
                throw new java.lang.RuntimeException("widths[" + i + "] incorrect!");
            }
        }

        // check maxHeight
        if (checkMaxHeight != maxHeight) {
            throw new java.lang.RuntimeException("maxHeight incorrect!");
        }
    }
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
	    int[] skirt = piece.getSkirt();
	    int max = 0;
	    // check the col from x to x + piece.width
	    for (int i = 0, iHeight; i < piece.getWidth(); i++) {
	        iHeight = heights[i + x] - skirt[i];
	        if (iHeight > max) {
	            max = iHeight;
            }
        }
		return max; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
	    if (x < 0 || x >= width || y < 0 || y >= height) {
	        return true;
        }
		return grid[x][y]; // YOUR CODE HERE
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		else {
		    committed = false;
		    backup();
        }
			
		// YOUR CODE HERE
        assert(x >= 0);
        assert(y >= 0);


        // check for PLACE_OUT_BOUNDS
        if (x < 0 || y < 0 || piece.getHeight() + y > height || piece.getWidth() + x > width) {
            return PLACE_OUT_BOUNDS;
        }

        // check for PLACE_BAD
        int posX, posY;
        boolean row_filled = false;
        for (TPoint tp : piece.getBody()) {
            posX = x + tp.x;
            posY = y + tp.y;
            if (grid[posX][posY]) {
                return PLACE_BAD;
            }
            grid[posX][posY] = true;
//            widths[posY] += 1;
            if (++widths[posY] >= width) {
                row_filled = true;
            }
            if (posY >= heights[posX]) {
                heights[posX] = posY + 1;
            }
            if (posY >= maxHeight) {
                maxHeight = posY + 1;
            }
        }

        sanityCheck();

		if (row_filled) {
		    return PLACE_ROW_FILLED;
        }
		return PLACE_OK;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		// YOUR CODE HERE
		if (committed) {
		    backup();
        }
		committed = false;
		// shift down
        for (int i = 0; i < widths.length; i++) {
            if (widths[i] == width) {
                rowsCleared++;
            }
        }

        maxHeight = 0;
        for (int c = 0; c < width; c++) {
            int to = 0, from = 0;
            int colHeight = heights[c];
            heights[c] = 0;
            for (; from < colHeight; from++) {
                // remove this row
                if (widths[from] == width) {
                    // next row will be removed either
                    if (widths[from + 1] == width) {
                        grid[c][to++] = false;
                    }
                    // remove only one row
                    // do nothing here
                }
                // not remove this row
                else {
//                    grid[c][to++] = grid[c][from];
                    if (grid[c][from]) {
                        grid[c][to++] = true;
                        heights[c] = to;
                        if (to > maxHeight) {
                            maxHeight = to;
                        }
                    }
                    else {
                        grid[c][to++] = false;
                    }
                }
            }
            while (to <= from) {
                grid[c][to++] = false;
            }
        }

        // updates widths
        int from = 0, to = 0;
        for (; from < height; from++) {
            if (widths[from] == width) {
                if (widths[from + 1] == width) {
                    widths[to++] = 0;
                }
            }
            // not remove this row
            else {
                widths[to++] = widths[from];
            }
        }
        while (to < from) {
            widths[to++] = 0;
        }

        sanityCheck();
		return rowsCleared;
	}


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
	    if (committed) {
	        return;
        }
        int[] tmp1d = heights;
	    heights = xheights;
	    xheights = tmp1d;

	    tmp1d = widths;
	    widths = xwidths;
	    xwidths = tmp1d;

	    boolean[][] tmp2d = grid;
	    grid = xgrid;
	    xgrid = tmp2d;

	    maxHeight = xmaxHeight;

        committed = true;
        sanityCheck();
	}

	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}

	private void backup() {
        // copy main data structure to backups
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, xgrid[i], 0, grid[i].length);
        }
        System.arraycopy(heights, 0, xheights, 0, width);
        System.arraycopy(widths, 0, xwidths, 0, height);
        xmaxHeight = maxHeight;
    }


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
		    buff.append(widths[y]);
		    if (widths[y] < 10) {
		        buff.append(' ');
            }
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append(" +");
				else buff.append("  ");
			}
			buff.append('|');
			buff.append(y);
			buff.append("\n");
		}
		buff.append(" ");
		for (int x=0; x<width+1; x++) buff.append(" -");
        buff.append("\n   ");
        for (int x=0; x<width; x++) {
            buff.append(heights[x]);
            buff.append(',');
        }
		return(buff.toString());
	}

	private void testPlaceDirectly(Piece p, int x, int y) {
	    place(p, x, y);
	    commit();
    }

	public static void main(String[] args) {
	    Board b = new Board(10, 10);
        Piece L = new Piece(Piece.L1_STR);
        Piece seven = L.computeNextRotation().computeNextRotation();
        Piece stk = new Piece(Piece.STICK_STR);
        Piece stkR = stk.computeNextRotation();
        Piece sq = new Piece(Piece.SQUARE_STR);

        System.out.println("drop L on x = 0, get y: " + b.dropHeight(L, 0));
//        System.out.println("drop L on x = 0, get y: " + b.dropHeight(L, 0));
//        System.out.println("drop seven on x = 1, get y: " + b.dropHeight(seven, 1));
//        System.out.println("drop stkR on x = 2, get y: " + b.dropHeight(stkR, 2));
    }
}


