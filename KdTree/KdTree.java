//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdDraw.html
import edu.princeton.cs.algs4.StdDraw;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdOut.html
import edu.princeton.cs.algs4.StdOut;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Point2D.html
import edu.princeton.cs.algs4.Point2D;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/RectHV.html
import edu.princeton.cs.algs4.RectHV;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Stack.html
import edu.princeton.cs.algs4.Stack;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/In.html
import edu.princeton.cs.algs4.In;

public class KdTree {
    private Node root;
    private int N;
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        
        public Node (Point2D p, RectHV rect) {
            this.p=p;
            this.rect=rect;
            this.lb=null;
            this.rt=null;
        }
    }
    
    public KdTree() { 
        root=null;
        N=0;
    }
    
    
    public boolean isEmpty(){
        return size()==0;
    }
    
    public int size(){
        return N;
    }
    
    public void insert(Point2D p){
        if (p==null) throw new NullPointerException("argument to insert() is null");
        root=insert(root, p, 0.0, 0.0, 1.0, 1.0, 1);
    }
    
    private Node insert(Node node, Point2D pt, double xmin, double ymin, double xmax, double ymax, int orientation) {
        if (node==null) {
            N++;
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            return new Node(pt, rect);
        }
        if (orientation==1){
            if (pt.x() < node.p.x()) node.lb=insert(node.lb, pt, xmin, ymin, node.p.x(), ymax, -1);
            else if ((pt.x() > node.p.x())||(!pt.equals(node.p))) node.rt=insert(node.rt, pt, node.p.x(), ymin, xmax, ymax, -1);
         
        }
        else if (orientation==-1) {
            if (pt.y()<node.p.y()) node.lb=insert(node.lb, pt, xmin, ymin, xmax, node.p.y(), 1);
            else if ((pt.y() > node.p.y())||(!pt.equals(node.p))) node.rt=insert(node.rt, pt, xmin, node.p.y(), xmax, ymax, 1);      
        }
        return node;
    }
    
    public boolean contains(Point2D p){
        if (p==null) throw new NullPointerException("argument to contains() is null");
        return get(root, p, 1)!=null;
    }
    
    private Point2D get(Node node, Point2D pt, int orientation){
        if (node==null) return null;
        if (pt.equals(node.p)) return pt;
        else {
            if (orientation==1){
                double cmp=pt.x()-node.p.x();
                if (cmp<0) return get(node.lb, pt, -1);
                else return get(node.rt, pt, -1);
            }
            else {
                double cmp=pt.y()-node.p.y();
                if (cmp<0) return get(node.lb, pt, 1);
                else return get(node.rt, pt, 1);
            }
        }
    }
    
    public void draw(){
        StdDraw.show(0);
        StdDraw.setXscale(-0.25, 1.25);
        StdDraw.setYscale(-0.25, 1.25);
        if (root!=null) {
            root.rect.draw();
            draw(root, 1);
        }
        StdDraw.show();
    }
    
    private void draw(Node node, int orientation) {
        if (node!=null) {
            
            RectHV rect=node.rect;
            Point2D pt=node.p;
            
            if (orientation==1) {
                
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(pt.x(), rect.ymin(), pt.x(), rect.ymax());
                    
            }
            
            else {
                
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(rect.xmin(), pt.y(), rect.xmax(), pt.y());
                
            }
            
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            pt.draw();
            
            draw(node.lb, -orientation);
            draw(node.rt, -orientation);
            
        }
    }
    
    public Iterable<Point2D> range(RectHV rect){
        if (rect==null) throw new NullPointerException("argument to range() is null");
        Stack<Point2D> points = new Stack<Point2D>();
        rangeSearch(root, rect, points, 1);
        return points;
    }
    
    private void rangeSearch(Node node, RectHV rect, Stack<Point2D> points, int orientation) {
        if (node!=null) {
            
            Point2D p=node.p;
            if (rect.contains(p)) points.push(p);
            
            RectHV largeRect=node.rect;
            
            if (orientation==1) {
                RectHV leftRect=new RectHV(largeRect.xmin(), largeRect.ymin(), p.x(), largeRect.ymax());
                RectHV rightRect=new RectHV(p.x(), largeRect.ymin(), largeRect.xmax(), largeRect.ymax());
                if (rect.intersects(leftRect)) rangeSearch(node.lb, rect, points, -1);
                if (rect.intersects(rightRect)) rangeSearch(node.rt, rect, points, -1);   
            }
            else {
                RectHV topRect=new RectHV(largeRect.xmin(), p.y(), largeRect.xmax(), largeRect.ymax());
                RectHV bottomRect=new RectHV(largeRect.xmin(), largeRect.ymin(), largeRect.xmax(), p.y());
                if (rect.intersects(bottomRect)) rangeSearch(node.lb, rect, points, 1);
                if (rect.intersects(topRect)) rangeSearch(node.rt, rect, points, 1);   
            }
        }
    }
        
        
        
    public Point2D nearest(Point2D p){
        if (p==null) throw new NullPointerException("argument to nearest() is null");
        Point2D closest;
        if (root==null) return null;
        
        else {
            double minSquaredDist=Double.POSITIVE_INFINITY;
            closest=nearest(p, root, 1, minSquaredDist);
        }
        
        return closest;
    }
    
    private Point2D nearest(Point2D q, Node node, int orientation, double minDistSquared) {
        double minSquared=minDistSquared;
        Point2D runningBest=null;
        Node searchFirst;
        Node searchLast;
        Point2D nearSideBest;
        Point2D farSideBest;
        
        if  ((node.lb==null)&&(node.rt==null)) {
            if (q.distanceSquaredTo(node.p) < minSquared) runningBest = node.p;
        }
        
        else {
                    
            if (((orientation==1)&&(q.x() < node.p.x())) || ((orientation==-1)&&(q.y() < node.p.y()))){ 
                searchFirst= node.lb;
                searchLast=node.rt;
            }
            
            else {
                searchFirst= node.rt;
                searchLast=node.lb;
            }
            
            if (searchFirst!=null) {
                nearSideBest = nearest(q, searchFirst, -orientation, minSquared);
                if (nearSideBest!=null) {
                    runningBest=nearSideBest;
                    minSquared=runningBest.distanceSquaredTo(q);
                }
            }
            
            double nodeToq = node.p.distanceSquaredTo(q);   
            if (nodeToq < minSquared){
                runningBest=node.p;
                minSquared=nodeToq;
            }
            
            
            if ((searchLast!=null)&&(searchLast.rect.distanceSquaredTo(q) < minSquared)) {
                farSideBest = nearest(q, searchLast, -orientation, minSquared);
                if (farSideBest!=null) {
                    runningBest=farSideBest;
                    minSquared=runningBest.distanceSquaredTo(q);
                }
            }
        }
      
        return runningBest;
    }
    
    public static void main(String[] args) { 
        KdTree kdTree=new KdTree();
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            double x=in.readDouble();
            double y=in.readDouble();
            Point2D pt=new Point2D(x,y);
//            StdOut.print(pt);
            kdTree.insert(pt);
//            StdOut.println(kdTree.contains(pt));
        }
        kdTree.draw();
        
    }
    
    
    
}
