 import java.io.*;
 import java.util.*;

 public class Main{
 	static String initState="";
 	static HashSet<String> endStates = new HashSet<String>();
 	static HashMap<String, HashMap<String, ArrayList<String>>> transition = new HashMap<String, HashMap<String, ArrayList<String>>>();

	public static void main(String[] args) throws IOException{
		Scanner in=new Scanner(System.in);
		//this are the variables of initial state
		//String initState = "";
		//set of variables of final states;
		//HashSet<String> endStates = new HashSet<String>(); ALAN ACUERDATE DE PREGUNTAR ESTO
		//variable for the transitions in the lambda - NDFA
		//HashMap<String, HashMap<String, ArrayList<String>>> transition = new HashMap<String, HashMap<String, ArrayList<String>>>();
		//ALAN ACUERDATE DE PREGUNTAR ESTO!!!

		//this is the location of the file name test1.txt
		System.out.println("Hello, insert the name of your test file in the format: NAME.txt");
		String nameFile=in.next();
		File file = new File(nameFile);
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
					for(int i=0; i<destination.length; i++){
						transition.get(init).get(pro).add(destination[i]);
					}
				}
				else{
					transition.get(init).get(pro).add(destination[0]);
				}			
			}

			System.out.println("initial state: " + initState);//this will print the initial state name
			System.out.println("Set of final states: " + endStates);//this will print the set of final states
			System.out.println("transition table: " + transition);//this will print the transition table of the lambda - NDFA
		}
		System.out.println("Hello, enter a string you want to know if can be created with the automata");
		String testCase=in.next();

		System.out.println(checkString(testCase));
	}
	public static boolean checkString(String test){
		int len=0;
		ArrayList<String> myArrayList=new ArrayList<String>();
		ArrayList<String> aux=new ArrayList<String>();
		myArrayList.add(initState);

		if(test.equals("lmd")){
			if(endStates.contains(initState)){
				return true;
			}
			else{
				return false;
			}
		}

		while(test.length()>len){
			aux=new ArrayList<String>();
			//System.out.println(myArrayList);
			//System.out.println("Contenido en arraylist " +myArrayList.get(0));
			System.out.println("cuales voy a iterar "+myArrayList);
			for(int i=0; i<myArrayList.size(); i++){
				//System.out.println("len " +len);
				//System.out.println("i: "+i);
				//System.out.println(transition.get(myArrayList.get(i)));
				System.out.println("print prueba array i position "+myArrayList.get(i));
				System.out.println("print char debajo de prueba array "+test.charAt(len));
				System.out.println("a ver que "+transition.get(i));
				if(transition.containsKey(myArrayList.get(i))){
					if(transition.get(myArrayList.get(i)).containsKey(Character.toString(test.charAt(len)))){
						for(int j=0; j<transition.get(myArrayList.get(i)).get(Character.toString(test.charAt(len))).size(); j++){
							if(!aux.contains(transition.get(myArrayList.get(i)).get(Character.toString(test.charAt(len))).get(j))){
								aux.add(transition.get(myArrayList.get(i)).get(Character.toString(test.charAt(len))).get(j));
							}
						}
					}
					//Empiezo a checar lambdas
					if(transition.get(myArrayList.get(i)).containsKey("lmd")){	
						ArrayList<String>visited=new ArrayList<String>();//estados ya visitados que tienen lambda (para que no se cicle)
						ArrayList<String>lambdas=new ArrayList<String>();//estados origen para procesar lambda
						visited.add(myArrayList.get(i));
						lambdas.add(myArrayList.get(i)); //aqui guardo los estados que pueden procesar lambda
						ArrayList<String>auxLmd=new ArrayList<String>();//guarda los destinos de lambda
						ArrayList<String>auxAux; //acualiza lambdas, es decir los estados que pueden procesar lambda
						//auxLmd.add(myArrayList.get(i));
						
						//si ya no hay (o simplemente no hubo ninguno) mas estados nuevos que procesen lambda rompe el while
						while(lambdas.size()>0){
							System.out.println("Estados de lambdas a procesar "+lambdas);

							auxAux=new ArrayList<String>();
							//itero sobre los estados origen que pueden procesar lambda
							for(int k=0; k<lambdas.size(); k++){
								//aqui guardo en auxLmd los estados destino al procesar lambda de los estados origen
								for(int l=0; l<transition.get(lambdas.get(k)).get("lmd").size();l++){
									if(!auxLmd.contains(transition.get(lambdas.get(k)).get("lmd").get(l))){
										auxLmd.add(transition.get(lambdas.get(k)).get("lmd").get(l)); //los destinos de lambda
									}
								}

							}
							System.out.println("estados que llego procesando la lambda "+auxLmd);

							//empiezo a checar si los Estadoos destino pueden procesar la letra que se busca de la palabra
							for(int k=0; k<auxLmd.size(); k++){

								if(transition.get(auxLmd.get(k)).containsKey(Character.toString(test.charAt(len)))){
									System.out.println("NO ENTREES");
									for(int m=0; m<transition.get(auxLmd.get(k)).get(Character.toString(test.charAt(len))).size(); m++){
										if(!aux.contains(transition.get(auxLmd.get(k)).get(Character.toString(test.charAt(len))).get(m))){
											aux.add(transition.get(auxLmd.get(k)).get(Character.toString(test.charAt(len))).get(m));
										}
										
									}
								}
								//checo si alguno de los estados destino tambien pueden procesar lambda para asi guardarlo en el auxiliar que servira de reset, 
								//asi como en los visitados para evitar los ciclos
								System.out.println("valor de la k "+k);
								if(transition.get(auxLmd.get(k)).containsKey("lmd")){
									System.out.println("character "+test.charAt(len));

									if(!visited.contains(auxLmd.get(k))){
										auxAux.add(auxLmd.get(k));
										visited.add(auxLmd.get(k));
									}
									
								}
							}
							
							//reseteo los estados que ahora son origen y pueden procesar lambda
							lambdas=auxAux;
						}
					}
				}

				
			}
			if(aux.size()==0){
					return false;
			}
			myArrayList=aux;
			len++;
		}
		System.out.println(aux);
		for(int i=0; i<myArrayList.size(); i++){
			if(endStates.contains(myArrayList.get(i))){
				return true;
			}
		}

		return false;
	}

	
}

















