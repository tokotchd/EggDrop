package egg.drop.sandbox;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SolutionGeneratorWorstCase 
{
	private Hashtable<ProblemState, Solution> solutionCache;
	private Strategy strategyToUse;
	
	public SolutionGeneratorWorstCase(Strategy strategyToUse) 
	{
		solutionCache = new Hashtable<ProblemState, Solution>();
		this.strategyToUse = strategyToUse;
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
	
	public Solution getWorstSolution(int minFloor, int maxFloor, int eggsRemaining)
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
		//Case 6, Apply Strategy and return worst case scenario
		else 
		{
			int abstractMinFloor = 1;
			int abstractMaxFloor = floorsRemaining;
			
			int guessedFloor = strategyToUse.getFloor(abstractProblemState);
			if(guessedFloor < 1 || guessedFloor >= floorsRemaining)
			{
				System.out.println("Error: Strategy attempted to guess floor " + guessedFloor + " with " + floorsRemaining + " floors remaining and " + eggsRemaining + " eggs remaining.");
				return null;
			}
			
			//Egg Survives
			int notBreakMinFloor = guessedFloor + 1;
			int notBreakMaxFloor = abstractMaxFloor;
			Solution worstCaseNotBreak = getWorstSolution(notBreakMinFloor, notBreakMaxFloor, eggsRemaining);
			
			//Egg breaks
			int breakMinFloor = abstractMinFloor;
			int breakMaxFloor = guessedFloor;
			Solution worstCaseBreak = getWorstSolution(breakMinFloor, breakMaxFloor, eggsRemaining - 1);
			
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
			solutionCache.put(abstractProblemState, worstOfBoth);
			Solution contextualizedSolution = contextualizeSolution(worstOfBoth.listOfFloorsDropped, worstOfBoth.didBreakAtEachDrop, minFloor);
			return contextualizedSolution;
		}
	}
}