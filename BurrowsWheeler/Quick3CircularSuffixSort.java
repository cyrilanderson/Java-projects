//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdRandom.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/StdRandom.java.html
import edu.princeton.cs.algs4.StdRandom;

/**
 * Modification of 3-way radix quicksort to handle sorting circular suffix array of a string
 * The suffix strings are not represented explicitly. An array of ints is passed in, with the ints
 * representing the index of the particular suffix array. Character contents at each location in each array is
 * worked out through modular arithmetic based on the fact each suffixx array is simply a circular permutation of the orginal string
 */

public class Quick3CircularSuffixSort {
    
    private static final int CUTOFF =  15;   // cutoff to insertion sort

    // do not instantiate
    private Quick3CircularSuffixSort() { } 

    /**  
     * Rearranges the array of strings in ascending order.
     *
     * @param a the array to be sorted
     */
    public static void sort(int[] a, String s) {
        StdRandom.shuffle(a);
        sort(a, s, 0, a.length-1, 0);
//        assert isSorted(a);
    }

    // return the dth character of the ith suffix array of s, -1 if d = length of s
    private static int charAt(String s, int i, int d) { 
        int length = s.length();
        assert d >= 0 && d <= length;
        if (d == length) return -1;
        return s.charAt((i+d)%length);
    }


    // 3-way string quicksort a[lo..hi] starting at dth character
    private static void sort(int[] a, String s, int lo, int hi, int d) { 

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, s, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(s, a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(s, a[i], d);
            if      (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(a, s, lo, lt-1, d);
        if (v >= 0) sort(a, s, lt, gt, d+1);
        sort(a, s, gt+1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private static void insertion(int[] a, String s, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d, s); j--)
                exch(a, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    // DEPRECATED BECAUSE OF SLOW SUBSTRING EXTRACTION IN JAVA 7
    // private static boolean less(String v, String w, int d) {
    //    assert v.substring(0, d).equals(w.substring(0, d));
    //    return v.substring(d).compareTo(w.substring(d)) < 0; 
    // }

    // is suffix array v of s less than suffix array w of s, starting at character d
    //If strings identical from d onwards, return false. 
    private static boolean less(int v, int w, int d, String s) {
//        assert v.substring(0, d).equals(w.substring(0, d));
        int length = s.length();
        for (int i = d; i < length; i++) {
            if (s.charAt((v+i)%length) < s.charAt((w+i)%length)) return true;
            if (s.charAt((v+i)%length) > s.charAt((w+i)%length)) return false;
        }
        return false;
    }

    // is the array sorted
//    private static boolean isSorted(int[] a, String s) {
//        for (int i = 1; i < a.length; i++)
//            if (a[i].compareTo(a[i-1]) < 0) return false;
//        return true;
//    }
    
    
    public static void main(String[] args) { 
        
    }
    
}
