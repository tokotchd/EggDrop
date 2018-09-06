package egg.drop.v3;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

//A composite solution set is a solution set that contains both drops and relations to other (composite and atomic) solutions.
public class CompositeSolutionSet extends SolutionSet 
{
	public final Hashtable<Integer,List<SolutionSet>> worstCaseDropSolutionSets; //Every possible floor drop can have exactly 1 or 2 "worstCases" 
	//all local variables are created on construction and then cached for speed
	private final List<SolutionSet> bestSolutionSet; 
	private final int bestSolutionDrop;
	private final int bestSolutionSize;
	private final double numberOfUniqueSolutions;
	
	public CompositeSolutionSet(ProblemState problemState, Hashtable<Integer, List<SolutionSet>> dropSolutionSets) 
	{
		super(problemState);
		this.worstCaseDropSolutionSets = dropSolutionSets;
		int tempBestSolutionDrop = dropSolutionSets.keys().nextElement();
		List<SolutionSet> tempBestSolutionSets = dropSolutionSets.get(tempBestSolutionDrop); //every composite solution has at least the first solution from the first element.
		for(Map.Entry<Integer, List<SolutionSet>> dropAndworstCaseSolutionSets: dropSolutionSets.entrySet())
		{
			int thisSolutionDrop = dropAndworstCaseSolutionSets.getKey();
			List<SolutionSet> thisSolutionSets = dropAndworstCaseSolutionSets.getValue(); //because if list of SolutionSets has more than one element, they will all have the same length.
			if(thisSolutionSets.get(0).getBestWorstCaseSolutionLength() < tempBestSolutionSets.get(0).getBestWorstCaseSolutionLength())
			{
				tempBestSolutionSets = thisSolutionSets;
				tempBestSolutionDrop = thisSolutionDrop;
			}
		}
		this.bestSolutionDrop = tempBestSolutionDrop;
		this.bestSolutionSet = tempBestSolutionSets;
		this.bestSolutionSize = tempBestSolutionSets.get(0).getBestWorstCaseSolutionLength() + 1;
		this.numberOfUniqueSolutions = calculateNumberOfUniqueSolutions();
	}
	
	private int calculateNumberOfUniqueSolutions() 
	{
		//recursively drill down to collect size of each solutionSet and add them together.
		//TODO - come back here and sanity check that multiplying the number of unique worst case solutions for this drop times 2
		//ends up being the same amount as adding the two together with proper traversal.
		int totalSolutions = 0;
		for(List<SolutionSet> worstCaseSolutionSets: worstCaseDropSolutionSets.values())
		{
			for(SolutionSet worstCaseSolutionSet: worstCaseSolutionSets)
			{
				totalSolutions += worstCaseSolutionSet.getNumberOfUniqueSolutions();
			}
		}
		return totalSolutions;
	}

	@Override
	public SolutionSet getOptimizedSolutionSetOnly()
	{
		Hashtable<Integer,List<SolutionSet>> optimizedWorstCaseSolutionSets = new Hashtable<Integer,List<SolutionSet>>();
		int bestSubSolutionSize = bestSolutionSize - 1; //we have to knock off one for the size of "this solution" - it doesn't apply to the contained sub solutions.
		for(Map.Entry<Integer, List<SolutionSet>> solutionSets: worstCaseDropSolutionSets.entrySet())
			if(solutionSets.getValue().get(0).getBestWorstCaseSolutionLength() == bestSubSolutionSize)
			{
				List<SolutionSet> newOptimizedSolutionSet = new ArrayList<SolutionSet>(2);
				for(SolutionSet solutionSet: solutionSets.getValue())
				{
					newOptimizedSolutionSet.add(solutionSet.getOptimizedSolutionSetOnly());
				}
				optimizedWorstCaseSolutionSets.put(solutionSets.getKey(),newOptimizedSolutionSet);
			}
		return new CompositeSolutionSet(problemState, optimizedWorstCaseSolutionSets);
	}
	
	@Override
	public int getBestWorstCaseSolutionLength() 
	{
		return bestSolutionSize;
	}
	
	@Override
	public String toString()
	{
		return problemState.toString() + " " + bestSolutionSet.get(0).toString();
	}
	
	public void printAllSolutionSets(PrintStream printStream, String thisLinePrefix)
	{
		for(List<SolutionSet> dropSolutionSets: worstCaseDropSolutionSets.values())
			for(SolutionSet dropSolutionSet: dropSolutionSets)
				dropSolutionSet.printAllSolutionSets(printStream, thisLinePrefix + super.toString() + " ");
	}

	@Override
	public double getNumberOfUniqueSolutions() 
	{
		return this.numberOfUniqueSolutions;
	}
}
