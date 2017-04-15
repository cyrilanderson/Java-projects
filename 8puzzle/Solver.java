//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Stack.html
import edu.princeton.cs.algs4.Stack;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/MinPQ.html
import edu.princeton.cs.algs4.MinPQ;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdOut.html
import edu.princeton.cs.algs4.StdOut;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/In.html
import edu.princeton.cs.algs4.In;

public class Solver {
    private boolean solvable;
    private int moves;
    private Stack<Board> solutionSequence;
    private MinPQ<SearchNode> searchNodes;
    
    public Solver(Board initial) { 
        
        if (initial==null) throw new NullPointerException("Empty board.");
        
        Board twinBoard=initial.twin();    
        searchNodes=new MinPQ<SearchNode>();
        SearchNode initNode=new SearchNode(initial, 0, null);
        SearchNode twinNode=new SearchNode(twinBoard, 0, null);
        searchNodes.insert(initNode);
        searchNodes.insert(twinNode);
        solutionSequence=new Stack<Board>();
        boolean stop=false;
        while (!stop){
            SearchNode removed=searchNodes.delMin();
            if (removed.board.isGoal()) {
                moves=removed.move;
                stop=true;
                solutionSequence.push(removed.board);
                SearchNode prior=removed.previous;
                while (prior!=null){
                    solutionSequence.push(prior.board);
                    prior=prior.previous;
                }
                Board last=solutionSequence.peek();
                if (last.equals(initial)) {
                    solvable=true;
                }
                else {
                    solvable=false;
                    solutionSequence=null;
                    moves=-1;
                }
            }
            else /**(removed.board().isGoal()==false */ {
                Iterable<Board> neighbors=removed.board.neighbors();
                for (Board neighbor: neighbors) {
                    if ((removed.previous==null)||(!neighbor.equals(removed.previous.board))){
                        SearchNode toAdd=new SearchNode(neighbor, removed.move+1, removed);
                        searchNodes.insert(toAdd);                        
                    }/**if*/
                }/**for*/                 
                    
            }/**else*/    
        }/**while*/
    }/**Solver constructor*/
    
    public boolean isSolvable(){
        return solvable;
    }
    
    public int moves(){
        return moves;
    }
    
    public Iterable<Board> solution(){
        return solutionSequence;
    }
        
    private static class SearchNode implements Comparable<SearchNode>{
        private Board board;
        private int move;
        private int priority;
        private SearchNode previous;
        
        public SearchNode(Board gameBoard, int movesMade, SearchNode priorNode){
            board=gameBoard;
            move=movesMade;
            priority=board.manhattan()+move;
            previous=priorNode;
        }
        
        public int move(){
            return move();
        }
        
        public Board board() {
            return board;
        }
        
        public SearchNode previous() {
            return previous;
        }
        
        public int compareTo(SearchNode that){
            if (priority < that.priority) return -1;
            if (priority > that.priority) return 1;
            if (priority==that.priority) {
                if ((priority-move)<(that.priority-that.move)) return -1;
                if ((priority-move)>(that.priority-that.move)) return 1;
            }
            return 0;
        }
    }
            
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
    
    
}
