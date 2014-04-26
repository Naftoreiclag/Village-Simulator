/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.renderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import naftoreiclag.village.MapData;
import naftoreiclag.village.rendering.camera.DebugCamera;
import naftoreiclag.village.rendering.util.TBuffy;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.*;

public class OverworldRenderer extends CommonRenderer
{
	MapData map;
	
	Texture tex_debug = null;
	Texture tex_grass = null;
	Texture tex_rock = null;
	Texture tex_moss = null;
	Texture tex_grass_side = null;
	Texture tex_grass_tall = null;
	Texture tex_sky = null;
	
	public OverworldRenderer(int width, int height, MapData map)
	{
		super(new DebugCamera(90, ((float) width) / ((float) height), 0.1f, 1000f), width, height);
		
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
		map.grass.setTexture(tex_grass.getTextureID());
		map.rock.setTexture(tex_rock.getTextureID());
		map.sidegrass.setTexture(tex_grass_side.getTextureID());
		map.tallgrass.setTexture(tex_grass_tall.getTextureID());
	
		map.grass.upload();
		map.rock.upload();
		map.sidegrass.upload();
		map.tallgrass.upload();
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
		// If the W value is zero, it is like sunlight. Otherwise, it is lamplike
	    glLight(GL_LIGHT0, GL_POSITION, TBuffy.floaty(1.0f, 2.5f, 0.3f, 0.0f));

		//glDisable(GL_LIGHTING);
		map.rock.render();
	    //glLight(GL_LIGHT0, GL_AMBIENT, floatBuffy(0.3f, 0.3f, 0.3f, 1.0f));
	    
		map.grass.render();

		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		map.sidegrass.render();
		map.tallgrass.render();
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		
		glDisable(GL_LIGHTING);
		
		//sky.render();
		glEnable(GL_LIGHTING);
	    //glLight(GL_LIGHT0, GL_AMBIENT, floatBuffy(0.0f, 0.0f, 0.0f, 1.0f));
		//glEnable(GL_LIGHTING);
		
	}

	private void loadTextures()
	{
		// Load textures
		tex_debug = loadImage("resources/debug.png");
		tex_grass = loadImage("resources/camograss.png");
		tex_rock = loadImage("resources/oilgranite.png");
		tex_moss = loadImage("resources/moss.png");
		tex_sky = loadImage("resources/sky.png");
		tex_grass_side = loadImage("resources/camograss_side.png");
		tex_grass_tall = loadImage("resources/camograss_side.png");
	}

	private Texture loadImage(String path)
	{
		Texture texture = tex_debug;
		
		// Load textures
		try
		{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(path)));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return texture;
	}
}