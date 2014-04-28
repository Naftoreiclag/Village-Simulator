/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import naftoreiclag.village.rendering.model.Model;
import naftoreiclag.village.rendering.util.ModelBuilder;

import org.lwjgl.util.vector.Vector3f;

public class Hills
{
	// Standard sizes for horizontal and vertical scale
	public static final float horzu = 1.0f;
	public static final float vertu = 5.0f;
	
	// Maximum steepness for a triangle to become grass
	public static final double steepThreshold = 0.25;
	
	public Model grass;
	public Model rock;
	public Model sidegrass;
	public Model tallgrass;
	
	int size = 64;
	
	public float[][] map = new float[size][size];
	public Vector3f[][] mapNormals = new Vector3f[size][size];
	
	public Hills()
	{
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
	
	public void updateNormals(int x, int z)
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
	
	public void forceUpdateNormals()
	{
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				updateNormals(x, z);
			}
		}
	}
	
	public void makeModelFancy()
	{
		forceUpdateNormals();
		
		ModelBuilder mb_rock = new ModelBuilder();
		ModelBuilder mb_grass = new ModelBuilder();
		ModelBuilder mb_sidegrass = new ModelBuilder();
		ModelBuilder mb_tallgrass = new ModelBuilder();

		// How scaled the texture is
		float textureScale = 0.5f;
		
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
				
				ModelBuilder.Vertex mV = new ModelBuilder.Vertex(mx, my, mz, mapNormals[mxi][mzi], mxi * textureScale, mzi * textureScale);
				ModelBuilder.Vertex bV = new ModelBuilder.Vertex(bx, by, bz, mapNormals[bxi][bzi], bxi * textureScale, bzi * textureScale);
				ModelBuilder.Vertex nV = new ModelBuilder.Vertex(nx, ny, nz, mapNormals[nxi][nzi], nxi * textureScale, nzi * textureScale);
				ModelBuilder.Vertex dV = new ModelBuilder.Vertex(dx, dy, dz, mapNormals[dxi][dzi], dxi * textureScale, dzi * textureScale);
				
				double mbn = steepness(m, b, n);
				double bnd = steepness(b, n, d);
				double ndm = steepness(n, d, m);
				double dmb = steepness(d, m, b);
				
				double flat = smallest(mbn, bnd, ndm, dmb);
				
				boolean bothAreSteep = false;
				
				if(mbn == flat || ndm == flat)
				{
					if(mbn < steepThreshold)
					{
						// MBN is flat
						
						mb_grass.addTriangle(mV, bV, nV);
						
						if(ndm < steepThreshold)
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
							
							Vector3f jut;
							ModelBuilder addTo;
							
							if(mbnA > ndmA)
							{
								jut = Vector3f.cross(mapNormals[mxi][mzi], new Vector3f(nx - mx, ny - my, nz - mz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}
							
							addTo.addQuad(
									mx, my, mz, mapNormals[mxi][mzi], 0.99f, 0.01f, 
									nx, ny, nz, mapNormals[nxi][nzi], 0.01f, 0.01f, 
									nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.01f, 0.99f, 
									mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.99f, 0.99f);
						}
					}
					else
					{
						// MBN is steep
						
						mb_rock.addTriangle(mV, bV, nV);
						
						if(ndm < steepThreshold)
						{
							// MBN is steep
							// NDM is flat
							
							mb_grass.addTriangle(nV, dV, mV);

							float ndmA = (n + d + m) / 3.0f;
							float mbnA = (m + b + n) / 3.0f;

							Vector3f jut;
							ModelBuilder addTo;
							
							if(ndmA > mbnA)
							{
								jut = Vector3f.cross(mapNormals[nxi][nzi], new Vector3f(mx - nx, my - ny, mz - nz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
								
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}
							
							addTo.addQuad(
									nx, ny, nz, mapNormals[nxi][nzi], 0.99f, 0.01f, 
									mx, my, mz, mapNormals[mxi][mzi], 0.01f, 0.01f, 
									mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.01f, 0.99f, 
									nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.99f, 0.99f);
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
					if(bnd < steepThreshold)
					{
						// BND is flat
						
						mb_grass.addTriangle(bV, nV, dV);
						
						if(dmb < steepThreshold)
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
							
							Vector3f jut;
							ModelBuilder addTo;
							
							if(bndA > dmbA)
							{
								jut = Vector3f.cross(mapNormals[bxi][bzi], new Vector3f(dx - bx, dy - by, dz - bz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}

							addTo.addQuad(
									bx, by, bz, mapNormals[bxi][bzi], 0.99f, 0.01f, 
									dx, dy, dz, mapNormals[dxi][dzi], 0.01f, 0.01f, 
									dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.01f, 0.99f, 
									bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.99f, 0.99f);
						}
					}
					else
					{
						// BND is steep
						
						mb_rock.addTriangle(bV, nV, dV);
						
						if(dmb < steepThreshold)
						{
							// BND is steep
							// DMB is flat

							float dmbA = (d + m + b) / 3.0f;
							float bndA = (b + n + d) / 3.0f;
							
							mb_grass.addTriangle(dV, mV, bV);

							Vector3f jut;
							ModelBuilder addTo;
							
							if(dmbA > bndA)
							{
								jut = Vector3f.cross(mapNormals[dxi][dzi], new Vector3f(bx - dx, by - dy, bz - dz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}
							
							addTo.addQuad(
									dx, dy, dz, mapNormals[dxi][dzi], 0.99f, 0.01f, 
									bx, by, bz, mapNormals[bxi][bzi], 0.01f, 0.01f, 
									bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.01f, 0.99f, 
									dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.99f, 0.99f);
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
						
						if(triA < steepThreshold || triB < steepThreshold)
						{
							float dmA = (d + m) / 2.0f;
							float bnA = (b + n) / 2.0f;

							Vector3f jut;
							ModelBuilder addTo;
							
							if(dmA > bnA)
							{
								jut = Vector3f.cross(mapNormals[dxi][dzi], new Vector3f(mx - dx, my - dy, mz - dz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}
							
							addTo.addQuad(
									dx, dy, dz, mapNormals[dxi][dzi], 0.99f, 0.01f, 
									mx, my, mz, mapNormals[mxi][mzi], 0.01f, 0.01f, 
									mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.01f, 0.99f, 
									dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.99f, 0.99f);
						}
					}
					
					if(x != 0)
					{
						double triA = steepness(map[x][z], map[x][z + 1], map[x - 1][z]);
						double triB = steepness(map[x][z], map[x][z + 1], map[x - 1][z + 1]);
						
						if(triA < steepThreshold || triB < steepThreshold)
						{
							// I was wondering when MBA would show up...
							float mbA = (m + b) / 2.0f;
							float ndA = (n + d) / 2.0f;

							Vector3f jut;
							ModelBuilder addTo;
							
							if(mbA > ndA)
							{
								jut = Vector3f.cross(mapNormals[mxi][mzi], new Vector3f(bx - mx, by - my, bz - mz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}
							
							addTo.addQuad(
									mx, my, mz, mapNormals[mxi][mzi], 0.99f, 0.01f, 
									bx, by, bz, mapNormals[bxi][bzi], 0.01f, 0.01f, 
									bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.01f, 0.99f, 
									mx + jut.x, my + jut.y, mz + jut.z, mapNormals[mxi][mzi], 0.99f, 0.99f);
						}
					}
					
					if(z != size - 2)
					{
						double triA = steepness(map[x][z + 1], map[x + 1][z + 1], map[x + 1][z]);
						double triB = steepness(map[x][z + 1], map[x + 1][z + 1], map[x + 1][z + 2]);
						
						if(triA < steepThreshold || triB < steepThreshold)
						{
							float bnA = (b + n) / 2.0f;
							float dmA = (d + m) / 2.0f;

							Vector3f jut;
							ModelBuilder addTo;
							
							if(bnA > dmA)
							{
								jut = Vector3f.cross(mapNormals[bxi][bzi], new Vector3f(nx - bx, ny - by, nz - bz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}
							
							addTo.addQuad(
									bx, by, bz, mapNormals[bxi][bzi], 0.99f, 0.01f, 
									nx, ny, nz, mapNormals[nxi][nzi], 0.01f, 0.01f, 
									nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.01f, 0.99f, 
									bx + jut.x, by + jut.y, bz + jut.z, mapNormals[bxi][bzi], 0.99f, 0.99f);
						}
					}
					
					if(x != size - 2)
					{
						double triA = steepness(map[x + 1][z + 1], map[x + 1][z], map[x + 2][z]);
						double triB = steepness(map[x + 1][z + 1], map[x + 1][z], map[x + 2][z + 1]);
						
						if(triA < steepThreshold || triB < steepThreshold)
						{
							float ndA = (n + d) / 2.0f;
							float mbA = (m + b) / 2.0f;

							Vector3f jut;
							ModelBuilder addTo;
							
							if(ndA > mbA)
							{
								jut = Vector3f.cross(mapNormals[nxi][nzi], new Vector3f(dx - nx, dy - ny, dz - nz), null);
								jut.y *= 0.3f;
								jut.normalise();
								addTo = mb_sidegrass;
							}
							else
							{
								jut = new Vector3f(0.0f, 1.0f, 0.0f);
								addTo = mb_tallgrass;
							}
							
							addTo.addQuad(
									nx, ny, nz, mapNormals[nxi][nzi], 0.99f, 0.01f, 
									dx, dy, dz, mapNormals[dxi][dzi], 0.01f, 0.01f, 
									dx + jut.x, dy + jut.y, dz + jut.z, mapNormals[dxi][dzi], 0.01f, 0.99f, 
									nx + jut.x, ny + jut.y, nz + jut.z, mapNormals[nxi][nzi], 0.99f, 0.99f);
						}
					}
				}
			}
		}
		
		grass = mb_grass.bake();
		rock = mb_rock.bake();
		sidegrass = mb_sidegrass.bake();
		tallgrass = mb_tallgrass.bake();
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
}
