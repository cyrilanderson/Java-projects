
import java.util.TreeMap;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Queue.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Queue.java.html
import edu.princeton.cs.algs4.Queue;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Digraph.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Digraph.java.html
import edu.princeton.cs.algs4.Digraph;

public class BFSAncestor {
    private TreeMap<Integer, Integer> distFromSource;
    private Queue<Integer> q;
    private int ancestor;
    private int length;
        
    
    public BFSAncestor(Digraph G, int v, int w) { 
        distFromSource = new TreeMap<Integer, Integer>();
        bfsancestor(G, v, w);
    }
    
    public BFSAncestor(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
        distFromSource = new TreeMap<Integer, Integer>();
        bfsancestor(G, v, w);
    }
    
    public void bfsancestor(Digraph G, int v, int w) {
        if (v==w) {
            ancestor=v;
            length=0;
        }
        
        else {
            
            length=Integer.MAX_VALUE;
            ancestor = -1;
            
            Queue<Integer> q = new Queue<Integer>();
            distFromSource.put(v, 0);
            distFromSource.put(~w, 0);
            q.enqueue(v);
            q.enqueue(~w);
            while(!q.isEmpty()) {
                int next = q.dequeue();
                int dist = distFromSource.get(next);
                if (1+dist < length) {
                    if (next>=0) { 
                        for (int adjacent: G.adj(next)) {
                            if (!distFromSource.containsKey(adjacent)) {
                                distFromSource.put(adjacent, 1+dist);
                                q.enqueue(adjacent);
                                if (distFromSource.containsKey(~adjacent)) {
                                    int total = 1 + dist +distFromSource.get(~adjacent);
                                    if (total < length) {
                                        length=total;
                                        ancestor=adjacent;
                                    }
                                }
                            }
                        }
                    }
                    else if (next<0) {
                        for (int adjacent: G.adj(~next)) {
                            if (!distFromSource.containsKey(~adjacent)) {
                                distFromSource.put(~adjacent, 1+dist);
                                q.enqueue(~adjacent);
                                if (distFromSource.containsKey(adjacent)) {
                                    int total = distFromSource.get(adjacent) + 1 + dist;
                                    if (total < length) {
                                        length=total;
                                        ancestor=adjacent;
                                    }
                                }
                            }
                        }
                    }
                } 
            }
        }      
    }
        
    
    public void bfsancestor(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {

        length=Integer.MAX_VALUE;
        ancestor = -1;
        
        Queue<Integer> q = new Queue<Integer>();
        for (int s: v) {
            distFromSource.put(s, 0);
            q.enqueue(s);
        }
        
        for (int t: w) {
            distFromSource.put(~t, 0);
            q.enqueue(~t);
            if (distFromSource.containsKey(t)) {
                length = 0;
                ancestor = t;
            }
        }
        while(!q.isEmpty()) {
            int next = q.dequeue();
            int dist = distFromSource.get(next);
            if (1+dist < length) {
                if (next >= 0) { 
                    for (int adjacent: G.adj(next)) {
                        if (!distFromSource.containsKey(adjacent)) {
                            distFromSource.put(adjacent, 1+dist);
                            q.enqueue(adjacent);
                            if (distFromSource.containsKey(~adjacent)) {
                                int total = 1+dist + distFromSource.get(~adjacent);
                                if (total < length) {
                                    length=total;
                                    ancestor=adjacent;
                                }
                            }
                        }
                    }
                }
                else if (next<0) {
                    for (int adjacent: G.adj(~next)) {
                        if (!distFromSource.containsKey(~adjacent)) {
                            distFromSource.put(~adjacent, 1+dist);
                            q.enqueue(~adjacent);
                            if (distFromSource.containsKey(adjacent)) {
                                int total = distFromSource.get(adjacent) + 1 + dist;
                                if (total < length) {
                                    length=total;
                                    ancestor=adjacent;
                                }
                            }
                        }
                    }
                }
            } 
        }
    }
    
    public int getLength() {
        if (length < Integer.MAX_VALUE) return length;
        else return -1;
    }
    
    public int getAncestor() {
        return ancestor;
    }
}
