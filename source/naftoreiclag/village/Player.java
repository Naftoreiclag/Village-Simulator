/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import org.lwjgl.input.Keyboard;

public class Player
{
	public float x;
	public float z;
	
	public float direction;
	
	float spd = 0.005f;
	
	public void input(long delta)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			direction = (float) directTo(direction, 180f, 0.05f);
			z -= spd * delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			direction = (float) directTo(direction, 0f, 0.05f);
			z += spd * delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			direction = (float) directTo(direction, 270f, 0.05f);
			x -= spd * delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			direction = (float) directTo(direction, 90f, 0.05f);
			x += spd * delta;
		}
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
}
