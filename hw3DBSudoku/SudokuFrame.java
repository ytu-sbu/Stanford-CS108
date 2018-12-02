import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

/**
 * creates a GUI for Sudoku class, user inputs a sudoku in String format, click check button, will get the solution
 * displayed on right panel. If the auto check option is selected, as soon as the input string has been edited, the solution
 * will computed automatically.
 */
 public class SudokuFrame extends JFrame {
     private final JTextArea leftText;
     private final JTextArea rightText;
     private final JButton checkB;
     private final JCheckBox autoCheckB;

     private Sudoku sudoku;

     public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		setLayout(new BorderLayout(4, 4));
//		Container contentPane = this.getContentPane();

        leftText = new JTextArea(15, 20);
        leftText.setBorder(new TitledBorder("Puzzle"));
        rightText = new JTextArea(15, 20);
        rightText.setBorder(new TitledBorder("Solution"));
        add(leftText, BorderLayout.WEST);
        add(rightText, BorderLayout.EAST);
        rightText.setEditable(false);
        leftText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (autoCheckB.isSelected()) {
                    solveSudoku();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (autoCheckB.isSelected()) {
                    solveSudoku();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (autoCheckB.isSelected()) {
                    solveSudoku();
                }
            }
        });

        checkB = new JButton("Check");
        checkB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        autoCheckB = new JCheckBox("Auto Check");
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.add(checkB);
        p2.add(autoCheckB);
        add(p2, BorderLayout.SOUTH);

		// Could do this:
		// setLocationByPlatform(true);

        setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void solveSudoku() {
	    try {
            sudoku = new Sudoku(Sudoku.textToGrid(leftText.getText()));
            int count = sudoku.solve();
            rightText.setText(sudoku.getSolutionText());
            rightText.append("\nsolutions:" + count);
            rightText.append("\nelapsed:" + sudoku.getElapsed() + "ms");
        }
        catch (RuntimeException e) {
	        rightText.setText("Parsing problem");
        }
    }
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}
}
