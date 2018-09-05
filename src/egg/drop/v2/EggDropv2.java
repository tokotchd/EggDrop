package egg.drop.v2;

public class EggDropv2{
	public static final int START_FLOOR = 1;
	public static final int NUM_FLOORS_INITIAL = 100;
	public static final int NUM_EGGS_INITIAL = 3;
	public static final boolean debug = false;
		
	//Version 2 outputs a contextualized optimal solution in reasonable time for N floors, M eggs using caching for speed.
	public static void main(String[] args) 
	{
		FastSolutionGenerator solver = new FastSolutionGenerator();
		Solution solution = solver.getBestWorstSolution(START_FLOOR, NUM_FLOORS_INITIAL, NUM_EGGS_INITIAL);
		System.out.println("Optimized Worst Case: " + solution.listOfFloorsDropped.size() + " drops.\n" + solution.toString());
		
	}
}
