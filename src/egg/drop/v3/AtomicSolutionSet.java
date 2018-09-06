package egg.drop.v3;

//An atomic solution set represents any of the base cases of the egg drop problem that are not expressed in terms of other solutions.
public class AtomicSolutionSet extends SolutionSet
{
	Solution solution;
	public AtomicSolutionSet(ProblemState problemState, Solution solution) 
	{
		super(problemState);
		this.solution = solution;
	}
	
	@Override
	public int getBestCaseSolutionLength() 
	{
		return solution.size();
	}

	@Override
	public void printAllSolutionSets(String thisLinePrefix) 
	{
		System.out.println(thisLinePrefix + this.toString());
	}

	@Override
	public SolutionSet getOptimizedSolutionSetOnly() 
	{
		return this;
	}

	@Override
	public double getNumberOfUniqueSolutions() 
	{
		return 1;
	}
}
