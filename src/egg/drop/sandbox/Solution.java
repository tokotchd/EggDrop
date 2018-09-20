package egg.drop.sandbox;
import java.util.Collections;
import java.util.List;

public class Solution implements Comparable<Solution>
{
	protected final List<Integer> listOfFloorsDropped; //this should be a non-modifiable list to ensure that we're doing deep copies of arrays properly.
	protected final List<Boolean> didBreakAtEachDrop; //this does not affect the behavior of the program, is only used for pretty print statements.
	
	public Solution(List<Integer> listOfFloorsDropped, List<Boolean> didBreakAtEachDrop) {
		this.listOfFloorsDropped = Collections.unmodifiableList(listOfFloorsDropped);
		this.didBreakAtEachDrop = Collections.unmodifiableList(didBreakAtEachDrop);
	}
	
	@Override
	public int compareTo(Solution other) 
	{
		int comparedFloors = Integer.compare(this.listOfFloorsDropped.size(), other.listOfFloorsDropped.size());
		if(comparedFloors != 0)
			return comparedFloors;
		else //if the number of drops is the same, we want to favor the more conservative approach to save eggs.
		{
			int thisNumBreaks = 0;
			int otherNumBreaks = 0;
			for(int i = 0; i < didBreakAtEachDrop.size(); i++)
			{
				if(didBreakAtEachDrop.get(i))
				{
					thisNumBreaks++;
				}
				if(other.didBreakAtEachDrop.get(i))
				{
					otherNumBreaks++;
				}
			}
			return Integer.compare(thisNumBreaks, otherNumBreaks);
		}
	}
	
	public String toString() 
	{
		String stringToReturn = "";
		stringToReturn += "[";
		for(int i = 0; i < listOfFloorsDropped.size();i++)
		{
			stringToReturn += "" + listOfFloorsDropped.get(i) + (didBreakAtEachDrop.get(i)?"B":"S") + ",";
		}
		stringToReturn += "]";
		return stringToReturn;
	}
}