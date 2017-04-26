
import java.io.IOException;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Image;

/*import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;*/
import javax.imageio.*;
import javax.swing.*;

import java.io.File;

import javax.swing.event.*;

import java.util.ArrayList;

//the whole pixelGui is a frame, that uses several listeners to make its parts work
public class PixelGUI extends JFrame implements ActionListener, ChangeListener, ItemListener {

    //the pixel grid
    private JPanel pixelPanel;

    //the color changing thumbnail
    private JButton colorThumbnail;

    private JPanel previousColorsPanel;
    private JButton c1;
    private JButton c2;
    private JButton c3;
    private JButton c4;
    private JButton c5;

    //a panel that holds the sliders 
    private JPanel sliderPanel;

    private JLabel redLabel;
    private JLabel redValueLabel;
    private JSlider redSlider;

    private JLabel greenLabel;
    private JLabel greenValueLabel;
    private JSlider greenSlider;

    private JLabel blueLabel;
    private JLabel blueValueLabel;
    private JSlider blueSlider;

    //a panel that holds the advanced checkbox and createBitmapButton
    private JPanel optionButtons;
    private JCheckBox advancedCheckBox;

    //saves the bitmap shown in the grid
    private JButton createBitmapButton;

    //this will keep track of the current pixel highlighted
    private static PixelButton currentPixel;

    //track the current top dialog box for use with the ok button
    private JDialog currentDialog;

    //the dialog box and elements of the advanced options
    private JDialog advancedFrame;
    private JRadioButton gridFiller;
    private JRadioButton extBitmap;
    private JRadioButton none;
    private JButton submitOption;

    //a prompt to let the user know they must select a pixel
    private JDialog selectPixelFrame;
    private JLabel selectPixelLabel;
    private JButton ok;

    //the dialog box and elements necessary for the fill grid option, which fills rows x cols pixels with a color
    private JDialog fillGridFrame;
    private JLabel fg_rows;
    private JLabel fg_cols;
    private JTextField fg_rowsTB;
    private JTextField fg_colsTB;
    private JLabel fg_redLabel;
    private JLabel fg_redValueLabel;
    private JSlider fg_redSlider;
    private JLabel fg_greenLabel;
    private JLabel fg_greenValueLabel;
    private JSlider fg_greenSlider;
    private JLabel fg_blueLabel;
    private JLabel fg_blueValueLabel;
    private JSlider fg_blueSlider;
    private JButton fg_thumbnail;
    private JButton fillButton;

    private JFileChooser fc;
    private JButton accept;
    private JButton deny;

    //the rgb range
    public static final int MIN_RANGE = 0;
    public static final int MAX_RANGE = 255;

    //if a pixel needs to be picked right now
    public boolean pickPixel;

    //the icon that stores all the pixel information for saving
    Icon icon;

    //another underlying element for the pixel grid
    ArrayList <ArrayList<PixelButton>> pixelButtons;

    public PixelGUI (int x, int y){

        super("Pixel Editor");

        pickPixel = false;

        //initializing the main layout components of the application
        getContentPane().setLayout(new BorderLayout());
        sliderPanel = new JPanel(new GridLayout(8, 1));
        pixelPanel = new JPanel(new GridLayout(y, x));
        optionButtons = new JPanel(new FlowLayout());
        previousColorsPanel = new JPanel(new GridLayout(1, 5));
        ok = new JButton("OK");
        selectPixelLabel = new JLabel("Select one of the Pixels to Proceed");

        //setLayout(new GridLayout(y, x));
        pixelButtons = new ArrayList <ArrayList<PixelButton>>();

        icon = new Icon(y, x);
        
        //fill the arraylist with JButtons
        for(int i = 0; i < x; i++){
            ArrayList temp_arr = new ArrayList<PixelButton>();
            for(int j = 0; j < y; j++){
                Pixel p = icon.getPixelAt(i, j);
                PixelButton temp = new PixelButton("", i, j);
                //System.out.println(i + ", " + j);
                Color c = new Color(p.getRed(), p.getGreen(), p.getBlue());
                //System.out.println(p.getRed());
                temp.setBackground(c);
                temp.setOpaque(true);
                temp.addActionListener(this);
                temp_arr.add(temp);
                pixelPanel.add(temp);
            }
            pixelButtons.add(temp_arr);
        }

        //set the current pixel to the top left button
        currentPixel = getButtonAt(0, 0);
        currentPixel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        currentPixel.setText("O");

        //these panels get filled with the slider labels
        JPanel redPanel = new JPanel(new FlowLayout());
        JPanel greenPanel = new JPanel(new FlowLayout());
        JPanel bluePanel = new JPanel(new FlowLayout());

        redLabel = new JLabel("Red: ");
        redSlider = sliderInit();
        redValueLabel = new JLabel(Integer.toString(MAX_RANGE));
        redPanel.add(redLabel);
        redPanel.add(redValueLabel);

        greenLabel = new JLabel("Green: ");
        greenSlider = sliderInit();
        greenValueLabel = new JLabel(Integer.toString(MAX_RANGE));
        greenPanel.add(greenLabel);
        greenPanel.add(greenValueLabel);

        blueLabel = new JLabel("Blue: ");
        blueSlider = sliderInit();
        blueValueLabel = new JLabel(Integer.toString(MAX_RANGE));
        bluePanel.add(blueLabel);
        bluePanel.add(blueValueLabel);
        
        //fill the slider panel with the three color panels and their corresponding sliders
        sliderPanel.add(redPanel);
        sliderPanel.add(redSlider);

        sliderPanel.add(greenPanel);
        sliderPanel.add(greenSlider);

        sliderPanel.add(bluePanel);
        sliderPanel.add(blueSlider);

        //clicking this button sets the color of the currently selected pixel
        colorThumbnail = buttonInit("Click to Set Current Pixel");
        sliderPanel.add(colorThumbnail);

        //these will hold the last 5 colors used
        c1 = buttonInit("1");
        c2 = buttonInit("2");
        c3 = buttonInit("3");
        c4 = buttonInit("4");
        c5 = buttonInit("5");
        
        previousColorsPanel.add(c5);
        previousColorsPanel.add(c4);
        previousColorsPanel.add(c3);
        previousColorsPanel.add(c2);
        previousColorsPanel.add(c1);

        sliderPanel.add(previousColorsPanel);

        //a checkbox to start performing advanced tasks on the grid
        advancedCheckBox = new JCheckBox("Advanced Options");
        advancedCheckBox.setSelected(false);
        advancedCheckBox.addItemListener(this);

        //used to save the current pixel grid to a file
        createBitmapButton = new JButton("Create a Bitmap");
        createBitmapButton.setOpaque(true);
        createBitmapButton.addActionListener(this);

        optionButtons.add(advancedCheckBox);
        optionButtons.add(createBitmapButton);

        sliderPanel.setBorder(BorderFactory.createEmptyBorder(45, 10, 45, 10));
        pixelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        optionButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getContentPane().add(sliderPanel, BorderLayout.EAST);
        getContentPane().add(pixelPanel, BorderLayout.CENTER);
        getContentPane().add(optionButtons, BorderLayout.SOUTH);

        setSize(900, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
    
        //sets the frame in the middle of the screen
        setLocationRelativeTo(null);
        
        setVisible(true);
        
    }

    //get the button at x, y in the arraylist
    private PixelButton getButtonAt(int x_dim, int y_dim){
        return pixelButtons.get(x_dim).get(y_dim);
    }

    @Override
	public void actionPerformed(ActionEvent e){
        //this is if the source is a button
        Object source = e.getSource();

        //this is if the source is one of the pixel buttons specifically
        PixelButton pbSource = new PixelButton("", 0, 0);

        //if this is one of the color grid buttons
		if (source.getClass().equals(pbSource.getClass())){
            
            pbSource = (PixelButton)e.getSource();
            pixelButtonClicked(pbSource);
        }
        //if the user is trying to set the current pixel's color to that of the thumbnail
        else if (source == colorThumbnail){

            thumbnailClicked();
        }
        //if the source is one of the five recently used color buttons
        else if (source == c1 || source == c2 || source == c3 || source == c4 || source == c5){
            
            recentlyUsedClicked((JButton)source);
        }
        //if the user wants to create a new bitmap using info from the grid
        else if (source == createBitmapButton){
          
            JFileChooser fc_save = new JFileChooser();
            int returnVal = fc_save.showSaveDialog(this);

            if(returnVal == JFileChooser.APPROVE_OPTION){
                File filename = fc_save.getSelectedFile();
                icon.saveBitmap(filename.toString());
            }
            else {
                System.out.println("Error Saving File");
            }

        }
        //if the user is deciding which advanced option to use on the grid
        else if (source == submitOption){

            if(gridFiller.isSelected()){
                selectPixelPrompt();
                pickPixel = true;
            }
            else if(extBitmap.isSelected()){
                externalBitmapDialogue();
            }
            else {
                advancedCheckBox.setSelected(false);
            }
            //after the decision, get rid of the dialog box
            advancedFrame.dispose();

        }
        //if the source was the button used to fill the grid with a particular color
        else if (source == fillButton){

            fillButtonClicked();
        }
        //the source is the accept button to fill using an external bitmap
        else if (source == accept){
            currentDialog.dispose();
            extBitmapFiller();
        }
        //if the source was the ok button or deny button, simply dispose of the parent dialog
        else if (source == ok || source == deny){
            currentDialog.dispose();
        }
	}

    //if the state of a slider was changed, this will handle it
    @Override
    public void stateChanged(ChangeEvent e) {

        JSlider source = (JSlider)e.getSource();

        //only adjust if mouse has been unclicked
        //if (!source.getValueIsAdjusting()) {

        int colorVal = (int)source.getValue();

        //if the source was one of the primary sliders
        if(source == redSlider || source == greenSlider || source == blueSlider){

            if(source == redSlider){
                redValueLabel.setText(Integer.toString(colorVal));
            }
            else if (source == blueSlider){
                blueValueLabel.setText(Integer.toString(colorVal));
            }
            else if (source == greenSlider){
                greenValueLabel.setText(Integer.toString(colorVal));
            }

            Color c = new Color(Integer.parseInt(redValueLabel.getText()), Integer.parseInt(greenValueLabel.getText()), Integer.parseInt(blueValueLabel.getText()));
            colorThumbnail.setBackground(c);

            //make sure the thumbnail text is visible
            if(c.getRed() + c.getGreen() + c.getBlue() < (127 * 3 - 60)){
                colorThumbnail.setForeground(Color.WHITE);
            }
            else {
                colorThumbnail.setForeground(Color.BLACK);
            }
        }
        //if the source was one of the advanced option sliders used to fill the grid
        else if (source == fg_redSlider || source == fg_greenSlider || source == fg_blueSlider){
            if(source == fg_redSlider){
                fg_redValueLabel.setText(Integer.toString(colorVal));
            }
            else if (source == fg_blueSlider){
                fg_blueValueLabel.setText(Integer.toString(colorVal));
            }
            else if (source == fg_greenSlider){
                fg_greenValueLabel.setText(Integer.toString(colorVal));
            }

            Color c = new Color(Integer.parseInt(fg_redValueLabel.getText()), Integer.parseInt(fg_greenValueLabel.getText()), Integer.parseInt(fg_blueValueLabel.getText()));
            fg_thumbnail.setBackground(c);
        }
            
        //}
    }

    //when the checkbox is changed, this handles the event
    @Override
    public void itemStateChanged(ItemEvent e) {

        Object source = e.getItemSelectable();

        if (source == advancedCheckBox) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                //advancedCheckBox.setSelected(false);
                advancedDialog();
            }
        }

        //if (e.getStateChange() == ItemEvent.DESELECTED){

        // }
    }
    
    //initializes a JSlider with the basic options necessary in this program
    private JSlider sliderInit (){
        JSlider s = new JSlider(MIN_RANGE, MAX_RANGE, MAX_RANGE);
        s.addChangeListener(this);
        s.setMajorTickSpacing(MAX_RANGE);
        s.setMinorTickSpacing(51);
        s.setPaintTicks(true);
        s.setPaintLabels(true);

        return s;
    }
    //initializes a JButton with the basic options necessary in this program 
    private JButton buttonInit (String text){
        JButton b = new JButton(text);

        Color c = new Color(255, 255, 255);
        b.setBackground(c);
        b.setOpaque(true);
        b.addActionListener(this);

        return b;
    }

    //recolors the thumbnails of the recently used colors buttons
    private void recolorThumbnails(Color c){

        c1.setBackground(c2.getBackground());
        adjustButtonHighlight(c2.getBackground(), c1);

        c2.setBackground(c3.getBackground());
        adjustButtonHighlight(c3.getBackground(), c2);

        c3.setBackground(c4.getBackground());
        adjustButtonHighlight(c4.getBackground(), c3);

        c4.setBackground(c5.getBackground());
        adjustButtonHighlight(c5.getBackground(), c4);

        c5.setBackground(c);
        adjustButtonHighlight(c, c5);

    }

    //brings up the advanced dialogue options
    private void advancedDialog(){

        advancedFrame = new JDialog(this, "Advanced Options", true);

        JPanel opPanel = new JPanel(new GridLayout(3, 1));
        JPanel fullPanel = new JPanel(new FlowLayout());

        ButtonGroup b = new ButtonGroup();

        gridFiller = new JRadioButton("Grid Filler");
        gridFiller.addActionListener(this);
        extBitmap = new JRadioButton("Use External Bitmap");
        extBitmap.addActionListener(this);
        none = new JRadioButton("No Options");
        none.addActionListener(this);
        none.setSelected(true);

        submitOption = new JButton("Enable Option");
        submitOption.addActionListener(this);

        b.add(gridFiller);
        b.add(extBitmap);
        b.add(none);

        opPanel.add(gridFiller);
        opPanel.add(extBitmap);
        opPanel.add(none);
        fullPanel.add(opPanel);
        fullPanel.add(submitOption);
        advancedFrame.getContentPane().add(fullPanel);

        advancedFrame.pack();

        advancedFrame.setSize(250, 150);		
    
        advancedFrame.setLocationRelativeTo(null);
        
        advancedFrame.setVisible(true);

    }

    //informs the user that they must select a pixel to continue
    public void selectPixelPrompt(){

        selectPixelFrame = new JDialog(this, "Instruction", true);
        currentDialog = selectPixelFrame;

        JPanel fullPanel = new JPanel(new BorderLayout());
        JPanel okButtonPanel = new JPanel();

        ok.addActionListener(this);

        fullPanel.add(selectPixelLabel, BorderLayout.CENTER);
        okButtonPanel.add(ok);
        fullPanel.add(okButtonPanel, BorderLayout.SOUTH);

        selectPixelFrame.getContentPane().add(fullPanel);

        selectPixelFrame.pack();

        selectPixelFrame.setSize(220, 120);		
    
        selectPixelFrame.setLocationRelativeTo(null);
        
        selectPixelFrame.setVisible(true);

    }

    //brings up the fill grid dialogue, which takes the users information and fills the grid using the numbers provided
    public void fillGridDialogue(){
        pickPixel = false;
        fillGridFrame = new JDialog(this, "Fill Grid", true);
        JPanel fullPanel = new JPanel(new BorderLayout());
        JPanel fg_TBPanel = new JPanel(new GridLayout(2, 2)); 
        JPanel fg_sliderPanel = new JPanel(new GridLayout(6, 1));
        JPanel fg_buttonPanel = new JPanel(new GridLayout(2, 1));

        fg_rows = new JLabel("Enter the rows to fill:");
        fg_cols = new JLabel("Enter the columns to fill: ");

        //check values when entered
        fg_rowsTB = new JTextField(10);
        fg_colsTB = new JTextField(10);

        fg_TBPanel.add(fg_rows);
        fg_TBPanel.add(fg_rowsTB);
        fg_TBPanel.add(fg_cols);
        fg_TBPanel.add(fg_colsTB);
        fullPanel.add(fg_TBPanel, BorderLayout.NORTH);

        JPanel redPanel = new JPanel(new FlowLayout());
        JPanel greenPanel = new JPanel(new FlowLayout());
        JPanel bluePanel = new JPanel(new FlowLayout());

        fg_redLabel = new JLabel("Red: ");
        fg_redSlider = sliderInit();
        fg_redValueLabel = new JLabel(Integer.toString(MAX_RANGE));
        redPanel.add(fg_redLabel);
        redPanel.add(fg_redValueLabel);

        fg_greenLabel = new JLabel("Green: ");
        fg_greenSlider = sliderInit();
        fg_greenValueLabel = new JLabel(Integer.toString(MAX_RANGE));
        greenPanel.add(fg_greenLabel);
        greenPanel.add(fg_greenValueLabel);

        fg_blueLabel = new JLabel("Blue: ");
        fg_blueSlider = sliderInit();
        fg_blueValueLabel = new JLabel(Integer.toString(MAX_RANGE));
        bluePanel.add(fg_blueLabel);
        bluePanel.add(fg_blueValueLabel);
        
        fg_sliderPanel.add(redPanel);
        fg_sliderPanel.add(fg_redSlider);

        fg_sliderPanel.add(greenPanel);
        fg_sliderPanel.add(fg_greenSlider);

        fg_sliderPanel.add(bluePanel);
        fg_sliderPanel.add(fg_blueSlider);

        fg_TBPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        fg_sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        fullPanel.add(fg_sliderPanel);

        fg_thumbnail = buttonInit("");
        fillButton = buttonInit("Fill");
        fg_buttonPanel.add(fg_thumbnail);
        fg_buttonPanel.add(fillButton);

        fullPanel.add(fg_buttonPanel, BorderLayout.SOUTH);

        fillGridFrame.add(fullPanel);

        fillGridFrame.pack();

        fillGridFrame.setSize(375, 450);		
    
        fillGridFrame.setLocationRelativeTo(null);
        
        fillGridFrame.setVisible(true);
    }

    //dialogue that helps the user find a bitmap file 
    public void externalBitmapDialogue(){

        fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            File filename = fc.getSelectedFile();

            Image image = null;
            
            try {
                image = ImageIO.read(filename);
            } 
            catch (IOException e) {
                System.err.println("Error reading the image");
            }

            // Use a label to display the image
            JDialog preview = new JDialog(this, "Image Preview", true);
            currentDialog = preview;

            JPanel mainPanel = new JPanel(new BorderLayout());
            JPanel previewPanel = new JPanel();
            JPanel acceptPreviewButtons = new JPanel(new GridLayout(1, 2));
            accept = new JButton("Accept");
            deny = new JButton("Deny");
            accept.addActionListener(this);
            deny.addActionListener(this);
            acceptPreviewButtons.add(deny);
            acceptPreviewButtons.add(accept);

            JLabel lblimage = new JLabel(new ImageIcon(image));
            
            previewPanel.add(lblimage);
            mainPanel.add(previewPanel, BorderLayout.CENTER);
            mainPanel.add(acceptPreviewButtons, BorderLayout.SOUTH);

            preview.add(mainPanel);
            preview.setSize(400, 400);
            preview.setLocationRelativeTo(null);
            preview.setVisible(true);

        }
        else {
            System.out.println("Error Opening File");
        }

        
    }

    //Uses the file info provided by externalBitmapDialogue and fills the grid using info from the bitmap
    private void extBitmapFiller (){
        Icon importedIcon = Icon.importBitmap(fc.getSelectedFile().toString());

            PixelButton temp = currentPixel;
            currentPixel = getButtonAt(0, 0);

            for(int i = 0; (i < icon.get_y_dim()) && (i < importedIcon.get_y_dim()); i++){
                for(int j = 0; (j < icon.get_x_dim()) && (j < importedIcon.get_x_dim()); j++){
                    currentPixel = getButtonAt(j, i);
                    Pixel p = importedIcon.getPixelAt(i, j);
                    //System.out.println(j + ", " + i);
                    Color c = new Color(p.getRed(), p.getGreen(), p.getBlue());
                    currentPixel.setBackground(c);

                    icon.setPixelAt(currentPixel.get_x_dim(), currentPixel.get_y_dim(), c.getRed(), c.getGreen(), c.getBlue());
                }
            }

            //fill grid as full as possible with icon
            currentPixel = temp;
    }

    //adjusts the colors of the button based on the brightness of the color
    private void adjustButtonHighlight(Color c, JButton b){

        Color highlightColor;

        //change the font color for visibility
        if(c.getRed() + c.getGreen() + c.getBlue() < (127 * 3 - 60)){
            highlightColor = Color.WHITE;
        }
        else {
            highlightColor = Color.BLACK;
        }

        //b.setBorder(BorderFactory.createLineBorder(highlightColor));
        b.setForeground(highlightColor);
    }

    //a pixel button was clicked and will be highlighted
    private void pixelButtonClicked(PixelButton pbSource){
        //highlight the new current pixel so the user knows which they are editing
        currentPixel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        currentPixel.setText("");
        pbSource.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pbSource.setText("O");

        //the current pixel gets the pixelButton at x, y
        currentPixel = getButtonAt(pbSource.get_x_dim(), pbSource.get_y_dim());
        //System.out.println(pbSource.get_x_dim() + ", " + pbSource.get_y_dim());

        //change the font color for visibility
        adjustButtonHighlight(currentPixel.getBackground(), currentPixel);

        //change the rows and cols of the grid at the current pixel
        if(pickPixel == true){
            fillGridDialogue();
        }
    }

    //the color thumbnail was clicked, so we set the color of the current pixel 
    private void thumbnailClicked(){
        //create a new color from the values in the sliders
        Color c = new Color(Integer.parseInt(redValueLabel.getText()), Integer.parseInt(greenValueLabel.getText()), Integer.parseInt(blueValueLabel.getText()));
        currentPixel.setBackground(c);

        //change the font color for visibility
        adjustButtonHighlight(currentPixel.getBackground(), currentPixel);

        //update the icon at the same spot as the grid
        icon.setPixelAt(currentPixel.get_x_dim(), currentPixel.get_y_dim(), c.getRed(), c.getGreen(), c.getBlue());

        //reset the recently used color list if the color selected is new
        if(colorThumbnail.getBackground().getRGB() != c5.getBackground().getRGB()){
            recolorThumbnails(c);
        }
    }

    //one of the recently used colors buttons was clicked, so we reset the color of the color thumbnail
    private void recentlyUsedClicked(JButton source){

        Color bg = ((JButton)source).getBackground();

        //readjust the labels and slider values to sync up with the old color value
        colorThumbnail.setBackground(bg);
        redValueLabel.setText(Integer.toString(bg.getRed()));
        greenValueLabel.setText(Integer.toString(bg.getGreen()));
        blueValueLabel.setText(Integer.toString(bg.getBlue()));

        redSlider.setValue(bg.getRed());
        greenSlider.setValue(bg.getGreen());
        blueSlider.setValue(bg.getBlue());

        if(bg.getRGB() != c5.getBackground().getRGB()){
            Color temp = colorThumbnail.getBackground();
            recolorThumbnails(temp);
        }
    }

    //the button used to fill the grid was clicked, so we use info from the dialogue to fill in the grid
    private void fillButtonClicked(){
        //check fg_rows and fg_cols, then save currentPixel to temp and fill from currentPixel like in extBitmap
        if(fg_rowsTB.getText().isEmpty() || fg_colsTB.getText().isEmpty()){
            if(fg_rowsTB.getText().isEmpty()){
                fg_rows.setForeground(Color.RED);
            }

            if(fg_colsTB.getText().isEmpty()){
                fg_cols.setForeground(Color.RED);
            }
        }

        else {
            int y_dim = Integer.parseInt(fg_colsTB.getText());
            int x_dim = Integer.parseInt(fg_rowsTB.getText());

            //this could be used to ensure the input is never more than the size of the grid
            if(false){
                //
            }
            // if(y_dim > icon.get_y_dim()){
            //     fg_rows.setForeground(Color.RED);
            // }
            // else if (x_dim > icon.get_x_dim()){
            //     fg_cols.setForeground(Color.RED);
            // }
            else {  
                //get the color value from the color thumbnail
                Color c = new Color(Integer.parseInt(fg_redValueLabel.getText()), Integer.parseInt(fg_greenValueLabel.getText()), Integer.parseInt(fg_blueValueLabel.getText()));   
                fillGridFrame.setVisible(false);
                fillGridFrame.dispose();

                PixelButton temp = currentPixel;
                int tempx = temp.get_x_dim();
                int tempy = temp.get_y_dim();

                //fill in the grid starting from the current pixel outward (southeastward)
                //make sure not to go beyond the bounds of either the grid or the icon, or the (x, y) input
                for(int i = 0; (i + tempy < icon.get_y_dim()) && (i < y_dim); i++){
                    for(int j = 0; (j + tempx < icon.get_x_dim()) && (j < x_dim); j++){
                        currentPixel = getButtonAt(tempx + j, tempy + i);
                        //System.out.println(currentPixel.get_x_dim() + ", " + currentPixel.get_y_dim());
                        currentPixel.setBackground(c);

                        icon.setPixelAt(currentPixel.get_x_dim(), currentPixel.get_y_dim(), c.getRed(), c.getGreen(), c.getBlue());
                    }
                }

                currentPixel = temp;
            }
        }
    }
 
}
