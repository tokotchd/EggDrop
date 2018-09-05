package egg.drop.v3;

public abstract class SolutionSet implements Comparable<SolutionSet>
{
	protected final ProblemState problemState;
	public SolutionSet(ProblemState problemState)
	{
		this.problemState = problemState;
	}
	
	public abstract int getBestCaseSolutionLength();
	
	public String toString() 
	{
		String stringToReturn = problemState.toString();
		return stringToReturn;
	}
	
	public int compareTo(SolutionSet other) 
	{
		return Integer.compare(getBestCaseSolutionLength(), other.getBestCaseSolutionLength());
	}
	
	public abstract void printAllSolutionSets(String thisLinePrefix);
	public abstract SolutionSet getOptimizedSolutionSetOnly();
}
