import java.util.Iterator;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdRandom.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/StdRandom.java.html
import edu.princeton.cs.algs4.StdRandom;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Queue.html
//http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Queue.java.html
import edu.princeton.cs.algs4.Queue;


public class Board {
    private int[] board;
    private int N;
    
    public Board(int[][] blocks) { 
        N=blocks.length;
        board=new int[N*N];
        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                board[i*N+j]=blocks[i][j];
            }
        }
    }
    
    public int dimension(){
        return N;
    }
    
    public int hamming(){
        int hammingScore=0;
        for (int cell=0; cell<N*N-1; cell++){
            if (board[cell]!=cell+1) hammingScore+=1;
        }
        return hammingScore;   
    }
    
    public int manhattan(){
        int manhattanScore=0;
        for (int cell=0; cell<N*N; cell++){
            int actualValue=board[cell];
            int actualRow=cell/N;
            int actualColumn=cell%N;
            int theoreticalRow;
            int theoreticalColumn;
            if (actualValue!=0){
                theoreticalRow=(actualValue-1)/N;
                theoreticalColumn=(actualValue-1)%N;
                manhattanScore+=Math.abs(actualRow-theoreticalRow)+Math.abs(actualColumn-theoreticalColumn);   
            }
        
        }
        return manhattanScore;
    }
    
    public boolean isGoal(){
        if (board[N*N-1]!=0) return false;
        for (int i=0; i<N*N-1; i++){
            if (board[i]!=i+1) return false;
        }
        return true;
    }
    
    private Board swap(int p, int q) {
        /**
         * Private auxiliary function used by twin() and neighbors()
         * Given two cell numbers, will return a new board with cells swapped
         * 
         */
        int[][] temp = new int[N][N];
        for (int i=0; i<N*N; i++){
            int row=i/N;
            int col=i%N;
            temp[row][col]=board[i];
        }
        Board swapped=new Board(temp);
        int r=swapped.board[p];
        swapped.board[p]=swapped.board[q];
        swapped.board[q]=r;
        return swapped;
    }
    
    /**locates the cell containing the zero.
      * Auxiliary function for neighbors()
      */
    private int findZeroLocation(){
        int target=0;
        for (int cell=0; cell<N*N; cell++){
            if (board[cell]==0) target=cell; 
        }
        return target;
    }
    
    /**
         * Generate two random numbers first and second between 0 and N-1 such that:
         * i) The numbers are distinct
         * ii) board doesn't contain a zero at either cell number
         * Board twinBoard = this.swap(first, second);
         * return twinBoard;
         */
    public Board twin(){
        
        boolean flag=false;
        int first=0;
        int second=0;
        while (!flag){
            first=StdRandom.uniform(N*N);
            second=StdRandom.uniform(N*N);
            if ((first!=second)&&(board[first]!=0)&&(board[second]!=0)) flag=true;
        }
        Board twinBoard=swap(first, second);
        return twinBoard;
    }
    
    public boolean equals(Object y){
        if (y==this) return true;
        if (y==null) return false;
        if (y.getClass()!=this.getClass()) return false;
        Board that=(Board) y;
        if (this.N!=that.N) return false;
        for (int i=0; i<N*N; i++) {
            if (that.board[i]!=this.board[i]) return false;
        }
        return true;
    }
    
    /**
         * Create a new Queue<Board> neighborList
         * 1. Go through board to find the cell containing 0 item
         * 2. Find its row and column in the grid, (i,j)
         * 3. Identify potential cells around zero cell - (i-1,j), (1+1,j), (i,j-1), (i,j+1)
         * 4. Rule out possible cells around zero cell if cell with zero is on edge or corner
         * 5. For each valid adjacent cell, 
         *   i)Generate new board with zero and that cell's contents are switched
         *   ii) Enqueue this board to the Queue
         * return neighborList
         */
    public Iterable<Board> neighbors(){
        
        Queue<Board> neighborList=new Queue<Board>();
        int zeroLocation=findZeroLocation();
        if (zeroLocation>=N){
            Board up=swap(zeroLocation, zeroLocation-N);
            neighborList.enqueue(up);
        }
        if (zeroLocation<(N-1)*N){
            Board down=swap(zeroLocation, zeroLocation+N);
            neighborList.enqueue(down);
        }
        if (zeroLocation%N >0){
            Board left=swap(zeroLocation, zeroLocation-1);
            neighborList.enqueue(left);
        }
        if (zeroLocation%N<N-1){
            Board right=swap(zeroLocation, zeroLocation+1);
            neighborList.enqueue(right);
        }
        return neighborList;
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i*N+j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    public static void main(String[] args) { 
        
    }
    

}
