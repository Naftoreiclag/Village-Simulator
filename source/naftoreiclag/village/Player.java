/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import naftoreiclag.village.environment.Hills;

import org.lwjgl.input.Keyboard;

public class Player
{
	public float x;
	public float y = 2.5f;
	public float z;
	
	public float direction;
	
	float spd = 0.05f;
	
	public Hills map;
	
	public void input()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			direction = (float) directTo(direction, 180f, 0.05f);
			z -= spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			direction = (float) directTo(direction, 0f, 0.05f);
			z += spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			direction = (float) directTo(direction, 270f, 0.05f);
			x -= spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			direction = (float) directTo(direction, 90f, 0.05f);
			x += spd;
		}
		
		y = getFoo(x, z);
	}

	public double directTo(double aaa, double bbb, float amnt)
	{
		// Convert to Vectors
		double ax = Math.cos(Math.toRadians(aaa));
		double ay = Math.sin(Math.toRadians(aaa));
		double bx = Math.cos(Math.toRadians(bbb));
		double by = Math.sin(Math.toRadians(bbb));

		double cx = (ax + bx) / 2d;
		double cy = (ay + by) / 2d;

		double midDir = Math.toDegrees(Math.atan2(cy, cx));

		return midDir;
	}

	
	private float getFoo(float x, float z)
	{
		if(x >= 0 && z >= 0 && x < Hills.size && z < Hills.size)
		{
			
			// M  y  D
			//
			// h  P  s
			//
			// B  g  N
			
			x /= Hills.horzu;
			z /= Hills.horzu;
			
			int xi = (int) x;
			int zi = (int) z;
			
			float m = map.map[xi][zi];
			float d = map.map[xi + 1][zi];
			float b = map.map[xi][zi + 1];
			float n = map.map[xi+1][zi+1];
			
			float xr = x - xi;
			float zr = z - zi;
			
			float md = d - m;
			float bn = n - b;
			
			float y = m + (md * xr);
			float g = b + (bn * xr);
			
			float yg = g - y;
			
			float p = y + (yg * zr);
			
			return p * Hills.vertu;
		}
		else
		{
			return 0;
		}
	}
}
