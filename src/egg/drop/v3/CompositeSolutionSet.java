package egg.drop.v3;

import java.util.Hashtable;
import java.util.Map;

//A composite solution set is a solution set that contains both drops and relations to other (composite and atomic) solutions.
public class CompositeSolutionSet extends SolutionSet 
{
	public final Hashtable<Integer,SolutionSet> dropSolutionSets;
	private final SolutionSet bestSolutionSet; //all variables are cached for speed
	private final int bestSolutionDrop;
	private final int bestSolutionSize;
	private final double numberOfUniqueSolutions;
	
	public CompositeSolutionSet(ProblemState problemState, Hashtable<Integer,SolutionSet> dropSolutionSets) 
	{
		super(problemState);
		this.dropSolutionSets = dropSolutionSets;
		int tempBestSolutionDrop = dropSolutionSets.keys().nextElement();
		SolutionSet tempBestSolutionSet = dropSolutionSets.get(tempBestSolutionDrop); //every composite solution has at least the solution from dropping on the lowest floor.
		for(Map.Entry<Integer, SolutionSet> dropAndSolutionSet: dropSolutionSets.entrySet())
		{
			int thisSolutionDrop = dropAndSolutionSet.getKey();
			SolutionSet thisSolutionSet = dropAndSolutionSet.getValue();
			if(thisSolutionSet.getBestCaseSolutionLength() < tempBestSolutionSet.getBestCaseSolutionLength())
			{
				tempBestSolutionSet = thisSolutionSet;
				tempBestSolutionDrop = thisSolutionDrop;
			}
		}
		this.bestSolutionDrop = tempBestSolutionDrop;
		this.bestSolutionSet = tempBestSolutionSet;
		this.bestSolutionSize = tempBestSolutionSet.getBestCaseSolutionLength() + 1;
		this.numberOfUniqueSolutions = calculateNumberOfUniqueSolutions();
	}
	
	private int calculateNumberOfUniqueSolutions() 
	{
		
		//recursively drill down to collect size of each solutionSet and add them together?
		int totalSolutions = 0;
		for(SolutionSet dropSolutionSet: dropSolutionSets.values())
		{
			totalSolutions += dropSolutionSet.getNumberOfUniqueSolutions();
		}
		return totalSolutions;
	}

	@Override
	public SolutionSet getOptimizedSolutionSetOnly()
	{
		Hashtable<Integer,SolutionSet> optimizedSolutionSet = new Hashtable<Integer,SolutionSet>();
		int bestSolutionSize = bestSolutionSet.getBestCaseSolutionLength();
		for(Map.Entry<Integer, SolutionSet> solutionSet: dropSolutionSets.entrySet())
			if(solutionSet.getValue().getBestCaseSolutionLength() == bestSolutionSize)
				optimizedSolutionSet.put(solutionSet.getKey(), solutionSet.getValue().getOptimizedSolutionSetOnly());
		return new CompositeSolutionSet(problemState, optimizedSolutionSet);
	}
	
	@Override
	public int getBestCaseSolutionLength() 
	{
		return bestSolutionSize;
	}
	
	@Override
	public String toString()
	{
		if(EggDropv3.debug)
		{
			String stringToReturn = problemState.toString() + " " + bestSolutionSet.toString();
			return stringToReturn;
		}
		else
			return super.toString();
	}
	
	public void printAllSolutionSets(String thisLinePrefix)
	{
		for(SolutionSet dropSolutionSet: dropSolutionSets.values())
		{
			dropSolutionSet.printAllSolutionSets(thisLinePrefix + super.toString() + " ");
		}
	}

	@Override
	public double getNumberOfUniqueSolutions() 
	{
		return this.numberOfUniqueSolutions;
	}
}
