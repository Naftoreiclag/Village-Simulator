/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.util.glu.GLU.*;

//I named this class "Main" just so java newbies can find the
//main method faster! Aren't I so nice? :)

public class Main
{
	int dispW = 640;
	int dispH = 480;
	
	int geomHand;
	int indexHand;
	
	MapData map;
	
	CameraTest cam;
	
	Texture texture = null;
	
	public void run()
	{
		try
		{
			init();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		// Enable projection
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		// Enable something else
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		// Lookie lookie
		gluPerspective(60f, 640f / 480f, 0.01f, 200);
		// Params: eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz
		gluLookAt(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		
		cam = new CameraTest();
		
		// Enable three-dee
		glEnable(GL_DEPTH_TEST);
		
		// Enable textures
		glEnable(GL_TEXTURE_2D);

		// Enable vertex buffer objects
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		// Reserve spots for the data
		geomHand = glGenBuffers();
		indexHand = glGenBuffers();
		
		// Upload data to GPU
		sendData();
		
		// Load textures
		try
		{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("resources/debug.png")));
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		Mouse.setGrabbed(true);
		
		while(!Display.isCloseRequested())
		{
			int dx = Mouse.getDX();
			int dy = Mouse.getDY();
			
			cam.yaw += dx;
			cam.pitch += dy;
			
			// Clear the screen ===
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			
			cam.look();
			
			// Draw data
			drawData();

			Display.update();

			Display.sync(60);
		}

		// We are no longer using VBOs
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		
		// Free up memory that we used
		glDeleteBuffers(geomHand);
		glDeleteBuffers(indexHand);

		// Blow up display (Destroy it!)
		Display.destroy();
	}

	public void sendData()
	{
		
		
		// Geometry
		FloatBuffer geom = BufferUtils.createFloatBuffer(20);
		geom.put(-0.5f).put(-0.5f).put(-0.5f).put(0.0f).put(1.0f);
		geom.put(+0.5f).put(-0.5f).put(-0.5f).put(1.0f).put(1.0f);
		geom.put(+0.5f).put(+0.5f).put(-0.5f).put(1.0f).put(0.0f);
		geom.put(-0.5f).put(+0.5f).put(-0.5f).put(0.0f).put(0.0f);
		geom.flip();

		// Indices
		IntBuffer indices = BufferUtils.createIntBuffer(6);
		indices.put(0);
		indices.put(1);
		indices.put(2);
		indices.put(0);
		indices.put(2);
		indices.put(3);
		indices.flip();

		// Vertex Sending ======
		glBindBuffer(GL_ARRAY_BUFFER, geomHand); // Select this spot as an array buffer
		glBufferData(GL_ARRAY_BUFFER, geom, GL_STATIC_DRAW); // Send data
		
		/*
		 *  Note: The difference between ELEMENT_ARRAY and ARRAY is that 
		 *  ELEMENT_ARRAY is used to denote an array full of "pointers" to 
		 *  another array.
		 */
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHand); // Select this spot as an array buffer
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); // Send data
		
		
		
		/*
		// Map data
		map = new MapData();
		
		map.loadDataFromFile("foobar");
		

		glBindBuffer(GL_ARRAY_BUFFER, geomHand); // Select this spot as an array buffer
		glBufferData(GL_ARRAY_BUFFER, map.convertToGeometry(), GL_STATIC_DRAW); // Send data
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHand); // Select this spot as an array buffer
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, map.convertToIndices(), GL_STATIC_DRAW); // Send data
	
		*/
	}

	private void drawData()
	{
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		
		glPushMatrix();
		
		glBindBuffer(GL_ARRAY_BUFFER, geomHand);
		glVertexPointer(3, GL_FLOAT, 5 << 2, 0 << 2);
		glTexCoordPointer(2, GL_FLOAT, 5 << 2, 3 << 2);
		
		//31 * 31 * 
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0L);
		
		glPopMatrix();
	}

	public void init() throws LWJGLException
	{
		Display.setDisplayMode(new DisplayMode(dispW, dispH));
		Display.setFullscreen(false);
		Display.create();
		
		glViewport(0, 0, dispW, dispH);
	}
	
	// This is where the magic begins
	public static void main(String[] args)
	{
		Main m = new Main();
		m.run();
	}
}