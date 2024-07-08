package application;

import java.util.PriorityQueue;

public class SRTF extends Scheduler{
	PriorityQueue<Process> readyQueue;// The data structure for this algorithm is the heap which is one of the quickest algorithm to find the minimum of some data
										// thanks to our Comparable processes class the heap know which process has the least burst time
	SRTF() {//HEAP
		intiateRandomProcesses(8);
	}
	
    public void exec(){
        int time = 0;                               // variable to keep track of time
        Process currentlyRunning = null;     
        
        readyQueue = new PriorityQueue<>();
        while(!allProcessesFinished()){     // while there are still unfinished processes
            checkProcessesArrival(time);    


            
            if(currentlyRunning != null)            //start executing if the processes is valid from the previous iteration
                currentlyRunning.remainingTime--;   
            
             
            //the root of the heap is the process with the least remaining time 
            //if the currently running process is still the process with the least burst time, the variable won't change 
            // if there is a new root (a process with a least burst time ) the variable will change 
            // this makes the algorithm with preemption	
            currentlyRunning = readyQueue.peek();                        
            
            if(currentlyRunning != null && currentlyRunning.startTime == -1)   // if the process has just started to run-with start time -1 which is default
                currentlyRunning.startTime = time;                              //record its starting time
 
            if(currentlyRunning != null && currentlyRunning.remainingTime == 0){    //When the processes finishes executing
                                                                                     
                currentlyRunning.finishTime = time;         // record finishing time
                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime;   // calculate turn around time
                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.burstTime;    // calculate waiting time
                
                readyQueue.poll();//since the process -with the least burst time- is the root of the heap
                currentlyRunning = null;// it is deleted by polling the heap

                
                currentlyRunning =   readyQueue.peek();                // the new root of the heap is 2nd least process in terms of remaining time

                if(currentlyRunning != null && currentlyRunning.startTime == -1)    // if the process has just started to run-with start time -1 which is default
                    currentlyRunning.startTime = time;                              //record its starting time
            }
            
            time++;         
        }
        
    }
    public void checkProcessesArrival(int time){    //This function checks if there are any process that arrived at a given time 
													//and then adds it to the ready queue
        for (Process p : processes) 
            if(p.arrivalTime == time)   
                readyQueue.add(p);      
    }
    
}
