/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import naftoreiclag.village.environment.Hills;
import naftoreiclag.village.rendering.camera.DebugCamera;
import naftoreiclag.village.rendering.camera.PlayerCamera;
import naftoreiclag.village.rendering.renderer.OverworldRenderer;

import org.lwjgl.opengl.Display;

// I named this class "Main" just so java newbies can find the
// main method faster! Aren't I so nice? :)

public class Main implements Runnable
{
	Hills map;
	PlayerCamera camera;
	DebugCamera camera2;
	OverworldRenderer renderer;
	Player player;
	
	@Override
	public void run()
	{
		System.out.println(Runtime.getRuntime().availableProcessors());
		
		map = new Hills();
		map.loadDataFromFile("foo");
		
		int width = 640;
		int height = 480;
		
		camera = new PlayerCamera(90, ((float) width) / ((float) height), 0.1f, 1000f);
		camera2 = new DebugCamera(90, ((float) width) / ((float) height), 0.1f, 1000f);
		player = new Player();
		player.map = map;
		camera.setPlayer(player);
		renderer = new OverworldRenderer(camera, width, height, map, player);

		renderer.setup();
		
		while(!Display.isCloseRequested())
		{
			player.input();
			camera.updatePositionToPlayer();
			camera2.handleUserInput();
			renderer.render();
		}

		renderer.cleanup();
		
		System.exit(0);
	}

	// This is where the magic begins
	public static void main(String[] args)
	{
		Main main = new Main();
		main.run();
	}
}