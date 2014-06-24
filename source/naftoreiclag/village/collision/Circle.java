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
	
	/* How well it resists other circles pushing it. 
	 * If this is -1, then it cannot be moved. 
	 * If it is 0, the other circle can push it perfectly.
	 */
	public final double pushResistance;
	
	// Describes where it relatively wants to be in the next tick
	public Vector2d motion = new Vector2d();
	
	public Circle(Vector2d loc, double rad, double weight)
	{
		this.loc = loc;
		this.rad = rad;
		this.radsq = rad * rad;
		this.pushResistance = weight;
	}
	
	public Circle(double x, double y, double rad, double weight)
	{
		this(new Vector2d(x, y), rad, weight);
	}
}
