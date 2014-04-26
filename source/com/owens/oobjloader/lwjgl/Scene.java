/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.lwjgl;

import java.util.*;

public class Scene
{

	ArrayList<VBO> vboList = new ArrayList<VBO>();

	public Scene()
	{
	}

	public void addVBO(VBO r)
	{
		vboList.add(r);
	}

	public void render()
	{
		for (int loopi = 0; loopi < vboList.size(); loopi++)
		{
			vboList.get(loopi).render();
		}
	}
}
