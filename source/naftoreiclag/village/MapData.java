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
		FloatBuffer verts = BufferUtils.createFloatBuffer(size * size * 8 * 2);
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				//     A 
				//
				// C   M   D
				//       P
				//     B   N
				
				
				float m = map[x][z];
				float c = map[x > 0 ? x - 1 : x][z];
				float d = map[x < size - 1 ? x + 1 : x][z];
				float a = map[x][z > 0 ? z - 1 : z];
				float b = map[x][z < size - 1 ? z + 1 : z];
				float n = map[x < size - 1 ? x + 1 : x][z < size - 1 ? z + 1 : z];
				
				// Calculate differences in height
				
				float cd_d = c - d;
				if(x == 0 || x == size - 1) // if we are on the edge, we only have one sample, so we double the data to imitate having two.
				{
					cd_d *= 2;
				}
				
				float ab_d = a - b;
				if(z == 0 || z == size - 1)
				{
					ab_d *= 2;
				}
				
				float mn_d = m - n;
				float bd_d = b - d;
				if(x == size - 1 || z == size - 1)
				{
					mn_d *= 2;
					bd_d *= 2;
				}
				
				Vector3f m2d = new Vector3f(0, d - m, vertu);
				Vector3f m2b = new Vector3f(vertu, b - m, 0);
				Vector3f b2n = new Vector3f(0, n - b, vertu);
				Vector3f d2n = new Vector3f(vertu, n - d, 0);
				
				Vector3f nn_1 = new Vector3f();
				Vector3f nn_2 = new Vector3f();
				
				Vector3f.cross(m2d, m2b, nn_1);
				Vector3f.cross(b2n, d2n, nn_2);
				
				Vector3f n_n = new Vector3f();
				Vector3f.add(nn_1, nn_2, n_n);
				n_n.normalise();
				
				Vector3f m_n = new Vector3f(cd_d * vertu, 2 * horzu, ab_d * vertu);
				m_n.normalise();
				
				verts.put(x).put(m).put(z).put(m_n.x).put(m_n.y).put(m_n.z).put(x).put(z);
				
				float p = (m + n + d + b) / 4.0f;

				verts.put(x + 0.5f).put(p).put(z + 0.5f).put(n_n.x).put(n_n.y).put(n_n.z).put(x + 0.5f).put(z + 0.5f);
			}
		}
		
		verts.flip();
		
		return verts;
	}
	
	public IntBuffer convertToIndices()
	{
		IntBuffer ind = BufferUtils.createIntBuffer(((size - 1) * (size - 1)) * 12);

		for(int x = 0; x < size - 1; ++ x)
		{
			for(int z = 0; z < size - 1; ++ z)
			{
				// M   D
				//   P
				// B   N
				
				int ulx = x;
				int ulz = z;
				int urx = x + 1;
				int urz = z;
				int dlx = x;
				int dlz = z + 1;
				int drx = x + 1;
				int drz = z + 1;
				
				int mi = posToLin(ulx, ulz);
				int di = posToLin(urx, urz);
				int bi = posToLin(dlx, dlz);
				int ni = posToLin(drx, drz);
				int pi = mi + 1;
				
				/*
				float m = map[ulx][ulz];
				float d = map[urx][urz];
				float b = map[dlx][dlz];
				float n = map[drx][drz];
				*/
				
				ind.put(pi).put(di).put(mi).put(pi).put(ni).put(di).put(pi).put(bi).put(ni).put(pi).put(mi).put(bi);
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
		return (x + (z * size)) * 2;
	}
}
