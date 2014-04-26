/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;

import java.nio.FloatBuffer;

import naftoreiclag.village.rendering.camera.DebugCamera;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.*;

public class OverworldRenderer extends Renderer
{
	public OverworldRenderer()
	{
		super(new DebugCamera(90, 640f / 480f, 0.1f, 1000f));
	}
	
	@Override
	public void setup()
	{
		setupLWJGLDisplay();
		setupOpenGL();
		
	    setupLights();
	    setupCamera();
	}

	@Override
	public void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		
		glPushMatrix();
	
			camera.applyMatrix();
	
			// If the W value is zero, it is like sunlight. Otherwise, it is lamplike
		    glLight(GL_LIGHT0, GL_POSITION, floatBuffy(1.0f, 2.5f, 0.3f, 0.0f));
		
		glPopMatrix();
	
		Display.update();
		Display.sync(60);
	}

	@Override
	public void cleanup()
	{
		// We are no longer using VBOs
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	
		// Blow up display (Destroy it!)
		Display.destroy();
	}

	private void setupLWJGLDisplay()
	{
		int width = 640;
		int height = 480;
		
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setFullscreen(false);
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
			
			Display.destroy();
			System.exit(1);
		}
		
		glViewport(0, 0, width, height);
	}

	private void setupOpenGL()
	{
		// Enable something else
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		// Enable three-dee
		glEnable(GL_DEPTH_TEST);
		
		// Enable textures
		glEnable(GL_TEXTURE_2D);
		
		//
		glClearColor(93f / 255f, 155f / 255f, 217 / 255f, 0.0f);
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// Enable vertex buffer objects
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
	}

	private void setupLights()
	{
		glEnable(GL_LIGHTING);
	    glEnable(GL_LIGHT0);
	    
	    glEnable(GL_COLOR_MATERIAL);
	    glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
	    
	    glLight(GL_LIGHT0, GL_DIFFUSE, floatBuffy(0.4f, 0.4f, 0.4f, 1.0f));
	    glLight(GL_LIGHT0, GL_AMBIENT, floatBuffy(0.2f, 0.2f, 0.2f, 1.0f));
	    glLight(GL_LIGHT0, GL_SPECULAR, floatBuffy(0.0f, 0.0f, 0.0f, 1.0f));
	    
	}

	private void setupCamera()
	{
		camera.doLWJGLStuff();
		camera.doOpenGLStuff();
	}

	private FloatBuffer floatBuffy(float ... data)
	{
		FloatBuffer f = BufferUtils.createFloatBuffer(data.length);
		f.put(data);
		f.flip();
		return f;
	}
}