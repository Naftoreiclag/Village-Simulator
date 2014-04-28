/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.collision;

public class Line
{
	public Vector2d a;
	public Vector2d b;
	
	public Line(Vector2d a, Vector2d b)
	{
		this.a = a;
		this.b = b;
	}
	
	public Line(double ax, double ay, double bx, double by)
	{
		this(new Vector2d(ax, ay), new Vector2d(bx, by));
	}
}
