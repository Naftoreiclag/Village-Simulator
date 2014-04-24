/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class MapData
{
	// Standard sizes for horizontal and vertical scale
	float horzu = 1.0f;
	float vertu = 1.0f;
	
	int size = 32;
	
	public float[][] map = new float[size][size];
	
	public void loadDataFromFile(String filename)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File("saves/heightmap.png"));
		} catch (IOException e)
		{
		}
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				map[x][z] = ((float) (img.getRGB(x, z) & 0x000000FF)) / 256f;
			}
		}
	}
	
	public FloatBuffer convertToGeometry()
	{
		// Size squared x floats per vertex x addition of middle vertex
		FloatBuffer verts = BufferUtils.createFloatBuffer(size * size * 8);
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				//    A 
				//
				// C  M  D
				//
				//    B  N
				
				
				float m = map[x][z];
				float c = map[x > 0 ? x - 1 : x][z];
				float d = map[x < size - 1 ? x + 1 : x][z];
				float a = map[x][z > 0 ? z - 1 : z];
				float b = map[x][z < size - 1 ? z + 1 : z];
				float n = map[x < size - 1 ? x + 1 : x][z < size - 1 ? z + 1 : z];
				
				// Actual point ===
				
				float xdiff = c - d;
				if(x == 0 || x == size - 1) // if we are on the edge, we only have one sample, so we double the data to imitate having two.
				{
					xdiff *= 2;
				}
				
				float zdiff = a - b;
				if(z == 0 || z == size - 1)
				{
					zdiff *= 2;
				}
				
				Vector3f normal = new Vector3f(xdiff * vertu, 2 * horzu, zdiff * vertu);
				
				normal.normalise();
				
				verts.put(x).put(map[x][z] - 2.0f).put(z).put(normal.x).put(normal.y).put(normal.z).put(x).put(z);
				
				// Middley point ===
				
				//verts.put(((float) x) + 0.5f).put(map[x][z] - 2.0f).put(((float) z) + 0.5f).put(normal.x).put(normal.y).put(normal.z).put(x).put(z);
			}
		}
		
		verts.flip();
		
		return verts;
	}
	
	public IntBuffer convertToIndices()
	{
		IntBuffer ind = BufferUtils.createIntBuffer(((size - 1) * (size - 1)) * 6);

		for(int x = 0; x < size - 1; ++ x)
		{
			for(int z = 0; z < size - 1; ++ z)
			{
				int ulx = x;
				int ulz = z;
				int urx = x + 1;
				int urz = z;
				int dlx = x;
				int dlz = z + 1;
				int drx = x + 1;
				int drz = z + 1;
				
				int a = posToLin(ulx, ulz);
				int c = posToLin(urx, urz);
				int d = posToLin(dlx, dlz);
				int b = posToLin(drx, drz);
				
				float af = map[ulx][ulz];
				float bf = map[urx][urz];
				float cf = map[dlx][dlz];
				float df = map[drx][drz];
				
				if(magicCompare(af, bf, cf, df))
				{
					ind.put(a).put(d).put(c).put(b).put(c).put(d);
				}
				else
				{
					ind.put(a).put(d).put(b).put(a).put(b).put(c);
				}
			}
		}
		
		ind.flip();
		
		return ind;
	}
	
	private boolean magicCompare(float a, float b, float c, float d)
	{
		return Math.abs(a - b) < Math.abs(c - d);
		/*
		
		if(a > c)
		{
		    if(a > d)
		    {
		        return true;
		    }
		    else
		    {
		        if(d > b)
		        {
		            return false;
		        }
		        else
		        {
		            return true;
		        }
		    }
		}
		else
		{
		    if(c > b)
		    {
		        return false;
		    }
		    else
		    {
		        if(b > d)
		        {
		            return true;
		        }
		        else
		        {
		            return false;
		        }
		    }
		}*/
		
	}
	
	private int posToLin(int x, int z)
	{
		return x + (z * size);
	}
}
