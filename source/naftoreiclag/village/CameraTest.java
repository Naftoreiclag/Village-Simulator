/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import org.lwjgl.opengl.GL11;

public class CameraTest
{
	float x = 0.0f;
	float y = 0.0f;
	float z = 0.0f;

	float yaw = 0.0f;
	float pitch = 0.0f;
	
	float speed = 0.2f;

	public void look()
	{
		GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(x, y, z);
	}
	
	public void goLeft()
	{
		x -= speed * (float) Math.sin(Math.toRadians(yaw - 90));
		z += speed * (float) Math.cos(Math.toRadians(yaw - 90));
	}

	public void goRight()
	{
		x -= speed * (float) Math.sin(Math.toRadians(yaw + 90));
		z += speed * (float) Math.cos(Math.toRadians(yaw + 90));
	}

	public void goForward()
	{
		x -= speed * (float) Math.sin(Math.toRadians(yaw));
		z += speed * (float) Math.cos(Math.toRadians(yaw));
	}

	public void goBack()
	{
		x += speed * (float) Math.sin(Math.toRadians(yaw));
		z -= speed * (float) Math.cos(Math.toRadians(yaw));
	}
	
	public void goUp()
	{
		y -= speed;
	}
	
	public void goDown()
	{
		y += speed;
	}
}
