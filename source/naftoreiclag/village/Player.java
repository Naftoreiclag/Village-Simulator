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
	
	float spd = 0.1f;
	
	public void input()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			z -= spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			z += spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			x -= spd;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			x += spd;
		}
	}
}
