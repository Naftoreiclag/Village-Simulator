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

		double steepThres = 0.25;
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
				
				boolean bothAreSteep = false;
				
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
										mx, my, mz, mapNormals[mxi][mzi], 0.99f, 0.01f, 
										nx, ny, nz, mapNormals[nxi][nzi], 0.01f, 0.01f, 
										nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.01f, 0.99f, 
										mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.99f, 0.99f);
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

							float ndmA = (n + d + m) / 3.0f;
							float mbnA = (m + b + n) / 3.0f;

							if(ndmA > mbnA)
							{
								// Hang
								
								Vector3f foo = new Vector3f(mx - nx, my - ny, mz - nz);
								Vector3f jut = Vector3f.cross(mapNormals[nxi][nzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										nx, ny, nz, mapNormals[nxi][nzi], 0.99f, 0.01f, 
										mx, my, mz, mapNormals[mxi][mzi], 0.01f, 0.01f, 
										mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.01f, 0.99f, 
										nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.99f, 0.99f);
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
							
							bothAreSteep = true;
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
							
							mb_rock.addTriangle(dV, mV, bV);
							
							float bndA = (b + n + d) / 3.0f;
							float dmbA = (d + m + b) / 3.0f;
							
							if(bndA > dmbA)
							{
								// Hang
								
								Vector3f foo = new Vector3f(dx - bx, dy - by, dz - bz);
								Vector3f jut = Vector3f.cross(mapNormals[bxi][bzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										bx, by, bz, mapNormals[bxi][bzi], 0.99f, 0.01f, 
										dx, dy, dz, mapNormals[dxi][dzi], 0.01f, 0.01f, 
										dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.01f, 0.99f, 
										bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.99f, 0.99f);
							}
							else
							{
								// Stand
							}
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

							float dmbA = (d + m + b) / 3.0f;
							float bndA = (b + n + d) / 3.0f;
							
							mb_grass.addTriangle(dV, mV, bV);
							
							if(dmbA > bndA)
							{
								// Hang
								
								// Replace b and d
								
								Vector3f foo = new Vector3f(bx - dx, by - dy, bz - dz);
								Vector3f jut = Vector3f.cross(mapNormals[dxi][dzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										dx, dy, dz, mapNormals[dxi][dzi], 0.99f, 0.01f, 
										bx, by, bz, mapNormals[bxi][bzi], 0.01f, 0.01f, 
										bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.01f, 0.99f, 
										dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.99f, 0.99f);
							}
							else
							{
								// Stand
							}
						}
						else
						{
							// BND is steep
							// DMB is steep
							
							mb_rock.addTriangle(dV, mV, bV);
							
							bothAreSteep = true;
						}
					}
				}
				
				if(bothAreSteep)
				{
					if(z != 0)
					{
						double triA = steepness(map[x + 1][z], map[x][z], map[x][z - 1]);
						double triB = steepness(map[x + 1][z], map[x][z], map[x + 1][z - 1]);
						
						if(triA < steepThres || triB < steepThres)
						{
							float dmA = (d + m) / 2.0f;
							float bnA = (b + n) / 2.0f;
							
							if(dmA > bnA)
							{
								// hang
								
								Vector3f foo = new Vector3f(mx - dx, my - dy, mz - dz);
								Vector3f jut = Vector3f.cross(mapNormals[dxi][dzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										dx, dy, dz, mapNormals[dxi][dzi], 0.99f, 0.01f, 
										mx, my, mz, mapNormals[mxi][mzi], 0.01f, 0.01f, 
										mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.01f, 0.99f, 
										dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.99f, 0.99f);
							}
						}
					}
					
					if(x != 0)
					{
						double triA = steepness(map[x][z], map[x][z + 1], map[x - 1][z]);
						double triB = steepness(map[x][z], map[x][z + 1], map[x - 1][z + 1]);
						
						if(triA < steepThres || triB < steepThres)
						{
							// I was wondering when MBA would show up...
							float mbA = (m + b) / 2.0f;
							float ndA = (n + d) / 2.0f;
							
							if(mbA > ndA)
							{
								// hang
								
								Vector3f foo = new Vector3f(bx - mx, by - my, bz - mz);
								Vector3f jut = Vector3f.cross(mapNormals[mxi][mzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										mx, my, mz, mapNormals[mxi][mzi], 0.99f, 0.01f, 
										bx, by, bz, mapNormals[bxi][bzi], 0.01f, 0.01f, 
										bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.01f, 0.99f, 
										mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.99f, 0.99f);
							}
						}
					}
					
					if(z != size - 2)
					{
						double triA = steepness(map[x][z + 1], map[x + 1][z + 1], map[x + 1][z]);
						double triB = steepness(map[x][z + 1], map[x + 1][z + 1], map[x + 1][z + 2]);
						
						if(triA < steepThres || triB < steepThres)
						{
							float bnA = (b + n) / 2.0f;
							float dmA = (d + m) / 2.0f;
							
							if(bnA > dmA)
							{
								// hang
								
								Vector3f foo = new Vector3f(nx - bx, ny - by, nz - bz);
								Vector3f jut = Vector3f.cross(mapNormals[bxi][bzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										bx, by, bz, mapNormals[bxi][bzi], 0.99f, 0.01f, 
										nx, ny, nz, mapNormals[nxi][nzi], 0.01f, 0.01f, 
										nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.01f, 0.99f, 
										bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.99f, 0.99f);
							}
						}
					}
					
					if(x != size - 2)
					{
						double triA = steepness(map[x + 1][z + 1], map[x + 1][z], map[x + 2][z]);
						double triB = steepness(map[x + 1][z + 1], map[x + 1][z], map[x + 2][z + 1]);
						
						if(triA < steepThres || triB < steepThres)
						{
							float ndA = (n + d) / 2.0f;
							float mbA = (m + b) / 2.0f;
							
							if(ndA > mbA)
							{
								// hang
								
								Vector3f foo = new Vector3f(dx - nx, dy - ny, dz - nz);
								Vector3f jut = Vector3f.cross(mapNormals[nxi][nzi], foo, null);
								jut.y *= 0.3f;
								jut.normalise();
								
								mb_sidegrass.addQuad(
										nx, ny, nz, mapNormals[nxi][nzi], 0.99f, 0.01f, 
										dx, dy, dz, mapNormals[dxi][dzi], 0.01f, 0.01f, 
										dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.01f, 0.99f, 
										nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.99f, 0.99f);
							}
						}
					}
				}
			}
		}
		
		grass = mb_grass.bake();
		rock = mb_rock.bake();
		sidegrass = mb_sidegrass.bake();
	}
	
	private void performSteepSteepCheck(int x, int z, ModelBuilder mb_sidegrass)
	{
		
	}

	public Vector3f straighten(Vector3f a)
	{
		return (Vector3f) (new Vector3f(a.x, a.y * 2,a.z)).normalise();
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
	
	private double steepness(float a, float b)
	{
		return Math.abs(a - b);
	}
	
	private int posToLin(int x, int z)
	{
		return (x + (z * size)) * 2;
	}
}
