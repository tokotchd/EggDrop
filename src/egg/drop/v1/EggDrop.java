package egg.drop.v1;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EggDrop{
	private static final int START_FLOOR = 1;
	private static final int NUM_FLOORS_INITIAL = 10;
	private static final int NUM_EGGS_INITIAL = 2;
	private static final boolean debug = true;
		
	//Version 1 outputs the contextualized optimum solution (without caching) and does not work for large values of floors or eggs.
	public static void main(String[] args) 
	{
		ProblemState initialProblemState = new ProblemState(new ArrayList<Integer>(0), START_FLOOR, NUM_FLOORS_INITIAL, NUM_EGGS_INITIAL, new ArrayList<Boolean>(0));
		ArrayList<Integer> linearDrops = new ArrayList<Integer>();
		ArrayList<Boolean> didBreak = new ArrayList<Boolean>();
		for(int counter = START_FLOOR; counter < NUM_FLOORS_INITIAL; counter++)
		{
			linearDrops.add(counter);
			didBreak.add(false);
		}
		Solution initialSolution = new Solution(linearDrops, didBreak);
		System.out.println("FINAL SOLUTION:" + getWorstCase(initialProblemState, initialSolution));
	}
	private static Solution getWorstCase(ProblemState problemState, Solution previousBestSolution) 
	{
		Solution bestSolution = previousBestSolution;
		if(problemState.currentEggs == 1) //base case, linear search.
		{
			ArrayList<Integer> floorDrops = new ArrayList<Integer>(problemState.listOfFloorsDropped);
			ArrayList<Boolean> didBreak = new ArrayList<Boolean>(problemState.didBreakAtEachDrop);
			for(int currentFloor = problemState.currentMinFloor; currentFloor < problemState.currentMaxFloor; currentFloor++) 
			{
				floorDrops.add(currentFloor);
				didBreak.add(false);
			}
			Solution thisSolution = new Solution(floorDrops, didBreak);
			if(thisSolution.compareTo(bestSolution) < 0)
				bestSolution = thisSolution;
		}
		else if(problemState.currentEggs == 0)
			System.out.println("ERROR: Attempted to get worst case with 0 eggs remaining");
		else //non-base case, recursively call getWorstCase on currentProblemState based on guesses and minimize the worst case. 
		{
			if(problemState.currentMaxFloor - problemState.currentMinFloor <= 1) //we can use process of elimination to determine that it's the min floor in this case.
			{
				Solution thisSolution = new Solution(problemState.listOfFloorsDropped,problemState.didBreakAtEachDrop);
				if(thisSolution.compareTo(bestSolution) < 0)
					bestSolution = thisSolution;
			}
			else //otherwise, there are two cases for every guess: the egg breaks and the egg doesn't break.
			{
				for(int guessedFloor = problemState.currentMinFloor; guessedFloor < problemState.currentMaxFloor - (problemState.currentEggs - 1); guessedFloor++)
				{
					ArrayList<Integer> floorDrops = new ArrayList<Integer>(problemState.listOfFloorsDropped);
					floorDrops.add(guessedFloor);
					//Egg breaks
					ArrayList<Boolean> brokenArray = new ArrayList<Boolean>(problemState.didBreakAtEachDrop);
					brokenArray.add(true);
					ProblemState eggBreaks = new ProblemState(floorDrops, guessedFloor+1, problemState.currentMaxFloor, problemState.currentEggs - 1, brokenArray);
					Solution worstCaseBreak = getWorstCase(eggBreaks, previousBestSolution);
					//Egg doesn't break
					ArrayList<Boolean> survivedArray = new ArrayList<Boolean>(problemState.didBreakAtEachDrop);
					survivedArray.add(false);
					ProblemState eggSurvives = new ProblemState(floorDrops, problemState.currentMinFloor, guessedFloor, problemState.currentEggs, survivedArray);
					Solution worstCaseSurvive = getWorstCase(eggSurvives, previousBestSolution);
					Solution worstOfBoth;
					if(worstCaseBreak.compareTo(worstCaseSurvive) > 0)
						worstOfBoth = worstCaseBreak;
					else
						worstOfBoth = worstCaseSurvive;
					if(worstOfBoth.compareTo(bestSolution) < 0)
						bestSolution = worstOfBoth;
				}
			}
		}
		return bestSolution;
	}
	
	//wrapper class to quickly construct and compare Solutions with one another.
	//A solution is a complete solution to the problem, including 
	private static class Solution implements Comparable<Solution>{
		final List<Integer> listOfFloorsDropped; //this should be a non-modifiable list to ensure that we're doing deep copies of arrays properly.
		final List<Boolean> didBreakAtEachDrop; //this does not affect the behavior of the program, is only used for pretty print statements.
		
		public Solution(List<Integer> listOfFloorsDropped, List<Boolean> didBreakAtEachDrop) {
			this.listOfFloorsDropped = Collections.unmodifiableList(listOfFloorsDropped);
			this.didBreakAtEachDrop = didBreakAtEachDrop;
			if(debug)
				System.out.println(this);
		}
		
		@Override
		public int compareTo(Solution other) {
			return(Integer.compare(this.listOfFloorsDropped.size(), other.listOfFloorsDropped.size()));
		}
		
		public String toString() 
		{
			String stringToReturn = "";
			for(int i = 0; i < NUM_FLOORS_INITIAL - START_FLOOR + 1;i++)
				stringToReturn += "\t";
			stringToReturn += "[";
			for(int i = 0; i < listOfFloorsDropped.size();i++)
			{
				stringToReturn += "" + listOfFloorsDropped.get(i) + (didBreakAtEachDrop.get(i)?"B":"S") + ",";
			}
			stringToReturn += "]";
			return stringToReturn;
		}
	}
	
	//wrapper class to quickly construct and pass current problemState through the recursion.
	private static class ProblemState {
		final List<Integer> listOfFloorsDropped; //this should be a non-modifiable list to ensure that we're doing deep copies of arrays properly.
		final List<Boolean> didBreakAtEachDrop; //this does not affect the behavior of the program, is only used for pretty print statements.
		private final int currentMinFloor;
		private final int currentMaxFloor;
		private final int currentEggs;
		
		public ProblemState(ArrayList<Integer> listOfFloorsDropped, int currentMinFloor, int currentMaxFloor, int currentEggs, ArrayList<Boolean> didBreakAtEachDrop)//first constructor for the very first problem state.
		{
			this.currentMinFloor = currentMinFloor;
			this.currentMaxFloor = currentMaxFloor;
			this.currentEggs = currentEggs;
			this.listOfFloorsDropped = Collections.unmodifiableList(listOfFloorsDropped);
			this.didBreakAtEachDrop = Collections.unmodifiableList(didBreakAtEachDrop);
			if(debug)
				System.out.println(this);
		}
		
		public String toString() 
		{
			//TODO - String builder for performance
			String stringToReturn = "";
			for(int i = 0; i < listOfFloorsDropped.size();i++)
				stringToReturn += "\t";
			stringToReturn += "[";
			for(int i = 0; i < listOfFloorsDropped.size();i++)
			{
				stringToReturn += "" + listOfFloorsDropped.get(i) + (didBreakAtEachDrop.get(i)?"B":"S") + ",";
			}
			stringToReturn += "]" + "(" + currentMinFloor + "," + currentMaxFloor + "," + currentEggs + ")";
			return stringToReturn;
		}
	}
}
