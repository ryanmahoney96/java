
import yahoofinance.YahooFinance;
import yahoofinance.Stock;
import yahoofinance.histquotes.Interval; 
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

public class EquityAccount extends Account {

/*
Account + a list of stock purchases, and the total value of the stock portfolio.  
*/

    private ArrayList <StockPurchase> stocks;
    private Double mostRecentEvaluation;

    public EquityAccount (String a, String f, String l, String m){

        super(a, f, l, m);

        mostRecentEvaluation = 0.0;

        stocks = new ArrayList <StockPurchase>();
        
    }

    //Each stock purchase has a stock name, ticker symbol, purchase price per share, and number of shares at that price.
    public void buyStocks(String s, double pps, int ns, int month, int day, int year){
        
        LocalDate ld = LocalDate.of(year, month, day);
        StockPurchase sp = new StockPurchase(s, pps, ns, year, month, day);

        int i = 0;

        //leverage the isBefore method to sort the transactions according to timestamp
        for(; i < stocks.size(); i++){
            if(ld.isBefore(stocks.get(i).getTimestamp())){
                break;
            }
        }

        stocks.add(i, sp);
    }

    //iterates through the list of Stock Purchases and adds the worth of each to a return value
    public double getValue(){

        double value = 0.0;

        if(!mostRecentEvaluation.equals(0.0)){
            value = mostRecentEvaluation;
        }

        else {
            //if we have not generated a report yet, we base the value off of the purchase price (assuming the value sits around that value)
            for(StockPurchase sp : stocks){
                value += sp.getValue();
            }
        }

        return value;
    }

    public String getAccountType(){
        return "Stock";
    }

/*
Our client needs to generate reports that include the account number, the name and primary address of the account holder, a list of all stock information, and a total value for the account. You should grab the current values of stocks from an internet service like Yahoo!. Every report will have a date that it was generated.
*/
    
    //generate a report for today
    public void generateReport(){
        LocalDate today = LocalDate.now();
        generateReport(today.getMonthValue(), today.getDayOfMonth(), today.getYear());
    }

    public void generateReport(int month, int day, int year){
        
        //the date of the report
        LocalDate reportDate = LocalDate.of(year, month, day);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        //use a stringbuilder to put together the html file
        StringBuilder sb = new StringBuilder(htmlTop);
        //format to look like money
        DecimalFormat money = new DecimalFormat("0.00");

        double temp_value = 0;
        double profit = 0;

        Stock stock;
        double quotePrice;
        double quoteValue;
        int numShares;

        sb.append("<p>Stock Report for Account ");
        sb.append(super.getAccNum());
        sb.append("</p><p>Name of the Account Holder: ");
        sb.append(super.getLast());
        sb.append(", ");
        sb.append(super.getFirst());
        sb.append("</p><p>Address of the Account Holder: ");
        sb.append(super.getMailAddr());
        sb.append("</p>");

        sb.append("<table>");

        for(StockPurchase s: stocks){
            
            //if the timestamp of the stock purchase comes before or on the date of the report, proceed
            if(!s.getTimestamp().isAfter(reportDate)){
                try {
                    //get the historical value of the stock using yahoo 
                    stock = YahooFinance.get(s.getTickerSymbol(), cal, cal, Interval.DAILY);
                    quotePrice = stock.getQuote().getPrice().doubleValue();

                    //use the number of shares purchased and the value according to yahoo to generate a total value for the stock
                    numShares = s.getNumShares();
                    quoteValue = quotePrice * numShares;

                    sb.append("<tr><td>");
                    sb.append(s.exportStockPurchase());
                    sb.append("</td></tr>");
            
                    //add the value of this stock purchase to the temporary value of the whole account
                    temp_value += quoteValue;

                    sb.append("<tr><td>    -New Value Per Stock: $");

                    sb.append(money.format(quotePrice));
                    sb.append(", New Value of Shares: $");
                    sb.append(money.format(quoteValue));
                    sb.append(", ");

                    //determine if money was made or lost on the stock purchase based on the original purchase
                    if(quoteValue >= s.getValue()){
                        sb.append("Profit of $");
                        sb.append(money.format(quoteValue - s.getValue()));
                        profit += quoteValue - s.getValue();
                    }
                    else {
                        sb.append("Loss of $");
                        sb.append(money.format(s.getValue() - quoteValue));
                        profit += quoteValue - s.getValue();
                    }
                    
                    sb.append("</td></tr>");
                }

                catch(IOException ioe){
                    System.out.println("Error in Yahoo Finance API");
                }
            }
            //do not continue printing the stocks past the report date
            else {
                break;
            }
        }

        sb.append("</table>");

        sb.append("<p>Current Portfolio Value: $");
        mostRecentEvaluation = temp_value;
        sb.append(money.format(temp_value));
        sb.append(", Total ");
        
        //print the total gain or loss on the account up to the report date
        if(profit > 0){
            sb.append("Profit of $");
        }
        else {
            sb.append("Loss of $");
        }

        sb.append(money.format(profit));

        sb.append("<hr>");

        sb.append("Report Generated on ");
        sb.append(reportDate.getMonth());
        sb.append(" ");
        sb.append(reportDate.getDayOfMonth());
        sb.append(", ");
        sb.append(reportDate.getYear());

        sb.append(htmlBottom);

        //write the string builder content to an html file
        super.writeFile("Stock", sb.toString());

    }


}