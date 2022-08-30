/*
 * @author Peguero Marmolejos, Emily
 */

import java.util.*;
import java.io.*;

public class MM1 {

    
    static float simulated_Clock = 0;
    
    static LinkedList<events> future_Events_List = new LinkedList<events>();
    static LinkedList<events> delayed_Events_List = new LinkedList<events>();
    
    static double lamda = 6; //given interarrival time
    static double mu = 5; //given interprocess service time
    
    public int count = 0;
    static int numb_of_jobs = 0;
    static int numb_of_delayed_events = 0;

    static float idle_time = 0;
    static float busy_time = 0;
    static float delay_time = 0;
    static float service_time = 0;
    static float interarrival_time = 0;
    
    public static FileWriter Output, Summary;
    
	public static boolean status = true; //true is idle, false is busy
    


	 public static void write_to_file(String a){
		try {
			 Output = new FileWriter("Output.txt", true);
			 Output.append(a);
			 Output.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 }
    
    public static void simulation() {
    		future_Events_List.add(create_arrival_event());//adding first arrival event to fel
    		write_to_file(simulated_Clock + " : " + future_Events_List.get(0).name + " : was added to the fel.\n");
    		handle_events(future_Events_List.remove(0));
    		totals();
   	}
    
    public static events create_arrival_event() {
    	float first = (float) ((-lamda) * Math.log(Math.random()));
    	events arr = new events(first , "Arrival");
    	numb_of_jobs ++;
    	arr.set_Name(numb_of_jobs);
    	write_to_file(simulated_Clock + " : " + arr.name + " : was created with arrival time of : " + first + " . \n");
    	return arr;
    }

    public static events create_end_of_service() {
    	float second  = (float) ((-mu) * Math.log(Math.random()));
    	events end = new events(second , "Departure");
    	future_Events_List.add(end);
    	return end;
    }

    public static void handle_events(events aord){
    	while(numb_of_jobs <= 1000) {
    		if(aord.type.equalsIgnoreCase("ARRIVAL")) {
    			if(!status) {
				/* if the server is busy then the event is from the delay list and 
				no extra work must be done, delay += 0.0 only the clock will be updated*/
    				simulated_Clock += aord.numb; //updating value of simulated clock
    				interarrival_time += aord.numb;
    			}else {
    				simulated_Clock += aord.numb; //updating value of simulated clock
    				interarrival_time += aord.numb;

    				status = false; //server is now busy
    				idle_time = simulated_Clock - busy_time;	// updating idle time after server becomes busy
    			}
			
				events next_arrival = create_arrival_event();//creates next arrival event

				events departure = create_end_of_service();// creates end of service and adds it to fel
				departure.set_Name(aord.count);
		    	write_to_file(simulated_Clock + " : " + departure.name + " : was created and added to the fel with  arrival time of : " + departure.numb + " . \n");

		    	if(departure.numb > next_arrival.numb) {
				// if the departure event is scheduled later than next_arrival event, add next_arrival to delayed_Events_List
					numb_of_delayed_events ++;//increase the number of delayed events
					delayed_Events_List.add(next_arrival);
					write_to_file(simulated_Clock + " : " + delayed_Events_List.get(0).name + " : was added to the DEL.\n");
					delay_time += departure.numb - next_arrival.numb;
				}
				else {//otherwise add the next_arrival to the future_Events_List
					future_Events_List.add(next_arrival);
					write_to_file(simulated_Clock + " : " + next_arrival.name + " : was added to the fel.\n");
				}	
				handle_events(future_Events_List.remove(0));// remove end of service from fel
    		}
    		else if(aord.type.equalsIgnoreCase("DEPARTURE")) {
    			service_time += aord.numb;
    			simulated_Clock += aord.numb;// update simulated clock
    			write_to_file(simulated_Clock + " : " + aord.name + " : was executed.\n");
    			if(!(delayed_Events_List.isEmpty())) {//there is at least one event on the delayed list
    				write_to_file(simulated_Clock + " : " + delayed_Events_List.get(0).name + " is removed from DEL.\n");
    				handle_events(delayed_Events_List.remove(0));
    			}
    			else {//if delay list is empty just make server idle
    				status = true; //server is now idle
    				busy_time = simulated_Clock - idle_time;//calculates busy period when server becomes idle
    				handle_events(future_Events_List.remove(0));
    			}
    		}
		}
    }//handle_events
	
    public static void totals() {
    	/*avg service time, the avg interarrival times, the avg busy period and the avg system time*/

    	try {
    		
    		Summary = new FileWriter("Summary.txt");
    		Summary.write("");
			 Summary.append("Emily Peguero Marmolejos\n");
			 Summary.append("**************************** SUMMARY *************************\n");
			 Summary.append("System Time : " + simulated_Clock + "\n");
			 Summary.append("Delayed Events : " + numb_of_delayed_events + "\n");
			 Summary.append("Total Jobs : " + (numb_of_jobs - 1) + "\n");
			 Summary.append("Total Idle Time : " + idle_time + "\n");
			 Summary.append("Delayed Time : " + delay_time + "\n");
			 Summary.append("Busy Time : " + busy_time);
			 Summary.append("Service Time : " + service_time + "\n");
			 Summary.append("Average Interarrival Time : " + interarrival_time / (numb_of_jobs-1) + " \n");
			 Summary.append("Average Delay Time : " + delay_time / numb_of_delayed_events + "\n");
			 Summary.append("**Average System Time : " + simulated_Clock / (numb_of_jobs-1) + "\n");
			 Summary.append("**Average Interarrival Time : " + idle_time / (numb_of_jobs-1) + "\n");
			 Summary.append("**Average Busy Period : " + busy_time / (numb_of_jobs-1) + "\n");
			 Summary.append("**Average Service Time : " + service_time / (numb_of_jobs-1) + "\n");
			 Summary.close();

	    } catch (IOException e) {
	    	System.out.println("An error occurred.");
	    	e.printStackTrace();
	    }

    }
		
    
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MM1 mm1 = new MM1();
		mm1.write_to_file("Emily Peguero Marmolejos \n");
		mm1.simulation();

	}
}
