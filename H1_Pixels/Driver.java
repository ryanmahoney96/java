
import java.util.Random;

// Homework 1 asks you to write a program to create 24-bit bitmap images. You will start by creating a Pixel class. A Pixel represents a single dot of color on a computer screen. The color is determined by a combination of 3 values representing the intensity of Red, Green, and Blue (called an RGB value).

// The range of values for each the colors is from 0 - 255 (where 0 represents no amount of color and 255 represents the most intense amount of color). Since Java has 32 bit integers a single int can and should represent all three values (you must use a single int to represent three groups of eight bits- one group for each color). The first eight bits will represent the amount of Red, the second eight the amount of Green, the third eight will represent the amount of Blue, and the last group of eight bits will be unused and should always be 0's.

// The Pixel class should allow you to get and set each of the intensity levels by passing in an int. For example, your class should have the following methods:

// public void setRed(int r) //r should be between 0 - 255
// public int getRed()
// public void setGreen(int g) //g should be between 0 - 255
// public int getGreen()
// public void setBlue(int b) //b should be between 0 - 255
// public int getBlue()

// You must manipulate the values to store the intensity values in the 32 bits that are stored for the RGB value. I would look at the Integer and String classes for helpers. Or, you can manipulate the bits by shifting using the shift operators << and >> or using bitwise AND (&) and bitwise OR (|).

// Your class must also provide a printHex() method that returns a string with the hexadecimal representation in the form of #RRGGBB.
//++++++++++++++++++++++++++++++++++++++++++++++

// Next, create an Icon class that is a 2D collection of Pixels.

// The Icon class should have two constructors. The first is the default constructor that will create a 40 X 40 pixel Icon. The second constructor will take two integers representing the number of rows and columns of pixels in the Icon. Create a Pixel object for each column of each row.

// The Icon class allows the user to specify a pixel by row and column and get/set it's RGB value.
//++++++++++++++++++++++++++++++++++++++++++++++

// The Icon class should be able to create a string representation of the bitmap that shows the RGB values for all the pixels in it. It should also be able to produce a 24-bit bitmap image of the Icon.

// The format of bitmap images is described here:
// http://en.wikipedia.org/wiki/BMP_file_format
// http://www.dragonwins.com/domains/getteched/bmp/bmpfileformat.htm

// In your driver, create a small 5X5 Icon object with different colored pixels (red top left, green top right, blue bottom left, black bottom right). Then, using the Icon, create a bitmap image file of your Icon. In addition, the Icon should print out the hex values of all the pixels in rows and columns to the command prompt.

// Other
// Well structured and commented code
// Interface matches the methods in the description above
// A driver that tests the functionality of the classes

public class Driver {

    public static void main (String[] args){

        //Pixel p = new Pixel();
        //p.setRed(10);        
        //p.setGreen(20);
        //p.setBlue(30);

        //System.out.println(p.getRed());
        //System.out.println(p.getGreen());
        //System.out.println(p.getBlue());

        //p.printHex();

        Random r = new Random();

        //set the dimensions to random numbers (1 - 300)
        int x = 300;
        int y = 300;
 
        //create a new icon of these dimensions
        Icon randIco = new Icon(x, y);

        for(int i = 0; i < y; i++){
            for(int j = 0; j < x; j++){
                //fill every pixel with a random rgb value
                randIco.setPixelAt(i, j, r.nextInt(254), r.nextInt(254), r.nextInt(254));
            }
        }

        //hard set the four corner pixels
        randIco.setPixelAt(0, 0, 255, 0, 0);
        randIco.setPixelAt(0, x - 1, 0, 0, 255);
        randIco.setPixelAt(y - 1, 0, 0, 255, 0);
        randIco.setPixelAt(y - 1, x - 1, 0, 0, 0);

        //save the bitmap to a file for viewing
        randIco.saveBitmap("randIco.bmp");

        //randIco.printHex();

        //create a new icon, 5 x 5, with the four corners set and white in all the others
        Icon baseIco = new Icon(5, 5);

        baseIco.setPixelAt(0, 0, 255, 0, 0);
        baseIco.setPixelAt(0, 4, 0, 0, 255);
        baseIco.setPixelAt(4, 0, 0, 255, 0);
        baseIco.setPixelAt(4, 4, 0, 0, 0);

        baseIco.saveBitmap("baseIco.bmp");

        //print the grid as hex values in the command window
        baseIco.printHex();

    }


}