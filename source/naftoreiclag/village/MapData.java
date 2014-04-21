/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.util.Random;

public class MapData
{
	int size = 32;
	
	public float[][] value = new float[size][size];
	
	public void loadDataFromFile(String filename)
	{
		Random r = new Random();
		
		for(int x = 0; x < size; ++ x)
		{
			for(int y = 0; y < size; ++ y)
			{
				value[x][y] = r.nextFloat();
			}
		}
	}
	
	public void convertToGeometry()
	{
		
	}
}
