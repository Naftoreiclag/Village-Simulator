/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

public class GameStateOverworld extends GameState
{
	@Override
	protected GameState simpleTick(int delta)
	{
		return new GameStateShutdown();
	}

	@Override
	protected void simpleSetup()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void simpleCleanup()
	{
		// TODO Auto-generated method stub
		
	}
}
