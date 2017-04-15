//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Queue.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Queue.java.html
import edu.princeton.cs.algs4.Queue;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Stack.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Stack.java.html
import edu.princeton.cs.algs4.Stack;

public class DictionaryTrieST {
    private static final int R = 26;        // Upper case letters from A to Z, ASCII 65 to 90


    private Node root;      // root of trie
    private int N;          // number of keys in trie
    private Stack<Node> prefixQuery;

    // R-way trie node
    private static class Node {
        private int val;
        private Node[] next = new Node[R];
    }

   /**
     * Initializes an empty string symbol table and initializes the prefixQuery stack
     */
    public DictionaryTrieST() {
        prefixQuery = new Stack<Node>();
    }


    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the Boggle score value associated with the given key if the key is in the symbol table
     *     and <tt>0</tt> if the key is not in the symbol table
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public int get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return 0;
        return x.val;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        return get(key) != 0;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c-65], key, d+1);
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(String key, int val) {
        root = put(root, key, val, 0);
        //When first key-val pair inserted into the dictionary, put root onto the prefixQuery
        if (N==1) prefixQuery.push(root);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == 0) N++;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c-65] = put(x.next[c-65], key, val, d+1);
        return x;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return N;
    }

    /**
     * Is this symbol table empty?
     * @return <tt>true</tt> if this symbol table is empty and <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean canExtendPrefix(char s) {
        if (isEmpty() || prefixQuery.peek().next[s-65] == null) return false;
        else {
            if (s=='Q') {
                Node nextCharNode = prefixQuery.peek().next[s-65];
                prefixQuery.push(nextCharNode);
                s='U';
                if (prefixQuery.peek().next[s-65] != null) {
                    backupPrefixQuery();
                    return true;
                }
                else {
                    backupPrefixQuery();
                    return false;
                }    
            }
            else return true;
        }
    }
    
    public void addToPrefix(char s) {
        Node nextCharNode = prefixQuery.peek().next[s-65];
        prefixQuery.push(nextCharNode);
        if (s=='Q') {
            s='U';
            nextCharNode = prefixQuery.peek().next[s-65];
            prefixQuery.push(nextCharNode);
        }
    }
    
    public void backupPrefixQuery() {
        if (!prefixQuery.isEmpty()) prefixQuery.pop();
    }
    
    public boolean isValidWord(String word) {
        return contains(word);
    }

}
