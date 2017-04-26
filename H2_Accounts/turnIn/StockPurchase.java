
import yahoofinance.YahooFinance;
import yahoofinance.Stock;
import java.time.LocalDate;
import java.io.IOException;
import java.text.DecimalFormat;
 
public class StockPurchase {

    private Stock stock;
    private String name;
    private String tickerSymbol;
    private double pricePerShare;
    private int numShares;
    private LocalDate timestamp;
    //timestamp

    //Each stock purchase has a ticker symbol, purchase price per share, and number of shares at that price.
    public StockPurchase (String s, double pps, int ns, int year, int month, int day){

        try {
            timestamp = LocalDate.of(year, month, day);
            //gets the most recent information on the stock -given a ticker symbol- that can be found
            stock = YahooFinance.get(s);

            //if the stock exists
            if(stock != null){
                name = stock.getName();
                tickerSymbol = s;
                pricePerShare = pps;
                numShares = ns;
                timestamp = LocalDate.of(year, month, day);
            }
            else {
                System.out.println("Invalid Ticker Symbol");
            }
        }
        catch (IOException ioe){
            System.out.println("Error in Yahoo Finance API");
        }
    
    }

    public String getName(){
        return name;
    }

    public String getTickerSymbol(){
        return tickerSymbol;
    }

    //gets the price per share when bought
    public double getPricePerShare(){
        return pricePerShare;
    }

    //get the number of shares bought
    public int getNumShares(){
        return numShares;
    }

    //calculate the value of this purchase using the price and quantity
    public double getValue(){
        return pricePerShare * numShares;
    }

    public LocalDate getTimestamp(){
        return timestamp;
    }

    //exports the stock purchase as a string full of information
    public String exportStockPurchase(){
        
        DecimalFormat money = new DecimalFormat("0.00");
        StringBuilder t = new StringBuilder("<p>");        
        t.append(name);
        t.append(" (");
        t.append(tickerSymbol);
        t.append(") Stock Bought on ");
        t.append(timestamp.getMonth().toString());
        t.append(" ");
        t.append(timestamp.getDayOfMonth());
        t.append(", ");
        t.append(timestamp.getYear());
        t.append(": ");
        t.append(numShares);
        t.append(" Shares Bought at $");
        t.append(money.format(pricePerShare));
        t.append(" ($");
        t.append(money.format(getValue()));
        t.append(" total value)<br></p>");   

        return t.toString();
    }


}