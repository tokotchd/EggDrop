package egg.drop.v3;

import java.io.PrintStream;

public abstract class SolutionSet implements Comparable<SolutionSet>
{
	protected final ProblemState problemState;
	public SolutionSet(ProblemState problemState)
	{
		this.problemState = problemState;
	}
	
	public abstract int getBestWorstCaseSolutionLength();
	
	public String toString() 
	{
		String stringToReturn = problemState.toString();
		return stringToReturn;
	}
	
	//compares the best case solution of this worst case solution set to the best case solution of the worst cases of the other given solution.
	public int compareTo(SolutionSet other) 
	{
		return Integer.compare(getBestWorstCaseSolutionLength(), other.getBestWorstCaseSolutionLength());
	}
	
	public abstract void printAllSolutionSets(PrintStream printStream, String thisLinePrefix);
	public abstract SolutionSet getOptimizedSolutionSetOnly();
	public abstract double getNumberOfUniqueSolutions();
}
