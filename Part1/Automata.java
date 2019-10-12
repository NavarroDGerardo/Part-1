/**
*
*This is a Java program that read form a file the elements
*that define an NDFA - lamda and that indicates if a string 
*is accepted by the automata.
*
*@author Diego Gerardo Navarro Gonzalez
*@author Alan Omar Zavala Levaro
*
*/

import java.io.*;
import java.util.*;

public class Automata{
    //this is the variable of initial state
    static String initState="";
    //Structure that contains all final states;
    static HashSet<String> endStates = new HashSet<String>();
    //Structure that contains the transition table, being the string the origin state, and the hash contains as the key the character and as the value the destination states
    static HashMap<String, HashMap<String, ArrayList<String>>> transition = new HashMap<String, HashMap<String, ArrayList<String>>>();

    public boolean processString(String name, String test) throws IOException{
        setStates(name);
        return checkString(test);
    }

    public void setStates(String name) throws IOException{
        /**
        *
        *in this method we will get the name of the .txt file and the string that will be tested in the automata.
        *
        */
        File file;
        Scanner scan;
        try{
            file = new File(name + ".txt");
            scan = new Scanner(file); //the scanner will read the file;
        }catch(FileNotFoundException e1){
            throw e1;
        }

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
                
                String init = process[0].substring(0, 2);//we will get the initial state of where we
                String pro = process[0].substring(3, process[0].length());//this will get the character that we will process from the initial state
                //this will get the destination state by processing the character form the line above
                //String fin=process[1];
                String[] destination=process[1].split(",");
                
            
                //if the transition HashMap<> doesn't contain the init state of the proceess we need to create a new Map with the name of the state
                //and a new ArrayList for the characters that the state can process
                if(!transition.containsKey(init)) transition.put(init, new HashMap<String, ArrayList<String>>());
                //we will need to get the Map inside the initial state so we can check if the letter that we need to process is not in the Map of the
                //initial state we need to create a new map with the letter that we need to process and a ArrayList to add all the states that we can
                //arrive processing that letter.
                if(!transition.get(init).containsKey(pro)) transition.get(init).put(pro, new ArrayList<>());
                //after cheking the two conditions above we can finally add the final state to the HashMap of the inistial state and tell from whic state
                //we can arrive by processing fin.
                if(destination.length>1){
                    //We iterate through the destination states and add them to the Arraylist contained in the HashmMap of the Hashmap
                    for(int i=0; i<destination.length; i++){
                        transition.get(init).get(pro).add(destination[i]);
                    }
                }
                else{
                    transition.get(init).get(pro).add(destination[0]);//this is if there is only one state to add.
                }           
            }

            System.out.println("initial state: " + initState);//this will print the initial state name
            System.out.println("Set of final states: " + endStates);//this will print the set of final states
            System.out.println("transition table: " + transition);//this will print the transition table of the lambda - NDFA
        }
    }

    //we start the method to procces the string, this method return the results as a boolean and also generates an Arraylist that 
    //is send to the method isAccepted
    public static boolean checkString(String test){
        int len=0;//variable for the length of the string to process
        ArrayList<String> myArrayList=new ArrayList<String>(); //This structure has the current states that will be checked to do the transition
        ArrayList<String> aux=new ArrayList<String>(); //this structure is the one that will update the current states to proccess
        myArrayList.add(initState); //it start with initial state

        //First we verified if the string to procces is the empty string
        if(test.equals("lmd")){
            return isAccepted(myArrayList); //this method receives the partial final states in order to see if by using lambda can reach other states.
        }
        //here starts the procces to verify that the strins is accepted with a while loop that runs the length of the string received.
        while(test.length()>len){
            
            //here the aux ArrayList is restarted in order to get the next letter to process states.
            aux=new ArrayList<String>();
            //this loop iterates over the current states in order to get their destinations states that procces the letter we want
            for(int i=0; i<myArrayList.size(); i++){
                
                if(transition.containsKey(myArrayList.get(i))){ //here we assure that the state exists in the hash meaning that it can reach other states.
                    if(transition.get(myArrayList.get(i)).containsKey(Character.toString(test.charAt(len)))){ //We check if the state can procces the letter to find
                        //here we iterate through the destiantion states in order to add them to the aux Arraylist that later will update the current states
                        for(int j=0; j<transition.get(myArrayList.get(i)).get(Character.toString(test.charAt(len))).size(); j++){ 
                            if(!aux.contains(transition.get(myArrayList.get(i)).get(Character.toString(test.charAt(len))).get(j))){
                                aux.add(transition.get(myArrayList.get(i)).get(Character.toString(test.charAt(len))).get(j)); //here we add them to the aux
                            }
                        }
                    }
                    //Here we check if the states in the myArraylist structure (the current ones) also can procces lamda
                    if(transition.get(myArrayList.get(i)).containsKey("lmd")){  
                        ArrayList<String>visited=new ArrayList<String>();//Here we save the lambdas that have been proccessed in order to avoid cycles (infinite loop)
                        ArrayList<String>lambdas=new ArrayList<String>();//Here we save the states that can proccess lamdbda
                        visited.add(myArrayList.get(i));//we save the current state in the visited ones to not repeat it
                        lambdas.add(myArrayList.get(i)); //we save the states that can proccess lambda
                        ArrayList<String>auxLmd=new ArrayList<String>();//Now we save here the states that we can reach by proccessing lambda
                        ArrayList<String>auxAux; //acualiza lambdas, es decir los estados que pueden procesar lambda
                        //auxLmd.add(myArrayList.get(i));
                        
                        //here we create a loop in order to procces all the lambdas, and when there are no lambdas to procces, it ends
                        while(lambdas.size()>0){
                            
                            //here is the same procces as in the update of current states, we create an auxiliar variable to update to current states that procces lambda
                            auxAux=new ArrayList<String>();
                            //here we iterate through the current states that can proccess lambda
                            for(int k=0; k<lambdas.size(); k++){
                                //here we get the states that can be reach by proccessing lambda
                                for(int l=0; l<transition.get(lambdas.get(k)).get("lmd").size();l++){
                                    //here we verified that we do not save the same state twice
                                    if(!auxLmd.contains(transition.get(lambdas.get(k)).get("lmd").get(l))){
                                        auxLmd.add(transition.get(lambdas.get(k)).get("lmd").get(l)); //los destinos de lambda
                                    }
                                }
                            }
                            //now we check if the states reached by lambda, can procces the char we are looking for
                            for(int k=0; k<auxLmd.size(); k++){
                                if(transition.get(auxLmd.get(k)).containsKey(Character.toString(test.charAt(len)))){
                                    //here we iterate trough the states that we get we can use to reach the letter
                                    for(int m=0; m<transition.get(auxLmd.get(k)).get(Character.toString(test.charAt(len))).size(); m++){
                                        //here we verified that the state that can reach the letter, is not already save to the aux arraylist that updates the current states
                                        if(!aux.contains(transition.get(auxLmd.get(k)).get(Character.toString(test.charAt(len))).get(m))){
                                            aux.add(transition.get(auxLmd.get(k)).get(Character.toString(test.charAt(len))).get(m));
                                        }   
                                    }
                                }
                                //here we verified if the states obtained that can procces the letter, also can procces lambda in order to update the auxAux Arraylist
                                //so then we check again if there are more states that can reach what we want via lambda
                                if(transition.get(auxLmd.get(k)).containsKey("lmd")){
                                    //also we update the visited ones in order to avoid the cycles with the lambdas
                                    if(!visited.contains(auxLmd.get(k))){
                                        auxAux.add(auxLmd.get(k));
                                        visited.add(auxLmd.get(k));
                                    }
                                }
                            }
                            
                            //here we update the states that can procces lambda in order to start again until we have a final result of states that procces the letter we want
                            lambdas=auxAux;
                        }
                    }
                }

                
            }
            //we check that if the aux have no elements, this means the letter we are looking form cannot be reached with any state.
            if(aux.size()==0){
                    return false;
            }
            myArrayList=aux; //here we update the current states to continue with the next letter
            len++; //we move to the next letter
        }
        //after checking all the letters in the string, we send the last states we got to the isAccepted mehod in order to verify if one of those states 
        //is a final state in the automata and then returns if it is accepted or not
        return isAccepted(myArrayList); 


    }
    //this method verified if we get a final state or not, also checking if our current "final states" can reach via lambda other states that also have to be
    //considered as the last states we got by proccesing the string. So this methos receives the last states we got
    public static boolean isAccepted(ArrayList<String> partialFinalStates){

        ArrayList<String>lastStates=new ArrayList<String>(); //here we save the last states we got by proccesing lambda (in the ones that is possible)
        //here we iterate to see which "last states" can procces lambda to obtain more "last states"
        for(int i=0; i<partialFinalStates.size(); i++){
            //we verified that the states can reach other state
            if(transition.containsKey(partialFinalStates.get(i))){
                //we verified if them can procces lambda
                if(transition.get(partialFinalStates.get(i)).containsKey("lmd")){
                    //here as before, we create structures to find the lambdas and avoid cycles between them
                    ArrayList<String> finalLambdas=new ArrayList<String>(); //here we have the states that can proccess lambda
                    ArrayList<String> auxFLambdas; //this structure will update the finalLambdas structure
                    ArrayList<String> finalVisited=new ArrayList<String>(); //here we saved the ones that already have been proccessed to avoid cycles
                    finalLambdas.add(partialFinalStates.get(i)); //we add the first element in the list in order to start
                    finalVisited.add(partialFinalStates.get(i)); //we add the first element in the list in order to start the visited ones
                    //iterate through the states that can procces lambda
                    while(finalLambdas.size()>0){
                                
                                //we restart the structure that will update the finalLambdas structure
                                auxFLambdas=new ArrayList<String>();
                                //iterate throught the current states that we are going to check if can proccess lamhda
                                for(int k=0; k<finalLambdas.size(); k++){
                                    //we verified that the state can proccess lambda
                                    if(transition.get(finalLambdas.get(k)).containsKey("lmd")){
                                        //iterate through the states that can be reached by proccessing lambda 
                                        for(int l=0; l<transition.get(finalLambdas.get(k)).get("lmd").size();l++){
                                            //here we add the new last states to the lastStates structure and also we heck that they are not repeated
                                            if(!lastStates.contains(transition.get(finalLambdas.get(k)).get("lmd").get(l))){
                                                lastStates.add(transition.get(finalLambdas.get(k)).get("lmd").get(l)); //los destinos de lambda
                                            }
                                            //we update the visited ones to avoid cycles, and also insert in the auxFLambdas the new last states that can 
                                            //proccess lambda too
                                            if(!finalVisited.contains(transition.get(finalLambdas.get(k)).get("lmd").get(l))){
                                                finalVisited.add(transition.get(finalLambdas.get(k)).get("lmd").get(l));
                                                auxFLambdas.add(transition.get(finalLambdas.get(k)).get("lmd").get(l)); //los destinos de lambda
                                            }
                                            
                                        }
                                    }
                                }
                                finalLambdas=auxFLambdas; //we update the states that can proccess lambda
                    }
                }
            }
        }
        //finally we check if the last states we get by proccesing lambda contains a final state of the automata
        for(int i=0; i<partialFinalStates.size(); i++){
            if(endStates.contains(partialFinalStates.get(i))){
                return true;
            }
        }
        //and also if one of the original last states we got, contains a final state of the automata
        for(int i=0; i<lastStates.size(); i++){
            if(endStates.contains(lastStates.get(i))){
                return true;
            }
        }

        return false;
    }
}