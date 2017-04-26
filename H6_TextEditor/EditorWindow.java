
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;

public class EditorWindow extends JFrame implements Observer, DocumentListener, ActionListener, WindowListener {

    private ServerInterface server;
    private Document dld;
    private String ip;
    private JTextArea displayArea;
    private JButton saveButton;
    private boolean changeNecessary;

    public EditorWindow (ServerInterface temp){

        super("Text Editor");

        server = temp;
        server.attach(this);

        displayArea = new JTextArea(server.getText());

        saveButton = new JButton("Save");
        saveButton.addActionListener(this);

        JPanel textArea = new JPanel(new BorderLayout());

        textArea.add(displayArea, BorderLayout.CENTER);

        //using a scrollpane in case of large text
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel buttonArea = new JPanel(new BorderLayout());

        JPanel buttonFormat = new JPanel(new GridLayout(1, 3));
        buttonFormat.add(new JLabel());
        buttonFormat.add(saveButton);
        buttonFormat.add(new JLabel());
        
        buttonArea.add(buttonFormat, BorderLayout.CENTER);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonArea, BorderLayout.SOUTH);

        this.setSize(400, 300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dld = displayArea.getDocument();

        dld.addDocumentListener(this);

        changeNecessary = true;

        addWindowListener(this);

        //!!!Strings are IMMUTABLE!!!
        //this note was used to determine how I would handle newline characters
    }

    public void update() {

        //if this window is not being actively typed in, a change will be necessary
        if(changeNecessary){

            //System.out.println("Updating Text");

            //remove the listener and add it back right after to prevent roundabout updates
            dld.removeDocumentListener(this);

            //sends a request to the server for the most recent copy of the text
            displayArea.setText(server.getText());

            dld.addDocumentListener(this);
        }

        changeNecessary = true;
    }

    //if the text box was "changed"
    public void changedUpdate(DocumentEvent documentEvent) {
        
        //System.out.println("CHANGE");

        // if(!displayArea.getText().equals(server.getText())){                
        //     server.setText(displayArea.getText());
        // }

    }
    public void insertUpdate(DocumentEvent documentEvent) {
        
        //an update is triggered every time a character is added to the text area
        //System.out.println("INSERT");

        //because this window is being actively typed in, it does not require the most recent copy (because it is producing it)
        changeNecessary = false;

        server.setText(displayArea.getText());

    }
    public void removeUpdate(DocumentEvent documentEvent) {
        
        //System.out.println("REMOVE");
        changeNecessary = false;

        server.setText(displayArea.getText());

    }

    @Override
	public void actionPerformed(ActionEvent e){

        //the one button in the editor is the save button, which will save the contents of this window's text area into a user defined file
        JFileChooser fc_save = new JFileChooser();
        int returnVal = fc_save.showSaveDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            File filename = fc_save.getSelectedFile();

            System.out.println("::SAVING TEXT::");
            
            PrintWriter outputFile = null;

            try {
                outputFile = new PrintWriter(filename);

                char charArray[] = displayArea.getText().toCharArray();

                for(char c: charArray){
                    if(c != '\n'){
                        outputFile.print(c);
                    }
                    else {
                        outputFile.println();
                    }
                }
            }
            catch (Exception fileNotSavedEx){
                System.out.println("Error Saving the File Text");
            }
            finally {
                if(outputFile != null){
                    outputFile.close();
                }
            }
        
        }
        else {
            System.out.println("Error Initiating File Save");
        }

    }

    @Override
    public void windowDeactivated (WindowEvent w){
    }

    @Override
    public void windowActivated (WindowEvent w){
    }

    @Override
    public void windowDeiconified (WindowEvent w){
    }

    @Override
    public void windowIconified (WindowEvent w){
    }

    @Override
    public void windowClosed (WindowEvent w){
    }

    @Override
    public void windowClosing (WindowEvent w){
        //when the window is closing, we want to detach ourselves from the server's updates
        //System.out.println("WINDOW CLOSING");
        server.closeConnection();
    }

    @Override
    public void windowOpened (WindowEvent w){
    }
}