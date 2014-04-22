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

public class MapData
{
	int size = 32;
	
	public float[][] value = new float[size][size];
	
	public void loadDataFromFile(String filename)
	{
		/*
		Random r = new Random();
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				value[x][z] = r.nextFloat();
			}
		}
		*/
		
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
				value[x][z] = ((float) (img.getRGB(x, z) & 0x000000FF)) / 256f;
			}
		}
	}
	
	public FloatBuffer convertToGeometry()
	{
		FloatBuffer geo = BufferUtils.createFloatBuffer(size * size * 5);
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				geo.put(x - 16).put(value[x][z] - 2.0f).put(z - 16).put(x / 2f).put(z / 2f);
			}
		}
		
		geo.flip();
		
		return geo;
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
				
				int ul = posToLin(ulx, ulz);
				int ur = posToLin(urx, urz);
				int dl = posToLin(dlx, dlz);
				int dr = posToLin(drx, drz);
				
				ind.put(ur).put(dr).put(dl).put(ul).put(ur).put(dl);
			}
		}
		
		ind.flip();
		
		return ind;
	}
	
	private int posToLin(int x, int z)
	{
		return x + (z * size);
	}
}
