/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;

public class MapData
{
	int size = 32;
	
	public float[][] value = new float[size][size];
	
	public void loadDataFromFile(String filename)
	{
		Random r = new Random();
		
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				value[x][z] = r.nextFloat();
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
				geo.put(x - 16).put(value[x][z] - 2.0f).put(z - 16).put(x / ((float) size)).put(z / ((float) size));
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
				
				int ul = ulx + (ulz * size);
				int ur = urx + (urz * size);
				int dl = dlx + (dlz * size);
				int dr = drx + (drz * size);
				
				ind.put(dl).put(dr).put(ur).put(dl).put(ur).put(ul);
			}
		}
		
		ind.flip();
		
		return ind;
	}
}
