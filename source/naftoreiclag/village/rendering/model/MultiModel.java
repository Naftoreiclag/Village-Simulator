/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 * 
 * Special thanks to Sean R. Owens for his public domain code which I added.
 */

package naftoreiclag.village.rendering.model;

import java.util.*;

// It is a collection of multiple models that can render at once.

public class MultiModel extends Model
{
	private ArrayList<Model> crazyModels = new ArrayList<Model>();
	
	public MultiModel()
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
