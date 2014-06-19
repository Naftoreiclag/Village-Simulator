/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.rendering.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import naftoreiclag.village.rendering.model.InterleavedSprite;

public class PolygonGen
{
	public static InterleavedSprite makeCircle(float radius, float holeRadius, int numDivisions)
	{
		int numVertices = numDivisions * 2;
		int numIndicies = numDivisions * 6;
		
		FloatBuffer vertBuff = BufferUtils.createFloatBuffer(numVertices * 4);
		IntBuffer intBuff = BufferUtils.createIntBuffer(numIndicies);
		
		double divisionSize = Math.toRadians(360d / ((double) numDivisions));
		
		for(int i = 0; i < numDivisions; ++ i)
		{
			vertBuff.put(((float) Math.cos(divisionSize * i)) *     radius).put(((float) Math.sin(divisionSize * i)) *     radius).put(0.0f).put(0.0f);
			vertBuff.put(((float) Math.cos(divisionSize * i)) * holeRadius).put(((float) Math.sin(divisionSize * i)) * holeRadius).put(0.0f).put(0.0f);
		
			int offset = i * 2;
			
			intBuff.put(offset).put(offset + 1).put((offset + 2) % numVertices);
			intBuff.put(offset + 1).put((offset + 3) % numVertices).put((offset + 2) % numVertices);
		}
		
		vertBuff.flip();
		intBuff.flip();
		return new InterleavedSprite(vertBuff, intBuff, numIndicies);
	}
	

	public static InterleavedSprite makeVector(float x, float y, float halfThickness)
	{
		int numVertices = 4;
		int numIndicies = 6;
		
		FloatBuffer vertBuff = BufferUtils.createFloatBuffer(numVertices * 4);
		IntBuffer intBuff = BufferUtils.createIntBuffer(numIndicies);
		
		
		
		for(int i = 0; i < numDivisions; ++ i)
		{
			vertBuff.put(((float) Math.cos(divisionSize * i)) *     radius).put(((float) Math.sin(divisionSize * i)) *     radius).put(0.0f).put(0.0f);
			vertBuff.put(((float) Math.cos(divisionSize * i)) * holeRadius).put(((float) Math.sin(divisionSize * i)) * holeRadius).put(0.0f).put(0.0f);
		
			int offset = i * 2;
			
			intBuff.put(offset).put(offset + 1).put((offset + 2) % numVertices);
			intBuff.put(offset + 1).put((offset + 3) % numVertices).put((offset + 2) % numVertices);
		}
		
		vertBuff.flip();
		intBuff.flip();
		return new InterleavedSprite(vertBuff, intBuff, numIndicies);
	}
}
