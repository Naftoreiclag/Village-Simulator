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
	Model grass;
	Model rock;
	Model sidegrass;
	
	// Standard sizes for horizontal and vertical scale
	float horzu = 1.0f;
	float vertu = 5.0f;
	
	int size = 64;
	
	public float[][] map = new float[size][size];
	public Vector3f[][] mapNormals = new Vector3f[size][size];
	
	public MapData()
	{
		grass = new Model();
		rock = new Model();
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				mapNormals[x][z] = new Vector3f();
			}
		}
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
				
				double mbn = steepness(m, b, n);
				double bnd = steepness(b, n, d);
				double ndm = steepness(n, d, m);
				double dmb = steepness(d, m, b);
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
		
		rock.putVerts(topVertsBuff);
		rock.putIndices(topIntBuff, inde.size());
	}
	
	public void forceUpdateNormals()
	{
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

				mapNormals[x][z].set(cd_d * vertu, 2 * horzu, ab_d * vertu);
				mapNormals[x][z].normalise();
			}
		}
	}
	
	public void makeModelFancy()
	{
		forceUpdateNormals();
		
		ModelBuilder mb_rock = new ModelBuilder();
		ModelBuilder mb_grass = new ModelBuilder();
		ModelBuilder mb_sidegrass = new ModelBuilder();

		double steepThres = 0.15;
		float texS = 0.5f;
		
		for(int x = 0; x < size - 1; ++ x)
		{
			for(int z = 0; z < size - 1; ++ z)
			{
				// M  0  D
				//
				// 2  P  3
				//
				// B  1  N
				
				int mxi = x;
				int mzi = z;
				int bxi = x;
				int bzi = z + 1;
				int nxi = x + 1;
				int nzi = z + 1;
				int dxi = x + 1;
				int dzi = z;
				
				float m = map[mxi][mzi];
				float b = map[bxi][bzi];
				float n = map[nxi][nzi];
				float d = map[dxi][dzi];
				
				float mx = mxi * horzu;
				float my = m * vertu;
				float mz = mzi * horzu;
				float bx = bxi * horzu;
				float by = b * vertu;
				float bz = bzi * horzu;
				float nx = nxi * horzu;
				float ny = n * vertu;
				float nz = nzi * horzu;
				float dx = dxi * horzu;
				float dy = d * vertu;
				float dz = dzi * horzu;
				
				ModelBuilder.Vertex mV = new ModelBuilder.Vertex(mx, my, mz, mapNormals[mxi][mzi], mxi * texS, mzi * texS);
				ModelBuilder.Vertex bV = new ModelBuilder.Vertex(bx, by, bz, mapNormals[bxi][bzi], bxi * texS, bzi * texS);
				ModelBuilder.Vertex nV = new ModelBuilder.Vertex(nx, ny, nz, mapNormals[nxi][nzi], nxi * texS, nzi * texS);
				ModelBuilder.Vertex dV = new ModelBuilder.Vertex(dx, dy, dz, mapNormals[dxi][dzi], dxi * texS, dzi * texS);
				
				double mbn = steepness(m, b, n);
				double bnd = steepness(b, n, d);
				double ndm = steepness(n, d, m);
				double dmb = steepness(d, m, b);
				
				double flat = smallest(mbn, bnd, ndm, dmb);
				
				if(mbn == flat || ndm == flat)
				{
					if(mbn < steepThres)
					{
						// MBN is flat
						
						mb_grass.addTriangle(mV, bV, nV);
						
						if(ndm < steepThres)
						{
							// MBN is flat
							// NDM is flat
							
							mb_grass.addTriangle(nV, dV, mV);
						}
						else
						{
							// MBN is flat
							// NDM is steep
							
							mb_rock.addTriangle(nV, dV, mV);
							
							float mbnA = (m + b + n) / 3.0f;
							float ndmA = (n + d + m) / 3.0f;
							
							if(mbnA > ndmA)
							{
								// Hang
								
								Vector3f foo = new Vector3f(nx - mx, ny - my, nz - mz);
								Vector3f jut = Vector3f.cross(mapNormals[mxi][mzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										mx, my, mz, mapNormals[mxi][mzi], 1, 0, 
										nx, ny, nz, mapNormals[mxi][mzi], 0, 0, 
										nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[mxi][mzi], 0, 1, 
										mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 1, 1);
							}
							else
							{
								// Stand
							}
						}
					}
					else
					{
						// MBN is steep
						
						mb_rock.addTriangle(mV, bV, nV);
						
						if(ndm < steepThres)
						{
							// MBN is steep
							// NDM is flat
							
							mb_grass.addTriangle(nV, dV, mV);
							
							float mbnA = (m + b + n) / 3.0f;
							float ndmA = (n + d + m) / 3.0f;
							

							if(ndmA > mbnA)
							{
								// Hang
								
								Vector3f foo = new Vector3f(mx - nx, my - ny, mz - nz);
								Vector3f jut = Vector3f.cross(mapNormals[nxi][nzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										nx, ny, nz, mapNormals[nxi][nzi], 1, 0, 
										mx, my, mz, mapNormals[nxi][nzi], 0, 0, 
										mx + jut.x, my + jut.y, mz + jut.z, mapNormals[nxi][nzi], 0, 1, 
										nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 1, 1);
							}
							else
							{
								// Stand
							}
						}
						else
						{
							// MBN is steep
							// NDM is steep
							
							mb_rock.addTriangle(nV, dV, mV);
						}
					}
				}
				else
				{
					if(bnd < steepThres)
					{
						// BND is flat
						
						mb_grass.addTriangle(bV, nV, dV);
						
						if(dmb < steepThres)
						{
							// BND is flat
							// DMB is flat
							
							mb_grass.addTriangle(dV, mV, bV);
						}
						else
						{
							// BND is flat
							// DMB is steep
							
							float mbnA = (m + b + n) / 3.0f;
							float ndmA = (n + d + m) / 3.0f;
							
							mb_rock.addTriangle(dV, mV, bV);
						}
					}
					else
					{
						// BND is steep
						
						mb_rock.addTriangle(bV, nV, dV);
						
						if(dmb < steepThres)
						{
							// BND is steep
							// DMB is flat
							
							mb_grass.addTriangle(dV, mV, bV);
						}
						else
						{
							// BND is steep
							// DMB is steep
							
							mb_rock.addTriangle(dV, mV, bV);
						}
					}
				}
			}
		}
		
		grass = mb_grass.bake();
		rock = mb_rock.bake();
		sidegrass = mb_sidegrass.bake();
	}
	
	public Vector3f straighten(Vector3f a)
	{
		return (Vector3f) (new Vector3f(a.x, a.y * 2,a.z)).normalise();
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
						/* M */ (x    ) * horzu, m * vertu, (z    ) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), x    , z,
						/* B */ (x    ) * horzu, b * vertu, (z + 1) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), x    , z + 1,
						/* N */ (x + 1) * horzu, n * vertu, (z + 1) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), x + 1, z + 1,
						/* D */ (x + 1) * horzu, d * vertu, (z    ) * horzu, new Vector3f(0.0f, 0.0f, 0.0f), x + 1, z    );
			}
		}
		
		grass = mb.bake();
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
				
				double mbn = steepness(m, b, n);
				double bnd = steepness(b, n, d);
				double ndm = steepness(n, d, m);
				double dmb = steepness(d, m, b);
				
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
		
		grass.putVerts(verts);
		
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
		
		grass.putIndices(indices, ((size - 1) * (size - 1)) * 12);
	}
	
	private double smallest(double a, double b, double c, double d)
	{
		return a<b?(a<c?(a<d?a:d):(c<d?c:d)):(b<c?(b<d?b:d):(c<d?c:d));
	}
	
	private double steepness(float a, float b, float c)
	{
		float mean = (a + b + c) / 3.0f;
		
		return Math.abs(mean - a) + Math.abs(mean - b) + Math.abs(mean - c);
	}
	
	private int posToLin(int x, int z)
	{
		return (x + (z * size)) * 2;
	}
}
