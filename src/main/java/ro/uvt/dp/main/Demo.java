package ro.uvt.dp.main;

import ro.uvt.dp.commands.AccountOperationsInvoker;
import ro.uvt.dp.decorators.LifeInsuranceDecorator;
import ro.uvt.dp.decorators.RoundUpDecorator;
import ro.uvt.dp.entities.*;

import java.util.ArrayList;

import static ro.uvt.dp.entities.Account.TYPE.EUR;
import static ro.uvt.dp.entities.Account.TYPE.RON;

public class Demo {
    public static void main(String[] args) {
        try {
            Bank bank = Bank.getInstance("UVT123");
            AccountOperationsInvoker invoker = new AccountOperationsInvoker();

            Client client1 = bank.createClient("John","Str 1", new ArrayList<>());
            Client client2 = bank.createClient("Mary", "Str 2", new ArrayList<>());

            Account ronAccount1 = bank.createAccount(RON, 400, client1);
            Account eurAccount1 = bank.createAccount(EUR, 300, client1);
            Account ronAccount2 = bank.createAccount(RON, 1000, client2);

            System.out.println();

            eurAccount1.retrieveUsingCommand(invoker,100);
            bank.sendMessageToClient("You retrieved 100 EUR. New balance: " + eurAccount1.getTotalAmount(), client1);
            ronAccount2.depositUsingCommand(invoker,300);
            bank.sendMessageToClient("You deposited 300 RON. New balance: " + ronAccount2.getTotalAmount(), client2);

            System.out.println();

            RoundUpDecorator newRonAccount2 = new RoundUpDecorator(ronAccount2);
            bank.sendMessageToClient("You just opted for a Round Up account!", client2);

            System.out.println();

            newRonAccount2.transferUsingCommand(invoker, ronAccount1,100.3);
            client2.sendMessage("Hey John, I just transferred you the money for your insurance.");
            client2.sendMessage("You should get a notification from the bank pretty soon");
            bank.sendMessageToClient("Your new round up balance: " + String.format("%.2f", newRonAccount2.getRoundUpBalance()), client2);
            bank.sendMessageToClient("You just received 100.3 RON from Mary. New balance: " + String.format("%.2f", ronAccount1.getTotalAmount()),client1);

            System.out.println();

            LifeInsuranceDecorator newRonAccount1 = new LifeInsuranceDecorator(ronAccount1);
            bank.sendMessageToClient("You just opted for insurance!", client1);
            newRonAccount1.updateLifeInsurance(true, 5000);
            bank.sendMessageToClient("You are now insured with maximum reimbursement of " + newRonAccount1.getMaxInsurance(), client1);

            System.out.println();

            bank.sendMessageToAll("Day finished. Bank system going to maintenance...");

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
