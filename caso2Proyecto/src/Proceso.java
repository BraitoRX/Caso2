import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Proceso extends Thread {
    static Double time = 0D;
    static String references;
    private RAM ram;
    private TLB tlb;
    private AgingStructure replace;
    static ArrayList<Integer> pages;
    static Boolean end=true; 
    private int id;
    static int pageFaults;

    public Proceso(RAM ram, TLB tlb, AgingStructure replace, int id) {
        this.ram = ram;
        this.tlb = tlb;
        this.replace = replace;
        pages = new ArrayList<Integer>();
        this.id = id;
    }

    public void run() {
        if (id == 1) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(references));
                String c=br.readLine();
                while (c != null) {
                    int a = Integer.valueOf(c);
                    readReference(a);
                    Thread.sleep(1);
                    c=br.readLine();
                }
                br.close();
                end = false;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            while(end){
                AggingAlgorithm();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } 

        }
    }

    public void readReference(int num) {
        
        Boolean tlbHit = tlb.search(num);
        if (tlbHit) {
            // System.out.println("TLB hit");
            time += 2*Math.pow(10, -6);
        } else {
            // System.out.println("TLB miss");
            time += 30*Math.pow(10, -6);
            Boolean tpHit = ram.search(num);
            if (tpHit) {
                // System.out.println("TP hit");
            } else {
                // System.out.println("TP miss");
                tlb.add(ram.add(num,tlb));
                pageFaults++;
                time +=10;
                time += 30*Math.pow(10, -6);
            }

        }
        synchronized (pages) {
            pages.add(num);
        }

    }

    public void AggingAlgorithm() {
        synchronized (pages) {
            synchronized (replace) {
                replace.reference(pages);
            }
            pages.clear();
        }
    }

    public static void main(String[] args) throws Exception {
        
        Scanner input= new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo de referencias si alta o baja");
        references=input.nextLine();
        if (references.equals("alta")) {
            references="ej_paginas/ej_Alta_64paginas.txt";
        }else if (references.equals("baja")) {
            references="ej_paginas/ej_Baja_64 paginas.txt";
        }
        System.out.println("Ingrese el tamaño de la memoria RAM");
        int RAMsize=input.nextInt();
        System.out.println("Ingrese el tamaño de la memoria TLB");
        int TLBsize=input.nextInt();
        TLB tlb = new TLB(TLBsize);
        AgingStructure agingStructure = new AgingStructure(64);
        RAM ram = new RAM(RAMsize, 64, agingStructure);
        Proceso proceso = new Proceso(ram, tlb, agingStructure, 0);
        Proceso proceso2 = new Proceso(ram, tlb, agingStructure, 1);
        input.close();
        proceso.start();
        proceso2.start();
        proceso.join();
        proceso2.join();
        System.out.println("Tiempo total: " + time*Math.pow(10, 6));
        System.out.println("Page Faults: " +  pageFaults);
    }
}
