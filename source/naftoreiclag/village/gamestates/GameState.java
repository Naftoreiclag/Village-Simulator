/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

public abstract class GameState
{
	/* Like run() in Runnable, but returns a new GameState for the Main class to run (to prevent a stack overflow error)
	 * Most subclasses should not override this; they should override tick instead.
	 */
	public GameState run()
	{
		GameState returnVal;
		while(true)
		{
			returnVal = this.tick(0);
			
			if(returnVal != null)
			{
				return returnVal;
			}
		}
	}
	
	// Override this method for logic-based states
	protected abstract GameState tick(int delta);
}
