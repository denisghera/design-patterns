package ro.uvt.dp.main;

import ro.uvt.dp.entities.*;

import java.util.ArrayList;

import static ro.uvt.dp.entities.Account.TYPE.EUR;
import static ro.uvt.dp.entities.Account.TYPE.RON;

public class Demo {
    public static void main(String[] args) {
//        - implemented Singleton on Bank class
//        - implemented Factory Method for Account class
//        - implemented Builder pattern for Client class
//        - the rest of the code refactored to accommodate new implementations
//        - unit tests refactored to accommodate new implementations
//        - below is a demo
        try {
            Bank bank = Bank.getInstance("UVT123");
            System.out.println("Bank initial state: " + bank);

            Client client1 = bank.createClient("John","Str 1", new ArrayList<>());
            Client client2 = bank.createClient("Mary", "Str 2", new ArrayList<>());
            System.out.println("\nBank state after clients creation: " + bank);

            Account ronAccount1 = bank.createAccount(RON, 400, client1);
            Account eurAccount1 = bank.createAccount(EUR, 300, client1);
            Account ronAccount2 = bank.createAccount(RON, 1000, client2);
            System.out.println("\nBank state after accounts creation: " + bank);

            eurAccount1.retrieve(100);
            ronAccount2.deposit(300);
            ronAccount2.transfer(ronAccount1, 100);
            System.out.println("\nBank state after some operations: " + bank);

            eurAccount1.retrieve(200);
            client1.removeAccount(eurAccount1.getAccountCode());
            System.out.println("\nBank state after account removal: " + bank);

            System.out.println("\nDaily report at the end of the day:\n" + bank.generateDailyReport());

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
