//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Digraph.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Digraph.java.html
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.Arrays;


public class SAP {
    
    private Digraph SAPGraph;
    private HashMap<String, int[]> searchCache;

    //cache recently computed length() and ancestor() queries. Both ancestor and length computed at the sanne time.
    //So someone searching for one would probably seek the other.
    //private int lastAncestor; //actually should keep track of the v,w pair that led to it too
   // private int lastLength; //actually should keep track of the v,w pair that led to it too
    
    public SAP(Digraph G) { 
        if (G==null) throw new NullPointerException("Null argument");
        SAPGraph=new Digraph(G);
        searchCache = new HashMap<String, int[]>(100);
    }
    
    //I should refactor the two length() and ancestor() pairs of methods to extract out a common helper function.
    //Helper function can turn search items into string, check cache for it, if so, retrieve computed values, return one desired
    //Otherwise launch BFS search, find two values, store them in cache with search string, and then return desired value.
    public int length(int v, int w) {
        if ((v>=SAPGraph.V())||(v<0)||(w>=SAPGraph.V())||(w<0)) throw new IndexOutOfBoundsException("Vertices out of bounds");
        int length;
        int ancestor;
        String lookup="";
        String vToString = Integer.toString(v);
        String wToString = Integer.toString(w);
        lookup = lookup.concat(vToString).concat(",").concat(wToString);
        if (searchCache.containsKey(lookup)) {
            int[] storedResult = searchCache.get(lookup);
            length = storedResult[1];
        }
        else {
            BFSAncestor search = new BFSAncestor(SAPGraph, v, w);
            length = search.getLength();
            ancestor = search.getAncestor();
            int[] storedResult = new int[2];
            storedResult[0] = ancestor;
            storedResult[1] = length;
            searchCache.put(lookup, storedResult);
        }
        
        return length;
    }
    
    //I should refactor the two length() and ancestor() pairs of methods to extract out a common helper function.
    //Helper function can turn search items into string, check cache for it, if so, retrieve computed values, return one desired
    //Otherwise launch BFS search, find two values, store them in cache with search string, and then return desired value.
    public int ancestor(int v, int w) {
        if ((v>=SAPGraph.V())||(v<0)||(w>=SAPGraph.V())||(w<0)) throw new IndexOutOfBoundsException("Vertices out of bounds");
        int length;
        int ancestor;
        String lookup="";
        String vToString = Integer.toString(v);
        String wToString = Integer.toString(w);
        lookup = lookup.concat(vToString).concat(",").concat(wToString);
        if (searchCache.containsKey(lookup)) {
            int[] storedResult = searchCache.get(lookup);
            ancestor = storedResult[0];
        }
        else {
            BFSAncestor search = new BFSAncestor(SAPGraph, v, w);
            length = search.getLength();
            ancestor = search.getAncestor();
            int[] storedResult = new int[2];
            storedResult[0] = ancestor;
            storedResult[1] = length;
            searchCache.put(lookup, storedResult);
        }
        return ancestor;
    }
    
    //I should refactor the two length() and ancestor() pairs of methods to extract out a common helper function.
    //Helper function can turn search items into string, check cache for it, if so, retrieve computed values, return one desired
    //Otherwise launch BFS search, find two values, store them in cache with search string, and then return desired value.
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v==null)||(w==null)) throw new NullPointerException("Null argument");
        for (int x: v) {
            if ((x>=SAPGraph.V())||(x<0)) throw new IndexOutOfBoundsException("Vertices out of bounds");
        }
        for (int y:w) {
            if ((y>=SAPGraph.V())||(y<0)) throw new IndexOutOfBoundsException("Vertices out of bounds");
        }
        
        int length;
        int ancestor;
        String lookup="";
        
        
        for (int num: v) {
            String numString = Integer.toString(num);
            lookup = lookup.concat(numString).concat("x");
        }
        
        lookup = lookup.concat(",");
        
        for (int num: w) {
            String numString = Integer.toString(num);
            lookup = lookup.concat(numString).concat("x");
        }
        
        if (searchCache.containsKey(lookup)) {
            int[] storedResult = searchCache.get(lookup);
            length = storedResult[1];
        }
        else {
            BFSAncestor search = new BFSAncestor(SAPGraph, v, w);
            length = search.getLength();
            ancestor = search.getAncestor();
            int[] storedResult = new int[2];
            storedResult[0] = ancestor;
            storedResult[1] = length;
            searchCache.put(lookup, storedResult);
        }
        
        return length;
    }
    
    //I should refactor the two length() and ancestor() pairs of methods to extract out a common helper function.
    //Helper function can turn search items into string, check cache for it, if so, retrieve computed values, return one desired
    //Otherwise launch BFS search, find two values, store them in cache with search string, and then return desired value.
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v==null)||(w==null)) throw new NullPointerException("Null argument");
        for (int x: v) {
            if ((x>=SAPGraph.V())||(x<0)) throw new IndexOutOfBoundsException("Vertices out of bounds");
        }
        for (int y:w) {
            if ((y>=SAPGraph.V())||(y<0)) throw new IndexOutOfBoundsException("Vertices out of bounds");
        }
        int length;
        int ancestor;
        String lookup="";
        
        
        for (int num: v) {
            String numString = Integer.toString(num);
            lookup = lookup.concat(numString).concat("x");
        }
        
        lookup = lookup.concat(",");
        
        for (int num: w) {
            String numString = Integer.toString(num);
            lookup = lookup.concat(numString).concat("x");
        }
        
        if (searchCache.containsKey(lookup)) {
            int[] storedResult = searchCache.get(lookup);
            ancestor = storedResult[0];
        }
        else {
            BFSAncestor search = new BFSAncestor(SAPGraph, v, w);
            length = search.getLength();
            ancestor = search.getAncestor();
            int[] storedResult = new int[2];
            storedResult[0] = ancestor;
            storedResult[1] = length;
            searchCache.put(lookup, storedResult);
        }
        
        return ancestor;
    }
    
    
    
    public static void main(String[] args) { 
        
    }
    
    
    
}
