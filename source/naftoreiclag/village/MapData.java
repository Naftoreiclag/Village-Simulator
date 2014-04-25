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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class MapData
{
	Model rock;
	Model grass;
	
	// Standard sizes for horizontal and vertical scale
	float horzu = 1.0f;
	float vertu = 5.0f;
	
	int size = 32;
	
	public float[][] map = new float[size][size];
	
	public MapData()
	{
		rock = new Model();
		grass = new Model();
	}
	
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
	
	public void makeGrassModel()
	{
		List<Float> vert = new ArrayList<Float>();
		List<Integer> inde = new ArrayList<Integer>();
		
		List<Triangle> trias = new ArrayList<Triangle>();
		
		boolean[][][] included = new boolean[size - 1][size - 1][4];

		for(int x = 0; x < size - 1; ++ x)
		{
			for(int z = 0; z < size - 1; ++ z)
			{
				// M  0  D
				//
				// 2  P  3
				//
				// B  1  N
				
				float m = map[x    ][z    ];
				float d = map[x + 1][z    ];
				float b = map[x    ][z + 1];
				float n = map[x + 1][z + 1];
				
				double mbn = flatness(m, b, n);
				double bnd = flatness(b, n, d);
				double ndm = flatness(n, d, m);
				double dmb = flatness(d, m, b);
				/*
				double flat = smallest(mbn, bnd, ndm, dmb);
				
				if(mbn == flat)
				{
					p = (m + n) / 2.0f;
				}
				else if(bnd == flat)
				{
					p = (b + d) / 2.0f;
				}
				else if(ndm == flat)
				{
					p = (n + m) / 2.0f;
				}
				else if(dmb == flat)
				{
					p = (d + b) / 2.0f;
				}
				*/
				
				double flatThresh = 0.5;
				
				included[x][z][0] = mbn < flatThresh;
				included[x][z][1] = mbn < flatThresh;
				included[x][z][2] = mbn < flatThresh;
				included[x][z][3] = mbn < flatThresh;
			}
		}
		
		IntBuffer topIntBuff = BufferUtils.createIntBuffer(inde.size());
		for(int f : inde)
		{
			topIntBuff.put(f);
		}
		
		FloatBuffer topVertsBuff = BufferUtils.createFloatBuffer(vert.size());
		for(float f : vert)
		{
			topVertsBuff.put(f);
		}
		
		grass.putVerts(topVertsBuff);
		grass.putIndices(topIntBuff, inde.size());
	}
	
	public void makeModelAlt()
	{
		ModelBuilder mb = new ModelBuilder();

		for(int x = 0; x < size - 1; ++ x)
		{
			for(int z = 0; z < size - 1; ++ z)
			{
				// M  0  D
				//
				// 2  P  3
				//
				// B  1  N
				
				float m = map[x    ][z    ];
				float d = map[x + 1][z    ];
				float b = map[x    ][z + 1];
				float n = map[x + 1][z + 1];
				
				mb.addQuad(
						/* M */ (x    ) * horzu, m * vertu, (z    ) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f,
						/* B */ (x    ) * horzu, b * vertu, (z + 1) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 1.0f,
						/* N */ (x + 1) * horzu, n * vertu, (z + 1) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), 1.0f, 1.0f,
						/* D */ (x + 1) * horzu, d * vertu, (z    ) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), 1.0f, 0.0f);
			}
		}
		
		rock = mb.bake();
	}
	
	public void makeModel()
	{
		// Size squared x floats per vertex x addition of middle vertex
		FloatBuffer verts = BufferUtils.createFloatBuffer(size * size * 8 * 2);
		
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
				float n = map[x < size - 1 ? x + 1 : x][z < size - 1 ? z + 1 : z];
				float p = (m + n + d + b) / 4.0f;
				
				double mbn = flatness(m, b, n);
				double bnd = flatness(b, n, d);
				double ndm = flatness(n, d, m);
				double dmb = flatness(d, m, b);
				
				double flat = smallest(mbn, bnd, ndm, dmb);
				
				if(mbn == flat)
				{
					p = (m + n) / 2.0f;
				}
				else if(bnd == flat)
				{
					p = (b + d) / 2.0f;
				}
				else if(ndm == flat)
				{
					p = (n + m) / 2.0f;
				}
				else if(dmb == flat)
				{
					p = (d + b) / 2.0f;
				}
				
				
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
				
				verts.put(x * horzu).put(m * vertu).put(z * horzu).put(m_n.x).put(m_n.y).put(m_n.z).put(x).put(z);
				
				// Calculate normals for P ===
				
				Vector3f m2d = new Vector3f(vertu, d - m, 0);
				Vector3f m2b = new Vector3f(0, b - m, vertu);
				Vector3f b2n = new Vector3f(vertu, n - b, 0);
				Vector3f d2n = new Vector3f(0, n - d, vertu);

				Vector3f p_n_1 = new Vector3f();
				Vector3f p_n_2 = new Vector3f();
				
				Vector3f.cross(m2b, m2d, p_n_1);
				Vector3f.cross(d2n, b2n, p_n_2);
				
				Vector3f p_n = new Vector3f();
				Vector3f.add(p_n_1, p_n_2, p_n);
				p_n.normalise();
				

				// Add P to data ===
				
				verts.put((x + 0.5f) * horzu).put(p * vertu).put((z + 0.5f) * horzu).put(p_n.x).put(p_n.y).put(p_n.z).put(x + 0.5f).put(z + 0.5f);
			}
		}
		
		rock.putVerts(verts);
		
		IntBuffer indices = BufferUtils.createIntBuffer(((size - 1) * (size - 1)) * 12);

		for(int x = 0; x < size - 1; ++ x)
		{
			for(int z = 0; z < size - 1; ++ z)
			{
				// M   D
				//   P
				// B   N
				
				int mi = posToLin(x    , z    );
				int di = posToLin(x + 1, z    );
				int bi = posToLin(x    , z + 1);
				int ni = posToLin(x + 1, z + 1);
				int pi = mi + 1;
				
				indices.put(pi).put(mi).put(di).put(pi).put(di).put(ni).put(pi).put(ni).put(bi).put(pi).put(bi).put(mi);
			}
		}
		
		rock.putIndices(indices, ((size - 1) * (size - 1)) * 12);
	}
	
	private double smallest(double a, double b, double c, double d)
	{
		return a<b?(a<c?(a<d?a:d):(c<d?c:d)):(b<c?(b<d?b:d):(c<d?c:d));
	}
	
	private double flatness(float a, float b, float c)
	{
		float mean = (a + b + c) / 3.0f;
		
		return Math.abs(mean - a) + Math.abs(mean - b) + Math.abs(mean - c);
	}
	
	private int posToLin(int x, int z)
	{
		return (x + (z * size)) * 2;
	}
}
