/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;

import naftoreiclag.village.MapData;
import naftoreiclag.village.rendering.TextureLib;
import naftoreiclag.village.rendering.camera.Camera;
import naftoreiclag.village.rendering.model.WackyModel;
import naftoreiclag.village.rendering.util.TBuffy;
import naftoreiclag.village.rendering.util.ObjLoader;
import static org.lwjgl.opengl.GL11.*;

public class OverworldRenderer extends CommonRenderer
{
	MapData map;
	
	WackyModel torus;
	
	public OverworldRenderer(Camera camera, int width, int height, MapData map)
	{
		super(camera, width, height);
		
		this.map = map;
	}

	@Override
	protected void simpleSetup()
	{
		loadTextures();
		
		// Map data
		map = new MapData();
		
		map.loadDataFromFile("foobar");
		map.makeModelFancy();
		map.grass.setTexture(TextureLib.getTextureFromName("camograss"));
		map.rock.setTexture(TextureLib.getTextureFromName("oilgranite"));
		map.sidegrass.setTexture(TextureLib.getTextureFromName("camograss_side"));
		map.tallgrass.setTexture(TextureLib.getTextureFromName("camograss_tall"));
	
		map.grass.upload();
		map.rock.upload();
		map.sidegrass.upload();
		map.tallgrass.upload();
		
		// OBJ loading test
		
		loadOBJ();
	}

	private void loadOBJ()
	{
		torus = ObjLoader.loadObj("resources/torus.obj", "resources/debug.png");
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

	    torus.render();
	    
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