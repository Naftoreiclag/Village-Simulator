/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.lwjgl;

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

import naftoreiclag.village.rendering.model.Model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;

import org.lwjgl.BufferUtils;

public class VBO extends Model
{
	public VBO(FloatBuffer verts, IntBuffer indices, int numIndices)
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
