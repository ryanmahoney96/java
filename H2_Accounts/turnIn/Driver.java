
/*
javac -cp ".;.\YahooFinanceAPI-3.6.0.jar" *.java
java -cp ".;.\YahooFinanceAPI-3.6.0.jar" Driver
*/

import java.util.ArrayList;
import java.util.Collections;
import java.text.DecimalFormat;

public class Driver {

    public static void main(String[] args) {
            
        DecimalFormat money = new DecimalFormat("0.00");

        SavingsAccount s = new SavingsAccount("123456789", "Ryan", "Mahoney", "My House", 1024, 1, 1, 1, 2014);
        s.makeDeposit(100, 1, 2, 2018);
        s.makeDeposit(30.00, 2, 1, 2018);
        s.makeWithdrawal(12.00, 3, 3, 2018);
        s.makeDeposit(200, 4, 5, 2017);
        s.makeWithdrawal(600.00, 6, 5, 2017);
        s.makeWithdrawal(400.00, 4, 3, 2017);
        s.makeWithdrawal(250.00, 4, 3, 2019);

        //generates a report using the account user and account type to create the file name
        s.generateReport(1, 1, 2017);

        SavingsAccount n = new SavingsAccount("1234", "Bryan", "Baloney", "His House", 512, 12, 1, 1, 1996);
        n.makeDeposit(10, 1, 2, 2018);
        n.makeDeposit(300.00, 2, 1, 2018);
        n.makeWithdrawal(102.00, 3, 3, 2018);
        n.makeDeposit(20, 4, 5, 2017);
        n.makeWithdrawal(60.00, 6, 5, 2017);
        n.makeWithdrawal(4.00, 4, 3, 2017);
        n.makeWithdrawal(50.00, 4, 3, 2019);

        n.generateReport(1, 1, 2017);

        //in the case of a stocks account, buying stocks is done by indicating the ticker symbol, price per share, shares bought, and the date as MM/DD/YYYY
        EquityAccount e = new EquityAccount("987654321", "Ryan", "Mahoney", "My House");
        e.buyStocks("MSFT", 64.01, 10, 3, 12, 2016);
        e.buyStocks("AAPL", 138.96, 10, 3, 3, 2016);
        e.buyStocks("GOOG", 857.34, 10, 5, 3, 2016);
        e.buyStocks("BUD", 32.00, 10, 1, 3, 2016);
        e.buyStocks("INTC", 99.33, 10, 1, 3, 2016);
        e.buyStocks("HOG", 7.34, 10, 11, 3, 2016);
        e.buyStocks("WMT", 57.34, 10, 1, 1, 2017);

        e.generateReport(1, 1, 2017);

        //gathering the accounts into one list
        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(s);
        accounts.add(n);
        accounts.add(e);

        //sorting the accounts from most valuable (gross value) to least
        sortAccounts(accounts);

        //print out the accounts according to value
        printAccounts(accounts);       
    }

    //sort by using the abstract getValue method of an account
    public static void sortAccounts(ArrayList<Account> a){
        for(int i = 0; i < a.size(); i++){
            for(int j = i; j < a.size(); j++){
                if(a.get(i).getValue() < a.get(j).getValue()){
                    Collections.swap(a, j, i);
                }
            }
        }
    }

    //print out some information and the values of the accounts
    public static void printAccounts(ArrayList<Account> accounts){
        DecimalFormat money = new DecimalFormat("0.00");
        
        for(Account a : accounts){
            StringBuilder sb = new StringBuilder(a.getLast());
            sb.append(", ");
            sb.append(a.getFirst());
            sb.append(" ");
            sb.append(a.getAccountType());
            sb.append(" Account, Value $");
            sb.append(money.format(a.getValue()));
            System.out.println(sb.toString());
        } 
    }
}



