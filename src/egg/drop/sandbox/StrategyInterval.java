package egg.drop.sandbox;

public class StrategyInterval extends Strategy
{

	@Override
	public int getFloor(ProblemState problemState)
	{
		int firstInterval = 20;
		int secondInterval = 10;
		if(problemState.currentFloors > firstInterval)
		{
			return firstInterval;
		} else if(problemState.currentFloors > secondInterval)
		{
			return secondInterval;
		} else {
			return 1;
		}
	}

}
