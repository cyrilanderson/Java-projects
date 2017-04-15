//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/BinaryStdIn.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BinaryStdIn.java.html
import edu.princeton.cs.algs4.BinaryStdIn;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/BinaryStdOut.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BinaryStdOut.java.html
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;
    
    public static void encode() {
        String inputString = BinaryStdIn.readString();
        if (inputString.length()==1) {
            int first = 0;
            BinaryStdOut.write(first);
            char c = inputString.charAt(0);
            BinaryStdOut.write(c);
            BinaryStdOut.flush();
        }
        else {
            CircularSuffixArray circSuffArray = new CircularSuffixArray(inputString);
            int length = circSuffArray.length();
            int first=-1;
            char[] t = new char[length];
            //Build char array t[] of last characters in sorted circular suffix array and 
            //find first, the index of the original string in the sorted array
            for (int i=0; i<length; i++) {
                int idx = circSuffArray.index(i);
                if (idx==0) first=i;
                t[i]=inputString.charAt((idx+length-1)%length);
            }
            BinaryStdOut.write(first);
            for (int j=0; j<length; j++) {
                BinaryStdOut.write(t[j]);
            }
            BinaryStdOut.flush();
        }
    }
    
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String endChars = BinaryStdIn.readString();
        int numChars = endChars.length();
        if (numChars==1) {
            char c = endChars.charAt(0);
            BinaryStdOut.write(c);
            BinaryStdOut.flush();
        }
        else {
            char[] t = endChars.toCharArray();
            int[] next = new int[numChars];
            for (int i=0; i<numChars; i++) next[i] = i;
            sortChars(t, next);
            
            //Recover original string using first and next[] array. Outputs chars to BinaryStdOut as it goes
            int nextStringIndex=first;
            for (int m=1; m<=numChars; m++) {
                nextStringIndex = next[nextStringIndex];
                BinaryStdOut.write(t[nextStringIndex]);  
            }
            BinaryStdOut.flush();
        }
    }
    
    private static void sortChars(char[] a, int[] next) {
        int N = a.length;
        int[] count = new int[R+1];
        int[]  aux = new int[N];
        for (int i = 0; i < N; i++)
            count[a[i]+1]++;
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];
        for (int i = 0; i < N; i++) 
            aux[count[a[i]]++] = i;
        for (int i = 0; i < N; i++)
            next[i] = aux[i];
    }
    
    public static void main(String[] args) { 
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
    
}
