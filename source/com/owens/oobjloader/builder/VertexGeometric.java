/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.builder;

import java.util.*;
import java.text.*;
import java.io.*;
import java.io.IOException;

public class VertexGeometric
{

	public float x = 0;
	public float y = 0;
	public float z = 0;

	public VertexGeometric(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString()
	{
		if (null == this)
		{
			return "null";
		} else
		{
			return x + "," + y + "," + z;
		}
	}
}
