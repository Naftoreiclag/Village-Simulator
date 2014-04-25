/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class ModelBuilder
{
	// Where the data is stored to be baked
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private List<Triangle> triangles = new ArrayList<Triangle>();
	
	// Add a triangle
	public void addTriangle(Vertex a, Vertex b, Vertex c)
	{
		// Find appropriate indices for these, re-using old ones if possible
		int ai = -1;
		int bi = -1;
		int ci = -1;
		for(int index = 0; index < vertices.size(); ++ index)
		{
			if(vertices.get(index).same(a))
			{
				ai = index;
			}
			if(vertices.get(index).same(b))
			{
				bi = index;
			}
			if(vertices.get(index).same(c))
			{
				ci = index;
			}
			
			if(ai != -1 && bi != -1 && ci != -1)
			{
				break;
			}
		}
		if(ai == -1)
		{
			ai = vertices.size();
			vertices.add(a);
		}
		if(bi == -1)
		{
			bi = vertices.size();
			vertices.add(b);
		}
		if(ci == -1)
		{
			ci = vertices.size();
			vertices.add(c);
		}
		
		// Add this to our triangles
		triangles.add(new Triangle(ai, bi, ci));
	}
	
	// Add a triangle
	public void addTriangle(
			float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
			float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
			float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3)
	{
		// Make vertexes from this
		Vertex a = new Vertex(x1, y1, z1, normal1, texX1, texY1);
		Vertex b = new Vertex(x2, y2, z2, normal2, texX2, texY2);
		Vertex c = new Vertex(x3, y3, z3, normal3, texX3, texY3);
		
		this.addTriangle(a, b, c);
	}
	
	// Add a quad. Note: this just adds two triangles at once
	public void addQuad(Vertex a, Vertex b, Vertex c, Vertex d)
	{
		// 1  2
		//    
		// 4  3
		
		this.addTriangle(a, b, c);
		this.addTriangle(a, c, d);
	}
	
	// Add a quad. Note: this just adds two triangles at once
	public void addQuad(
			float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
			float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
			float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3,
			float x4, float y4, float z4, Vector3f normal4, float texX4, float texY4)
	{
		// 1  2
		//    
		// 4  3
		
		this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x2, y2, z2, normal2, texX2, texY2, x3, y3, z3, normal3, texX3, texY3);
		this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x3, y3, z3, normal3, texX3, texY3, x4, y4, z4, normal4, texX4, texY4);
	}
	
	// Bakes the data into a usable model. Note: You can bake this more than once if you really want to.
	public Model bake()
	{
		Model m = new Model();
		
		FloatBuffer v = BufferUtils.createFloatBuffer(vertices.size() * 8);
		for(Vertex f : vertices)
		{
			v.put(f.x).put(f.y).put(f.z).put(f.normal.x).put(f.normal.y).put(f.normal.z).put(f.texX).put(f.texY);
		}
		m.putVerts(v);
		
		IntBuffer i = BufferUtils.createIntBuffer(triangles.size() * 3);
		for(Triangle t : triangles)
		{
			i.put(t.a).put(t.b).put(t.c);
		}
		m.putIndices(i, triangles.size() * 3);
		
		System.out.println(vertices.size());
		
		return m;
	}
	
	// Class for storing a single vertex's data
	public static class Vertex
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
		
		private boolean vComp(Vector3f a, Vector3f b)
		{
			//return true;
			return a.x == b.x && a.y == b.y && a.z == b.z;
		}
		
		public boolean same(Vertex other)
		{
			return other.x == this.x && other.y == this.y && other.z == this.z && vComp(other.normal, this.normal) && other.texX == this.texX && other.texY == this.texY;
		}
	}
	
	// Private class for storing a tuple of integers
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
