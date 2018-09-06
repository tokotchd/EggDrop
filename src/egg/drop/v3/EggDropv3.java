package egg.drop.v3;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class EggDropv3{
	public static final int NUM_FLOORS_INITIAL = 100;
	public static final int NUM_EGGS_INITIAL = 3;
		
	//Version 3 creates and traverses the entire solution tree for a given problem state, but does not keep context.
	//This is the most useful program for outputting general state graph nodes and their connections, and/or 
	//Counting the number of unique solutions and unique optimal solutions there are to a given problem state.
	public static void main(String[] args) 
	{
		FastCompleteSolutionTreeTraverser solver = new FastCompleteSolutionTreeTraverser();
		CompositeSolutionSet completeWorstCaseSolutionSet = (CompositeSolutionSet)solver.getWorstCaseSolutionSets(NUM_FLOORS_INITIAL, NUM_EGGS_INITIAL);
		SolutionSet optimizedWorstCaseSolutionSet = completeWorstCaseSolutionSet.getOptimizedSolutionSetOnly();
		System.out.println("--------------------Best Worst Case Solution Set Starts Here--------------------");
		System.out.println(optimizedWorstCaseSolutionSet.toString());
		//only uncomment this line if the data sizes are small, otherwise the IO lag will cause the program to never finish.
//		System.out.println("-----------------------Complete Solution Set Starts Here------------------------");
//		completeWorstCaseSolutionSet.printAllSolutionSets(System.out, "");
//		System.out.println("-----------------------Optimized Solution Set Starts Here-----------------------");
//		optimizedWorstCaseSolutionSet.printAllSolutionSets(System.out, "");
		System.out.println("---------------------------------Cool Metadata----------------------------------");
		System.out.println("Number of unique solutions: " + completeWorstCaseSolutionSet.getNumberOfUniqueSolutions());
		System.out.println("Number of unique optimum solutions: " + optimizedWorstCaseSolutionSet.getNumberOfUniqueSolutions());
		
		try 
		{
			String filePath = ("/home/tokotchd/eggNodesOptimum-" + NUM_FLOORS_INITIAL + "-" + NUM_EGGS_INITIAL + ".txt");
			PrintStream printStream = new PrintStream(new File(filePath));
		    optimizedWorstCaseSolutionSet.printAllSolutionSets(printStream, "");
		    System.out.println("Wrote optimized node file to: " + filePath);
		    
		    //Only uncomment writing complete node tree if you're certain your data size is small enough.
//		    String filePath2 = ("/home/tokotchd/eggNodesComplete-" + NUM_FLOORS_INITIAL + "-" + NUM_EGGS_INITIAL + ".txt");
//			PrintStream printStream2 = new PrintStream(new File(filePath2));
//			completeWorstCaseSolutionSet.printAllSolutionSets(printStream2, "");
//		    System.out.println("Wrote complete node file to: " + filePath2);
		} catch (IOException e) 
		{
			System.out.println("Failed to be able to write nodes to file");
			e.printStackTrace();
		}
	}
}
