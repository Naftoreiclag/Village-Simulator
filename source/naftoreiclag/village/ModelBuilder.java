/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class ModelBuilder
{
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private List<Triangle> triangles = new ArrayList<Triangle>();
	
	public void addTriangle(
			float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
			float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
			float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3)
	{
		Vertex a = new Vertex(x1, y1, z1, normal1, texX1, texY1);
		Vertex b = new Vertex(x2, y2, z2, normal2, texX2, texY2);
		Vertex c = new Vertex(x3, y3, z3, normal3, texX3, texY3);
		
		int ai;
		int bi;
		int ci;
		
		// TODO: replace these booleans with a short
		boolean ar = false;
		boolean br = false;
		boolean cr = false;
		
		for(int index = 0; index < vertices.size(); ++ index)
		{
			if(vertices.get(index).equals(a))
			{
				ai = index;
				ar = true;
			}
			if(vertices.get(index).equals(b))
			{
				bi = index;
				br = true;
			}
			if(vertices.get(index).equals(c))
			{
				ci = index;
				cr = true;
			}
			
			if(ar && br && cr)
			{
				break;
			}
		}
		
		if(!ar)
		{
			ai = vertices.size();
			vertices.add(a);
		}
		if(!br)
		{
			bi = vertices.size();
			vertices.add(b);
		}
		if(!cr)
		{
			ci = vertices.size();
			vertices.add(c);
		}
	}
	
	private class Vertex
	{
		private float x;
		private float y;
		private float z;
		private Vector3f normal;
		private float texX;
		private float texY;
		
		public Vertex(float x, float y, float z, Vector3f normal, float texX, float texY)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.normal = normal;
			this.texX = texX;
			this.texY = texY;
		}
		
		public boolean equals(Vertex other)
		{
			return other.x == this.x && other.y == this.y && other.z == this.z&& other.normal.equals(this.normal) && other.texX == this.texX && other.texY == this.texY;
		}
	}
	
	private class Triangle
	{
		private int a;
		private int b;
		private int c;
		
		public Triangle(int a, int b, int c)
		{
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}
}
