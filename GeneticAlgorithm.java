/*
 * Written By: James McCune; Purpose is to implement a Genetic Algorithm with the one-max problem.
 */

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


public class GeneticAlgorithm {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int pop_size = 0;
		int string_size = 0;
		boolean continueprompt = true;
		do {
			System.out.println("Enter Population Size: ");
			try {
				pop_size = input.nextInt();
				continueprompt = false;
			} catch (InputMismatchException IME) {
				System.out.println("This should be an integer.");
				input.nextLine();
			}
		} while (continueprompt);
		continueprompt = true;
		
		do{
			System.out.println("Enter String Size: ");
			try {
				string_size = input.nextInt();
				continueprompt = false;
			} catch (InputMismatchException IME) {
				System.out.println("This should be an integer.");
				input.nextLine();
			}
		}while(continueprompt);
		input.close();
		
		long time = System.currentTimeMillis();
		new GeneticAlgorithm(pop_size,string_size);
		System.out.println("\n\t\tRunning Time: " + (System.currentTimeMillis()-time) + " ms");
		
	}
	
	public GeneticAlgorithm(int popsize, int stringsize){
		geneticAlgorithm(popsize, stringsize);
	}
	
	//Inclusive Random Number Function
	private static int random(int min, int max){
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	//Generates Random Binary String
	private ArrayList<Integer> generateBinaryString(int stringsize){
		ArrayList<Integer> string = new ArrayList<Integer>();
		for(int i = 0; i < stringsize; i++){
			if(random(0,1) == 0){
				string.add(0);
			}
			else{
				string.add(1);
			}
		}
		return string;
	}
	
	//Generates Binary String Population
	private ArrayList<ArrayList<Integer>> generateRandPop(int popsize, int stringsize){
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < popsize; i++){
			list.add(generateBinaryString(stringsize));
		}
		
		
		return list;
	}
	
	//Compares Two Binary Strings
	private int tournament(ArrayList<ArrayList<Integer>> pop){
		int rand1 = random(0,pop.size()-1);
//		System.out.println("Tournament Player 1: " + pop.get(rand1) + " Fitness: " + calculateFitness(pop.get(rand1)));
		int rand2 = random(0, pop.size()-1);
//		System.out.println("Tournament Player 2: " + pop.get(rand2) + " Fitness: " + calculateFitness(pop.get(rand2)));
		
		if(calculateFitness(pop.get(rand1)) >= calculateFitness(pop.get(rand2))){
//			System.out.println("\tTournament Winner: " + pop.get(rand1));
			return rand1;
		}
		else{
//			System.out.println("\tTournament Winner: " + pop.get(rand2));
			return rand2;
		}
	}
	
	//Calculates The Fitness Of A Binary String
	private int calculateFitness(ArrayList<Integer> string){
		int fitness = 0;
		for(int i = 0; i < string.size(); i++){
			if(string.get(i) == 1){
				fitness++;
			}
		}
		return fitness;
	}

	//Checks For Greatest Population Fitness
	private boolean checkFitness(ArrayList<ArrayList<Integer>> pop){
		for(int i = 0; i < pop.size(); i++){
			if(calculateFitness(pop.get(i)) == pop.get(i).size()){
				return true;
			}
		}
		return false;
	}
	
	//Produces Children Based Off Parents
	private ArrayList<ArrayList<Integer>> produceChildren(ArrayList<Integer> x, ArrayList<Integer> y){
//		System.out.println("\t\tParent 1: " + x);
//		System.out.println("\t\tParent 2: " + y);
		ArrayList<ArrayList<Integer>> children = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> child1 = new ArrayList<Integer>();
		ArrayList<Integer> child2 = new ArrayList<Integer>();
		
		//60% Of The Time
		if(random(0,100) <= 60){
			//Creates Child1 - Random Mix of X & Y
			for(int i = 0; i < x.size(); i++){
				if(random(0,1) == 1){
					child1.add(x.get(i));
					child2.add(y.get(i));
				}
				else{
					child1.add(y.get(i));
					child2.add(x.get(i));
				}
			}
		
			//Creates Opposite Child of Child1
//			for(int i = 0; i < child1.size(); i++){
//				if(child1.get(i) == 1){
//					child2.add(0);
//				}
//				else{
//					child2.add(1);
//				}
//			}
		}
		//Else Just Copy Each Parent to Child
		else{
			child1 = x;
			child2 = y;
		}
		
		
		
		children.add(child1);
		children.add(child2);
	
//		System.out.println("\t\t\tChild 1: " + child1);
//		System.out.println("\t\t\tChild 2: " + child2);
		for(int i = 0; i < children.size(); i++){
//			System.out.print("\t\tChild " + i + ": ");
			for(int j = 0; j < children.get(i).size(); j++){
//				System.out.print(children.get(i).get(j));
				if(random(0,100) <= (1/child1.size()*100)){
					if(children.get(i).get(j) == 1){
						children.get(i).set(j, 0);
					}
					else{
						children.get(i).set(j, 1);
					}
					
				}
			}
//			System.out.print("\n");
		}
//		System.out.println("\t\t\tChild 1 after mutation: " + child1);
//		System.out.println("\t\t\tChild 2 after mutation: " + child2);
		
		return children;
	}
	
	//Displays Statistics
	private void displayStatistics(ArrayList<ArrayList<Integer>> pop){
		double average = 0;
		double best = 0;
		double worst = calculateFitness(pop.get(0));
		
		for(int i = 0; i < pop.size(); i++){
			int fitness = calculateFitness(pop.get(i));
			average += fitness;
			if(fitness > best){
				best = fitness;
			}
			if(fitness < worst){
				worst = fitness;
			}
		}
		average = average/pop.size();
		
		//System.out.println("\tGeneration Statistics: ");
		System.out.println("\tBest: " + best);
		System.out.println("\tWorst: " + worst);
		System.out.println("\tAverage: " + average);
	}
	
	//Retains Most Fit Individual From Old Pop and Removes Worst From New Pop
	private ArrayList<ArrayList<Integer>> keepMostFit(ArrayList<ArrayList<Integer>> oldpop, ArrayList<ArrayList<Integer>> newpop){
		int mostfit = 0;
		int worst = 0;
		
		for(int i = 0; i < oldpop.size(); i++){
			if(calculateFitness(oldpop.get(i)) > calculateFitness(oldpop.get(mostfit))){
				mostfit = i;
			}
		}
		
		for(int i = 0; i < newpop.size(); i++){
			if(calculateFitness(newpop.get(i)) < calculateFitness(newpop.get(worst))){
				worst = i;
			}
		}
//		System.out.println("Most Fit Parent: " + oldpop.get(mostfit));
		newpop.set(worst, oldpop.get(mostfit));
		return newpop;
	}
	
	//Genetic Algorithm Start
	private boolean geneticAlgorithm(int popsize, int stringsize){
		int num = 0;
		ArrayList<ArrayList<Integer>> pop = generateRandPop(popsize, stringsize);
//		printList(pop);
		displayStatistics(pop);
		while(checkFitness(pop) != true){
			System.out.println("\nStarting Generation: " + num);
			ArrayList<ArrayList<Integer>> newpop = new ArrayList<ArrayList<Integer>>();
			for(int i = 0; i < pop.size()/2; i++){
				int x = tournament(pop);
				//System.out.println("Tournament 1 Winner: " + x);
				int y = tournament(pop);
				//System.out.println("Tournament 2 Winner: " + y);
				
				ArrayList<ArrayList<Integer>> children = produceChildren(pop.get(x), pop.get(y));
				for(int j = 0; j < children.size(); j++){
					newpop.add(children.get(j));
				}
			}
//			printList(newpop);
			
			pop = keepMostFit(pop,newpop);
			displayStatistics(pop);
			num++;
			
		}
		return true;
	}
	
}

