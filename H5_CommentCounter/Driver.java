
public class Driver {

    public static void main(String[] args) {

        //spec contained in order to clean up view in VSCode
        if(false){
            /*
            Homework 5 asks you to use the State pattern to create an application that will count the number of single line comment characters, multi-line comment characters, javadoc comment characters, and code characters there are in a Java file. The input should come from a file. The application should have a file chooser to select a file name and to start the analysis.

            You should parse the input one character at a time and an event should be generated from each character. The event should be handled by the underlying state machine that is part of your context class. Once all the text has been parsed, display the number of comment characters, javadoc comment characters, and code characters.

            Try and think about the different states that need to be created if you parse the text character by character. Obvious states are 'Code' and 'Comment'. When in the 'Code' state, if an 'i', 'n', and 't' are read in then you should increment the number of code characters for each character. However, when a '/' is read in it may be a division sign or it may be the start of a comment, you may need a separate state to model this. The next character read may determine that it was a division operator in which case you should increment the number of code characters. But if is it a '*' then it begins a comment and you should increment the number of comment characters by 2.

            After you have drawn your state machine diagram it should be relatively simple to create the code using the state pattern.

            Note: The java file that this program will parse will compile with no errors. You do not have to worry about cases where the java code will not compile.
            */
        }

        CommentCounter cc = new CommentCounter();
        cc.analyzeFile();

    }
    
}