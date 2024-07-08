package application;

import java.util.LinkedList;
import java.util.Queue;

public class FCFS extends Scheduler {
	Queue<Process> readyQueue;// The data structure for this algorithm is the queue FIFO which is the same as FCFS concept
	FCFS() {
		intiateRandomProcesses(8);
	}
	
	@Override
	 public void exec(){
	        int time = 0;                          // variable to keep track of time
	        Process currentlyRunning = null;       
	        
	        readyQueue= new LinkedList<Process>();                      // processes which are ready to run
	     
	        while(!allProcessesFinished()){     // while there are still unfinished processes
	            checkProcessesArrival(time);    
	            
	            if(currentlyRunning == null){          // if there is no process running currently
	                if(readyQueue.isEmpty()) {		//no processes at time 0 fix
	                						 	//this will a avoid a null pointer Exception
	                	time++;
	                	continue;
	                }
	            	currentlyRunning = readyQueue.peek();   // get the first process in readyQueue
	                
	                if(currentlyRunning != null)                //start executing if the processes is valid
	                    currentlyRunning.startTime = time;      
	            }
	               
	            if (currentlyRunning != null){
	                
	                if (time != currentlyRunning.startTime) //This will continue executing the processes after its starting time
	                    currentlyRunning.remainingTime--;   

	                if(currentlyRunning != null && currentlyRunning.remainingTime == 0){    //When the processes finishes executing
	                                                                                         
	                    currentlyRunning.finishTime = time;         // record finishing time
	                    currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime;   // calculate turn around time
	                    currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.burstTime;    // calculate waiting time
	                
	                    // finished processes are removed and deleted
	                    readyQueue.remove();          
	                    currentlyRunning = null;
	                    
	                    if (readyQueue.size() != 0)              //get the next process to be executed   
	                        currentlyRunning = readyQueue.peek();   //since we are using a Queue data structure the next will be in-line with the last one
	                
	                    if(currentlyRunning != null)                // if a processes is found its start time is recorded
	                        currentlyRunning.startTime = time;      
	                }
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
