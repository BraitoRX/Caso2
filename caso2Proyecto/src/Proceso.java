import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Proceso extends Thread {
    static Double time = 0D;
    static String references;
    private RAM ram;
    private TLB tlb;
    private AgingStructure replace;
    static ArrayList<Integer> pages;
    static Boolean end=true; 
    private int id;

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
                int contador= 0;
                while (c != null) {
                    long startTime = System.currentTimeMillis();
                    long elapsedTime = 0L;
                    long nowTime=0L;
                    int a = Integer.valueOf(c);
                    readReference(a);
                    nowTime = System.currentTimeMillis();
                    elapsedTime = nowTime - startTime;
                    if (elapsedTime >=2) {
                        Thread.sleep(2);
                        startTime = System.currentTimeMillis();
                    }
                    System.out.println(contador);
                    c=br.readLine();
                    contador++;
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
                long startTime = System.currentTimeMillis();
                long elapsedTime = 0L;
                long nowTime=0L;
                AggingAlgorithm();
                nowTime = System.currentTimeMillis();
                elapsedTime = nowTime - startTime;
                if (elapsedTime >=1) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } 

        }
    }

    public void readReference(int num) {

        Boolean tlbHit = tlb.search(num);
        if (tlbHit) {
            System.out.println("TLB hit");
            time += 2*Math.pow(10, -3);
        } else {
            System.out.println("TLB miss");
            time += 30*Math.pow(10, -3);
            Boolean tpHit = ram.search(num);
            if (tpHit) {
                System.out.println("TP hit");
                time += 30*Math.pow(10, -3);
            } else {
                System.out.println("TP miss");
                tlb.add(ram.add(num));
                time +=10;
                time += 30*Math.pow(10, -3);
            }

        }
        synchronized (pages) {
            pages.add(num);
        }

    }

    public void AggingAlgorithm() {
        synchronized (pages) {
            synchronized (replace) {
                for (int i = 0; i < pages.size(); i++) {
                    replace.reference(pages.get(i));
                }
                replace.referenceFalses();
            }
            pages.clear();
        }
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        TLB tlb = new TLB(2);
        AgingStructure agingStructure = new AgingStructure(4, 64);
        RAM ram = new RAM(5, 64, agingStructure);
        Proceso proceso = new Proceso(ram, tlb, agingStructure, 0);
        Proceso proceso2 = new Proceso(ram, tlb, agingStructure, 1);
        Proceso.references="ej_paginas/ej_Baja_64 paginas.txt";
        proceso.start();
        proceso2.start();
        proceso.join();
        proceso2.join();
        System.out.println("Tiempo total: " + time);
    }
}
//3251.976000000024 alta localidad0
//7320.129999999915 baja localidad
