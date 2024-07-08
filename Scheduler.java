package application;

import java.util.ArrayList;
import java.util.Random;

public abstract class Scheduler {
	
    ArrayList<Process> processes= new ArrayList<>();
    static ArrayList<Process> readyQueue;
    
	protected abstract void exec();
        
    public abstract void checkProcessesArrival(int time);
    
    public void intiateRandomProcesses(int size) {
        //This function initializes 8 processes with random burst time between 5 and 100
    	// The processes enter the ready queue every 10 quantum of times
        Random r = new Random();
        for (int i=0;i < size;i++) {
        	int burst =r.nextInt(95)+5;//5 to 100 CPU burst
        	int arr=r.nextInt(11);
            Process rand = new Process(i, i*arr,burst);//arrival is random

            if (rand != null) {         
            	processes.add(rand);
            }
        }

    }
    
    public boolean allProcessesFinished(){  
        for (Process p : processes) 
            if(p.finishTime == -1)          // by default a process finish time is -1,
            		return false;						//so it is not finished if it is still on its initial state             
        return true;
    }


    
    public double ATT(){      
        
        double avgTurnaround = 0;
        
        for (Process p : processes)
            avgTurnaround += p.turnaround;
        
        avgTurnaround /= processes.size();
        
        return avgTurnaround;
    }
    
    public double AWT(){     
        
        double avgWaitingTime = 0;
        
        for (Process p : processes)
            avgWaitingTime += p.waitingTime;
        
        avgWaitingTime /= processes.size();
        
        return avgWaitingTime;
    }


    
}
