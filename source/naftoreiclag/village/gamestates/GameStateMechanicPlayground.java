/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

import java.util.logging.Level;
import java.util.logging.Logger;

import naftoreiclag.village.UserSettings;
import naftoreiclag.village.rendering.TextureLib;
import naftoreiclag.village.rendering.camera.Camera2D;
import naftoreiclag.village.rendering.model.FlatInterleavedModel;
import naftoreiclag.village.rendering.model.Model;
import naftoreiclag.village.rendering.renderer.Renderer2D;
import naftoreiclag.village.rendering.util.TBuffy;

// To stop getting a headache over 3D stuff

public class GameStateMechanicPlayground extends GameState
{
	private static final Logger logger = Logger.getLogger(GameStateMechanicPlayground.class.getName());
	
	public static class TestRenderer extends Renderer2D
	{
		public TestRenderer(Camera2D camera, int width, int height)
		{
			super(camera, width, height);
		}
		
		Model test;

		@Override
		protected void simpleSetup()
		{
			test = new FlatInterleavedModel(
					TBuffy.floaty(
							  0f,   0f, 0f, 0f, 
							100f,   0f, 1f, 0f, 
							100f, 100f, 1f, 1f, 
							  0f, 100f, 0f, 1f),
					TBuffy.inty(0, 2, 1, 0, 3, 2),
					6);
			/*
			test = new InterleavedModel(
					TBuffy.floaty(
							0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 
							1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 
							1f, 1f, 0f, 0f, 0f, 0f, 1f, 1f, 
							0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f),
					TBuffy.inty(0, 2, 1, 0, 3, 2),
					6);
			*/
			test.setTexture(TextureLib.getDebugTexture());
			
			test.upload();
		}

		@Override
		protected void simpleRender()
		{
			test.render();
		}

		@Override
		protected void simpleCleanup()
		{
			test.cleanup();
		}
	}
	
	TestRenderer renderer;
	Camera2D camera;
	
	public GameStateMechanicPlayground()
	{
		super(50);
	}

	@Override
	protected GameState simpleStep(long delta)
	{
		logger.log(Level.INFO, "Crude FPS:" + (1000 / delta));
		renderer.render();
		return null;
	}

	@Override
	protected void simpleSetup()
	{
		camera = new Camera2D();
		renderer = new TestRenderer(camera, UserSettings.width, UserSettings.height);
		
		renderer.setup();
	}

	@Override
	protected void simpleCleanup()
	{
		renderer.cleanup();
	}
}
