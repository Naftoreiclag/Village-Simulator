/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

public abstract class GameState
{
	// Like run() in Runnable, but returns a new GameState for the Main class to run (to prevent a stack overflow error)
	public abstract GameState run();
}
