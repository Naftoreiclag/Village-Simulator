/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 * 
 * Special thanks to Sean R. Owens for his public domain code which I added.
 */

package naftoreiclag.village.rendering.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

// This one is the kind generated by the oobjloader things.
// It also can be used to send data that has the same interleaving.
// This is a static model with standard features.

public class BatchModel extends Model
{
	public BatchModel(FloatBuffer verts, IntBuffer indices, int numIndices)
	{
		super(verts, indices, numIndices);
	}

	@Override
	public void render()
	{
		glBindTexture(GL_TEXTURE_2D, texId);
		glBindBuffer(GL_ARRAY_BUFFER, vertHandle);
		glVertexPointer(3, GL_FLOAT, 32, 0);
		glNormalPointer(GL_FLOAT, 32, 12);
		glTexCoordPointer(2, GL_FLOAT, 32, 24);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHandle);
		glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_INT, 0L);
	}
}
