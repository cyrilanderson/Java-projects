//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/BinaryStdIn.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BinaryStdIn.java.html
import edu.princeton.cs.algs4.BinaryStdIn;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/BinaryStdOut.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BinaryStdOut.java.html
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    
    public static void encode() {
        LinkedList<Character> symbols = new LinkedList<Character>();
        for (int i=0; i<256; i++) {
            Character c = (char) i;
            symbols.add(i, c);
        }
        while (!BinaryStdIn.isEmpty()) {
            char nextChar = BinaryStdIn.readChar(); 
            int location = symbols.indexOf(nextChar);
            symbols.remove(location);
            symbols.add(0, nextChar);
            BinaryStdOut.write((char) location);
        }
        BinaryStdOut.flush();
    }
    
    public static void decode() {
        LinkedList<Character> symbols = new LinkedList<Character>();
        for (int i=0; i<256; i++) {
            Character c = (char) i;
            symbols.add(i, c);
        }
        while (!BinaryStdIn.isEmpty()) {
            int location = BinaryStdIn.readChar();
            char nextChar = symbols.get(location);
            symbols.remove(location);
            symbols.add(0, nextChar);
            BinaryStdOut.write(nextChar);
        }
        BinaryStdOut.flush();
    }
    
    public static void main(String[] args) { 
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
    
}
