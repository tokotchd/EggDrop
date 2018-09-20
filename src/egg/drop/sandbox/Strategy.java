package egg.drop.sandbox;

public abstract class Strategy 
{
	//returns the floor that this strategy drops at based on problem state.
	public abstract int getFloor(ProblemState problemState);
}
