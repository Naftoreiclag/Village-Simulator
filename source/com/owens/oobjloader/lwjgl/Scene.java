/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.lwjgl;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import naftoreiclag.village.rendering.model.Model;

public class Scene extends Model
{
	private ArrayList<Model> crazyModels = new ArrayList<Model>();
	
	public Scene()
	{
		super(null, null, 0);
	}

	public void addModel(Model r)
	{
		crazyModels.add(r);
	}
	
	public void upload()
	{
		for(Model v : crazyModels)
		{
			v.upload();
		}
	}

	public void render()
	{
		for(Model v : crazyModels)
		{
			v.render();
		}
	}
	
	public void cleanup()
	{
		for(Model v : crazyModels)
		{
			v.cleanup();
		}
	}
}
