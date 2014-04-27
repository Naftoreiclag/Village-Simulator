/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.builder;

public class VertexNormal
{
	public float x = 0;
	public float y = 0;
	public float z = 0;

	public void add(float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public VertexNormal(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString()
	{
		return x + "," + y + "," + z;
	}
}
