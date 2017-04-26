
//this is an exception used by the Profiler to indicate what kind of error occurred while starting/stoppping timers
public class ProfilerException extends Exception{
    
    public ProfilerException(){
        super();
    }
    public ProfilerException(String message){
        super(message);
    }
}