package application;

import java.util.Scanner;

//Eyas Shalhoub
//1201681
//OS project that simulates some CPU scheduling methods

public class Main  {

	public static void main(String[] args) {

		Scheduler cpu = null;
		double ATT=0;
		double AWT=0;

		Scanner scan= new Scanner(System.in);
		String choice="";
		while(true) {
			cpu=null;
			ATT=0;
			AWT=0;
			System.out.println("Chose Algorim to schedule an 8 random burst time processes:");
			System.out.println("1-SRTF");
			System.out.println("2-FCFS");
			System.out.println("3-RR");
			System.out.println("4-MLFBQ");
			System.out.println("Anything else will close the app");		
			choice=scan.next();
			System.out.println("\tATT\t"+"\t\tAWT");

			for ( int i=0;i<100000;i++) {
				if(choice.equals("1"))
					cpu = new SRTF();
				else if(choice.equals("2"))
					cpu=new FCFS();
				else if(choice.equals("3"))
					cpu=new RR(20);
				else if(choice.equals("4"))
					cpu=new MultilevelFeedbackQueues();
				else {
					scan.close();
					System.exit(0);
				}

				cpu.exec();
				ATT=ATT+cpu.ATT();
				AWT=AWT+cpu.AWT();
				if(i==99) {
					System.out.print("100:\t"+ATT/100);
					System.out.println("\t100:\t"+AWT/100);
				}else if(i==999) {
					System.out.print("1000:\t"+ATT/1000);
					System.out.println("\t1000:\t"+AWT/1000);
				}
				else if(i==9999) {
					System.out.print("10k:\t"+ATT/10000);
					System.out.println("\t10k:\t"+AWT/10000);
				}
				if(i!=99999)
					cpu=null;
			}
			System.out.print("100k:\t"+ATT/100000);
			System.out.println("\t100k:\t"+AWT/100000);
			System.out.println();		
		
			
		}


	}
}
