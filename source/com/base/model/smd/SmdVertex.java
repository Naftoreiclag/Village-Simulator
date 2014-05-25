/* Copyright (c) 2010 "serser" http://code.google.com/u/103273266243665308417
 *
 * Distributed under the MIT License (http://www.opensource.org/licenses/mit-license.php)
 * See accompanying file SMDIMP_LICENSE
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.base.model.smd;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Clase que representa un vertice.
 */
public class SmdVertex
{
	public Vector3f position;
	public Vector3f normals;
	public Vector2f textCoord;
	public int boneIndex;
	public int vertIndex;
	public int geomIndex;
	public float weight = 1f;
}
