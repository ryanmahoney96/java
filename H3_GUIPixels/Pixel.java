
public class Pixel {

    // Pixel class
    // + single int to represent the color
    // + color manipulation algorithms
    // + has all the functionality specified above
    // + printHex() functionality

    //parseInt can parse according to a base
    //int b = Integer.parseInt("0101",2);

    //use this int to hold the red green blue values of the Pixel using the 32 bits (4 bytes) of the int 
    private int rgb;
    //private static int iso_r = 0x00ff0000;
    //private static int iso_g = 0x0000ff00;
    //private static int iso_b = 0x000000ff; 

    //default constructor sets the rgb value to white
    public Pixel(){

        //this is essentially 24 1's in series (00000000 11111111 11111111 11111111)
        rgb = new Integer(16777215);

    }

    //copy constructor uses the rgb of another pixel to set itself
    public Pixel (Pixel p){
        setRed(p.getRed());
        setGreen(p.getGreen());
        setBlue(p.getBlue());
    }

    public int getRGB(){
        return rgb;
    }

    //These getters and setters are used to interact with the RGB values of the rgb int of the pixel
    // 2 ^ 8 == 256 (seven 1's is 255) 
    public void setRed (int r){
        //if the value is outside the bounds 0 - 255
        if(r > 255 || r < 0){
            System.err.println("Red Error: You must Enter a valid integer between 0 and 255");
        }
        else {
            //store the values of green and blue temporarily by getting the binary 1's for full green and blue, then ANDing to get the values in rgb
            //int temp_gb = rgb & (iso_g | iso_b);
            
            //left shift to compensate for the offset within the 32-bit rgb integer
            int temp_r = r << 16;

            //bitwise ORing "aligns" the three numbers and fits them into the rgb int
            //      NULL    RED     GREEN     BLUE
            //    00000000  getRed  getGreen  getBlue
            rgb = temp_r | (getGreen() << 8) | getBlue();

        }
    }

    //returns the 0 - 255 red value in rgb
    public int getRed (){
        //right shift to get the 0 - 255 value of the red in rgb
        return rgb >> 16;
    }

    public void setGreen (int g){

        //if the value is outside the bounds 0 - 255
        if(g > 255 || g < 0){
            System.err.println("Green Error: You must Enter a valid integer between 0 and 255");

        }
        else {
            
            //left shift to compensate for the offset within the 32-bit rgb integer
            int temp_g = g << 8;

            //bitwise ORing "aligns" the three numbers and fits them into the rgb int
            //      NULL    RED     GREEN     BLUE
            //    00000000  getRed  getGreen  getBlue
            rgb = (getRed() << 16) | temp_g |  getBlue();

        }

    }
    public int getGreen (){

        return (rgb << 16) >>> 24;
    }

    public void setBlue (int b){

        if(b > 255 || b < 0){
            System.err.println("Blue Error: You must Enter a valid integer between 0 and 255");

        }
        else {
            //blue is the rightmost, and doesnt require shifting
            int temp_b = b;

            //bitwise ORing "aligns" the three numbers and fits them into the rgb int
            //      NULL    RED     GREEN     BLUE
            //    00000000  getRed  getGreen  getBlue
            rgb = (getRed() << 16) | (getGreen() << 8) | temp_b;
        }
    }
    public int getBlue (){

        return (rgb << 24) >>> 24;
    }

    //Spec asks to return a string
    public void printHex(){

        //get r, g, and b as 0 - 255 values
        int r = getRed();
        int g = getGreen();
        int b = getBlue();

        //using string concatenation, create the hexadecimal value of the pixel
        String hex = "#";

        //if the digit would be displayed as one digit instead of two, "add" a 0
        if(r < 0x10){
            hex += '0';
        }

        hex += Integer.toHexString(r);

        if(g < 0x10){
            hex += '0';
        }

        hex += Integer.toHexString(g);

        if(b < 0x10){
            hex += '0';
        }

        hex += Integer.toHexString(b);

        //print the hexadecimal value (no new line)
        System.out.print(hex);
   
    }
}