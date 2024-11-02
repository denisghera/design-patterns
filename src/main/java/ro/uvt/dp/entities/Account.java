package ro.uvt.dp.entities;

import ro.uvt.dp.exceptions.InvalidAmountException;
import ro.uvt.dp.exceptions.InsufficientFundsException;
import ro.uvt.dp.services.Operations;
import ro.uvt.dp.services.Transfer;

import java.util.UUID;

public abstract class Account implements Operations, Transfer {
	protected String accountCode;
	protected double amount = 0;
	public enum TYPE {
		EUR, RON
	};

	protected Account(double initialAmount) throws InvalidAmountException {
		this.accountCode = UUID.randomUUID().toString();
		deposit(initialAmount);
	}
	@Override
	public double getTotalAmount() {
		return amount + amount * getInterest();
	}
	@Override
	public void deposit(double sum) throws InvalidAmountException {
		if (sum <= 0) {
			throw new InvalidAmountException("Cannot deposit a negative or zero sum.");
		}
		this.amount += sum;
	}
	@Override
	public void retrieve(double sum) throws InvalidAmountException, InsufficientFundsException {
		if (sum <= 0) {
			throw new InvalidAmountException("Cannot retrieve a negative or zero sum.");
		}
		if (this.amount < sum) {
			throw new InsufficientFundsException("Insufficient funds.");
		}
		this.amount -= sum;
	}
	@Override
	public abstract double getInterest();
	@Override
	public void transfer(Account targetAccount, double sum) throws InsufficientFundsException, InvalidAmountException {
		if (targetAccount == null) {
			throw new IllegalArgumentException("Invalid transfer details.");
		}
		if (sum <= 0 ) {
			throw new InvalidAmountException("Sum should be greater than 0.");
		}
		if (this.amount < sum) {
			throw new InsufficientFundsException("Insufficient funds for transfer.");
		}
		if(targetAccount.getClass() != this.getClass()) {
			throw new IllegalArgumentException("Accounts must be of the same type");
		}
		this.retrieve(sum);
		targetAccount.deposit(sum);
	}
	public String getAccountCode() {
		return accountCode;
	}
	@Override
	public String toString() {
		return "code=" + accountCode + ", amount=" + this.getTotalAmount();
	}
}