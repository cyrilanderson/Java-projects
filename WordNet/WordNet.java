import java.util.HashMap;
import java.util.Set;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Digraph.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Digraph.java.html
import edu.princeton.cs.algs4.Digraph;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Bag.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Bag.java.html
import edu.princeton.cs.algs4.Bag;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/In.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/In.java.html
import edu.princeton.cs.algs4.In;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/DirectedCycle.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/DirectedCycle.java.html
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private SAP wordNetGraph;
    private HashMap<String, Bag<Integer>> nounToSynsetIDs;
    private HashMap<Integer, String> idToSynset;
    
    
    public WordNet(String synsets, String hypernyms) { 
        if ((synsets==null)||(hypernyms==null)) throw new NullPointerException("Null argument");
        In synsetsInput = new In(synsets);
        String[] synsetsData = synsetsInput.readAllLines();
        int numSynsets = synsetsData.length;
        nounToSynsetIDs = new HashMap<String, Bag<Integer>>(2*numSynsets);
        idToSynset = new HashMap<Integer, String>(4*numSynsets/3);
        int maxID = -1;
        for (String lineContents: synsetsData) {
            String[] lineParts = lineContents.split(",");
            String id = lineParts[0];
            int idNum = Integer.parseInt(id);
            if (idNum > maxID) maxID = idNum;
            String synset = lineParts[1];
            idToSynset.put(idNum, synset);
            String[] nouns = synset.split("\\s+");
            for (String noun: nouns) {
                if (!nounToSynsetIDs.containsKey(noun)) {
                    Bag<Integer> synsetIDs = new Bag<Integer>();
                    synsetIDs.add(idNum);
                    nounToSynsetIDs.put(noun, synsetIDs);
                }
                else nounToSynsetIDs.get(noun).add(idNum);
            }
        }
        
      
        Digraph WordGraph = new Digraph(maxID+1);
        In hypernymsInput = new In(hypernyms);
        while (!hypernymsInput.isEmpty()) {
            String hypernymsLine = hypernymsInput.readLine();
            String[] lineComponents = hypernymsLine.split(",");
            int synsetID = Integer.parseInt(lineComponents[0]);
            if (lineComponents.length >1) {
                for (int i=1; i<lineComponents.length; i++) {
                    WordGraph.addEdge(synsetID, Integer.parseInt(lineComponents[i]));
                }
            }
        }
        
        int vertices = WordGraph.V();
        int numRoots = 0;
        for (int v = 0; v<vertices; v++) {
            if (WordGraph.outdegree(v) == 0) numRoots++;
        }
        
        if (numRoots > 1) throw new IllegalArgumentException("Not a rooted Digraph. Multiple trees, multiple roots, forest.");
        
        DirectedCycle searchForDirectedCycle = new DirectedCycle(WordGraph);
        if (searchForDirectedCycle.hasCycle()) throw new IllegalArgumentException("Not a DAG. Cycle found");
        
        
        wordNetGraph = new SAP(WordGraph);
   
    }
    
    public Iterable<String> nouns() {
        Set<String> nounSet = nounToSynsetIDs.keySet();
        return nounSet;
    }
    
    public boolean isNoun(String word) {
        if (word==null) throw new NullPointerException("Null argument");
        return nounToSynsetIDs.get(word)!=null;
    }
    
    private Iterable<Integer> synsetsFromNoun(String noun) {
        if (noun==null) throw new NullPointerException("Null argument");
        return nounToSynsetIDs.get(noun);
    }
    
    private String synsetFromID(int id){
        return idToSynset.get(id);
    }
    
    public int distance(String nounA, String nounB) {
        if ((nounA==null)||(nounB==null)) throw new NullPointerException("Null argument");
        if ((!isNoun(nounA))||(!isNoun(nounB))) throw new IllegalArgumentException("At least one of the words not a valid WordNet noun");
        if (nounA.equals(nounB)) return 0;
        Iterable<Integer> nounASynsets = synsetsFromNoun(nounA);
        Iterable<Integer> nounBSynsets = synsetsFromNoun(nounB);
        int distance = wordNetGraph.length(nounASynsets, nounBSynsets);
        return distance;
    }
    public String sap(String nounA, String nounB) {
        if ((nounA==null)||(nounB==null)) throw new NullPointerException("Null argument");
        if ((!isNoun(nounA))||(!isNoun(nounB))) throw new IllegalArgumentException("At least one of the words not a valid WordNet noun");
        Iterable<Integer> nounASynsets = synsetsFromNoun(nounA);
        Iterable<Integer> nounBSynsets = synsetsFromNoun(nounB);
        int ancestorSynsetID = wordNetGraph.ancestor(nounASynsets, nounBSynsets);
        String sap = synsetFromID(ancestorSynsetID);
        return sap;
    }
    
    public static void main(String[] args) { 
        
    }
    

    
}
