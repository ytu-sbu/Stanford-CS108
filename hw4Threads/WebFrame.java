import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Semaphore;

/**
 * Provide a txt file contains urls to the constructor like: WebFrame wf = new WebFrame("links.txt");
 * WebFrame will read all the urls and show on frame. After the "Single Thread Fetch" or "Concurrent Fetch"
 * has been clicked, WebFrame try to get HTML file from each url and update the results on frame. The status
 * could be successful (showing the time, time cost, file size), err, or interrupt. If the "Single Thread Fetch" was
 * clicked, only one thread will be dispatched, if the "Concurrent Fetch" was clicked, multiple thread will be dispatched,
 * and the number of threads is defined on frame. A "Stop" button could stop the loading during any point in the process.
 */
public class WebFrame extends JFrame {
    private final DefaultTableModel model;
    private final JButton single, multiple, stop;
    private final JLabel run, complete, elapse;
    AtomicInteger runThread, completeThread;
    private final JTextField fetchNum;
    private final JProgressBar progress;
    private int urlNum = 0;
    Launcher launcher;
    private byte[] interruptLock = new byte[1];

    /**
     * Launcher thread dispatches worker-threads after click single thread fetch button or
     * concurrent fetch button. In the former case, only one worker-thread will be dispatched,
     * while in the second case, multiple threads will be dispatched. The max concurrent thread
     * number is defined by the number set in JTextField fetchNum by user.
     */
    class Launcher extends Thread {
        private AtomicInteger urlRow;
        Semaphore permits;
        private int concurNum;
        private ArrayList<Thread> threadPool;

        /**
         * Launcher constructor, takes the
         * @param concurNum as input, which defines the max allowable number of threads could be dispatched.
         */
        public Launcher(int concurNum) {
            this.concurNum = concurNum;
            urlRow = new AtomicInteger(0);
            runThread = new AtomicInteger(1);
            completeThread = new AtomicInteger();
            setRunningThreadNumber();
            permits = new Semaphore(concurNum + 1);
            threadPool = new ArrayList<>(urlNum + 5);
        }

        /**
         * get the url in String format from the GUI component TableModel
         * @param row specify the index of the url will be take
         * @return the url in String format
         */
        private String getURL(int row) {
            final String[] url = new String[1];
            // make it Thread safe
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        url[0] = (String) model.getValueAt(row, 0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("failed to get the url");
            }
            return url[0];
        }

        /**
         * the Launcher Thread dispatches worker threads. If it is interrupted, it should stop
         * dispatching more threads and interrupts all the worker-threads which have been started.
         */
        @Override
        public void run() {
            // start timing
            long startTime = System.currentTimeMillis();

            // dispatch all webWorkers
            try {
                for (int i = 0; i < urlNum; i++) {
                    // check if it is interrupted before start a new worker thread
                    if (isInterrupted()) {
                        throw new java.lang.InterruptedException("stop dispatch new thread");
                    }
                    // get url
                    int row = urlRow.getAndIncrement();
                    String url = getURL(row);
                    // create a new worker and start it
                    synchronized (interruptLock) {
                        WebWorker worker = new WebWorker(url, row, WebFrame.this);
                        worker.start();
                        threadPool.add(worker);
                    }
                }

            // wait for all workers finish
                permits.acquire(concurNum);
            }
            catch (java.lang.InterruptedException e) {
                for (Thread worker : threadPool) {
                    worker.interrupt();
                }
            }
            finally {
                // all worker-threads terminated, setups frame status and quite
                final long period = System.currentTimeMillis() - startTime;
                // setup the elapsed time
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        elapse.setText("Elapsed: " + Float.toString(period / 1000) + " S");
                    }
                });
                runThread.decrementAndGet();
                setRunningThreadNumber();
                enableFetch(true);
            }
        }
    }

    /**
     * After one task has finished, increase the completed task number
     * update the ProgressBar by one
     */
    void completeAndUpdate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                complete.setText("Completed:" + completeThread.incrementAndGet());
                progress.setValue(completeThread.get());
            }
        });
    }

    /**
     * set the content in a specified block on the table
     * @param status the content
     * @param row
     * @param col
     */
    void setTableValues(String status, int row, int col) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                model.setValueAt(status, row, col);
            }
        });
    }

    /**
     * update the Running Thread Number on frame
     */
    void setRunningThreadNumber() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int runNum = runThread.get();
                if (runNum > urlNum + 1 || runNum < 0) {
                    throw new java.lang.RuntimeException();
                }
                run.setText("Running: " + Integer.toString(runThread.get()));
            }
        });
    }


    /**
     * take a File name and generate a WebFrame GUI frame
     * @param fileName
     */
    public WebFrame(String fileName) {
        super("WebLoader");

        // set up the Look and Feel according to the OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored) { }

        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        // setup a table
        model = new DefaultTableModel(new String[] {"url", "status"}, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600, 300));
        JPanel panel = new JPanel();
        panel.add(scrollpane);
        add(panel);

        // setup the table content from a File
        File fLink = new File(fileName);
        try (Scanner sc = new Scanner(fLink)) {
            while (sc.hasNextLine()) {
                urlNum += 1;
                model.addRow(new String[] {sc.nextLine()});
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // defines the single button
        single = new JButton("Single Thread Fetch");
        single.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // start a launcher with one thread capability
                startLauncher(1);
            }
        });
        add(single);

        // defines multiple button
        multiple = new JButton("Concurrent Fetch");
        multiple.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // start a launcher with defined thread capability
                int threadNum = Integer.parseInt(fetchNum.getText());
                startLauncher(threadNum);
            }
        });
        add(multiple);

        // defines fetchNum textField
        fetchNum = new JTextField("1");
        fetchNum.setMaximumSize(new Dimension(100,20));
        add(fetchNum);

        // setup frame status components
        run = new JLabel("Running: 0");
        add(run);
        complete = new JLabel("Completed: 0");
        add(complete);
        elapse = new JLabel("Elapsed: ");
        add(elapse);
        progress = new JProgressBar(0, urlNum);
        add(progress);

        // defines stop button
        stop = new JButton("Stop");
        stop.setEnabled(false);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // interrupt worker thread
                synchronized (interruptLock) {
                    launcher.interrupt();
                }
                // set buttons status
                enableFetch(true);
            }
        });
        add(stop);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * setup the frame and start a launcher configured by the
     * @param threadNum the number of threads the launcher could dispatch
     */
    private void startLauncher(int threadNum) {
        enableFetch(false);
        reset();
        launcher = new Launcher(threadNum);
        launcher.start();
    }

    /**
     * enable two fetch button and disable stop button
     * @param enable
     */
    private void enableFetch(boolean enable) {
        // disable single and multiple fetch Buttons
        single.setEnabled(enable);
        multiple.setEnabled(enable);
        // enable stop button
        stop.setEnabled(!enable);
    }

    /**
     * make the frame set to initial status
     */
    private void reset() {
        run.setText("Running: 0");
        complete.setText("Completed: 0");
        elapse.setText("Elapsed: 0");
        progress.setMaximum(urlNum);
        progress.setValue(0);
        for (int i = 0; i < urlNum; i++) {
            model.setValueAt("", i, 1);
        }
    }

    public static void main(String[] args) {
        WebFrame wf = new WebFrame("links.txt");
    }
}
