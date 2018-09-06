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
	public SolutionSet getWorstCaseSolutionSets(int floorsRemaining, int eggsRemaining)
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
			Hashtable<Integer,List<SolutionSet>> firstFloorDroppedSolutionSet = new  Hashtable<Integer,List<SolutionSet>>(1);
			List<SolutionSet> firstFloorDroppedSolutionList = new ArrayList<SolutionSet>(1);
			SolutionSet droppedOnLowestFloor = getWorstCaseSolutionSets(floorsRemaining - 1, eggsRemaining);
			firstFloorDroppedSolutionList.add(droppedOnLowestFloor);
			firstFloorDroppedSolutionSet.put(1, firstFloorDroppedSolutionList);
			
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
			final Hashtable<Integer,List<SolutionSet>> dropSolutionSets = new Hashtable<Integer, List<SolutionSet>>();
			for(int guessedFloor = 1; guessedFloor < floorsRemaining; guessedFloor++)
			{
				//Egg Survives on Floor 3 of 10 - new range for minimum breaking floor is [4,10] -> 6 floors remaining
				//Egg Breaks on Floor 3 of 10 - new range for minimum breaking floor is [1,3] -> 3 floors remaining
				
				//Egg Survives
				SolutionSet worstCasesForEggSurviving = getWorstCaseSolutionSets(floorsRemaining - guessedFloor, eggsRemaining);
				
				//Egg breaks
				SolutionSet worstCasesForEggBreaking = getWorstCaseSolutionSets(guessedFloor, eggsRemaining - 1);		
				
				//If the best cases for each worst case solution set are equal in length, that means there are two unique worst cases for that 
				
				//Here, we compare the best cases from the two possible sub-solution-sets from the results of the drop 
				//and take the worst result of the two to be the worst result for this drop, with the inclusion of this drop.
				//If those two best cases are equal in length, however, that means that there are two unique "best worst" cases
				//for dropping on this floor and we must add them both.
				List<SolutionSet> worstOfBoth = new ArrayList<SolutionSet>(2); //length of either 2 or 1.
				int comparison = worstCasesForEggSurviving.compareTo(worstCasesForEggBreaking);
				if(comparison == 0) //if the two cases are equally as bad, then there exists two unique worst case scenarios for this drop.
				{
					worstOfBoth.add(worstCasesForEggBreaking);
					worstOfBoth.add(worstCasesForEggSurviving);
				}
				else if(comparison < 0) //if breaking is the worst case.
					worstOfBoth.add(worstCasesForEggBreaking);
				else
					worstOfBoth.add(worstCasesForEggSurviving);
				
				dropSolutionSets.put(guessedFloor, worstOfBoth);
			}
			final CompositeSolutionSet compositeSolutionSet = new CompositeSolutionSet(abstractProblemState, dropSolutionSets);
			solutionCache.put(abstractProblemState, compositeSolutionSet);
			return compositeSolutionSet;
		}
	}
}