
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PromptGUI extends JFrame implements ActionListener {

    //the dimensions of the future pixel editor
    private JLabel x;
    private JLabel y;

    private JTextField xTB;
    private JTextField yTB;
    
    private JButton submit;

    private JPanel all;
    private JPanel prompts;
    private JPanel buttons;

    public PromptGUI (){

        super("Pixel Dimensions");

        x = new JLabel("What is the width of the Image: ");
        y = new JLabel("What is the height of the Image: ");

        xTB = new JTextField(5);
        yTB = new JTextField(5);

        submit = new JButton("Submit");
        submit.addActionListener(this);

        all = new JPanel(new BorderLayout());
        prompts = new JPanel(new GridLayout(2, 2));
        buttons = new JPanel(new FlowLayout());
        
        prompts.add(x);
        prompts.add(xTB);
        prompts.add(y);
        prompts.add(yTB);
        buttons.add(submit);
        all.add(prompts, BorderLayout.NORTH);
        all.add(buttons, BorderLayout.SOUTH);
        add(all);

        setSize(400, 120);

        //close the program if the frame is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		//set the frame in the middle of the screen
        setLocationRelativeTo(null);
		
		setVisible(true);

    }

    //if a button is pressed
    @Override
    public void actionPerformed(ActionEvent e)
	{
        JButton source = (JButton)e.getSource();

        if(source.equals(submit)){
            int x_dim;
            int y_dim;

            //make sure the text boxes aren't empty
            if(xTB.getText().isEmpty() || yTB.getText().isEmpty()){
                if(xTB.getText().isEmpty()){
                    x.setForeground(Color.RED);
                }

                if(yTB.getText().isEmpty()){
                    y.setForeground(Color.RED);
                }
            }

            else {
                x_dim = Integer.parseInt(xTB.getText());
                y_dim = Integer.parseInt(yTB.getText());

                if(x_dim == 0){
                    x.setForeground(Color.RED);
                }
                else if (y_dim == 0){
                    y.setForeground(Color.RED);
                }
                else {  
                    //if the dimensions entered are valid, dispose of this frame and create a new pixelGUI with the dimensions
                    setVisible(false);
                    dispose();
                    
                    PixelGUI main_gui = new PixelGUI(x_dim, y_dim);
                }
            }
        }
	}
}