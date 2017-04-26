
public class Driver {

/*Homework 3 asks you to extend the Pixel assignment. Your job is to create a GUI to easily make bitmap images. You will reuse your Pixel and Icon classes and create a GUI to gather info about the pixels. The Pixel and Icon classes should not be aware of the GUI classes. -^

The application should prompt for the dimensions of the Icon on startup. Display a button for each pixel. There should be a way for the user to select a color. Once a button is clicked the Pixel associated with that button should have its color set to the appropriate levels and the color of the button should change. Look at the setBackground() method of the JButton class and the Color class to achieve this. -^

Below is a description of the required components:
grid- a 2D group of buttons. Click a button and that button will take on a color. The corresponding pixel in the bitmap will get the same RGB value. -^

color chooser- allows you to choose a color. Perhaps three sliders would be sufficient to allow you to select a value from 0 - 255 for red, green, and blue. There should be a preview of the selected color every time the sliders change. -^

last five colors- show the last five colors used. When someone clicks on a previously used color, adjust the color sliders to take on that color. -^

advanced checkbox- when this is checked a selected button doesn't take on the color from the color chooser. Instead, it brings up an 'advanced' dialog.

advanced dialog part 1- ask for the number of rows and columns from the clicked button onward to fill with a color. The rows will always be from the selected button and rows beneath and the columns will always be from the selected button and to the right.

advanced dialog part 2- allow the user to open up any 24-bit bitmap and add the pixels inside the file to your bitmap. The top, left pixel of the selected bitmap will go in the selected button. Always show a preview (large enough to see) of the selected file before adding it to the current bitmap.

create bitmap- clicking this button will show a file chooser to select where the file will be stored. Then allow the user to enter a name and save the bitmap file at that location.

REQUIRED:

In addition to your working code I am requiring that everyone turn in a reflection of what happened while writing this code. Throughout the writing of this program I want you to reflect on the development process.

Imagine that you are making this playback for someone who might have to extend your code in the future. Tell me:

What do you think they will need to know about it?
How will knowing about the evolution help them add to your code?
Record the things that you learned in case your viewers don't know about them.
Tell me about why you made the decisions you did and why you made any changes to your original code.
I need this reflection in an electronic document (text file or word document is fine). The quality of the reflection will not affect your grade but you do need to submit one.*/

    public static void main (String[] args){

        PromptGUI p = new PromptGUI();
    }
}