// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
    private int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private Bank bank;  
	
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}

	public synchronized int getBalance() {
		return balance;
	}

	public synchronized int getId() {
		return id;
	}

	private synchronized void change(boolean add, int amount) {
	    if (add) {
	    	balance += amount;
		}
		else {
			balance -= amount;
		}
		transactions += 1;
	}

	public void deposit(int amount) {
	    change(true, amount);
    }

    public void withdraw(int amount) {
	    change(false, amount);
    }

    public String toString() {
	    return "acct:" + id + " bal:" + balance + " trans:" + transactions;
    }
}
