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

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.Sphere;
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
	
	Texture debug = null;
	Texture texture = null;
	
	float roaty = 0f;
	
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
		
		cam = new CameraTest();
		
		// Enable three-dee
		glEnable(GL_DEPTH_TEST);
		
		// Enable textures
		glEnable(GL_TEXTURE_2D);
	    
	    doLightSetup();

		// Enable vertex buffer objects
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		// Reserve spots for the data
		geomHand = glGenBuffers();
		indexHand = glGenBuffers();
		
		// Upload data to GPU
		sendData();
		
		// Ugly test shading
		glShadeModel(GL_FLAT);

		// Load textures
		debug = loadImage("resources/debug.png");
		texture = loadImage("donotinclude/eeeeeeenicegrassscaled.png");

		Mouse.setGrabbed(true);
		
		//
		glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		
		while(!Display.isCloseRequested())
		{
			roaty += 0.2f;
			
			// Camera stuff ===

			if(Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				cam.goLeft();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				cam.goRight();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				cam.goForward();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S))
			{
				cam.goBack();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			{
				cam.goUp();
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				cam.goDown();
			}

			int dx = Mouse.getDX();
			int dy = Mouse.getDY();
			
			cam.yaw += dx * 0.05f;
			cam.pitch -= dy * 0.05f;
			
			// Clear the screen ===
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

			
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
	
	public void doLightSetup()
	{
		glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
        
        glLight(GL_LIGHT0, GL_DIFFUSE, floatBuffy(0.6f, 0.6f, 0.6f, 1.0f));
        glLight(GL_LIGHT0, GL_AMBIENT, floatBuffy(0.0f, 0.0f, 0.0f, 1.0f));
        glLight(GL_LIGHT0, GL_SPECULAR, floatBuffy(0.0f, 0.0f, 0.0f, 1.0f));
        
	}
	
	public FloatBuffer floatBuffy(float ... data)
	{
		FloatBuffer f = BufferUtils.createFloatBuffer(data.length);
		f.put(data);
		f.flip();
		return f;
	}

	public void sendData()
	{
		// Map data
		map = new MapData();
		
		map.loadDataFromFile("foobar");

		glBindBuffer(GL_ARRAY_BUFFER, geomHand); // Select this spot as an array buffer
		glBufferData(GL_ARRAY_BUFFER, map.convertToGeometry(), GL_STATIC_DRAW); // Send data
		
		/*
		 *  Note: The difference between ELEMENT_ARRAY and ARRAY is that 
		 *  ELEMENT_ARRAY is used to denote an array full of "pointers" to 
		 *  another array.
		 */
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexHand); // Select this spot as an array buffer
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, map.convertToIndices(), GL_STATIC_DRAW); // Send data
	
		
	}
	
	private Texture loadImage(String path)
	{
		Texture texture = debug;
		
		// Load textures
		try
		{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(path)));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return texture;
	}

	private void drawData()
	{
		glPushMatrix();
		cam.look();

	    glLight(GL_LIGHT0, GL_POSITION, floatBuffy(0.0f, 1.0f, 0.0f, 0.0f));
		// If the W value is zero, it is like sunlight. Otherwise, it is lamplike
		float lolx = 5.0f;
		float loly = 5.0f;
		float lolz = 5.0f;
		
		renderSphere(lolx, loly + 2.0f, lolz, 1.0f);
		renderAxes(0.0f, 0.0f, 0.0f);

		glPushMatrix();
			//glRotatef(roaty, 0.0f, 1.0f, 0.0f);
		
			glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
			glBindBuffer(GL_ARRAY_BUFFER, geomHand);
			glVertexPointer(3, GL_FLOAT, 8 << 2, 0 << 2);
			glNormalPointer(GL_FLOAT, 8 << 2, 3 << 2);
			glTexCoordPointer(2, GL_FLOAT, 8 << 2, 6 << 2);
			
			// 31 wide, 31 tall, 2 triangles each, 3 points per triangle
			glDrawElements(GL_TRIANGLES, 31 * 31 * 6, GL_UNSIGNED_INT, 0L);
		glPopMatrix();
		
		renderSphere(1.0f, 10.0f, 1.0f, 1.0f);

		glPopMatrix();
	}
	
	private void renderAxes(float x, float y, float z)
	{
		glPushMatrix();
		glTranslatef(x, y, z);

		glBindTexture(GL_TEXTURE_2D, 0);
		
		float thic = 0.5f;
		
		glColor3f(1.0f, 0.0f, 0.0f);
		for(float ox = 0.0f; ox < 10.0f; ox += 1.0f)
		{
			renderSphere(x + ox, y, z, thic);
		}
		glColor3f(0.0f, 1.0f, 0.0f);
		for(float oy = 0.0f; oy < 10.0f; oy += 1.0f)
		{
			renderSphere(x, y + oy, z, thic);
		}
		glColor3f(0.0f, 0.0f, 1.0f);
		for(float oz = 0.0f; oz < 10.0f; oz += 1.0f)
		{
			renderSphere(x, y, z + oz, thic);
		}
		glColor3f(1.0f, 1.0f, 1.0f);
		
		
		glPopMatrix();
	}
	

	private void renderSphere(float x, float y, float z, float radius)
	{
		glPushMatrix();
		glTranslatef(x, y, z);
		Sphere s = new Sphere();
		s.draw(radius, 16, 16);
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