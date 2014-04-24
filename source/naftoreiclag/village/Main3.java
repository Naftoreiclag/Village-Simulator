/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.LWJGLException;
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
public class Main3
{
	MapData map;
	
	DebugCam cam;
	
	Texture debug = null;
	Texture texture = null;
	
	private void run()
	{
		setupLWJGLDisplay();
		setupOpenGL();
		loadTextures();
		
		uploadVBOData();
		
		setupLights();
		setupCamera();
		//setupAction();
		
		while(!Display.isCloseRequested())
		{
			input();
			render();
		}

		cleanup();
		
		System.exit(0);
	}
	
	private void input()
	{
		cam.handleUserInput();
	}

	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		
		glBegin(GL_TRIANGLES);
		glEnd();
		
		renderSphere(0.0f, -10.0f, 0.0f, 1.0f);

		Display.update();
		Display.sync(60);
	}

	private void loadTextures()
	{
		debug = loadImage("resources/debug.png");
		texture = loadImage("donotinclude/eeeeeeenicegrassscaled.png");
	}

	private void setupLWJGLDisplay()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
			
			Display.destroy();
			System.exit(1);
		}
	}

	private void setupOpenGL()
	{
		// Enable projection
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		// Enable something else
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		// Enable three-dee
		glEnable(GL_DEPTH_TEST);
		
		// Enable textures
		glEnable(GL_TEXTURE_2D);
		
		//
		glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		
		//
		glViewport(0, 0, 640, 480);
	}

	private void uploadVBOData()
	{
		map = new MapData();
		map.loadDataFromFile("foobar");
	}

	private void setupLights()
	{
	}

	private void setupCamera()
	{
		cam = new DebugCam(90, 640f / 480f, 0.1f, 1000f);
		cam.doLWJGLStuff();
		cam.doOpenGLStuff();
	}

	private void cleanup()
	{
		Display.destroy();
	}
	
	private void renderSphere(float x, float y, float z, float radius)
	{
		glPushMatrix();
		glTranslatef(x, y, z);
		Sphere s = new Sphere();
		s.draw(radius, 16, 16);
		glPopMatrix();
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

	public static void main(String[] args)
	{
		Main3 m = new Main3();
		m.run();
	}
}
