import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class AgingStructure {
    private Hashtable<Integer, Queue<String>> agingStructure;
    static Boolean[] agingStructureStatus;
    private int maxValueQueue;
    public AgingStructure(int maxValueQueue,int numberOfQueues){
        this.maxValueQueue = maxValueQueue;
        this.agingStructure = new Hashtable<>();
        agingStructureStatus = new Boolean[numberOfQueues];
        for (int i = 0; i < numberOfQueues; i++) {
            Queue<String> queue = new LinkedList<>();
            for(int j = 0; j < maxValueQueue; j++){
                queue.add("0");
            }
            this.agingStructure.put(i, queue);
            agingStructureStatus[i] = false;
        }
    }
    public void reference(int page){
        Queue<String> queue = agingStructure.get(page);
        queue.remove();
        queue.add("1");
        agingStructureStatus[page] = true;
    }
    public void referenceFalses(){
        for (int i = 0; i < agingStructureStatus.length; i++) {
            if(!agingStructureStatus[i]){
                Queue<String> queue = agingStructure.get(i);
                queue.remove();
                queue.add("0");
            }
            agingStructureStatus[i] = false;
        }
    }
    public int getBestPage(TP tp) {
        int provePage = 0;
        int bestPageValue = -1;
        while ( provePage < agingStructure.size()) {
            if (tp.search(provePage)){
                if ( bestPageValue == -1  ) {
                    bestPageValue = provePage;
                }
                else {
                    Queue<String> queue = agingStructure.get(provePage);
                    String[] prove_string=queue.toArray(String[]::new);
                    Queue<String> bestQueue = agingStructure.get(bestPageValue);
                    String[] best_string=bestQueue.toArray(String[]::new);
                    // print bestPageValue
                    String a="";
                    for (int i = maxValueQueue-1; i >= 0; i--) {
                        a+=best_string[i];
                    }
                    //System.out.println("bestPageValue: "+bestPageValue+" "+a);
                    //print provePage
                    String b="";
                    for (int i = maxValueQueue-1; i >= 0; i--) {
                        b+=prove_string[i];
                    }
                    //System.out.println("provePage: "+provePage+" "+b);
                    for (int i = maxValueQueue-1; i >= 0; i--) {
                        if ( prove_string[i].equals("0") && best_string[i].equals("1") ) {
                            bestPageValue = provePage;
                            break;
                        }
                    }
                    
                }
            }
            provePage++;
        }
        return bestPageValue;
    }
    
    
}
