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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

// this one is crazy

public class CrazyModel extends Model
{
	public CrazyModel(FloatBuffer verts, IntBuffer indices, int numIndices)
	{
		super(verts, indices, numIndices);
	}

	@Override
	public void render()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0);
		GL11.glNormalPointer(GL11.GL_FLOAT, 32, 12);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 24);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexHandle);
		GL11.glDrawElements(GL11.GL_TRIANGLES, numIndices, GL11.GL_UNSIGNED_INT, 0L);
	}
}
