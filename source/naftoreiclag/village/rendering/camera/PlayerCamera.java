/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.camera;

import naftoreiclag.village.Player;

import org.lwjgl.input.Mouse;

public class PlayerCamera extends Camera3D
{
	Player track = null;
	
	public PlayerCamera(float fov, float aspectRatio, float zNear, float zFar)
	{
		super(fov, aspectRatio, zNear, zFar);
	}
	
	public void setPlayer(Player player)
	{
		this.track = player;
	}
	
	@Override
	public void setup()
	{
		super.setup();
		Mouse.setGrabbed(true);
	}
	
	public void updatePositionToPlayer()
	{
		this.x = track.x;
		this.y = track.y + 5;
		this.z = track.z + 5;
		this.pitch = 45f;
	}
}
