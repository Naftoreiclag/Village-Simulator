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
	@Override
	public GameState run()
	{
		return null;
	}

	// Dumb specialties. Can't Java just implicitly add "return null" for unoverrided methods!?
	// IN HINDSIGHT: I'm dumb.
	
	@Override
	protected GameState simpleTick(int _) { return null; }
	@Override
	protected void simpleSetup() { }
	@Override
	protected void simpleCleanup() { }
}
