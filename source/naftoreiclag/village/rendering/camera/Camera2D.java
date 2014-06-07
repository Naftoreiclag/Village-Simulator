/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.camera;

import static org.lwjgl.opengl.GL11.*;

public abstract class Camera2D extends Camera
{
	public float x;
	public float y;
	
	public float roll;

	@Override
	public void applyMatrix()
	{
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_MODELVIEW);
		glRotatef(roll, 0, 0, 1);
		glTranslatef(-x, -y, 0);
		glPopAttrib();
	}
}
