/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;

import naftoreiclag.village.rendering.camera.Camera;

public abstract class Renderer
{
	protected int width;
	protected int height;
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public Camera camera;
	
	public Renderer(Camera camera, int width, int height)
	{
		this.width = width;
		this.height = height;
		
		this.camera = camera;
	}
	
	public abstract void setup();
	
	public abstract void render();
	
	public abstract void cleanup();
}