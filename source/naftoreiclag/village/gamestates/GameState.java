/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

import org.lwjgl.opengl.Display;

public abstract class GameState
{
	/* Like run() in Runnable, but returns a new GameState for the Main class to run (to prevent a stack overflow error)
	 * Most subclasses should not override this; they should override tick instead.
	 */
	public GameState run()
	{
		// Setup
		this.simpleSetup();
		
		// Init delta tracking
		getDelta();
		
		// Begin ticking and handle return value
		GameState returnVal;
		while(true)
		{
			// Get the delta
			long delta = getDelta();
			
			// However, do not execute a step too quickly
			if(delta < 1)
			{
				continue;
			}
			
			// Perform tick and get return value
			returnVal = this.simpleStep();
			
			// However, we must check for some special escape cases.
			
			// If user is trying to close the program via the "x" on the window or other system means,
			if(Display.isCloseRequested())
			{
				// Return a shutdown.
				returnVal = this.getNewShutdownGameState();
			}
			
			// If something was non-null was returned, return it.
			// Note: in order to return null to close program, return instance of GameStateShutdown from simpleTick()
			if(returnVal != null)
			{
				// Cleanup
				this.simpleCleanup();
				
				// Goto the next state
				return returnVal;
			}
		}
	}
	
	// Subclasses can override what to return when close is requested
	protected GameState getNewShutdownGameState()
	{
		return new GameStateShutdown();
	}

	public static class DeltaStopwatch
	{
		// Get the time in milliseconds
		public static long getTime()
		{
			// Get the time, then convert it to milliseconds, then return it
			return System.nanoTime() / 1000000;
		}
		
		// Keep track of whenever the timer was last reset
		private long timeWhenTimerBegan;
		
		// Reset the time
		public void reset()
		{
			timeWhenTimerBegan = getTime();
		}
		
		// Time elapsed since getDelta() was last called (No reset, useful for waiting until delta will exceed a minimum value.)
		public long duration()
		{
			long time = getTime();
			long delta = time - timeWhenTimerBegan;
			return delta;
		}

		// Time elapsed since getDelta() was last called (It resets the timer)
		public long getDelta()
		{
			long time = getTime();
			long delta = time - timeWhenTimerBegan;
			timeWhenTimerBegan = time;
			return delta;
		}
	}

	/* NOTE: Methods overridden with the prefix "simple" imply that there is some underlying handling of these, 
	 *       and therefore you aren't overriding any actual handing or "under the hood" stuff.
	 */

	// Override these methods
	protected abstract GameState simpleStep(long delta);
	protected abstract void simpleSetup();
	protected abstract void simpleCleanup();
}
