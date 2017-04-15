//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdDraw.html
import edu.princeton.cs.algs4.StdDraw;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Point2D.html
import edu.princeton.cs.algs4.Point2D;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/RectHV.html
import edu.princeton.cs.algs4.RectHV;
//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Stack.html
import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

//http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/In.html
import edu.princeton.cs.algs4.In;


public class PointSET {
    private TreeSet<Point2D> setPoints;
    
    public PointSET() { 
        setPoints=new TreeSet<Point2D>();
    }
    
    public boolean isEmpty(){
        return setPoints.isEmpty();
    }
    
    public int size(){
        return setPoints.size();
    }
    
    public void insert(Point2D p){
        if (p==null) throw new NullPointerException("argument to insert() is null");
        setPoints.add(p);
    }
    
    public boolean contains(Point2D p){
        if (p==null) throw new NullPointerException("argument to contains() is null");
        return setPoints.contains(p);
    }
    
    public void draw(){
        StdDraw.show(0);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : setPoints) {
            p.draw();
        }
        StdDraw.show();
    }
    
   
    
    public Iterable<Point2D> range(RectHV rect){
        if (rect==null) throw new NullPointerException("argument to range() is null");
        Stack<Point2D> rectPoints=new Stack<Point2D>();
        for (Point2D p: setPoints){
            if (rect.contains(p)) rectPoints.push(p);
        }
        return rectPoints;
    }
    
    public Point2D nearest(Point2D p){
        if (p==null) throw new NullPointerException("argument to nearest() is null");
        if (isEmpty()) return null;
        Point2D closest=new Point2D(3, 3);
        double minSquaredDist=Double.POSITIVE_INFINITY;
        for (Point2D point: setPoints) {
            double pointDistSquared=p.distanceSquaredTo(point);
            if (pointDistSquared<minSquaredDist){
                closest=point;
                minSquaredDist=pointDistSquared;
            }
        }
        return closest;
    }
    
    

    
    public static void main(String[] args) { 
        PointSET ptSet=new PointSET();
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            double x=in.readDouble();
            double y=in.readDouble();
            Point2D p=new Point2D(x,y);
            ptSet.insert(p);
        }
        ptSet.draw();
        
    }
    
    
}
