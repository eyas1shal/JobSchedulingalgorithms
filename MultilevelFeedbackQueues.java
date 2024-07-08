package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MultilevelFeedbackQueues extends Scheduler {

    private int timeOver;
    private Queue<Process> readyQueue;//queue data structure eases the FCFS algorithm
    private ArrayList<Process> rr10Queue;
    private ArrayList<Process> rr50Queue;

    public MultilevelFeedbackQueues() {
        intiateRandomProcesses(8);
    }

    @Override
    public void exec() {
        readyQueue = new LinkedList<>();
        rr10Queue = new ArrayList<>();
        rr50Queue = new ArrayList<>();
        timeOver = 0;
        
        for (Process process : processes) {//go through RR for 10 (or less) time Quantum pieces then remove what's finished
        	
            Rr(process, 10, rr10Queue);
            removeFinishedProcesses(rr10Queue);
        }
      
        
        timeOver = 10;//add the time from previous algorithm
        for (Process process : processes) {//go through RR for 50 time Quantum pieces then remove what's finished
        	if (process.remainingTime > 0) {
	        	
	            Rr(process, 50+10, rr50Queue);
	            removeFinishedProcesses(rr50Queue);
        	}
        }
      
        for (Process process : processes) {//insert the left process into the ready queue to be executed in FCFS 
            if (process.remainingTime > 0) {
                readyQueue.add(process);
            }
        }
        timeOver = 60;//add the time from previous algorithm
        while (!allProcessesFinished()) {//Execute what's left in FCFS and until all the process are done
          
            FCfs();
            removeFinishedProcesses(readyQueue);
        }
    }

    public void FCfs() {
        int time = timeOver;
        Process currentlyRunning = null;

        while (!allProcessesFinished()) {// while there are still unfinished processes
           

            if (currentlyRunning == null) {// if there is no process running currently
                if (readyQueue.isEmpty()) {//no processes at time 0 fix
				 	//this will a avoid a null pointer Exception
                    time++;
                    continue;
                }
                currentlyRunning = readyQueue.peek(); // get the first process in readyQueue

                if (currentlyRunning != null) //start executing if the processes is valid
                    currentlyRunning.startTime = time;
            }

            if (currentlyRunning != null) {//This will continue executing the processes after its starting time
                if (time != currentlyRunning.startTime)
                    currentlyRunning.remainingTime--;

                if (currentlyRunning != null && currentlyRunning.remainingTime == 0) { //When the processes finishes executing
                    currentlyRunning.finishTime = time + 60;// record finishing time and added the previous time 
                    currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime;// calculate turn around time
                    currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.burstTime; // calculate waiting time
                 // finished processes are removed and deleted
                    readyQueue.remove(); 
                    currentlyRunning = null;

                    if (!readyQueue.isEmpty())//get the next process to be executed   
                        currentlyRunning = readyQueue.peek();//since we are using a Queue data structure the next will be in-line with the last one

                    if (currentlyRunning != null)// if a processes is found its start time is recorded
                        currentlyRunning.startTime = time+60;
                }
            }

            time++;
        }
        timeOver = time;
    }

    public void Rr(Process process, int timeQuantum, ArrayList<Process> queue) {
        int time = timeOver;
        Process currentlyRunning = process;
        int remainingTimeQuantum = timeQuantum;
        int i = 0;

        while (!allProcessesFinished() && (process == null || time != process.remainingTime)) {// while there are still unfinished processes or process is not done
        	if(time==timeQuantum)
        		break;
        	checkProcessesArrival(time, queue);
        	
        	if(queue.size()==0) {//If queue is empty keep searching
        		time++;
        		continue;
        	}
        	
            if (currentlyRunning != null) {//start executing if the processes is valid
                remainingTimeQuantum--;
                currentlyRunning.remainingTime--;
            }

            if (currentlyRunning != null && currentlyRunning.remainingTime == 0) {//When the processes finishes executing
                currentlyRunning.finishTime = time;// record finishing time
                currentlyRunning.turnaround = currentlyRunning.finishTime - currentlyRunning.arrivalTime;// calculate turn around time
                currentlyRunning.waitingTime = currentlyRunning.turnaround - currentlyRunning.burstTime;// calculate waiting time
              

                queue.remove(0);// finished processes are removed and deleted

                if (!queue.isEmpty()) {//processes in the ready queue that will enter is the next one that arrived
                    //the new index was used because the Array list processes changed positions thus indices changed
                    currentlyRunning = queue.get(0);
                    remainingTimeQuantum = timeQuantum;// reset remainingTimeQuantum back to original 

                    if (currentlyRunning != null && currentlyRunning.startTime == -1)// if the process has just started to run-with start time -1 which is default
                        currentlyRunning.startTime = time; //record its starting time
                } else {
                    currentlyRunning = null;
                }
            }

            if ((currentlyRunning == null || remainingTimeQuantum == 0) && !queue.isEmpty()) {// Empty ready queue or a process was executed for a timeQuantum
                //An equation will get the next process to execute
            	//i will be bigger than the queue's size 
            	//so the equation will make the Array list circular
                i = (i + 1) % queue.size();
                currentlyRunning = queue.get(i);
                remainingTimeQuantum = timeQuantum; // reset remainingTimeQuantum back to original 

                if (currentlyRunning != null && currentlyRunning.startTime == -1)// if the process has just started to run,
                    currentlyRunning.startTime = time;		// record its starting time
            }

            time++;
        }
        timeOver = time;
    }

    public void checkProcessesArrival(int time, ArrayList<Process> r) {//This function checks if there are any process that arrived at a given time 
		//and then adds it to the ready queue
        for (Process p : processes)
            if (p.arrivalTime == time)
                r.add(p);
    }

    private void removeFinishedProcesses(Queue<Process> queue) {
        queue.removeIf(process -> process.finishTime != -1);//java ready loop to remove item from queue
    }

    private void removeFinishedProcesses(ArrayList<Process> list) {
        list.removeIf(process -> process.finishTime != -1);//java ready loop to remove item from queue
    }

	@Override
	public void checkProcessesArrival(int time) {
		//useless//just to implement an abstract method
        for (Process p : processes) 
            if(p.arrivalTime == time)   
                readyQueue.add(p);  
	}
}

