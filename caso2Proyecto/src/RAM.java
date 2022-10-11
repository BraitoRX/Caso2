import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class RAM {
    private int[] RAM;
    private Queue<Integer> RAMfree;
    private AgingStructure replace;
    private TP tp;

    public RAM( int RAMsize,int TPsize,AgingStructure replace) {
        this.tp= new TP(TPsize);
        this.RAM = new int[RAMsize];
        this.replace = replace;
        this.RAMfree = new LinkedList<Integer>();
        for ( int i = 0; i < RAMsize; i++ ) {
            this.RAM[i] = -1;
            RAMfree.add(i);
        }
    }
    public Hashtable<Integer, Integer> add( int page ) {
        Integer pos =RAMfree.poll();
        Hashtable<Integer, Integer> pageTable = new Hashtable<Integer, Integer>();
        if ( pos != null ) {
            this.RAM[pos] = page;
            tp.add(page,pos);
            pageTable.put( page, pos );
        }
        else {
            synchronized(replace){
                int page_to_replace = replace.getBestPage(tp);
                int pos_to_replace = tp.searchPos(page_to_replace);
                this.RAM[pos_to_replace] = page;
                tp.add(page, pos_to_replace);
                tp.add(page_to_replace, -1);
                pageTable.put( page, pos_to_replace);
            }
        }
        return pageTable;
    }
    public boolean search( int frame ) {
        return tp.searchPos(frame)!=-1;
    }
    public int get( int frame ) {
        return this.RAM[frame];
    }
    
}
