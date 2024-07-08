package application;

import java.util.ArrayList;

public class RR extends Scheduler{
	
	private int timeQuantum;
	
	RR(int timeQuantum) {
		this.timeQuantum= timeQuantum;
		intiateRandomProcesses(8);
	}

	@Override
	public void exec(){
        int time = 0;                          // variable to keep track of time
        Process currentlyRunning = null;       
        int remainingTimeQuantum; 
        int i = 0;                             
        
        readyQueue = new ArrayList<Process>();                      

        
        if (timeQuantum <= 0){    // invalidInput  -> avoids runtime error     
            return;
        }
        else
            remainingTimeQuantum = timeQuantum;  
     
        while(!allProcessesFinished()){    // while there are still unfinished processes
            checkProcessesArrival(time);   
            
            if (currentlyRunning != null){//start executing if the processes is valid
                
                remainingTimeQuantum--;             
                currentlyRunning.remainingTime--;   // remaining time is stored to continue the process later
            }
                       
            if(currentlyRunning != null && currentlyRunning.remainingTime == 0){    //When the processes finishes executing
                                                                                     
                currentlyRunning.finishTime = time;         // record finishing time
                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime;   // calculate turn around time
                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.burstTime;    // calculate waiting time

                int index = readyQueue.indexOf(currentlyRunning);  // search for the finished process's index the queue
             // finished processes are removed and deleted
                readyQueue.remove(index);                         
                currentlyRunning = null;

                if (readyQueue.size() != 0){                                //get the next process to be executed 
                    currentlyRunning = readyQueue.get( index % readyQueue.size() );   //processes in the ready queue that will enter is the next one that arrived
                    //the new index was used because the Array list processes changed positions thus indices changed
                    remainingTimeQuantum = timeQuantum;         // reset remainingTimeQuantum back to original 

                    if(currentlyRunning != null && currentlyRunning.startTime == -1) // if the process has just started to run-with start time -1 which is default
                        currentlyRunning.startTime = time;                            //record its starting time
                }  
            }
            
            if((currentlyRunning == null || remainingTimeQuantum == 0) && readyQueue.size() != 0){          // Empty ready queue or a process was executed for a timeQuantum
                //An equation will get the next process to execute
            	//i will be bigger than the queue's size 
            	//so the equation will make the Array list circular
            	i = (i + 1) % readyQueue.size(); 
                currentlyRunning = readyQueue.get(i); 

                remainingTimeQuantum = timeQuantum;                             // reset remainingTimeQuantum back to original 
                
                if(currentlyRunning != null && currentlyRunning.startTime == -1)// if the process has just started to run,
                    currentlyRunning.startTime = time;                          // record its starting time
            }
            
            time++;         
        }
        
        
    }

	public int getTimeQuantum() {
		return timeQuantum;
	}

	public void setTimeQuantum(int timeQuantum) {
		this.timeQuantum = timeQuantum;
	}

    public void checkProcessesArrival(int time){    //This function checks if there are any process that arrived at a given time 
													//and then adds it to the ready queue
        for (Process p : processes) 
            if(p.arrivalTime == time)   
                readyQueue.add(p);     
    }

	
}
