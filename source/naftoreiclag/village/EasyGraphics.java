/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public abstract class EasyGraphics
{
	// Initializer
	protected void initialize(int dispW, int dispH) throws LWJGLException
	{
		// Make display
		Display.setDisplayMode(new DisplayMode(dispW, dispH));
		Display.setResizable(true);
		Display.create();
		
		// Set up OpenGL view port
		syncViewportAndDisplaySizes();
	}
	
	// Send the stuff to GPU and put on display
	protected void updateDisplay()
	{
		// If the view port is resized
		if (Display.wasResized())
		{
			// Re-sync
			syncViewportAndDisplaySizes();
		}
		
		//
		clearGPU();
		
		// Draw stuff
		sendStuffToGPU();
		
		// Update the display
		Display.update();
		
		// Make sure we go at 60 FPS
		Display.sync(60);
	}
	
	//
	private void clearGPU()
	{
    	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	//
	abstract void sendStuffToGPU();

	// Cleanup
	protected void cleanup()
	{
		// Delete the display
		Display.destroy();
	}

	// Resize the display
	public void resizeDisplay(int dispW, int dispH) throws LWJGLException
	{
		Display.setDisplayMode(new DisplayMode(dispW, dispH));
	}
	
	//
	public int getDisplayWidth()
	{
		return Display.getWidth();
	}
	
	//
	public int getDisplayHeight()
	{
		return Display.getHeight();
	}

	// Sync stuff
	private void syncViewportAndDisplaySizes()
	{
		// Get the target size
		final int dispW = Display.getWidth();
		final int dispH = Display.getHeight();
		
		// Set image output size
		glViewport(0, 0, dispW, dispH);
		glLoadIdentity();
		
		// Reset projection matrix
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		// Set the coordinates of the display
		glOrtho(0.0f, dispW, dispH, 0.0f, 1.0f, -1.0f);
		
		// Reset model view
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
}
