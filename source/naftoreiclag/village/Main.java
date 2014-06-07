/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import naftoreiclag.village.gamestates.GameState;
import naftoreiclag.village.gamestates.GameStateMechanicPlayground;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

// I named this class "Main" just so java newbies can find the
// main method faster! Amn't I so nice? :)

public class Main implements Runnable
{
	int width = 640;
	int height = 480;
	
	@Override
	public void run()
	{
		setupStaticDisplay();
		
		GameState state = new GameStateMechanicPlayground();
		
		boolean running = true;
		while(running)
		{
			state = state.run();
			
			if(state == null)
			{
				running = false;
			}
		}
	
		cleanupStaticDisplay();
		
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
	}
	
	private void cleanupStaticDisplay()
	{
		Display.destroy();
	}

	// This is where the magic begins
	public static void main(String[] args)
	{
		Main main = new Main();
		main.run();
	}
}