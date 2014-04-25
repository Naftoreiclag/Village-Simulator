/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

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
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model
{
	public FloatBuffer verts;
	public IntBuffer indices;
	public int vertHandle;
	public int indexHandle;
	
	public int numIndices;
	
	public int texId;
	
	public void putVerts(FloatBuffer verts)
	{
		this.verts = verts;
	}
	public void putIndices(IntBuffer indices, int numIndices)
	{
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
	
	public void cleanup()
	{
		// Free up memory that we used
		glDeleteBuffers(vertHandle);
		glDeleteBuffers(indexHandle);
	}
}
