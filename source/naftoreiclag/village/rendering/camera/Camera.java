/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.camera;

public abstract class Camera
{
	// Note: Do not call either method outside of a Renderer. The Renderer will handle it for you.
	public abstract void setup();
	public abstract void applyMatrix();
}
