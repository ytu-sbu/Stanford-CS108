// CommandLineInterface.java
/*
 Provided class that presents a command-line interface for the DB classes.
 It does simple i/o with the console and sends messages to the DB
 to do all the work. All the code is in main().
 Edit the code so it interfaces with the DB classes.
*/

import java.io.*;

public class CommandLineInterface {
	public static void main(String[] args) {

		DBTable db = new DBTable();	// DBTable to use for everything
        int length, selectedLength;
		
		try {
			// Create reader for typed input on console
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;
			
			while (true) {
			    /*
				int length = 0;
				int selectedLength = 0;
				*/
				// YOUR CODE HERE
                length = db.getRecordNum();
                selectedLength = db.getSelectNum();

				System.out.println("\n" + length + " records (" + selectedLength + " selected)");
				System.out.println("r read, p print, sa select and, so select or, da ds du delete, c clear sel");
				System.out.print("db:");
				line = reader.readLine().toLowerCase();
				
				if (line.equals("r")) {
					System.out.println("read");
					String fname;
					System.out.print("Filename:");
					fname = reader.readLine();
					// YOUR CODE HERE
                    db.read(fname);
				}
				else if (line.equals("p")) {
					System.out.println("print");
					// YOUR CODE HERE
                    db.print();
				}
				else if (line.equals("da")) {
				System.out.println("delete all");
					// YOUR CODE HERE
                    db.deleteAll();
				}
				else if (line.equals("ds")) {
					System.out.println("delete selected");
					// YOUR CODE HERE
                    db.deleteSel();
				}
				else if (line.equals("du")) {
					System.out.println("delete unselected");
					// YOUR CODE HERE
                    db.deleteUnsel();
				}
				else if (line.equals("c")) {
					System.out.println("clear selection");
					/// YOUR CODE HERE
                    db.clear();
				}
				else if (line.equals("so") || line.equals("sa")) {
					if (line.equals("so")) System.out.println("select or");
					else System.out.println("select and");
					
					System.out.print("Criteria record:");
					String text = reader.readLine();  // get text line from user
					// YOUR CODE HERE
                    if (line.equals("so")) {
                        db.searchInOr(text);
                    }
                    if (line.equals("sa")) {
                        db.searchInAnd(text);
                    }
				}
				else if (line.equals("q") || line.equals("quit")) {
					System.out.println("quit");
					break;
				}
				else {
					System.out.println("sorry, don't know that command");
				}
			}
		}
		catch (IOException e) {
			System.err.println(e);
		}
	}
}
