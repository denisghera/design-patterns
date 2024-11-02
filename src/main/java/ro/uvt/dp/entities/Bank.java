package ro.uvt.dp.entities;

import ro.uvt.dp.accounts.AccountEURFactory;
import ro.uvt.dp.accounts.AccountRONFactory;
import ro.uvt.dp.exceptions.InvalidAmountException;
import ro.uvt.dp.exceptions.LimitExceededException;
import ro.uvt.dp.services.AccountFactory;
import ro.uvt.dp.services.ClientBuilderInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ro.uvt.dp.entities.Account.TYPE.EUR;
import static ro.uvt.dp.entities.Account.TYPE.RON;

public class Bank {
	private static Bank instance;
	public final static int MAX_CLIENTS_NUMBER = 100;
	private List<Client> clients;
	private String bankCode;
	private final Map<Account.TYPE, AccountFactory> accountFactories = new HashMap<>();

	private Bank(String bankCode) {
		this.bankCode = bankCode;
		clients = new ArrayList<>();
		accountFactories.put(RON, new AccountRONFactory());
		accountFactories.put(EUR, new AccountEURFactory());
	}

	public static synchronized Bank getInstance(String bankCode) {
		if (instance == null) {
			instance = new Bank(bankCode);
		}
		return instance;
	}
	public static Bank getInstance() throws Exception {
		if (instance == null) {
			throw new Exception("Bank not initialised!");
		}
		return instance;
	}

	public Account createAccount(Account.TYPE type, double initialAmount, Client client) throws InvalidAmountException, LimitExceededException {
		AccountFactory factory = accountFactories.get(type);
		if (factory == null) {
			throw new IllegalArgumentException("Unsupported account type: " + type);
		}
		Account account = factory.create(initialAmount);
		client.addAccount(account);
		return account;
	}

	public Client createClient(String name, String address, List<Account> accounts) throws LimitExceededException {
		ClientBuilder builder = new ClientBuilder();
		builder.setName(name).setAddress(address);
		for (Account account : accounts) {
			builder.addAccount(account);
		}
		Client client = builder.build();
		addClient(client);
		return client;
	}

	private void addClient(Client client) throws LimitExceededException {
		if (clients.size() >= MAX_CLIENTS_NUMBER) {
			throw new LimitExceededException("Bank has reached its maximum number of clients.");
		}
		clients.add(client);
	}

	public String generateDailyReport() {
		StringBuilder report = new StringBuilder();
		for (Client client : clients) {
			report.append("Actions for client ").append(client.getName()).append(":\n");
			report.append(client.getPersonalReport());
		}
		return report.toString();
	}

	public void resetBank() {
		clients = new ArrayList<>();
	}
	public String getBankCode() {
		return bankCode;
	}
	public String toString() {
		return "\nBank [bankCode=" + bankCode + ", clients=" + clients + "]";
	}
}