/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.model;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class Model
{
	public final FloatBuffer verts;
	public final IntBuffer indices;
	public final int numIndices;
	
	protected int vertHandle;
	protected int indexHandle;
	
	protected int texId;
	
	public Model(FloatBuffer verts, IntBuffer indices, int numIndices)
	{
		this.verts = verts;
		this.indices = indices;
		this.numIndices = numIndices;
	}
	
	public void setTexture(int texId)
	{
		this.texId = texId;
	}
	
	public void upload()
	{
		verts.flip();
		indices.flip();
		
		vertHandle = glGenBuffers();
		indexHandle = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, vertHandle); // Select this spot as an array buffer
		glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW); // Send data
		
		/*
		 *  Note: The difference between ELEMENT_ARRAY and ARRAY is that 
		 *  ELEMENT_ARRAY is used to denote an array full of "pointers" to 
		 *  another array.
		 */
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHandle); // Select this spot as an array buffer
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); // Send data
	}
	
	public abstract void render();
	
	public void cleanup()
	{
		// Free up memory that we used
		glDeleteBuffers(vertHandle);
		glDeleteBuffers(indexHandle);
	}
}
