
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CommentCounter {

    //the counts of the characters
    private int codeChars;
    private int singleChars;
    private int multiChars;
    private int javadocChars;

    //holds the current "state" of the file (whether the file is in comment, code, etc.)
    private CharacterState currentState;

    public CommentCounter(){

        codeChars = 0;
        singleChars = 0;
        multiChars = 0;
        javadocChars = 0;

        changeState(CodeState.getInstance());
        
        System.out.println("Start state: Code State");

    }

    public void changeState(CharacterState c){
        currentState = c;
    }

    public void analyzeFile(){

        //the file chooser's frame
        JFrame fc_frame = null;

        //analyze a chosen file character by character, determining state each time
        try {

            fc_frame = new JFrame();

            //use a file chooser to locate a file to be parsed by the characters counter
            JFileChooser fc_pick = new JFileChooser();
            File filename = null;
            int returnVal = fc_pick.showOpenDialog(fc_frame);

            if(returnVal == JFileChooser.APPROVE_OPTION){
                filename = fc_pick.getSelectedFile();
                //System.out.println(filename.toString());
            }
            else {
                throw new FileErrorException("Error Obtaining File");
            }

            //use a file input stream on the file name to read the file itself
            FileInputStream fis = null;

            fis = new FileInputStream(filename);

            int intChar = 1;
            char character;

            //while intChar is not -1, or the eof
            while ( (intChar = fis.read()) > 1 ){
                character = (char) intChar;
                
                //if the character is a "regular" character
                if(character != '/' && character != '*' && character != '\n' && character != '\r' && character != '"' && character != '\\'){
                    charFound();
                }
                //eol characters are important, but not counted
                else if(character == '\n'){
                    newlineFound();
                }
                else if(character == '\r'){
                    continue;
                }
                else if(character == '/' || character == '\\'){
                    slashFound(character);
                }
                else if(character == '*'){
                    starFound();
                }
                else {
                    quoteFound();
                }
                
            }

            //displaying the results of the analysis conducted above
            JFrame displayFrame = new JFrame("Character Analysis of File " + filename.toString());
            JPanel displayPanel = new JPanel(new GridLayout(4, 1));
            
            guiPanelHelper(displayPanel, "<html><h1>Code Characters: " + codeChars + "</h1></html>");
            guiPanelHelper(displayPanel, "<html><h1>Single Line Comment Characters: " + singleChars + "</h1></html>");
            guiPanelHelper(displayPanel, "<html><h1>Multiline Comment Characters: " + multiChars + "</h1></html>");
            guiPanelHelper(displayPanel, "<html><h1>Javadoc Comment Characters: " + javadocChars + "</h1></html>");

            displayFrame.setVisible(true);  	

            displayFrame.setSize(500, 450);	

            displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            displayFrame.add(displayPanel); 

            displayFrame.setLocationRelativeTo(null);

        }
        catch (FileErrorException fee){
            System.err.println(fee.getMessage());
        }
        catch (IOException ioe){
            System.err.println("Error Parsing File");
            ioe.printStackTrace();
        }
        finally {
            fc_frame.dispose();
        }

        /*
        System.out.println("Code: " + codeChars);
        System.out.println("Single Line Comments: " + singleChars);
        System.out.println("Multi-line Comments: " + multiChars);
        System.out.println("Javadoc Comments: " + javadocChars);
        */

    }

    //helper function used to create the display panels in the gui
    private void guiPanelHelper (JPanel displayPanel, String s){
        JPanel panel = new JPanel();
        JLabel label = new JLabel(s);
        label.setForeground(Color.WHITE);
        panel.setBackground(Color.BLACK);
        panel.add(label);
        displayPanel.add(panel);
    }

    private void charFound(){
        currentState.charEvent(this);
    }

    private void starFound(){
        currentState.starEvent(this);
    }

    private void slashFound(char character){

        if(character == '\\' && currentState == StringState.getInstance()){
            ((StringState)currentState).backslashEvent(this);
        }
        else {
            currentState.slashEvent(this);
        }
    }

    private void quoteFound(){
        //this state does not handle the quote because code written in that order produces compiler errors
        if(currentState != SlashState.getInstance()){
            currentState.quoteEvent(this);
        }
        
    }

    private void newlineFound(){
        //these two states do not handle newlines productively
        if(currentState != CodeState.getInstance() && currentState != StringState.getInstance()){
            currentState.newlineEvent(this);
        }
    }

    public void incCode(){
        codeChars++;
    }
    
    public void incSingle(){
        singleChars++;
    }

    public void incMulti(){
        multiChars++;
    }
    public void incJavadoc(){
        javadocChars++;
    }

}