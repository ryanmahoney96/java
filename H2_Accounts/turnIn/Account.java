
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

abstract public class Account {

    //account number, a first and last name for the account holder, a primary mailing address of the account holder
    private String accNum;
    private String first;
    private String last;
    private String mailAddr;

    //only classes that inherit from Account may use these (used for html file building)
    protected static String htmlTop = "<!DOCTYPE html><html><head><title>Report</title></head><body>";
    protected static String htmlBottom = "</body></html>";

    //every account will inherit these traits, but may have more according to their type
    public Account (String a, String f, String l, String m){

        setAccNum(a);
        first = f;
        last = l;
        mailAddr = m;
    }
    
    public void setAccNum(String a){
        //make sure they are all numbers
        if (a.matches("[0-9]+")) {
            accNum = a; 
        }
        else {
            System.out.println("Error: Account Number must be all Digits");
        }
    }
    public String getAccNum(){
        return accNum;
    }

    public void setFirst(String f){
        first = f;
    }
    public String getFirst(){
        return first;
    }

    public void setLast(String l){
        last = l;
    }
    public String getLast(){
        return last;
    }

    public void setMailAddr(String m){
        mailAddr = m;
    }
    public String getMailAddr(){
        return mailAddr;
    }

    public void writeFile(String reportType, String report){
         try {
            //build the file name using the user's full name and the type of account
            StringBuilder filename = new StringBuilder(first);
            filename.append(last);
            filename.append(reportType);
            filename.append("Report.html");

            FileOutputStream fos = new FileOutputStream(filename.toString());
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            //take each character of the report string and output it to a file
            for(char c: report.toCharArray()){
                bos.write(c);
            }

            bos.close();
        }

        catch (FileNotFoundException f){
            System.out.println("Error, File not Found");
        }
        
        catch (IOException i){
            System.out.println("Error, could not perform IO Operation");
        }
    }

    //an abstract method is declared in a parent class. When another class extends it, that class MUST specify some functionality to the method or the program will refuse to compile
    //generate a report on the account including their personal information and a record of their transactions given a date
    abstract public void generateReport();
    abstract public void generateReport(int m, int d, int y);
    //get the value of the account based on the balance or the most recently recorded value of the stocks
    abstract public double getValue();
    //simply return the account type
    abstract public String getAccountType();

}