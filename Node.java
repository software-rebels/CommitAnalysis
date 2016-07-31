import java.util.*;

public class Node implements Comparable
{
	// Node object fields
	private String nodeName;
	private double nodeRank;
	
	//Constructor
	public Node(String name, double rank)
	{
		nodeName = name;
		nodeRank = rank;
	}
	
	//Compare method used to sort nodes
	public int compare(Node n)
	{
		int c = 0;
		if(this.nodeRank > n.getRank())
		{
			c = 1;
		}
		
		return c;
	}
	
	//Getter Methods
	public String toString()
	{
		return nodeName + " : " + nodeRank;
	}
	
	public double getRank()
	{
		return this.nodeRank;
	}
	
	public String getName()
	{
		return this.nodeName;
	}

	public boolean equals(Object obj)
	{
		if(((Node)obj).getName().equals(this.nodeName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public int compareTo(Object o1)
	{
		if(this.nodeRank == ((Node)o1).nodeRank)
		{
			return 0;
		}
		else if((this.nodeRank)>((Node)o1).nodeRank)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
}