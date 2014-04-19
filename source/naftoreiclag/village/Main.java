/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

//I named this class "Main" just so java newbies can find the
//main method faster! Aren't I so nice? :)

public class Main
{
	int dispW = 640;
	int dispH = 480;
	
	public void run()
	{
		try
		{
			init();
		} catch (LWJGLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		
		while(!Display.isCloseRequested())
		{
			clearScreen();

			vboTest();

			Display.update();

			Display.sync(60);
		}

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		Display.destroy();
	}

	public void vboTest()
	{
		// Vertexes
		FloatBuffer verts = BufferUtils.createFloatBuffer(9);
		verts.put(-0.5f).put(-0.5f).put(0.0f);
		verts.put(+0.5f).put(-0.5f).put(0.0f);
		verts.put(+0.5f).put(+0.5f).put(0.0f);
		verts.flip();
		
		// Colors
		FloatBuffer colors = BufferUtils.createFloatBuffer(9);
		colors.put(1).put(0).put(0);
		colors.put(0).put(1).put(0);
		colors.put(0).put(0).put(1);
		colors.flip();

		// Indices
		ShortBuffer indices = BufferUtils.createShortBuffer(3);
		indices.put((short) 0);
		indices.put((short) 1);
		indices.put((short) 2);
		indices.flip();

		//
		int vertHand = glGenBuffers();
		int colorHand = glGenBuffers();
		int indexHand = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vertHand);
		glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
		glVertexPointer(3, GL_FLOAT, 3 * 4, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, colorHand);
		glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
		glColorPointer(3, GL_FLOAT, 3 * 4, 0L);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHand);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_SHORT, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	static void clearScreen()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	public void init() throws LWJGLException
	{
		Display.setDisplayMode(new DisplayMode(dispW, dispH));
		Display.setFullscreen(false);
		//Display.setResizable(true);
		Display.create();
		
		glViewport(0, 0, dispW, dispH);
	}
	
	// This is where the magic begins
	public static void main(String[] args)
	{
		Main a = new Main();
		a.run();
	}
}