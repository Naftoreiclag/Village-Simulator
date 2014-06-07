/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

/* This class is to disambiguate between returning null to force-quit and returning null to quit safely.
 * It is a special return value for normal GameStates to indicate that the program should shut down.
 */

public class GameStateShutdown extends GameState
{
	public GameStateShutdown()
	{
		super(50);
	}
	
	@Override
	public GameState run()
	{
		return null;
	}
	
	@Override
	protected GameState simpleStep(long _) { return null; }
	@Override
	protected void simpleSetup() { }
	@Override
	protected void simpleCleanup() { }
}
