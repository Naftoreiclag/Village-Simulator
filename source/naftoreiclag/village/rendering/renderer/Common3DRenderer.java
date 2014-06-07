/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;


import naftoreiclag.village.rendering.TextureLib;
import naftoreiclag.village.rendering.camera.Camera;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public abstract class Common3DRenderer extends Renderer
{
	public Common3DRenderer(Camera camera, int width, int height)
	{
		super(camera, width, height);
	}
	
	@Override
	public void setup()
	{
		setupOpenGL();
		
		TextureLib.loadDebugTexture();
		
	    setupLights();
	    camera.setup();
	    
	    simpleSetup();
	}

	@Override
	public void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		
		glPushMatrix();
	
			camera.applyMatrix();
			simpleRender();
		
		glPopMatrix();
	
		Display.update();
		Display.sync(60);
	}

	@Override
	public void cleanup()
	{
		cleanupOpenGL();
	}

	protected abstract void simpleSetup();
	
	protected abstract void simpleRender();

	protected void setupOpenGL()
	{
		// Viewport
		glViewport(0, 0, width, height);
		
		// Enable something else
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		// Enable three-dee
		glEnable(GL_DEPTH_TEST);
		
		// Enable textures
		glEnable(GL_TEXTURE_2D);
		
		// Blue sky
		glClearColor(93f / 255f, 155f / 255f, 217 / 255f, 0.0f);
		
		// Enable culling
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		// Allow alpha texturing (Does not enable it yet)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// Enable vertex buffer objects
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
	}
	
	protected void cleanupOpenGL()
	{
		// We are no longer using VBOs
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		
		// Disable textures
		glDisable(GL_TEXTURE_2D);
		
		// Disable three-dee
		glDisable(GL_DEPTH_TEST);
	}

	protected abstract void setupLights();
}
