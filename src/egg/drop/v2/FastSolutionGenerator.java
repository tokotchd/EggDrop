package egg.drop.v2;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FastSolutionGenerator 
{
	//class to quickly construct and compare the generalized N-drop problem so that we can build a cache for performance.
	//hash NDropStates by eggs and floors so that they can be used in a HashTable for a cache lookup.
	private Hashtable<ProblemState, Solution> solutionCache;
	
	public FastSolutionGenerator() 
	{
		solutionCache = new Hashtable<ProblemState, Solution>();
	}
	
	private Solution contextualizeSolution(List<Integer> floorsDropped, List<Boolean> didBreak, int minFloor) 
	{
		ArrayList<Integer> contextualizedDrops = new ArrayList<Integer>(floorsDropped);
		for(int i = 0; i < contextualizedDrops.size(); i ++)
		{
			contextualizedDrops.set(i,contextualizedDrops.get(i) + (minFloor - 1));
		}
		Solution contextualizedSolution = new Solution(contextualizedDrops, didBreak);
		return contextualizedSolution;
	}
	
	public Solution getBestWorstSolution(int minFloor, int maxFloor, int eggsRemaining)
	{
		int floorsRemaining = (maxFloor - minFloor) + 1;
		ProblemState abstractProblemState = new ProblemState(floorsRemaining, eggsRemaining);
		
		//Case 1, cache contains abstract solution, we have to contextualize that solution.
		Solution cachedSolution = solutionCache.get(abstractProblemState);
		if(cachedSolution != null)
		{
			//fetch
			List<Integer> cachedSolutionDropped = cachedSolution.listOfFloorsDropped;
			List<Boolean> cachedSolutionBreaks = cachedSolution.didBreakAtEachDrop;
			//contextualize
			return contextualizeSolution(cachedSolutionDropped, cachedSolutionBreaks, minFloor);
		}
		//Case 2, one floor left. No additional drops required.
		else if(floorsRemaining == 1) 
		{
			ArrayList<Integer> floorDrops = new ArrayList<Integer>(0);
			ArrayList<Boolean> didBreak = new ArrayList<Boolean>(0);
			Solution thisSolution = new Solution(floorDrops, didBreak);
			solutionCache.put(abstractProblemState, thisSolution);
			return contextualizeSolution(floorDrops, didBreak, minFloor);
		}
		//Case 3, there's only 2 floors left, Then we need to drop on the lower floor.  We know conclusively from that drop that it must be that floor.
		else if(floorsRemaining == 2)
		{
			ArrayList<Integer> floorDrops = new ArrayList<Integer>(1);
			ArrayList<Boolean> didBreak = new ArrayList<Boolean>(1);
			floorDrops.add(1);
			didBreak.add(false);
			Solution thisSolution = new Solution(floorDrops, didBreak);
			solutionCache.put(abstractProblemState, thisSolution);
			return contextualizeSolution(floorDrops, didBreak, minFloor);
		}
		//Case 4, build and store the linear search.
		else if(eggsRemaining == 1)
		{
			//build
			ArrayList<Integer> linearFloorsDropped = new ArrayList<Integer>();
			ArrayList<Boolean> linearDidBreak = new ArrayList<Boolean>();
			for(int counter = 1; counter < floorsRemaining; counter++)
			{
				linearFloorsDropped.add(counter);
				linearDidBreak.add(false);
			}
			Solution linearSolution = new Solution(linearFloorsDropped, linearDidBreak);
			solutionCache.put(abstractProblemState, linearSolution);
			//contextualize
			return contextualizeSolution(linearFloorsDropped, linearDidBreak, minFloor);
		}
		//Case 5, safety error case.
		else if(eggsRemaining < 1 || floorsRemaining < 1)
		{
			System.out.println("ERROR: Attempted to get worst case with <1 eggs or floors remaining");
			return null;
		}
		else
		{
			Solution bestAbstractSolution = null;
			//Case 6, recursive check for solution Monte Carlo style.
			for(int guessedFloor = 1; guessedFloor < floorsRemaining; guessedFloor++)
			{
				//Egg Survives on Floor 3 of 10 - new range for minimum breaking floor is [4,10]
				//Egg Breaks on Floor 3 of 10 - new range for minimum breaking floor is [1,3]
				
				int abstractMinFloor = 1;
				int abstractMaxFloor = floorsRemaining;
				
				//Egg Survives
				int notBreakMinFloor = guessedFloor + 1;
				int notBreakMaxFloor = abstractMaxFloor;
				Solution worstCaseNotBreak = getBestWorstSolution(notBreakMinFloor, notBreakMaxFloor, eggsRemaining);
				
				//Egg breaks
				int breakMinFloor = abstractMinFloor;
				int breakMaxFloor = guessedFloor;
				Solution worstCaseBreak = getBestWorstSolution(breakMinFloor, breakMaxFloor, eggsRemaining - 1);
				
				//Determining worst case of egg breaking now and egg not breaking now.
				Solution worstOfBoth;
				if(worstCaseNotBreak.compareTo(worstCaseBreak) < 0) //if breaking is the worst case.
				{
					ArrayList<Integer> worstCaseFloorDrops = new ArrayList<Integer>(worstCaseBreak.listOfFloorsDropped);
					worstCaseFloorDrops.add(0,guessedFloor);
					ArrayList<Boolean> worstCaseDidBreak = new ArrayList<Boolean>(worstCaseBreak.didBreakAtEachDrop);
					worstCaseDidBreak.add(0,true);
					worstOfBoth = new Solution(worstCaseFloorDrops, worstCaseDidBreak);
				}
				else
				{
					ArrayList<Integer> worstCaseFloorDrops = new ArrayList<Integer>(worstCaseNotBreak.listOfFloorsDropped);
					worstCaseFloorDrops.add(0,guessedFloor);
					ArrayList<Boolean> worstCaseDidBreak = new ArrayList<Boolean>(worstCaseNotBreak.didBreakAtEachDrop);
					worstCaseDidBreak.add(0,false);
					worstOfBoth = new Solution(worstCaseFloorDrops, worstCaseDidBreak);
				}
				//and then if our worst solution is better than the current best solution, we use this instead.
				if(bestAbstractSolution == null || worstOfBoth.compareTo(bestAbstractSolution) <= 0)
				{
					bestAbstractSolution = worstOfBoth;
				}
			}
			solutionCache.put(abstractProblemState, bestAbstractSolution);
			Solution contextualizedSolution = contextualizeSolution(bestAbstractSolution.listOfFloorsDropped, bestAbstractSolution.didBreakAtEachDrop, minFloor);
			return contextualizedSolution;
		}
	}
}