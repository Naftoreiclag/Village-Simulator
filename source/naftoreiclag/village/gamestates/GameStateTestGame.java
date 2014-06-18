/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.logging.Level;
import java.util.logging.Logger;

import naftoreiclag.village.Player;
import naftoreiclag.village.UserSettings;
import naftoreiclag.village.collision.Line;
import naftoreiclag.village.collision.Space;
import naftoreiclag.village.environment.Hills;
import naftoreiclag.village.environment.Room;
import naftoreiclag.village.rendering.TextureLib;
import naftoreiclag.village.rendering.camera.Camera2D;
import naftoreiclag.village.rendering.model.InterleavedSprite;
import naftoreiclag.village.rendering.model.Model;
import naftoreiclag.village.rendering.renderer.Renderer2D;
import naftoreiclag.village.rendering.util.TBuffy;

// To stop getting a headache over 3D stuff

public class GameStateTestGame extends GameState
{
	private static final Logger logger = Logger.getLogger(GameStateTestGame.class.getName());
	
	public static class TestRenderer extends Renderer2D
	{
		public TestRenderer(Camera2D camera, int width, int height)
		{
			super(camera, width, height);
		}
		
		Model playerModel;
		Player player;

		@Override
		protected void simpleSetup()
		{
			TextureLib.loadTexture("sticky");
			
			playerModel = new InterleavedSprite(
					TBuffy.floaty(
							-25f, -25f, 0f, 0f, 
							 25f, -25f, 1f, 0f, 
							 25f,  25f, 1f, 1f, 
							-25f,  25f, 0f, 1f),
					TBuffy.inty(0, 2, 1, 0, 3, 2),
					6);
			playerModel.setTexture(TextureLib.getTextureFromName("sticky"));
			
			playerModel.upload();
		}

		@Override
		protected void simpleRender()
		{
			glEnable(GL_BLEND);
    		glTranslatef((float) player.collision.loc.a, (float) player.collision.loc.b, 0.0f);
			playerModel.render();
			glDisable(GL_BLEND);
		}

		@Override
		protected void simpleCleanup()
		{
			playerModel.cleanup();
		}
	}
	
	Space space;
	
	Player player;
	
	TestRenderer renderer;
	Camera2D camera;
	
	public GameStateTestGame()
	{
		super(0);
	}

	@Override
	protected GameState simpleStep(long delta)
	{
		logger.log(Level.INFO, "Crude FPS:" + (1000 / (delta + 1)));
		player.input(delta);
		
		space.simulate(delta);
		renderer.render();
		return null;
	}

	@Override
	protected void simpleSetup()
	{
		camera = new Camera2D();
		renderer = new TestRenderer(camera, UserSettings.width, UserSettings.height);
		
		player = new Player();
		player.spd = 0.5f;
		
		space = new Space();
		space.circles.add(player.collision);
		space.lines.add(new Line(50, 0, 50, 100));
		
		renderer.setup();
		renderer.player = player;
		
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	}

	@Override
	protected void simpleCleanup()
	{
		renderer.cleanup();
	}
}
