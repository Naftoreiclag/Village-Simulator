/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.util.logging.Level;
import java.util.logging.Logger;

import naftoreiclag.village.gamestates.GameState;
import naftoreiclag.village.gamestates.GameStateTestGame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

// I named this class "Main" just so java newbies can find the
// main method faster! Amn't I so nice? :)

public class Main implements Runnable
{
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	
	@Override
	public void run()
	{
		setupStaticDisplay();
		
		GameState state = new GameStateTestGame();
		
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
			Display.setDisplayMode(new DisplayMode(UserSettings.width, UserSettings.height));
			Display.setFullscreen(false);
			Display.setVSyncEnabled(true);
			Display.create();
			logger.log(Level.INFO, "Static display successfully set up.");
		}
		catch(LWJGLException e)
		{
			logger.log(Level.SEVERE, "Static display could not be set up.");
			logger.log(Level.SEVERE, e.toString(), e);

			logger.log(Level.SEVERE, "System will now forcibly exit.");
			Display.destroy();
			System.exit(1);
		}
	}
	
	private void cleanupStaticDisplay()
	{
		Display.destroy();
		logger.log(Level.INFO, "Static display cleaned up.");
	}

	// This is where the magic begins
	public static void main(String[] args)
	{
		Main main = new Main();
		main.run();
	}
}