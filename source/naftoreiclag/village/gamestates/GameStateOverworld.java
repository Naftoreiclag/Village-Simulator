/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

import naftoreiclag.village.Player;
import naftoreiclag.village.UserSettings;
import naftoreiclag.village.environment.Hills;
import naftoreiclag.village.rendering.camera.DebugCamera;
import naftoreiclag.village.rendering.camera.PlayerCamera;
import naftoreiclag.village.rendering.renderer.OverworldRenderer;

public class GameStateOverworld extends GameState
{
	public GameStateOverworld()
	{
		super(50);
	}
	
	Hills map;
	PlayerCamera camera;
	DebugCamera camera2;
	OverworldRenderer renderer;
	Player player;
	
	@Override
	protected GameState simpleStep(long delta)
	{
		player.input();
		camera.updatePositionToPlayer();
		camera2.handleUserInput();
		renderer.render();
		
		return null;
	}

	@Override
	protected void simpleSetup()
	{
		map = new Hills();
		map.loadDataFromFile("foo");
		
		camera = new PlayerCamera(90, ((float) UserSettings.width) / ((float) UserSettings.height), 0.1f, 1000f);
		camera2 = new DebugCamera(90, ((float) UserSettings.width) / ((float) UserSettings.height), 0.1f, 1000f);
		player = new Player();
		player.map = map;
		camera.setPlayer(player);
		renderer = new OverworldRenderer(camera, UserSettings.width, UserSettings.height, map, player);

		renderer.setup();
		
	}

	@Override
	protected void simpleCleanup()
	{
		renderer.cleanup();
	}
}
