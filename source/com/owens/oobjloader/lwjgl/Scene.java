/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.lwjgl;

import java.util.*;

import naftoreiclag.village.rendering.model.CrazyModel;

public class Scene
{
	ArrayList<CrazyModel> vboList = new ArrayList<CrazyModel>();

	public Scene()
	{
	}

	public void addVBO(CrazyModel r)
	{
		vboList.add(r);
	}

	public void render()
	{
		for (CrazyModel v : vboList)
		{
			v.render();
		}
	}
}
