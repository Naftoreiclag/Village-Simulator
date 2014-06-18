/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import naftoreiclag.village.collision.Circle;

import org.lwjgl.input.Keyboard;

public class Player
{
	public float spd = 0.005f;
	
	public Circle collision;
	
	public Player()
	{
		collision = new Circle(0, 0, 25, 1, 1);
	}
	
	public void input(long delta)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			collision.motion.b += -spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			collision.motion.b += spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			collision.motion.a += -spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			collision.motion.a += spd;
		}
	}
}
