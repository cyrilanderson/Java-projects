import java.util.ArrayList;
import java.util.HashMap; 

public class Outcast {
    private WordNet semantic;
    private String outcast;
    private int maxDistance;
    private HashMap<ArrayList<String>, Integer> computedDistances;
    
    public Outcast(WordNet wordnet) { 
        if (wordnet==null) throw new NullPointerException("Null argument");
        semantic=wordnet;
        computedDistances = new HashMap<ArrayList<String>, Integer>(1000);
    }
    
    public String outcast(String[] nouns) {
        if (nouns==null) throw new NullPointerException("Null argument");
       
        maxDistance=0;
        int numNouns = nouns.length;
        for (int i=0; i<numNouns; i++) {
            String wordi = nouns[i];
            if (!semantic.isNoun(wordi)) throw new IllegalArgumentException("One or more of the words received not a WordNet noun.");
            int di = 0;
            for (int j=0; j<numNouns; j++) {
                int distij;
                String wordj = nouns[j];
                ArrayList<String> wordPair_ij = new ArrayList<String>(2);
                ArrayList<String> wordPair_ji = new ArrayList<String>(2);
                wordPair_ij.add(0, wordi);
                wordPair_ij.add(1, wordj);
                wordPair_ji.add(0, wordj);
                wordPair_ji.add(1, wordi);
                if (computedDistances.containsKey(wordPair_ij)) {
                    distij = computedDistances.get(wordPair_ij);
                }
                else {
                    distij = semantic.distance(wordi, wordj);
                    computedDistances.put(wordPair_ij, distij);
                    computedDistances.put(wordPair_ji, distij);
                }
                di+=distij;
            }
            if (di > maxDistance) {
                maxDistance = di; 
                outcast = wordi;
            }
        }
        return outcast;
     
    }
    
    
    public static void main(String[] args) { 
        
    }
    

    
}
