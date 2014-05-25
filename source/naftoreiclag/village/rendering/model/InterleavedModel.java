/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// Model with interleaved data. Ideal for generated meshes.
// This is a static model with standard features.

public class InterleavedModel extends Model
{
	public InterleavedModel(FloatBuffer verts, IntBuffer indices, int numIndices)
	{
		super(verts, indices, numIndices);
	}
	
	@Override
	public void render()
	{
		glBindTexture(GL_TEXTURE_2D, texId);
		glBindBuffer(GL_ARRAY_BUFFER, vertHandle);
		glVertexPointer(3, GL_FLOAT, 8 << 2, 0 << 2);
		glNormalPointer(GL_FLOAT, 8 << 2, 3 << 2);
		glTexCoordPointer(2, GL_FLOAT, 8 << 2, 6 << 2);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHandle);
		glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_INT, 0L);
	}
}
