// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
    public static final int DEFAULTBALANCE= 1000;	 // number of accounts
	private Buffer buffer;
	private ArrayList<Account> accounts;
	private ArrayList<BadTransaction> badTrans = null;
	private boolean limitMode = false;
	private int limit = Integer.MIN_VALUE;

	private class Worker implements Runnable {
	    @Override
        public void run() {
	        while (true) {
	            Transaction trans = buffer.remove();
	            if (trans == null) {
	                break;
                }
                Account from = accounts.get(trans.from);
	            from.withdraw(trans.amount);
	            Account to = accounts.get(trans.to);
	            to.deposit(trans.amount);
				if (limitMode) {
					int bal = from.getBalance();
					if (bal <= limit) {
						badTrans.add(new BadTransaction(from.getId(), to.getId(), trans.amount, bal));
					}
				}
            }
        }
    }

    private class BadTransaction extends Transaction {
		private int balance;

		public BadTransaction(int from, int to, int amount, int bal) {
			super(from, to, amount);
			this.balance = bal;
		}

		@Override
		public String toString() {
			return super.toString() + " bal:" + balance;
		}
	}

	public Bank() {
	    buffer = new Buffer();
	    accounts = new ArrayList<>(ACCOUNTS);
	    // initialize the accounts
	    for (int i = 0; i < ACCOUNTS; i++) {
	        accounts.add(i,new Account(this, i, DEFAULTBALANCE));
        }
    }

    public static Bank createNormalBank() {
		return new Bank();
	}

    public static Bank createLimitedBank(int limit) {
		Bank bank = new Bank();
		bank.badTrans = new ArrayList<>();
		bank.limitMode = true;
		bank.limit = limit;
		return bank;
	}

	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount
				// YOUR CODE HERE
                buffer.add(new Transaction(from, to, amount));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

    /**
     * add a null to the buffer for every workers
     * @param numWorkers the number of workers
     */
	private void addNulls(int numWorkers) {
	    for (int i = 0; i < numWorkers; i++) {
	        buffer.add(null);
        }
    }

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) {
	    // start numWorkers Worker Thread
	    for (int i = 0; i < numWorkers; i++) {
	        new Thread(new Worker()).start();
        }
        readFile(file);
        addNulls(numWorkers);
        try {
            buffer.canAdd.acquire(Buffer.SIZE);
        }
        catch (InterruptedException ignored) { }
        printAccounts();
        if (limitMode) {
        	printBadTrans();
		}
	}

	private void printAccounts() {
	    for (Account account : accounts) {
	        System.out.println(account);
        }
    }

    private void printBadTrans() {
		System.out.println("Bad transactions...");
		for (BadTransaction bt : badTrans) {
			System.out.println(bt);
		}
	}
	
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}
		
		// YOUR CODE HERE
		Bank bank;
		if (args.length >= 3) {
			bank = Bank.createLimitedBank(Integer.parseInt(args[2]));
		}
		else {
			bank = Bank.createNormalBank();
		}
        bank.processFile(file, numWorkers);
	}
}

