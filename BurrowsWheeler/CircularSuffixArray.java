//import Quick3CircularSuffixSort;


public class CircularSuffixArray {
    
    private int inputStringLength;
    private int[] originalSuffixIndices;
    
    public CircularSuffixArray(String s) { 
        if (s==null) throw new java.lang.NullPointerException("String is null");
        inputStringLength=s.length();
        originalSuffixIndices = new int[inputStringLength];
        for (int j=0; j<inputStringLength; j++) {
            originalSuffixIndices[j]=j;
        }
        Quick3CircularSuffixSort.sort(originalSuffixIndices, s);
    }
    
    public int length() {
        return inputStringLength;
    }
    
    public int index(int i) {
        if ((i<0) || (i>=inputStringLength)) throw new java.lang.IndexOutOfBoundsException("Index out of range");
        return originalSuffixIndices[i];
    }
    
    public static void main(String[] args) { 
        
    }
    
    
}
