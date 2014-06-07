/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;

import naftoreiclag.village.rendering.TextureLib;
import naftoreiclag.village.rendering.camera.Camera;

import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public abstract class Common2DRenderer extends Renderer
{
	public Common2DRenderer(Camera camera, int width, int height)
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
	}
	
	protected void cleanupOpenGL()
	{
	}

	protected abstract void setupLights();
}