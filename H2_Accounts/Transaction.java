
import java.text.DecimalFormat;
import java.time.LocalDate;

public class Transaction {

    //every transaction has an amount (+/-) and a date the transaction was made
    private double amount;
    private LocalDate timestamp;

    public Transaction (double a, LocalDate t){
        amount = a;
        timestamp = t;
    }

    public double getAmount(){
        return amount;
    }

    public LocalDate getTimestamp(){
        return timestamp;
    }

    //exporting the transaction returns a string of the timestamp, type of transaction, and amount
    public String exportTransaction(){
        StringBuilder t = new StringBuilder(timestamp.getMonth().toString());
        t.append(" ");
        t.append(timestamp.getDayOfMonth());
        t.append(", ");
        t.append(timestamp.getYear());
        

        DecimalFormat money = new DecimalFormat("0.00");
        if(amount > 0){
            t.append(": deposit of $");
        }
        else {
            t.append(": withdrawal of $");
        }

        t.append(money.format(Math.abs(amount)));   

        return t.toString();
    }

}



