/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;

import static org.lwjgl.opengl.GL11.glClearColor;
import naftoreiclag.village.rendering.camera.Camera;

// I just love saying "Renderer", don't you? /sarcasm

public abstract class Renderer
{
	protected int width;
	protected int height;
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	/* Note: The constructor requires a camera, even though it is not stored
	 *       to ensure that all subclasses accept a camera in its constructor.
	 */
	
	public Renderer(Camera camera, int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public abstract void setup();
	public abstract void render();
	public abstract void cleanup();
}