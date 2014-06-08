/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureLib
{
	private static Texture debugTexture = null;
	public final static Map<String, Texture> lib = new HashMap<String, Texture>();
	
	// Get a pre-loaded texture's handle based on its name
	public static int getTextureFromName(String name)
	{
		Texture t = lib.get(name);
		
		if(t == null)
		{
			return debugTexture.getTextureID();
		}
		else
		{
			return t.getTextureID();
		}
	}
	
	// Load a texture and then return its handle
	public static int loadTexture(String name)
	{
		Texture texture = slickLoad(name);
		
		if(texture == null)
		{
			return debugTexture.getTextureID();
		}
		else
		{
			lib.put(name, texture);
			return texture.getTextureID();
		}
	}

	//
	public static void loadDebugTexture()
	{
		if(debugTexture != null)
		{
			debugTexture = slickLoad("debug");
		
			if(debugTexture == null)
			{
				// TODO: crash
			}
		}
	}
	
	//
	private static Texture slickLoad(String name)
	{
		Texture texture = null;
		try
		{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("resources/" + name + ".png")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return texture;
	}
}
