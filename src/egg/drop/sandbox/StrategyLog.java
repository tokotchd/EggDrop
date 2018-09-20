package egg.drop.sandbox;
import java.lang.Math;

public class StrategyLog extends Strategy 
{
	@Override
	public int getFloor(ProblemState problemState) 
	{
		int floors = problemState.currentFloors;
		int eggs = problemState.currentEggs;
		double partitions = Math.log(floors)/Math.log(eggs);
		int floorsToGoUp = (int) Math.ceil(floors / partitions);
		return floorsToGoUp;
	}
}
