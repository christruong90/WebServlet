import java.util.ArrayList;
import java.io.*;

public class ThreadRace {
    
    public static void main (String[] args) {
        RaceStatus raceStatus = new RaceStatus(500);
        new SimpleThread(raceStatus).start();
        new SimpleThread(raceStatus).start();
        while(true) {
            raceStatus.showRace();
            try {
                Thread.sleep((long)(5000));
                if(!raceStatus.isRunning) {
                    System.out.println("Done");
                    break;
                }
            } catch (InterruptedException e) {}
        }
    }
}


class RaceStatus {
    ArrayList<SimpleThread> threadHolder = new ArrayList<SimpleThread>();
    int maxDist;
    boolean isRunning;

    public RaceStatus(int n) {
        maxDist = n;
    }


    void addMe(SimpleThread simpleThread){
        threadHolder.add(simpleThread);
    }

    boolean getIsRunning() {
        return isRunning;
    }

    void showRace() {
        for(int i = 0; i < threadHolder.size(); i++) {
            int curDist = threadHolder.get(i).getCount();
            isRunning = threadHolder.get(i).getRunning();
            if(curDist > maxDist) {
                threadHolder.get(i).stopThread();
//                System.out.println("Thread " + threadHolder.get(i) + " ended");
            }
            System.out.println("Thread: " + i + " " + threadHolder.get(i).getCount());
        }

    }

}

class SimpleThread extends Thread {

    private int currDistance = 0;
    boolean running = true;

    public SimpleThread(RaceStatus raceStatus) {
        raceStatus.addMe(this);
    }

    public int getCount() {
        return currDistance;
    }

    public void stopThread() {
        running = false;
    }

    boolean getRunning() {
        return running;
    }

    public void run() {
        for (int i = 0; i < 10000 && running; i++) {
            try {
                sleep((long)(Math.random() * 500));
                currDistance++;
            } catch (InterruptedException e) {}
        }
    }
}
