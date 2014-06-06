/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import static org.lwjgl.opengl.GL11.glViewport;
import naftoreiclag.village.environment.Hills;
import naftoreiclag.village.gamestates.GameState;
import naftoreiclag.village.gamestates.GameStateTitleScreen;
import naftoreiclag.village.rendering.camera.DebugCamera;
import naftoreiclag.village.rendering.camera.PlayerCamera;
import naftoreiclag.village.rendering.renderer.OverworldRenderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

// I named this class "Main" just so java newbies can find the
// main method faster! Amn't I so nice? :)

public class Main implements Runnable
{
	Hills map;
	PlayerCamera camera;
	DebugCamera camera2;
	OverworldRenderer renderer;
	Player player;
	
	int width = 640;
	int height = 480;
	
	@Override
	public void run()
	{
		setupStaticDisplay();
		
		GameState game = new GameStateTitleScreen();
		
		boolean running = true;
		while(running)
		{
			game = game.run();
			
			if(game == null)
			{
				running = false;
			}
		}
		
		System.out.println(Runtime.getRuntime().availableProcessors());
		
		map = new Hills();
		map.loadDataFromFile("foo");
		
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
	
		// Blow up display (Destroy it!)
		Display.destroy();
		
		System.exit(0);
	}

	private void setupStaticDisplay()
	{
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

	// This is where the magic begins
	public static void main(String[] args)
	{
		Main main = new Main();
		main.run();
	}
}