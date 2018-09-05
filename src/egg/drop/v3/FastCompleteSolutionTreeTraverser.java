package egg.drop.v3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class FastCompleteSolutionTreeTraverser 
{
	private Hashtable<ProblemState, SolutionSet> solutionCache;
	
	public FastCompleteSolutionTreeTraverser() 
	{
		solutionCache = new Hashtable<ProblemState, SolutionSet>();
	}
	
	
	public SolutionSet getWorstCaseSolutionSet(int floorsRemaining, int eggsRemaining)
	{
		ProblemState abstractProblemState = new ProblemState(floorsRemaining, eggsRemaining);
		
		//Case 1, cache contains abstract solution, simply return it.
		if(solutionCache.get(abstractProblemState) != null)
		{
			return solutionCache.get(abstractProblemState);
		}
		//Case 2, one floor left. No additional drops required.
		else if(floorsRemaining == 1) 
		{
			ArrayList<Integer> floorDrops = new ArrayList<Integer>(0);
			ArrayList<Boolean> didBreak = new ArrayList<Boolean>(0);
			Solution baseCase2Solution = new Solution(floorDrops, didBreak);
			AtomicSolutionSet baseCase2SolutionSet = new AtomicSolutionSet(abstractProblemState, baseCase2Solution);
			solutionCache.put(abstractProblemState, baseCase2SolutionSet);
			return baseCase2SolutionSet;
		}
		//Case 3, there's only 2 floors left, Then we need to drop on the lower floor.  We know conclusively from that drop that it must be that floor.
		else if(floorsRemaining == 2)
		{
			ArrayList<Integer> floorDrops = new ArrayList<Integer>(1);
			ArrayList<Boolean> didBreak = new ArrayList<Boolean>(1);
			floorDrops.add(1);
			didBreak.add(false);
			Solution baseCase3Solution = new Solution(floorDrops, didBreak);
			AtomicSolutionSet baseCase3SolutionSet = new AtomicSolutionSet(abstractProblemState, baseCase3Solution);
			solutionCache.put(abstractProblemState, baseCase3SolutionSet);
			return baseCase3SolutionSet;
		}
		//Case 4, perform one step of the linear search.
		else if(eggsRemaining == 1)
		{
			Hashtable<Integer,SolutionSet> firstFloorDroppedSolutionSet = new  Hashtable<Integer,SolutionSet>(1);
			SolutionSet droppedOnLowestFloor = getWorstCaseSolutionSet(floorsRemaining - 1, eggsRemaining);
			firstFloorDroppedSolutionSet.put(1, droppedOnLowestFloor);
			
			CompositeSolutionSet baseCase4SolutionSet = new CompositeSolutionSet(abstractProblemState, firstFloorDroppedSolutionSet);
			
			solutionCache.put(abstractProblemState, baseCase4SolutionSet);
			return baseCase4SolutionSet;
		}
		//Case 5, safety error case.
		else if(eggsRemaining < 1 || floorsRemaining < 1)
		{
			System.out.println("ERROR: Attempted to get worst case with <1 eggs or floors remaining");
			return null;
		}
		else
		{
			//Case 6, recursive check for solution Monte Carlo style.
			final Hashtable<Integer,SolutionSet> dropSolutionSets = new Hashtable<Integer, SolutionSet>();
			for(int guessedFloor = 1; guessedFloor < floorsRemaining; guessedFloor++)
			{
				//Egg Survives on Floor 3 of 10 - new range for minimum breaking floor is [4,10] -> 6 floors remaining
				//Egg Breaks on Floor 3 of 10 - new range for minimum breaking floor is [1,3] -> 3 floors remaining
				
				//Egg Survives
				SolutionSet bestCaseSurvive = getWorstCaseSolutionSet(floorsRemaining - (guessedFloor), eggsRemaining);
				
				//Egg breaks
				SolutionSet bestCaseBreak = getWorstCaseSolutionSet(guessedFloor, eggsRemaining - 1);		
				
				//Determining worst case of egg breaking now and egg not breaking now.
				SolutionSet worstOfBoth;
				if(bestCaseSurvive.compareTo(bestCaseBreak) < 0) //if breaking is the worst case.
					worstOfBoth = bestCaseBreak;
				else
					worstOfBoth = bestCaseSurvive;
				
				dropSolutionSets.put(guessedFloor, worstOfBoth);
			}
			final CompositeSolutionSet compositeSolutionSet = new CompositeSolutionSet(abstractProblemState, dropSolutionSets);
			solutionCache.put(abstractProblemState, compositeSolutionSet);
			return compositeSolutionSet;
		}
	}
}