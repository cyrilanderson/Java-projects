//import Picture;
import edu.princeton.cs.algs4.Picture

import java.awt.Color;
import java.lang.Math;

public class SeamCarver {
    
    //contains int representing colors of pixels as 32 bit integer, where Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue
    private int[][] pixelColorData;
    private double[][] energies;
    private int width;
    private int height;
    // 1 is normal orientation, 0 is transposed
    private boolean currentOrientation;
    
    
    public SeamCarver(Picture picture) { 
        if (picture==null) throw new NullPointerException("Null argument");
        width=picture.width();
        height=picture.height();
        pixelColorData = new int[height][width];
        energies = new double[height][width];
        for (int row=0; row<height; row++){
            for (int col=0; col<width; col++) {
                pixelColorData[row][col] = picture.get(col, row).getRGB();
            }
        }
        currentOrientation=true;
        for (int row=0; row<height; row++){
            for (int col=0; col<width; col++) {
                energies[row][col]=-1;
            }
        }
    }
 
    // current picture
    public Picture picture() {
        if (currentOrientation==false) transpose();
        Picture newPic = new Picture(width, height);
        for (int row=0; row<height; row++) {
            for (int col=0; col<width; col++) {
                int pixelColorInt=pixelColorData[row][col];
                Color pixelColor = new Color(pixelColorInt);
                newPic.set(col, row, pixelColor);  
            }
        }
        
        return newPic;
    }
    
    // width of current picture
    public int width() {
        if (currentOrientation==true) return width;
        else return height;
    }
    
     // height of current picture
    public int height() {
        if (currentOrientation==true) return height;
        else return width;
    }
    
    private void transpose(){
        int[][] colorTemp = new int[width][height];
        double[][] energyTemp = new double[width][height];
        for (int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                colorTemp[j][i] = pixelColorData[i][j];
                energyTemp[j][i] = energies[i][j];
            }
        }
        //swap height and width
        int temp = width;
        width = height;
        height = temp;

//        width ^= height;
//        height ^= width;
//        width ^= height;
        
        // values of data arrays to the temps
        pixelColorData=colorTemp;
        energies = energyTemp;
        
        //flip orientation
        currentOrientation=!currentOrientation;
    }
    
    // energy of pixel at column x and row y in the normal orientation
    //The energy function used is the (look up technical name) energy function
    public double energy(int x, int y) {
        
        if (currentOrientation==false) {
            int temp = x;
            x = y;
            y = temp;
//            x^=y;
//            y^=x;
//            x^=y;
        }
        
        double energy;
        int left;
        int right;
        int below;
        int above;
        
        if (energies[y][x]!=-1) return energies[y][x];
        
        else {
            if ((x==0)||(y==0)||(x==width-1)||(y==height-1)) energy=1000;
            else {
                left=pixelColorData[y][x-1];
                right=pixelColorData[y][x+1];
                below=pixelColorData[y+1][x];
                above=pixelColorData[y-1][x];
                int[] leftColors=splitRGB(left);
                int[] rightColors=splitRGB(right);
                int[] belowColors=splitRGB(below);
                int[] aboveColors=splitRGB(above);
                energy=0.0;
                for (int colorComp=0; colorComp<3; colorComp++) {
                    energy+=(double) Math.pow(rightColors[colorComp] - leftColors[colorComp], 2);
                    energy+=(double) Math.pow(aboveColors[colorComp] - belowColors[colorComp], 2);
                }
                energy=Math.sqrt(energy);
            }
        }      
        return energy;
    }
    
    //Splits the 32 bit int representing a color as argb
    //Generates and returns an array splitRGB. [0] = red value [0..255]. 
    //[1] = green value. [2] = blue value. The alpha channel value is not needed here.
    private int[] splitRGB(int color32RGB) {
        int[] splitRGB=new int[3];
        for (int i=0; i<3; i++) {
            splitRGB[i]=(color32RGB>>(8*(2-i)))&0xFF;
        }
        return splitRGB;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (currentOrientation==true) transpose();
        return findSeam();
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (currentOrientation==false) transpose();
        return findSeam();
    }
    
    private int[] findSeam() {

        int[] seam=new int[height];
        double[][] distTo = new double[height][width];
        //vertexTo = -1 => above left; 0 =>above; 1 => above right
        //Only defined for rows 1..height-1
        int[][] vertexTo = new int[height][width];
        for (int col=0; col<width; col++){
            vertexTo[0][col]=-2;
            distTo[0][col]=1000;
            for (int row=1; row<height; row++) {
                distTo[row][col]=Double.POSITIVE_INFINITY;
            }
        }

        //This section implements a variation of djikstra's shortest path algorithm, using col, row points in the pixel
        //grid as graph nodes. Here the path lengths are based on the energies of the pixels and the shortest path is the shortest
        //energy path sum along a connected seam running from the top to the bottom of the image.
        //The top row is basically thought of as one point source here. The wave of computation for each greater cycle of Djikstra's
        //propagates like a horizontal straight line wave front from top to bottom from row 1 to row (height-1).
        //As you go down, you update the values of that row's distTo and vertexTo matrix values
        //Then, as per Djikstra's, we can trace back from the end point of the shortest path to the source.
        //Here, the lowest energy path can be thought of as a vertical path from top to bottomm where the least amount of interesting
        //things happen in the image. A seam with high energy would be a line where colors rapidly change - indicating an edge of some
        //object or interesting feature. We want to avoid these places in the seam we remove from the image. Thinking another way, if we
        //plot energy vs coordinates in 3d, the energy is a surface with peaks and valleys across a landscape. The lowest possible terrain is at
        //energy 0.
        //We want to cut along something like the lowest top to bottom path. In so doing we can avoid intereresting features

        //Note: interestingly, a similar approach with finding high energy  paths
        // would probably be useful for FINDING edges in computer vision applications

        for (int row=0; row<height-1; row++) {
            for (int col=0; col<width; col++) {
                for (int offset=-1; offset<=1; offset++) {
                    if ((col+offset>=0) && (col+offset<width)) {
                        if (currentOrientation==true){
                            if (distTo[row][col]+energy(col+offset, row+1) < distTo[row+1][col+offset]) {
                                distTo[row+1][col+offset]=distTo[row][col]+energy(col+offset, row+1);
                                vertexTo[row+1][col+offset] = -offset;
                            }
                        }
                        else if (currentOrientation==false) {
                            if (distTo[row][col]+energy(row+1, col+offset) < distTo[row+1][col+offset]) {
                                distTo[row+1][col+offset]=distTo[row][col]+energy(row+1, col+offset);
                                vertexTo[row+1][col+offset] = -offset;
                            }
                        }
                    }
                }
            }
        }

        // Keep running track of minimum Energy distance path, the energy total and the column on the bottom row where path ends
        //Iterate across the bottom row to find in which column is the endpoint on the bottom with the lowest distance from
        //top to bottom of image. The bottom end of the shortest path is there. Given that, you can then use the vertexTo to trace back,
        //row by row, to the top row
        double minDist=Double.POSITIVE_INFINITY;
        int minDistColumnIndex=-2;
        for (int col=0; col<width; col++) {
            if (distTo[height-1][col] < minDist) {
                minDist=distTo[height-1][col];
                minDistColumnIndex=col;
            }
        }

        //seam is an array of length height = number of rows in image.
        //seam[row] gives the column in which the vertical seam from top to bottom goes through at the given row
        //This form will make it easy to compute the new image and new associated energy matrix with the seam removed
        seam[height-1]=minDistColumnIndex;

        //trace back up from the bottom to create the seam.
        //seam[row-1] will be -1, 0, or 1 more than seam[row] depending on which of the three possible vertices on the row above
        //the seam descended down from: above left, above, above right
        int row=height-1;
        while (row>0) {
            seam[row-1] = seam[row] + vertexTo[row][seam[row]];
            row--;
        }
        return seam;  
    }
        
    /*remove horizontal seam from current picture verify validity of seam input 
    Expects the pic to be xpose. xpose if normal
    */
    public void removeHorizontalSeam(int[] seam) {
        if (seam==null) throw new NullPointerException("Provided seam is null");
        if (currentOrientation == true) transpose();
        removeSeam(seam);
    }
    
    // remove vertical seam from current 
    //Expects the picture to be in normal orientation. xpose if not.
    public void removeVerticalSeam(int[] seam) {
        if (seam==null) throw new NullPointerException("Provided seam is null");
        if (currentOrientation == false) transpose();
        removeSeam(seam);
    }

    //Removes a vertical seam
    private void removeSeam(int[] seam) {
        
        if (width<=1) throw new IllegalArgumentException("Picture <= 1 pixel across");

        if (seam.length != height) throw new IllegalArgumentException("seam too long");
        for (int row=0; row<seam.length; row++) {
            if (row<seam.length-1) {
                if (Math.abs(seam[row+1]-seam[row])>1) throw new IllegalArgumentException("jump more than one from step to step");;
            }
            
            if ((seam[row]<0)||(seam[row] > width-1)) throw new IllegalArgumentException("seam indices out of bounds");;
        }
        
        for (int row=0; row<height; row++) {
            int seamColumn = seam[row];
            //If the seamColumn is the rightmost in this orientation, no shift necessary
            if (seamColumn<width-1) {
                System.arraycopy(pixelColorData[row], seamColumn+1, pixelColorData[row], seamColumn, width-seamColumn-1);
                System.arraycopy(energies[row], seamColumn+1, energies[row], seamColumn, width-seamColumn-1);
            }
        }
        width--;
        
        //Adjusts energy on pixels on either side of the seam
        for (int row=0; row<height; row++) {
            int seamColumn=seam[row];
            if (currentOrientation==true) {
                if (seamColumn-1 >= 0) {
                    //Need to set to -1 first so that energy() will recognize it needs to recalculate
                    energies[row][seamColumn-1] = -1;
                    energies[row][seamColumn-1] = energy(seamColumn-1, row);
                }
                if (seamColumn < width) {
                    //Need to set to -1 first so that energy() will recognize it needs to recalculate
                    energies[row][seamColumn] = -1;
                    energies[row][seamColumn] = energy(seamColumn, row);
                }
            }
            else if (currentOrientation==false) {
                /*
                 * The energy() method assumes it's receiving coordinates in col, row for a matrix in normal orientation
                 * If currentOrientation==false, the matrix is in a transposed state, and coordinates are reversed
                 * energy(), to account for this, reverses the indices if a transpose state is detected. So have to take account of that here
                 * In this function, it works in the native row, col coordinates of the state of rotation. So if I need to make a 
                 * call to energy here, and the matrix is transposed, I need to flip the coordinates to put it in terms energy wants.
                 * The energies matrix coords don't have to flip because energies is transposed with pixelColorData
                 * Note also that since energies is (row, col) and energy is (col, row), flipping means the two calls have the numbers in
                 * the same order when currentOrientatio==false
                 */ 
                if (seamColumn-1 >= 0) {
                    //Need to set to -1 first so that energy() will recognize it needs to recalculate
                    energies[row][seamColumn-1] = -1;
                    energies[row][seamColumn-1] = energy(row,seamColumn-1);
                }
                if (seamColumn < width) {
                    //Need to set to -1 first so that energy() will recognize it needs to recalculate
                    energies[row][seamColumn] = -1; 
                    energies[row][seamColumn] = energy(row, seamColumn);
                }
            }
            
        }
    }
    
    
}
