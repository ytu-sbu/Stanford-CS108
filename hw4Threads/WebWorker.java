import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**
 * A WebWorker thread takes one url and try to loads the HTML from the server. According to the feedback
 * it sends message to WebFrame to update the status.
 */
public class WebWorker extends Thread {
    private String urlString;
    private int row;
    private WebFrame frame;

    public WebWorker(String url, int row, WebFrame frame) {
        urlString = url;
        this.row = row;
        this.frame = frame;
    }

    @Override
    public void run() {
        // get permit
        try {
            frame.launcher.permits.acquire();
        }
        catch (java.lang.InterruptedException e) {
            // setup Interrupted
            frame.setTableValues("interrupted", row, 1);
            return;
        }
        // increase the running thread number and setup on GUI
        frame.runThread.incrementAndGet();
        frame.setRunningThreadNumber();
        download();
        frame.runThread.decrementAndGet();
        frame.setRunningThreadNumber();
    }
/*
  This is the core web/download i/o code...
  */
    public void download() {
        long start = System.currentTimeMillis();
        InputStream input = null;
        StringBuilder contents = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            // Successful download if we get here
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String time = sdf.format(date);
            long period = System.currentTimeMillis() - start;
            frame.setTableValues(time + " " + Long.toString(period) + "ms " + contents.length() + " bytes", row, 1);
            frame.completeAndUpdate();
        }
        // Otherwise control jumps to a catch...
        catch (InterruptedException exception) {
            // YOUR CODE HERE
            // deal with interruption
            frame.setTableValues("interrupted", row, 1);
        }
        catch (IOException ignored) {
            frame.setTableValues("err", row, 1);
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }

            frame.launcher.permits.release();
        }
    }
}
