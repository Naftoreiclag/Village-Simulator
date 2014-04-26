/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class DebugCam extends Camera
{
	float speed = 0.2f;
	
	public DebugCam(float fov, float aspectRatio, float zNear, float zFar)
	{
		super(fov, aspectRatio, zNear, zFar);
	}
	
	@Override
	public void doLWJGLStuff()
	{
		Mouse.setGrabbed(true);
	}
	
	public void handleUserInput()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			goLeft();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			goRight();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			goForward();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			goBack();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			goUp();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			goDown();
		}

		int dx = Mouse.getDX();
		int dy = Mouse.getDY();
		
		yaw += dx * 0.05f;
		pitch -= dy * 0.05f;
	}
	
	public void goLeft()
	{
		x += speed * (float) Math.sin(Math.toRadians(yaw - 90));
		z -= speed * (float) Math.cos(Math.toRadians(yaw - 90));
	}

	public void goRight()
	{
		x += speed * (float) Math.sin(Math.toRadians(yaw + 90));
		z -= speed * (float) Math.cos(Math.toRadians(yaw + 90));
	}

	public void goForward()
	{
		x += speed * (float) Math.sin(Math.toRadians(yaw));
		z -= speed * (float) Math.cos(Math.toRadians(yaw));
	}

	public void goBack()
	{
		x -= speed * (float) Math.sin(Math.toRadians(yaw));
		z += speed * (float) Math.cos(Math.toRadians(yaw));
	}
	
	public void goUp()
	{
		y += speed;
	}
	
	public void goDown()
	{
		y -= speed;
	}
}
