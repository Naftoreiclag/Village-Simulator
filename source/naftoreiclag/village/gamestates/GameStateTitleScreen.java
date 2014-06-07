/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

public class GameStateTitleScreen extends GameState
{
	@Override
	protected GameState simpleStep(long delta)
	{
		return new GameStateOverworld();
	}

	@Override
	protected void simpleSetup()
	{
	}

	@Override
	protected void simpleCleanup()
	{
	}
}
