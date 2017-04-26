
import java.util.ArrayList;
import java.time.LocalDate;
import java.io.IOException;
import java.text.DecimalFormat;

public class SavingsAccount extends Account {

/*
Account + a balance, and an annual interest rate. One can make deposits and withdrawals from a savings account. Savings accounts earn income over time. These accounts earn interest once a year on January 1st. 
*/

    private double balance;
    private double interest;

    //a list of all transactions made on this account
    private ArrayList <Transaction> transactions;

    //account num, first name, last name, mail address, balance, interest, and the date of opening the account
    public SavingsAccount (String a, String f, String l, String m, int b, int i, int month, int day, int year){
        
        //calling the parent constructor to fill in the data it holds
        super(a, f, l, m);
        balance = 0;
        setInterestRate(i);

        transactions = new ArrayList <Transaction>();

        //the initial deposit should be a very early date (when the account was opened)
        makeDeposit(b, month, day, year);

    }

    //get the current balance left on the account on today's date
    public double getValue(){

        Transaction temp_transaction = null;
        Double temp_balance = 0.0;
        LocalDate reportDate = LocalDate.now();

        //get transactions up until report date (for every transaction until it goes past the report date)
        for(Transaction t: transactions){
            //check the last date processed and the current date and determine if interest would have Accumulated on jan1
            if(temp_transaction != null && (temp_transaction.getTimestamp().getYear() < t.getTimestamp().getYear()) && !t.getTimestamp().isAfter(reportDate)){
                int temp_year = temp_transaction.getTimestamp().getYear();
                int t_year = t.getTimestamp().getYear();
                for(; temp_year < t_year; temp_year++){
                    temp_balance = accumulateInterest(temp_balance);
                }
            }

            //if the timestamp comes before the report date, print
            if(!t.getTimestamp().isAfter(reportDate)){
                temp_balance += t.getAmount();
            }
            else {
                break;
            }

            //save the transaction for interest calculations
            temp_transaction = t;
        }

        return temp_balance;
    }
    //prints the balance as a dollar value
    public void printBalance(){
        System.out.printf("Balance is $%.2f", balance);
    }

    public double getInterestRate(){
        return interest;
    }
    //set the interest rate (must be positive, to ward off evil banks)
    public void setInterestRate(double i){
        if(i < 0){
            i = i * -1;
        }
        interest = i;
    }

    //return the new balance after interest is added to the previous balance
    public double accumulateInterest(double temp_balance){
        return (temp_balance + (temp_balance * interest * 0.01));
    }

    //make a deposit of 'a' dollars on a certain date
    public void makeDeposit(double a, int month, int day, int year){
        
        //make sure a is positive (otherwise this would SUBTRACT money from the balance)
        if(a < 0.0){
            a = a * -1.0;
        }
        makeTransaction(a, month, day, year);
    }

    //make a withdrawal of 'a' dollars
    public void makeWithdrawal(double a, int month, int day, int year){
       
        //make sure a is negative (otherwise this would ADD money to the balance)
        if(a > 0.0){
            a = a * -1.0;
        }
        makeTransaction(a, month, day, year);
    }

    //making a transaction is done by taking the amount (+/-) and the timestamp and adding it to the list of amounts (sorted low date -> high)
    private void makeTransaction(double a, int month, int day, int year){

        LocalDate ts = LocalDate.of(year, month, day);

        balance = balance + a;

        Transaction t = new Transaction(a, ts);

        int i = 0;

        //leverage the LocalDate.isBefore method to sort the transactions according to timestamp
        for(; i < transactions.size(); i++){
            if(ts.isBefore(transactions.get(i).getTimestamp())){
                break;
            }
        }
        //add the transaction at its sorted place
        transactions.add(i, t);
    }

    public String getAccountType(){
        return "Savings";
    }

    /*
    Our client needs to generate reports that include the account number, the name and primary address of the account holder, the current balance, information about all the transactions, the balance in the account after each compounding, and the annual interest rate. Every report will have a date that it was generated.
    */
    //generate a report for today
    public void generateReport(){
        LocalDate today = LocalDate.now();
        generateReport(today.getMonthValue(), today.getDayOfMonth(), today.getYear());
    }

    public void generateReport(int month, int day, int year){
        
        //the date of the report
        LocalDate reportDate = LocalDate.of(year, month, day);
        
        //use a stringbuilder to put together the html file
        StringBuilder sb = new StringBuilder(htmlTop);
        
        //format to look like money
        DecimalFormat money = new DecimalFormat("0.00");

        sb.append("<p>Savings Report for Account ");
        sb.append(super.getAccNum());
        sb.append("</p><p>Name of the Account Holder: ");
        sb.append(super.getLast());
        sb.append(", ");
        sb.append(super.getFirst());
        sb.append("</p><p>Address of the Account Holder: ");
        sb.append(super.getMailAddr());
        sb.append("</p>");

        sb.append("<table>");

        //this will hold a record of the last transaction processed (for its date)
        Transaction temp_transaction = null;
        Double temp_balance = 0.0;

        //get transactions up until report date (for every transaction until it goes past the report date)
        for(Transaction t: transactions){
            //check the last date processed and the current date and determine if interest would have Accumulated on jan1
            if(temp_transaction != null && (temp_transaction.getTimestamp().getYear() < t.getTimestamp().getYear()) && !t.getTimestamp().isAfter(reportDate)){
                int temp_year = temp_transaction.getTimestamp().getYear();
                int t_year = t.getTimestamp().getYear();
                for(; temp_year < t_year; temp_year++){
                    temp_balance = accumulateInterest(temp_balance);
                    sb.append("<tr><td>");
                    sb.append("Interest was Accumulated, Balance: $");
                    sb.append(money.format(temp_balance));
                    sb.append("</td></tr>");
                }
            }

            //if the timestamp comes before the report date, print
            if(!t.getTimestamp().isAfter(reportDate)){
                temp_balance += t.getAmount();
                sb.append("<tr><td>");
                sb.append(t.exportTransaction());
                sb.append("</td></tr>");
            }
            else {
                break;
            }

            //save the transaction for interest calculations
            temp_transaction = t;
        }

        sb.append("</table>");

        sb.append("<p>Current Balance: ");
        sb.append(money.format(temp_balance));

        sb.append("</p><p >Interest Compounded Annually: ");
        sb.append(interest);
        sb.append("%</p>");

        sb.append("<hr>");

        sb.append("Report Generated on ");
        sb.append(reportDate.getMonth());
        sb.append(" ");
        sb.append(reportDate.getDayOfMonth());
        sb.append(", ");
        sb.append(reportDate.getYear());

        sb.append(htmlBottom);

        //write to the html file using the string compiled above
        super.writeFile("Savings", sb.toString());

    }

}