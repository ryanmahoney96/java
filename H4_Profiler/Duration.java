
//this class is used to store the start and end times of a timer, along with any messages that may have been added to these times
public class Duration {

    //these times are stored as longs, gathered from the System.nanotime() method
    private long startTime;
    private long endTime;
    private String startMessage;
    private String endMessage;

    public Duration (){
        startTime = 0;
        endTime = 0;
        startMessage = "";
        endMessage = "";
    }

    public void setStart(long ns){
        startTime = ns;
    }
    public long getStart(){
        return startTime;
    }
    
    public void setEnd(long ns){
        endTime = ns;
    }
    public long getEnd(){
        return endTime;
    }
    
    public void setStartMessage(String msg){
        startMessage = msg;
    }
    public String getStartMessage(){
        return startMessage;
    }
    
    public void setEndMessage(String msg){
        endMessage = msg;
    }
    public String getEndMessage(){
        return endMessage;
    }

    //get the difference between this duration's start and end values (gives the number of nanoseconds the timer ran)
    public long getDifference(){
        return getEnd() - getStart();
    }
}