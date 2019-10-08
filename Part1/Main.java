 import java.io.*;
 import java.util.*;

 public class Main{
	public static void main(String[] args) throws IOException{
		//this are the variables of initial state
		String initState = "";
		//set of variables of final states;
		HashSet<String> endStates = new HashSet<String>();
		//variable for the transitions in the lambda - NDFA
		HashMap<String, HashMap<String, ArrayList<String>>> transition = new HashMap<String, HashMap<String, ArrayList<String>>>();
		
		//this is the location of the file name test1.txt
		File file = new File("test1.txt");
		Scanner scan = new Scanner(file); //the scanner will read the file;

		if(scan.hasNextLine()){//if the file is empty the following code will not run
			System.out.println("set of states: " + scan.nextLine());// this will print the set of states int the console
			System.out.println("alphabet symbols: " + scan.nextLine() + "\n");//this will print the alphabet that we will use in the lambda NDFA

			initState = scan.nextLine(); //this will get the third row of the .txt file wich include the name of the initial state
			String line = scan.nextLine();//this will get the fourth row of the .txt file wich include the set of final states
			String[] final_state = line.split(",");//this will split the fouth row and put it in an array, so we cna read it.
			for(String s : final_state){//next we will iterate through the array of final states
				endStates.add(s);//we add each final state to the HashSet of end states
			}

			//the next lines in the .txt file are the transition functions in the lambda NDFA, so we use a while loop, because we dont know when to stop
			while(scan.hasNextLine()){//this condition will stop until the last line in the .txt file
				line = scan.nextLine();//we read the next line in the .txt file
				String[] process = line.split("=>");//the structure of the transiitons is q0,lmd=>q0 so we split the String in an array of Strings
				//the array will contain two Strings. The first one will be "q0,lmd" and the second one will tell us where we arrive processing the char
				
				String init = process[0].substring(0, 3);//we will get the initial state of where we
				String pro = process[0].substring(3, process[0].length());//this will get the character that we will process from the initial state
				String fin = process[1];//this will get the destination state by processing the character form the line above

				//if the transition HashMap<> doesn't contain the init state of the proceess we need to create a new Map with the name of the state
				//and a new ArrayList for the characters that the state can process
				if(!transition.containsKey(init)) transition.put(init, new HashMap<String, ArrayList<String>>());
				//we will need to get the Map inside the initial state so we can check if the letter that we need to process is not in the Map of the
				//initial state we need to create a new map with the letter that we need to process and a ArrayList to add all the states that we can
				//arrive processing that letter.
				if(!transition.get(init).containsKey(pro)) transition.get(init).put(pro, new ArrayList<>());
				//after cheking the two conditions above we can finally add the final state to the HashMap of the inistial state and tell from whic state
				//we can arrive by processing fin.
				transition.get(init).get(pro).add(fin);
			}

			System.out.println("initial state: " + initState);//this will print the initial state name
			System.out.println("Set of final states: " + endStates);//this will print the set of final states
			System.out.println("transition table: " + transition);//this will print the transition table of the lambda - NDFA
		}
	}
}