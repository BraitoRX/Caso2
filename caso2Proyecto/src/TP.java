public class TP {
    private  int[] TP;
    public TP( int TPsize ) {
        this.TP = new int[TPsize];
        for ( int i = 0; i < TPsize; i++ ) {
            this.TP[i] = -1;
        }
    }
    public void add( int page, int frame ) {
        this.TP[page] = frame;
    }
    public int searchPos( int page ) {
        return this.TP[page];
    }
    public boolean search(int page) {
        return this.TP[page] != -1;
    }
    public void clear() {
        for ( int i = 0; i < TP.length; i++ ) {
            this.TP[i] = -1;
        }
    }
    
}
