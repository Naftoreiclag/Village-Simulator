/* Copyright (c) 2010 "serser" http://code.google.com/u/103273266243665308417
 *
 * Distributed under the MIT License (http://www.opensource.org/licenses/mit-license.php)
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.base.model.smd;

public class SmdMesh
{

	SmdTriangle[] triangles;
	private String textureName;
	private String texturePath;

	public SmdMesh()
	{
	}

	public SmdTriangle[] getTriangles()
	{
		return triangles;
	}

	public String getTextureName()
	{
		return textureName;
	}

	public String getTexturePath()
	{
		return texturePath;
	}

	public void setTriangles(SmdTriangle[] triangles)
	{
		this.triangles = triangles;
	}

	public void setTextureName(String textureName)
	{
		this.textureName = textureName;
	}

	public void setTexturePath(String texturePath)
	{
		this.texturePath = texturePath;
	}
}
