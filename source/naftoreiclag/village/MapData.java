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
		// Size squared x floats per vertex
		FloatBuffer verts = BufferUtils.createFloatBuffer(size * size * 8);
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				/*
				 *     A                X ->
				 *                    
				 * C   M   D          Z
				 *       P            |
				 *     B   N          V
				 *
				 * M is the point defined by x, z
				 */
				
				// Calculate height of these points ===
				
				float m = map[x][z];
				float c = map[x > 0 ? x - 1 : x][z];
				float d = map[x < size - 1 ? x + 1 : x][z];
				float a = map[x][z > 0 ? z - 1 : z];
				float b = map[x][z < size - 1 ? z + 1 : z];
				
				// Calculate normals for M ===
				
				float cd_d = c - d;
				if(x == 0 || x == size - 1)
				{
					cd_d *= 2;
				}
				
				float ab_d = a - b;
				if(z == 0 || z == size - 1)
				{
					ab_d *= 2;
				}

				Vector3f m_n = new Vector3f(cd_d * vertu, 2 * horzu, ab_d * vertu);
				m_n.normalise();

				// Add M to data ===
				
				verts.put(x).put(m).put(z).put(m_n.x).put(m_n.y).put(m_n.z).put(x).put(z);
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
				//    
				// B   N
				
				int mi = posToLin(x    , z    );
				int di = posToLin(x + 1, z    );
				int bi = posToLin(x    , z + 1);
				int ni = posToLin(x + 1, z + 1);
				
				float m = map[x    ][z    ];
				float d = map[x + 1][z    ];
				float b = map[x    ][z + 1];
				float n = map[x + 1][z + 1];
				
				double mbn = flatness(m, b, n);
				double bnd = flatness(b, n, d);
				double ndm = flatness(n, d, m);
				double dmb = flatness(d, m, b);
				
				double flat = smallest(mbn, bnd, ndm, dmb);
				
				if(mbn == flat || ndm == flat)
				{
					ind.put(mi).put(bi).put(ni).put(ni).put(di).put(mi);
				}
				else
				{
					ind.put(bi).put(ni).put(di).put(di).put(mi).put(bi);
				}
				
				//ind.put(mi).put(bi).put(ni).put(bi).put(ni).put(di).put(ni).put(di).put(mi).put(di).put(mi).put(bi);
				//ind.put(mi).put(bi).put(ni).put(ni).put(di).put(mi);
			}
		}
		
		ind.flip();
		
		return ind;
	}
	
	private double smallest(double a, double b, double c, double d)
	{
		if(a < b)
		{
			// A < B
			
			if(a < c)
			{
				// A < B
				// A < C
				
				if(a < d)
				{
					// A < B
					// A < C
					// A < D
					
					return a;
				}
				else
				{
					// A < B
					// A < C
					// D < A
					
					return d;
				}
			}
			else
			{
				// A < B
				// C < A
				
				if(c < d)
				{
					// A < B
					// C < A
					// C < D
					
					return c;
				}
				else
				{
					// A < B
					// C < A
					// D < C
					
					return d;
				}
			}
		}
		else
		{
			// B < A
			
			if(b < c)
			{
				// B < A
				// B < C
				
				if(b < d)
				{
					// B < A
					// B < C
					// B < D
					
					return b;
				}
				else
				{
					// B < A
					// B < C
					// D < B
					
					return d;
				}
			}
			else
			{
				// B < A
				// C < B
				
				if(c < d)
				{
					// B < A
					// C < B
					// C < D
					
					return c;
				}
				else
				{
					// B < A
					// C < B
					// D < C
					
					return d;
				}
			}
		}
		
	}
	
	private double flatness(float a, float b, float c)
	{
		float mean = (a + b + c) / 3.0f;
		
		return Math.abs(mean - a) + Math.abs(mean - b) + Math.abs(mean - c);
	}
	
	private int posToLin(int x, int z)
	{
		return (x + (z * size)) * 1;
	}
}
