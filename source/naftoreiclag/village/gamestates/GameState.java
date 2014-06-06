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
		
		GameState returnVal;
		while(true)
		{
			returnVal = this.simpleTick(0);
			
			if(Display.isCloseRequested())
			{
				returnVal = this.getNewShutdownGameState();
			}
			
			if(returnVal != null)
			{
				this.simpleCleanup();
				return returnVal;
			}
		}
	}
	
	// Subclasses can override what to return when close is requested
	protected GameState getNewShutdownGameState()
	{
		return new GameStateShutdown();
	}
	
	/* NOTE: Methods overridden with the prefix "simple" implies that there is some underlying handling of these, 
	 *       and that you aren't overriding any actual handing.
	 */

	// Override these methods
	protected abstract GameState simpleTick(int delta);
	protected abstract void simpleSetup();
	protected abstract void simpleCleanup();
}
