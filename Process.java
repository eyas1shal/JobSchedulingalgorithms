package application;

public class Process implements Comparable<Process> {
	//simulate PCB
	 	int pid;
	    int arrivalTime;
	    int burstTime;
	    int startTime;
	    int finishTime;
	    int turnaround;
	    int waitingTime;
	    int remainingTime;

	    
	    public Process(int pid, int arrivalTime, int burstTime) {
	        this.pid = pid;
	        this.arrivalTime = arrivalTime;
	        this.burstTime = burstTime;
	        this.startTime = -1;            // -1 mean hasn't started yet
	        this.finishTime = -1;           // -1 mean hasn't finished yet
	        this.remainingTime = this.burstTime;    // not processed at all, will be decremented later
	    }
	    

		@Override
		public int compareTo(Process o) {//This function is vital to run a priority queue depending on the remaing time
			
			return this.remainingTime-o.remainingTime;
		}

}
