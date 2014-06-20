/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.gamestates;

import static org.lwjgl.opengl.GL11.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import naftoreiclag.village.Player;
import naftoreiclag.village.UserSettings;
import naftoreiclag.village.collision.Circle;
import naftoreiclag.village.collision.Line;
import naftoreiclag.village.collision.Space;
import naftoreiclag.village.environment.Hills;
import naftoreiclag.village.environment.Room;
import naftoreiclag.village.rendering.TextureLib;
import naftoreiclag.village.rendering.camera.Camera2D;
import naftoreiclag.village.rendering.model.InterleavedSprite;
import naftoreiclag.village.rendering.model.Model;
import naftoreiclag.village.rendering.renderer.Renderer2D;
import naftoreiclag.village.rendering.util.PolygonGen;
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

		Circle test;
		Model circleTest;
		Model lineTest;
		
		Model playerCollision;

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
			
			circleTest = PolygonGen.makeCircle(30, 26, 16);
			circleTest.setTexture(TextureLib.getDebugTexture());
			circleTest.upload();
			
			lineTest = PolygonGen.makeVector(50, 100, 2);
			lineTest.setTexture(TextureLib.getDebugTexture());
			lineTest.upload();
			
			playerCollision = PolygonGen.makeCircle(25, 21, 16);
			playerCollision.setTexture(TextureLib.getDebugTexture());
			playerCollision.upload();
		}

		@Override
		protected void simpleRender()
		{
			glPushMatrix();
				glTranslatef((float) test.loc.a, (float) test.loc.b, 0.0f);
				circleTest.render();
			glPopMatrix();
			
			glPushMatrix();
				glTranslatef(50.0f, 0.0f, 0.0f);
				lineTest.render();
			glPopMatrix();
			
			glEnable(GL_BLEND);
			glPushMatrix();
	    		glTranslatef((float) player.collision.loc.a, (float) player.collision.loc.b, 0.0f);
				playerModel.render();
				playerCollision.render();
			glPopMatrix();
			glDisable(GL_BLEND);
		}

		@Override
		protected void simpleCleanup()
		{
			playerModel.cleanup();
			circleTest.cleanup();
			lineTest.cleanup();
			playerCollision.cleanup();
		}
	}
	
	Space space;
	
	Player player;
	Circle test;
	
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
		space.lines.add(new Line(50, 0, 100, 100));
		test = new Circle(100, 100, 30, -1, -1);
		space.circles.add(test);
		
		renderer.setup();
		renderer.player = player;
		renderer.test = test;
		
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	}

	@Override
	protected void simpleCleanup()
	{
		renderer.cleanup();
	}
}
