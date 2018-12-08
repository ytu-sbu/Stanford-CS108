// Buffer.java
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.*;

/*
 Holds the transactions for the worker
 threads.
*/
public class Buffer {
	public static final int SIZE = 64;
	private Deque<Transaction> buffer;
	Semaphore canAdd, canRemove;

	public Buffer() {
	    buffer = new LinkedList<>();
	    canRemove = new Semaphore(0);
	    canAdd = new Semaphore(SIZE);
    }
	
	// YOUR CODE HERE
    /**
     * add a Transaction to the end of the buffer, use Semaphore to implement a lock
     * @param tran the Transaction record to be added
     */
	public void add(Transaction tran) {
	    try {
	        canAdd.acquire();
        }
        catch (InterruptedException ignored) { }
        synchronized (buffer) {
//	        System.out.println("add to buffer: " + tran);
            buffer.add(tran);
        }
	    canRemove.release();
    }

    /**
     * @return a Transaction from the front of the buffer
     */
    public Transaction remove() {
	    try {
            canRemove.acquire();
        }
        catch (InterruptedException ignored) { }
        Transaction tran;
        synchronized (buffer) {
//            System.out.print("remove from buffer: ");
	        tran = buffer.remove();
//	        System.out.println(tran);
        }
        canAdd.release();
        return tran;
    }
}
