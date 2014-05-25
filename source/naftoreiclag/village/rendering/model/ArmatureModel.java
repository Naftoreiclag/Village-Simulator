/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.model;

// This model contains a static InterleavedModel and armature data.
// It contains methods to enact animations.

public class ArmatureModel extends Model
{
	InterleavedModel model;
	
	public ArmatureModel(InterleavedModel model)
	{
		super(null, null, 0);
	}

	@Override
	public void render()
	{
		model.render();
	}
	
	public void animate()
	{
		// liability
	}

}
