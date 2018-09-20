package egg.drop.sandbox;

public class StrategyRunner 
{
	public static void main(String[] args)
	{
		int minFloor = 1;
		int maxFloor = 100;
		int numEggs = 3;
		//********************************************Input Goes Here*****************************************************************
		Strategy strategyToRun = new StrategyLog();
		//********************************************Input Goes Here*****************************************************************
		SolutionGeneratorWorstCase sg = new SolutionGeneratorWorstCase(strategyToRun);
		Solution worstCaseSolutionForThisStrategy = sg.getWorstSolution(minFloor, maxFloor, numEggs);
		System.out.println("Contextualized Worst Case Scenario using this strategy:");
		System.out.println(worstCaseSolutionForThisStrategy);
		System.out.println("or " + worstCaseSolutionForThisStrategy.listOfFloorsDropped.size() + " drops.");
	}
	
}
