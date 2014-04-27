/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package naftoreiclag.village.rendering.model;

import java.util.*;

// this one is wacky

public class WackyModel extends Model
{
	private ArrayList<Model> crazyModels = new ArrayList<Model>();
	
	public WackyModel()
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
