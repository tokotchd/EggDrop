package egg.drop.v3;

public class EggDropv3{
	public static final int NUM_FLOORS_INITIAL = 10;
	public static final int NUM_EGGS_INITIAL = 2;
	public static final boolean debug = true;
		
	//Version 3 creates and traverses the entire solution tree for a given problem state, but does not keep context.
	//This is the most useful program for outputting general state graph nodes and their connections, and/or 
	//Counting the number of unique solutions and unique optimal solutions there are to a given problem state.
	public static void main(String[] args) 
	{
		FastCompleteSolutionTreeTraverser solver = new FastCompleteSolutionTreeTraverser();
		CompositeSolutionSet completeWorstCaseSolutionSet = (CompositeSolutionSet)solver.getWorstCaseSolutionSet(NUM_FLOORS_INITIAL, NUM_EGGS_INITIAL);
		SolutionSet optimizedWorstCaseSolutionSet = completeWorstCaseSolutionSet.getOptimizedSolutionSetOnly();
		System.out.println(optimizedWorstCaseSolutionSet);
		optimizedWorstCaseSolutionSet.printAllSolutionSets("");
		System.out.println(completeWorstCaseSolutionSet.getNumberOfUniqueSolutions());
		System.out.println(optimizedWorstCaseSolutionSet.getNumberOfUniqueSolutions());
	}
}
