import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class TLB {
    private Queue<Hashtable<Integer, Integer>> tlb;
    private int tlbSize;
    public TLB( int tlbSize ) {
        this.tlb = new LinkedList<Hashtable<Integer, Integer>>();
        this.tlbSize = tlbSize;
    }
    public void add( Hashtable<Integer, Integer> page ) {
        if ( this.tlb.size() == this.tlbSize ) {
            this.tlb.remove();
        }
        this.tlb.add( page );
    }
    public boolean search( int page ) {
        for ( Hashtable<Integer, Integer> tlbPage : this.tlb ) {
            if ( tlbPage.containsKey( page ) ) {
                return true;
            }
        }
        return false;
    }
    
}
