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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;

import org.lwjgl.BufferUtils;

public class VBO
{

	public final static int FL_SIZE = 4;
	public final static int INDICE_SIZE_BYTES = 4;
	public final static int ATTR_V_FLOATS_PER = 3;
	public final static int ATTR_N_FLOATS_PER = 3;
	public final static int ATTR_T_FLOATS_PER = 2;
	public final static int ATTR_SZ_FLOATS = ATTR_V_FLOATS_PER + ATTR_N_FLOATS_PER + ATTR_T_FLOATS_PER;
	public final static int ATTR_SZ_BYTES = ATTR_SZ_FLOATS * FL_SIZE;
	public final static int ATTR_V_OFFSET_BYTES = 0;
	public final static int ATTR_V_OFFSET_FLOATS = 0;
	public final static int ATTR_N_OFFSET_FLOATS = ATTR_V_FLOATS_PER;
	public final static int ATTR_N_OFFSET_BYTES = ATTR_N_OFFSET_FLOATS * FL_SIZE;
	public final static int ATTR_T_OFFSET_FLOATS = ATTR_V_FLOATS_PER + ATTR_N_FLOATS_PER;
	public final static int ATTR_T_OFFSET_BYTES = ATTR_T_OFFSET_FLOATS * FL_SIZE;
	public final static int ATTR_V_STRIDE2_BYTES = ATTR_SZ_FLOATS * FL_SIZE;
	public final static int ATTR_N_STRIDE2_BYTES = ATTR_SZ_FLOATS * FL_SIZE;
	public final static int ATTR_T_STRIDE2_BYTES = ATTR_SZ_FLOATS * FL_SIZE;

	private int textId = 0;
	private int vertexHandle = 0; // Vertex Attributes VBO ID
	private int indexHandle = 0; // indice VBO ID
	private int numIndices = 0;

	public VBO(int textId, int verticeAttributesID, int indicesID,
			int indicesCount)
	{
		this.textId = textId;
		this.vertexHandle = verticeAttributesID;
		this.indexHandle = indicesID;
		this.numIndices = indicesCount;
	}

	public void render()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, ATTR_V_STRIDE2_BYTES, ATTR_V_OFFSET_BYTES);
		GL11.glNormalPointer(GL11.GL_FLOAT, ATTR_N_STRIDE2_BYTES, ATTR_N_OFFSET_BYTES);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, ATTR_T_STRIDE2_BYTES, ATTR_T_OFFSET_BYTES);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexHandle);
		GL11.glDrawElements(GL11.GL_TRIANGLES, numIndices, GL11.GL_UNSIGNED_INT, 0L);
	}

	public void destroy()
	{
		// NOTE: We don't delete the textureID
		IntBuffer ib = BufferUtils.createIntBuffer(1);
		ib.reset();
		ib.put(vertexHandle);
		GL15.glDeleteBuffers(ib);
		ib.reset();
		ib.put(indexHandle);
		GL15.glDeleteBuffers(ib);
	}
}
