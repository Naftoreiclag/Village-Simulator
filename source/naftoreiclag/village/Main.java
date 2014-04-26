/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import naftoreiclag.village.rendering.camera.DebugCamera;
import naftoreiclag.village.rendering.renderer.OverworldRenderer;

import org.lwjgl.opengl.Display;

// I named this class "Main" just so java newbies can find the
// main method faster! Aren't I so nice? :)

public class Main
{
	MapData map;
	DebugCamera camera;
	OverworldRenderer renderer;
	
	public void run()
	{
		map = new MapData();
		map.loadDataFromFile("foo");
		
		int width = 640;
		int height = 480;
		
		camera = new DebugCamera(90, ((float) width) / ((float) height), 0.1f, 1000f);
		renderer = new OverworldRenderer(camera, width, height, map);

		renderer.setup();
		
		while(!Display.isCloseRequested())
		{
			camera.handleUserInput();
			renderer.render();
		}

		renderer.cleanup();
		
		System.exit(0);
	}

	// This is where the magic begins
	public static void main(String[] args)
	{
		Main m = new Main();
		m.run();
	}
}