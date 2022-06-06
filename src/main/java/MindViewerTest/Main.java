/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MindViewerTest;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import br.unicamp.cst.util.viewer.MindViewer;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author rgudwin
 */
public class Main {
    
    Mind m;
    
    void updateMemoryObject(MemoryObject mo) {
        double value = (double) mo.getI();
        mo.setI(value+0.01);
    }
    
    void updateMemoryContainer(MemoryContainer mc) {
        for (Memory mem : mc.getAllMemories()) {
            if (mem.getClass().getCanonicalName().equalsIgnoreCase("br.unicamp.cst.core.entities.MemoryObject")) {
                updateMemoryObject((MemoryObject)mem);
                //System.out.println("Updating subnode");
            }    
        }
    }

    void updateMind() {
        //System.out.println("Updating Mind");
        for (Memory mem : m.getRawMemory().getAllMemoryObjects()) {
            if (mem.getClass().getCanonicalName().equalsIgnoreCase("br.unicamp.cst.core.entities.MemoryObject")) {
                updateMemoryObject((MemoryObject)mem);
            }
            if (mem.getClass().getCanonicalName().equalsIgnoreCase("br.unicamp.cst.core.entities.MemoryContainer")) {
                updateMemoryContainer((MemoryContainer)mem);
            }
        }
        
    }
    
    public void StartTimer() {
        Timer t = new Timer();
        Main.mainTimerTask tt = new Main.mainTimerTask(this);
        t.scheduleAtFixedRate(tt, 0, 100);
    }

    public void tick() {
        if (m != null) {
            updateMind();
        } else {
            System.out.println("Mind is null");
        }
        //System.out.println("update");
    }
    
    class mainTimerTask extends TimerTask {

        Main wov;
        boolean enabled = true;

        public mainTimerTask(Main wovi) {
            wov = wovi;
        }

        public void run() {
            if (enabled) {
                wov.tick();
            }
        }

        public void setEnabled(boolean value) {
            enabled = value;
        }
    }
    
    
    public Mind prepareMind() {
        m = new Mind();
        m.createCodeletGroup("Sensory");
        m.createCodeletGroup("Perception");
        m.createCodeletGroup("Behavioral");
        m.createCodeletGroup("Motivational");
        m.createCodeletGroup("Motor");
        m.createMemoryGroup("StandardMemories");
        m.createMemoryGroup("Containers");
        
        MemoryObject m1 = m.createMemoryObject("M1", 1.12);
        m.registerMemory(m1,"StandardMemories");
        MemoryObject m2 = m.createMemoryObject("M2", 2.32);
        m.registerMemory(m2,"StandardMemories");
        MemoryObject m3 = m.createMemoryObject("M3", 3.44);
        m.registerMemory(m3,"StandardMemories");
        MemoryObject m4 = m.createMemoryObject("M4", 4.52);
        m.registerMemory(m4,"StandardMemories");
        MemoryObject m5 = m.createMemoryObject("M5", 5.12);
        m.registerMemory(m5,"StandardMemories");
        MemoryContainer m6 = m.createMemoryContainer("C1");
        m.registerMemory(m6,"Containers");
        MemoryContainer m7 = m.createMemoryContainer("C2");
        m.registerMemory(m7,"Containers");
        int mc1 = m7.setI(7.55, 0.23);
        int mc2 = m6.setI(6.33, 0.22);
        int mc3 = m6.setI(6.12, 0.13);
        int mc4 = m6.add(m7);
        //System.out.println("Memories: "+mc1+" "+mc2+" "+mc3+" "+mc4);
        
        Codelet c = new TestCodelet("Sensor1");
        c.addInput(m1);
        c.addInput(m2);
        c.addOutput(m3);
        c.addOutput(m4);
        c.addBroadcast(m5);
        m.insertCodelet(c,"Sensory");
        Codelet c2 = new TestCodelet("Motor1");
        c2.addInput(m4);
        c2.addInput(m5);
        c2.addOutput(m6);
        c2.addOutput(m3);
        c2.addBroadcast(m5);
        m.insertCodelet(c2,"Motor");
        
        Codelet mot1 = new TestCodelet("Curiosity");
        mot1.addInput(m7);
        mot1.addOutput(m5);
        m.insertCodelet(mot1,"Motivational");
        Codelet mot2 = new TestCodelet("Fear");
        mot2.addInput(m3);
        mot2.addOutput(m4);
        try {mot2.setActivation(1.0);} catch(Exception e){}
        m.insertCodelet(mot2,"Motivational");
        Codelet mot3 = new TestCodelet("Anger");
        mot3.addInput(m1);
        mot3.addOutput(m2);
        try {mot3.setActivation(0.5);} catch(Exception e){}
        m.insertCodelet(mot3,"Motivational");
        m.start();
        return(m);
    }
    
private void createAndShowGUI(Mind m) {
        MindViewer mv = new MindViewer(m,"MindViewer",m.getCodeletGroupList("Motor"));
        mv.setVisible(true);
    }   

public Main() {
    Mind m = prepareMind();
    createAndShowGUI(m);
}
    
    public static void main(String[] args) {
        Main mainApp = new Main();
        mainApp.StartTimer();
    }
    
}
