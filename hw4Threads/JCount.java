// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.event.*;

public class JCount extends JPanel {
    private JTextField text;
    private JLabel label;
    private JButton start, stop;
    private worker w;

	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// YOUR CODE HERE
		text = new JTextField("100000000");
//		text.set
		label = new JLabel("0");
		start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (w != null) {
					w.interrupt();
				}
				w = new worker();
				w.start();
			}
		});
		stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (w != null) {
					w.interrupt();
					w = null;
				}
			}
		});
		w = null;

        add(text);
        add(label);
        add(start);
        add(stop);
        add(Box.createVerticalStrut(40));
	}

	private class worker extends Thread {
		private int range;

		public worker() {
			 range = Integer.parseInt(text.getText());
		}

		@Override
		public void run() {
			for (int i = 0, j; i < range / 10000; i++) {
			    for (j = 0; j < 10000; j++) {
					Thread.yield();
					/*
			    	try {
						Thread.sleep(1);
					}
			    	catch (InterruptedException interrupt) {
			    	    break;
					} */
				}
			    if (this.isInterrupted() || j != 10000) {
			    	break;
				}
			    else {
			    	// set num final so that the anonymous class could get access to it
			        final int num = (i + 1) * 10000;
			        SwingUtilities.invokeLater(new Runnable() {
			        	public void run() {
							label.setText(String.valueOf(num));
						}
					});
				}
			}
		}

	}
	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

