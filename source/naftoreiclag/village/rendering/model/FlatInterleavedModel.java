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
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// Same as InterleavedModel, except without normals and only 2-Dimensional.
// This is a static model with standard features.

public class FlatInterleavedModel extends InterleavedModel
{
	public FlatInterleavedModel(FloatBuffer verts, IntBuffer indices, int numIndices)
	{
		super(verts, indices, numIndices);
	}
	
	@Override
	public void render()
	{
		glBindTexture(GL_TEXTURE_2D, texId);
		glBindBuffer(GL_ARRAY_BUFFER, vertHandle);
		glVertexPointer(2, GL_FLOAT, 4 << 2, 0 << 2);
		glTexCoordPointer(2, GL_FLOAT, 4 << 2, 2 << 2);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHandle);
		glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_INT, 0L);
	}
}
