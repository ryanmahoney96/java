

import javax.swing.UnsupportedLookAndFeelException;

public class Driver {

    public static void main (String [] args){

        //contains spec, contained for minimization in VScode
        if (false){/*

            Homework 4 asks you to create a code profiler using the singleton pattern. This code profiler will be used to measure the duration in time that sections of code take to execute. The profiler class should have a start() method to start a timer and a stop() method to stop the timer. In addition, the profiler will have a count() method to count how many times a section of code has been executed.

            Each of these methods will take a string that identifies the timer/counter. start() and stop() will take an optional message as well that is recorded with the duration. So, to measure how long a block of code takes to execute you will do this:

            //start the profiler 
            Profiler.getInstance().start("test timer");

            //do something interesting here

            //stop the profiler 
            Profiler.getInstance().stop("test timer");

            All pairs of start and stop calls must use the same string to identify an individual timer. The call to start and stop will record the current time. The difference between the two times is the amount of processing time. You should be able to use the same name multiple times to measure a block of code several times. For example, this code:

            for(int i = 0;i < 100;i++)
            {
                Profiler.getInstance().start("test timer", "Iteration: " + i);

                //do something interesting here

                Profiler.getInstance().stop("test timer" , "Iteration: " + i);
            }

            will record the body of the loop 100 times. Depending on what is happening in the loop the code might take different times to execute. The profiler will record all 100 durations.

            Sometimes, you want to know how many times a section of code has executed. The count() method will count the occurrences that this profiler method has been called. So, to count the number of occurrences of code you would do this:

            Profiler.getInstance().count("inside the foo() method");

            Occasionally, one might want to turn off profiling while the program is running. Include two methods, setEnabled(boolean) and isEnabled() that control whether the profiler records data.

            Next, the profiler should be able to produce a report that lists the average time for each timer name, the longest and shortest time for each timer, and a detailed list of exact timer durations (with the message if there is one), and the counter information. The method should be called generateReport(). The report must be GUI-based and written with HTML in some swing components (https://docs.oracle.com/javase/tutorial/uiswing/components/html.html). Please strive to make this program polished. Make the report look clean and professional.

            You should look at the Date, System, and HashMap classes.

            Things I am looking for:

            -> apply the singleton pattern from the video in a Profiler class
            -> have two versions of start() and stop()- one with an optional message one without
            -> be able to store the duration of time (and perhaps an optional message) for every single call to start and stop
            -> all calls to start() and stop() with the same identifier. If calls to start() and stop() are not strictly in order, throw a ProfilerException object
            -> include a count method that tracks how many times the passed in id has been executed
            -> implement the methods to turn on/off profiling 
            -> professional looking report
            -> use a sophisticated set of widgets (panels, tabs, layouts, etc.)
            -> use html in your widgets (tables, bold, italics, etc.)
            -> for each named timer show a summary of: average time, longest time, shortest time 
            -> for each named timer show the duration and optional message
            -> counter information
            -> a driver that tests all of the Profiler functionality

            I am not going to require a reflection this time but I would like everyone to submit a list of things that are NOT completed. It makes grading much easier if I know that you were not able to complete one or more features. 
            */
        }
        
        try {
            Profiler.getInstance().setEnabled(true);

            for(int i = 0; i < 30; i++){
                Profiler.getInstance().start("New Timer", "Start Iteration: " + i); 
                Profiler.getInstance().start("Second Timer", "Start Iteration: " + i);
                Profiler.getInstance().start("Third Timer", "Start Iteration: " + i);  
                Thread.sleep(10);
                Profiler.getInstance().stop("New Timer", "Stop Iteration: " + i);
                Profiler.getInstance().stop("Second Timer", "Stop Iteration: " + i);
                Profiler.getInstance().stop("Third Timer", "Stop Iteration: " + i);
            }
            
            Profiler.getInstance().generateReport();

        }
        //catch any exception involving the profiler specifically, displaying the exception details below
        catch(ProfilerException pe){
            System.err.println(pe.getMessage());
        }
        catch (InterruptedException ie){
            System.err.println("Error Sending Thread into Sleep");
        }
        catch (UnsupportedLookAndFeelException e) {
            System.err.println("Error getting UI Settings");
        }
        catch (ClassNotFoundException e){
            System.err.println("Error getting UI Settings");
        }
        catch (IllegalAccessException e) {
            System.err.println("Error getting UI Settings");
        }
        catch(InstantiationException e){
            System.err.println("Error getting UI Settings");
        }

    }

}
