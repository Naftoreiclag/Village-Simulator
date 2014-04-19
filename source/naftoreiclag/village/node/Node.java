package naftoreiclag.village.node;

import java.util.LinkedList;
import java.util.List;

public class Node
{
	private String className;
	
	private Node parent;
	private List<Node> children;
	
	public Node(String className, Node parent)
	{
		this.className = className;
		this.parent = parent;
		
		children = new LinkedList<Node>();
	}
	
	public void addChild(Node node)
	{
		children.add(node);
		node.parent = this;
	}
	
	public void removeChild(Node node)
	{
		children.remove(node);
		node.parent = null;
	}
	
	public void setParent(Node node)
	{
		if(parent == node)
		{
			return;
		}
		
		if(parent != null)
		{
			parent.children.remove(this);
		}
		
		if(node != null)
		{
			node.children.add(this);
			parent = node;
		}
	}

	public String getClassName()
	{
		return className;
	}
}
