/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;

import java.util.Random;

import naftoreiclag.village.Player;
import naftoreiclag.village.environment.Hills;
import naftoreiclag.village.rendering.TextureLib;
import naftoreiclag.village.rendering.camera.Camera;
import naftoreiclag.village.rendering.model.Model;
import naftoreiclag.village.rendering.util.TBuffy;
import naftoreiclag.village.rendering.util.ObjLoader;
import static org.lwjgl.opengl.GL11.*;

public class OverworldRenderer extends CommonRenderer
{
	Hills map;
	Player player;
	
	Model playerModel;
	
	Model trunk;
	Model leaves;
	
	private Tree[] trees = new Tree[10];
	
	public static class Tree
	{
		public final int x;
		public final int y;
		
		public Tree(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	public OverworldRenderer(Camera camera, int width, int height, Hills map, Player player)
	{
		super(camera, width, height);
		
		this.map = map;
		this.player = player;
	}

	@Override
	protected void simpleSetup()
	{
		loadTextures();
		
		// Map data
		map = new Hills();
		
		map.loadDataFromFile("foobar");
		map.makeModelFancy();
		map.grass.setTexture(TextureLib.getTextureFromName("camograss"));
		map.rock.setTexture(TextureLib.getTextureFromName("oilgranite"));
		map.sidegrass.setTexture(TextureLib.getTextureFromName("camograss_side"));
		map.tallgrass.setTexture(TextureLib.getTextureFromName("camograss_side"));
	
		map.grass.upload();
		map.rock.upload();
		map.sidegrass.upload();
		map.tallgrass.upload();
		
		// OBJ loading test
		
		loadOBJ();
		
		Random r = new Random();
		for(int i = 0; i < 10; ++ i)
		{
			trees[i] = new Tree(r.nextInt(64), r.nextInt(64));
		}
	}

	private void loadOBJ()
	{
		trunk = ObjLoader.loadObj("resources/noobtree.obj", "resources/noobtree_wood.png");
		leaves = ObjLoader.loadObj("resources/noobtree_leaves.obj", "resources/moss.png");
		playerModel = ObjLoader.loadObj("resources/player.obj", "resources/player.png");
	}

	protected void setupLights()
	{
		glEnable(GL_LIGHTING);
	    glEnable(GL_LIGHT0);
	    
	    glEnable(GL_COLOR_MATERIAL);
	    glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
	    
	    glLight(GL_LIGHT0, GL_DIFFUSE, TBuffy.floaty(0.4f, 0.4f, 0.4f, 1.0f));
	    glLight(GL_LIGHT0, GL_AMBIENT, TBuffy.floaty(0.2f, 0.2f, 0.2f, 1.0f));
	    glLight(GL_LIGHT0, GL_SPECULAR, TBuffy.floaty(0.0f, 0.0f, 0.0f, 1.0f));
	}

	@Override
	protected void simpleRender()
	{
		// Move light to right spot
		// If the W value is zero, it is like sunlight. Otherwise, it is lamplike
	    glLight(GL_LIGHT0, GL_POSITION, TBuffy.floaty(1.0f, 2.5f, 0.3f, 0.0f));

	    /*
	    for(Tree t : trees)
	    {
	    	glPushMatrix();
	    		GL11.glTranslatef(((float) t.x) * MapData.horzu, map.map[t.x][t.y] * MapData.vertu, ((float) t.y) * MapData.horzu);
			    trunk.render();
			    leaves.render();
	    	glPopMatrix();
	    }
	    */

    	glPushMatrix();
    		
    		glTranslatef(((float) player.x) * Hills.horzu, player.y, ((float) player.z) * Hills.horzu);
		    glRotatef(player.direction, 0.0f, 1.0f, 0.0f);
    		playerModel.render();
    	glPopMatrix();
	    
		map.rock.render();
		map.grass.render();

		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		map.sidegrass.render();
		map.tallgrass.render();
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		
		//torus.render();
	}

	private void loadTextures()
	{
		TextureLib.loadTexture("camograss");
		TextureLib.loadTexture("oilgranite");
		TextureLib.loadTexture("moss");
		TextureLib.loadTexture("camograss_side");
		TextureLib.loadTexture("camograss_tall");
	}
}