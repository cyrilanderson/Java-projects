//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Stack.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Stack.java.html
import edu.princeton.cs.algs4.Stack;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.HashMap;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/TST.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/TST.java.html
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    
    private DictionaryTrieST dictionaryWords;
    private TST<Integer> wordsFound;
    
    public BoggleSolver(String[] dictionary) { 
        dictionaryWords = new DictionaryTrieST();
        for (String word: dictionary) {
            int length = word.length();
            int score;
            if (length <3) continue;
            else if (length == 3 || length==4) score = 1;
            else if (length == 5 || length==6) score = length - 3;
            else if (length == 7) score = 5;
            else score = 11;
            dictionaryWords.put(word, score);
        }
    }
    
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        
        wordsFound = new TST<Integer>();
        
        //Extract the contents of board
        int rows = board.rows();
        int cols = board.cols();
        
        if (rows>1 || cols>1) {
              
            //REWORK AS HASHMAP<INTEGER, LINKEDLIST<SQUARE>>
//        This will impact precomputeBoardAdjacencies, across, and a few lines of dfsBoggleFinder
            HashMap<Integer, LinkedList<Integer>> boardStructure = new HashMap<Integer, LinkedList<Integer>>();
            for (int squares = 0; squares<rows*cols; squares++) {
                LinkedList<Integer> adjacentSquares = new LinkedList<Integer>();
                boardStructure.put(squares, adjacentSquares);
            }
        
            precomputeBoardAdjacencies(board, boardStructure);
            
            
            for (int i=0; i<rows*cols; i++) {
                dfsBoggleFinder(i, board,  boardStructure);
            }
        }
      
        return wordsFound.keys();

    }
    
    
    private void precomputeBoardAdjacencies(BoggleBoard board, HashMap<Integer, LinkedList<Integer>> boardStructure) {
        int rows = board.rows();
        int cols = board.cols();
        
        if (rows==1) {
            for (int i=0; i<cols; i++) {
                if (i>0) boardStructure.get(i).add(i-1);
                if (i<cols -1) boardStructure.get(i).add(i+1);
            }
        }
        
        else if (cols==1) {
            for (int j=0; j<rows; j++) {
                if (j>0) boardStructure.get(j).add(j-1);
                if (j < rows -1) boardStructure.get(j).add(j+1);
            }
        }
        
        else {
            for (int k=0; k<rows*cols; k++) {
            //Translate 1D index into (i,j) row / col index
                int row = k/cols;
                int col = k%cols;
                //Case 1: square on leftmost column
                
                if (col==0) {
                    //Square to right always connected
                    boardStructure.get(k).add(k+1);
                    //Case 1a: On leftmost column, not at top. So connected to square above and above right
                    if (row > 0) {
                        boardStructure.get(k).add(k-cols);
                        boardStructure.get(k).add(k-cols+1);
                    }
                    
                    //Case 1b: On leftmost column, not at bottom. So connected to square below and below right
                    if (row < rows - 1) {
                        boardStructure.get(k).add(k+cols);
                        boardStructure.get(k).add(k+cols+1);
                    }
                }
                
                //Case 2: square on rightmost column
                else if (col==cols-1) {
                    //Square to left always connected
                    boardStructure.get(k).add(k-1);
                    //Case 1a: On rightmost column, not at top. So connected to square above and above left
                    if (row > 0) {
                        boardStructure.get(k).add(k-cols);
                        boardStructure.get(k).add(k-cols-1);
                    }
                    
                    //Case 1b: On rightmost column, not at bottom. So connected to square below and below left
                    if (row < rows - 1) {
                        boardStructure.get(k).add(k+cols);
                        boardStructure.get(k).add(k+cols-1);
                    }
                }
                
                //Case 3: in middle columns
                else {
                    //Squares to left and right always connected
                    boardStructure.get(k).add(k-1);
                    boardStructure.get(k).add(k+1);
                    //Case 3a: Not at top row. Connected to above left, above, above right
                    if (row > 0) {
                        boardStructure.get(k).add(k-cols-1);
                        boardStructure.get(k).add(k-cols);
                        boardStructure.get(k).add(k-cols+1);
                    }
                    
                    //Case 3a: Not at bottom row. Connected to below left, below, below right
                    
                    if (row < rows - 1) {
                        boardStructure.get(k).add(k+cols-1);
                        boardStructure.get(k).add(k+cols);
                        boardStructure.get(k).add(k+cols+1);
                    }
                }
            }
        }
    }
    
    private void dfsBoggleFinder(int squareNumber, BoggleBoard board,  HashMap<Integer, LinkedList<Integer>> boardStructure) {
        int rows = board.rows();
        int cols = board.cols();
        int numSquares = rows*cols;
        boolean[] marked = new boolean[numSquares];
        StringBuilder prefix = new StringBuilder();
        dfsBoggleFind(squareNumber, board, boardStructure, marked, prefix);
    }

    // depth first search from v
    private void dfsBoggleFind(int v, BoggleBoard board,  HashMap<Integer, LinkedList<Integer>> boardStructure, boolean[] marked, StringBuilder prefix) {
        
        int cols = board.cols();
        char letter = board.getLetter(v/cols, v%cols);
        //Checks to see if adding the letter will give a valid prefix for the dictionary. If letter is Q, checks if Qu can be added and 
        //still have a valid prefix
        if (dictionaryWords.canExtendPrefix(letter)) {
            prefix.append(letter);
            if (letter=='Q') prefix.append('U');
            dictionaryWords.addToPrefix(letter);
            marked[v] = true;
            String current = prefix.toString();
            if (dictionaryWords.isValidWord(current)) wordsFound.put(current, scoreOf(current));    
            for (Integer w : boardStructure.get(v)) {
                if (!marked[w]) {
                    dfsBoggleFind(w, board, boardStructure, marked, prefix);
                }
            }
            int length = prefix.length();
            char finalChar = prefix.charAt(length-1);
            prefix.deleteCharAt(length-1);
            dictionaryWords.backupPrefixQuery();
            marked[v] = false;
            if (finalChar == 'U' && length > 1) {
                if (prefix.charAt(length-2) == 'Q') {
                    prefix.deleteCharAt(length-2);
                    dictionaryWords.backupPrefixQuery();
                }
            }
        }
    }

    
    
    public int scoreOf(String word) {
        return dictionaryWords.get(word);
    }
    
    public static void main(String[] args) { 
        
    }
    
}
