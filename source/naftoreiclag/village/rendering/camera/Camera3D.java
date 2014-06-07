/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.camera;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Camera3D extends Camera
{
	public float x;
	public float y;
	public float z;
	
	// TODO: make it so you can edit these
	public final float fov;
	public final float aspectRatio;
	public final float zNear;
	public final float zFar;
	
	public float pitch;
	public float yaw;
	public float roll;
	
	public Camera3D(float fov, float aspectRatio, float zNear, float zFar)
	{
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.zNear = zNear;
		this.zFar = zFar;
	}
	
	@Override
	public void setup()
	{
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspectRatio, zNear, zFar);
		glPopAttrib();
	}

	@Override
	public void applyMatrix()
	{
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_MODELVIEW);
		glRotatef(pitch, 1, 0, 0);
		glRotatef(yaw, 0, 1, 0);
		glRotatef(roll, 0, 0, 1);
		glTranslatef(-x, -y, -z);
		glPopAttrib();
	}
}
