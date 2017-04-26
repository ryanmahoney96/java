
//import java.time.LocalTime;
import java.lang.System;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.UnsupportedLookAndFeelException;

public class Profiler {
    
    //this holds the reference to the one Profiler that will be allowed to exist. This is part of the singeleton pattern, allowing us to refer to one Profiler that contains all the timer information
    private static Profiler instance = null;

    //the hasmaps that hold the timers (an arraylist is used to store a history of duration objects) and the counters (the nuber of times a timer is called)
    private HashMap <String, ArrayList <Duration>> timers;
    private HashMap <String, Integer> counters;

    //whether or not the profiler is enabled 
    private static boolean enabled = true;

    //the singleton pattern requires the constructor to be private, and to include a method called getInstance. This makes it so that only one Profiler can/will exist
    private Profiler (){

        timers = new HashMap <String, ArrayList <Duration>>();
        counters = new HashMap <String, Integer>();
    }

    //if the Profiler has yet to be made, make one. This will be the one and only object of its type
    public static Profiler getInstance(){
        if(instance == null){
            instance = new Profiler();
        }

        return instance;
    }

    //start a timer
    public void start(String title, String message) throws ProfilerException {
        
        //if the profiler is turned off, don't start this function
        if(isEnabled()){

            //include the try catch block to move exception handling from main into here. This would make it so an improper timer call does not end the program
            //try {
                ArrayList <Duration> ref = timers.get(title);

                //this list has yet to be initialized
                if(ref == null){
                    ref = new ArrayList <Duration>();
                }

                //there is at least one timer in the arraylist
                if(ref.size() > 0){
                    //if the last called timer was stopped, we may add a new one
                    if(ref.get(ref.size() - 1).getEnd() > 0){
                        addStart(title, message, ref);
                    }
                    //else, the last timer was not stopped and we cannot start a new one
                    else {
                        //throw exception
                        throw new ProfilerException("Cannot Start Timer: Previous Timer Left Unstopped");
                    }
                }
                //because there are no timers started, we can start a new one without issue
                else {
                    addStart(title, message, ref);
                }
            //}
            //catch (ProfilerException pe){
            //    System.out.println(pe.getMessage());
            //}
        
        }
    }
    
    //this may be used if the user does not wish to include a message (would have been easier in c++ with default paramters...)
    public void start(String title) throws ProfilerException {

        start(title, "");
    }

    //helper function that actually adds a start time to our list
    private void addStart(String title, String message, ArrayList <Duration> ref){
      
        Duration d = new Duration();
      
        //the timer right now
        d.setStart(System.nanoTime());
        d.setStartMessage(message);

        //ref contains compounded information, so we need not worry about losing data
        ref.add(d);
        timers.put(title, ref);

        //if the counter hashmap has no value at the title, this is the first time the function has been timed
        if(counters.get(title) == null){
            counters.put(title, 0);
        }

        //add one to the count, because the timer has been called
        int temp = count(title);
        counters.put(title, ++temp);
    }

    //end the timer on the title method
    public void stop(String title, String message) throws ProfilerException {
       
        //do not continue if the profiler is disabled
        if(isEnabled()){

            //try {
                ArrayList <Duration> ref = timers.get(title);

                //this list has yet to be initialized, you can not end any timers
                if(ref == null){
                    throw new ProfilerException("Cannot End Timer: No Timer List Found");
                }

                //there must be a timer in the list
                if(ref.size() > 0){
                    //if the last entry has a valid start time, add the end to the timer
                    if(ref.get(ref.size() - 1).getStart() > 0){
                        addEnd(title, message, ref);
                    }
                    //else, you cannot end an unstarted timer
                    else {
                        throw new ProfilerException("Cannot End Timer: Invalid Start Value Detected");
                    }
                }
                else {
                    //the size is 0 or less, there are no timers to end. Throw exception
                    throw new ProfilerException("Cannot End Timer: No Timers to End");
                }
            //}
            //catch (ProfilerException pe){
            //    System.out.println(pe.getMessage());
            //}
        }
        
    }
    public void stop(String title) throws ProfilerException {

        stop(title, "");
    }

    private void addEnd(String title, String message, ArrayList <Duration> ref){
        
        //get the last added timer to add an end to it
        Duration d = ref.get(ref.size() - 1);
        d.setEnd(System.nanoTime());
        d.setEndMessage(message);
        timers.put(title, ref);
    }

    //return the number of times the title method has been called
    public int count(String title){

        return counters.get(title);
    }

    public static void setEnabled(boolean setting){
        enabled = setting;
    }

    public static boolean isEnabled(){
        
        return enabled;
    }

    /*
    Next, the profiler should be able to produce a report that lists the average time for each timer name, the longest and shortest time for each timer, and a detailed list of exact timer durations (with the message if there is one), and the counter information. The method should be called generateReport(). The report must be GUI-based and written with HTML in some swing components (https://docs.oracle.com/javase/tutorial/uiswing/components/html.html). Please strive to make this program polished. Make the report look clean and professional.
    */
    public void generateReport() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        if(isEnabled()){
            ProfilerGUI reportGUI = new ProfilerGUI(timers, counters);
        }
    }

}