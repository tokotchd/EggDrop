package egg.drop.sandbox;

import java.util.Objects;

public class ProblemState 
{
	protected final int currentFloors;
	protected final int currentEggs;
	
	public ProblemState(int currentFloors, int currentEggs)//first constructor for the very first problem state.
	{
		this.currentFloors = currentFloors;
		this.currentEggs = currentEggs;
	}
	
	public String toString() 
	{
		String stringToReturn = "";
		stringToReturn += "(" + currentFloors + "," + currentEggs + ")";
		return stringToReturn;
	}
	
	public boolean equals(Object obj)
	{
		if (obj instanceof ProblemState)
		{
			ProblemState other = (ProblemState)obj;
			return other.currentFloors == this.currentFloors && other.currentEggs == this.currentEggs;
		}
		return false;
	}
	
	public int hashCode() 
	{
		return Objects.hash(currentFloors, currentEggs);
	}
}