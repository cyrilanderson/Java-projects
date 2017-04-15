# SeamCarver
Implementation of the seam carver algorithm, a content-aware resizing algorithm for images, in Java. Made use of shortest-path graph algorithm to identify lowest-energy-sum horizontal and vertical seams to remove from the image. (With energy defined as squared RGB pixel gradient across the seam) Developed as weekly project in online Algorithms course. References Picture class in Picture.java from publically available Princeton University CS algorithms and data structures package. SeamCarver.java is my own original work. 

Possible future improvements:
- Parallelize the algorithm for finding the lowest energy seam using Java parallel processing constructs and the parallel approach to graph shortest paths
- Construct a UI to make a simple Java app out of this using Swing or JavaFX
