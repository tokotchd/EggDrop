package egg.drop.sandbox;

public class StrategyBinBinLin extends Strategy
{
	@Override
	public int getFloor(ProblemState problemState) 
	{
		int floorToReturn = (int)Math.ceil(problemState.currentFloors / 2.0);
		return floorToReturn;
	}

}
