/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.collision;

public class Circle
{
	public Vector2d loc;
	public final double rad;
	public final double radsq;
	
	public Vector2d velocity = new Vector2d();
	
	public Circle(Vector2d loc, double rad)
	{
		this.loc = loc;
		this.rad = rad;
		this.radsq = rad * rad;
	}
	
	public Circle(double x, double y, double rad)
	{
		this(new Vector2d(x, y), rad);
	}
}
