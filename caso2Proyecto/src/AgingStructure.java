import java.util.ArrayList;


public class AgingStructure {
    private int[] agingStructure;

    public AgingStructure(int sizeTP){
        this.agingStructure = new int[sizeTP];
    }
    public void reference(ArrayList<Integer> reference){
        Integer i=0;
        for (int j = 0; j < agingStructure.length; j++) {
                i= agingStructure[j];
                if(reference.contains(j)){
                    i=i>>>1;
                    i+= -2147483648;
                }
                else{
                    i=i>>>1;
                }
                agingStructure[j]=i;
                // System.out.println(j+" "+ String.format("%32s", 
                // Integer.toBinaryString(i)).replaceAll(" ", "0"));
                
        }
    }
    public int getBestPage(RAM ram) {
        int min=-1;
        int pos=0;
        int[] ramArray = ram.getRAM();
        Boolean not_yet=false;
        for (int j = 0; j < ramArray.length; j++) {
            int posVP=ramArray[j];
            int bits=agingStructure[posVP];
            // System.out.println("BITS: " + String.format("%32s", 
            // Integer.toBinaryString(bits)).replaceAll(" ", "0")+ " "+posVP);
            // System.out.println("MIN: " + String.format("%32s", 
            // Integer.toBinaryString(min)).replaceAll(" ", "0") + " "+pos);
            if (bits==0){
                return posVP;
            }
            if ( !not_yet ) {
                min=bits;
                not_yet = true;
                pos=posVP;
            } else {
                if (min<0) {
                    if (bits>0) {
                        min=bits;
                        pos=posVP;
                    }
                    else{
                        min=Math.min(min, bits);
                        if (min==bits) {
                            pos=posVP;
                        }
                    }
                }
                else{
                    if (bits>0) {
                        min=Math.min(min, bits);
                        if (min==bits) {
                            pos=posVP;
                        }
                    }
                    if (bits==0 && min!=0){
                        min=bits;
                        pos=posVP;
                    }
                }
            }
        }
        return pos;
    }
    
}
