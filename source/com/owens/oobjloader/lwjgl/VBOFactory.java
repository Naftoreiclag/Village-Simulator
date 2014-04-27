/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.lwjgl;

import com.owens.oobjloader.builder.FaceVertex;
import com.owens.oobjloader.builder.Face;

import java.util.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import naftoreiclag.village.rendering.model.CrazyModel;

import org.lwjgl.BufferUtils;

public class VBOFactory
{
	public static CrazyModel build(int textureID, ArrayList<Face> triangles)
	{
		if (triangles.size() <= 0)
		{
			throw new RuntimeException("Can not build a VBO if we have no triangles with which to build it.");
		}

		// Now sort out the triangle/vertex indices, so we can use a
		// VertexArray in our VBO. Note the following is NOT the most efficient
		// way
		// to do this, but hopefully it is clear.

		// First build a map of the unique FaceVertex objects, since Faces may
		// share FaceVertex objects.
		// And while we're at it, assign each unique FaceVertex object an index
		// as we run across them, storing
		// this index in the map, for use later when we build the "index" buffer
		// that refers to the vertice buffer.
		// And lastly, keep a list of the unique vertice objects, in the order
		// that we find them in.
		HashMap<FaceVertex, Integer> indexMap = new HashMap<FaceVertex, Integer>();
		int nextVertexIndex = 0;
		ArrayList<FaceVertex> faceVertexList = new ArrayList<FaceVertex>();
		for (Face face : triangles)
		{
			for (FaceVertex vertex : face.vertices)
			{
				if (!indexMap.containsKey(vertex))
				{
					indexMap.put(vertex, nextVertexIndex++);
					faceVertexList.add(vertex);
				}
			}
		}

		// Now build the buffers for the VBO/IBO
		int verticeAttributesCount = nextVertexIndex;
		int indicesCount = triangles.size() * 3;

		int numMIssingNormals = 0;
		int numMissingUV = 0;
		FloatBuffer verticeAttributes;
		
		verticeAttributes = BufferUtils
				.createFloatBuffer(verticeAttributesCount *8);
		if (null == verticeAttributes)
		{
			System.err
					.println("VBOFactory.build: ERROR Unable to allocate verticeAttributes buffer of size "
							+ (verticeAttributesCount * 8)
							+ " floats.");
		}
		for (FaceVertex vertex : faceVertexList)
		{
			verticeAttributes.put(vertex.v.x);
			verticeAttributes.put(vertex.v.y);
			verticeAttributes.put(vertex.v.z);
			if (vertex.n == null)
			{
				// @TODO: What's a reasonable default normal? Maybe add code
				// later to calculate normals if not present in .obj file.
				verticeAttributes.put(1.0f);
				verticeAttributes.put(1.0f);
				verticeAttributes.put(1.0f);
				numMIssingNormals++;
			} else
			{
				verticeAttributes.put(vertex.n.x);
				verticeAttributes.put(vertex.n.y);
				verticeAttributes.put(vertex.n.z);
			}
			// @TODO: What's a reasonable default texture coord?
			if (vertex.t == null)
			{
				// verticeAttributes.put(0.5f);
				// verticeAttributes.put(0.5f);
				verticeAttributes.put((float) Math.random());
				verticeAttributes.put((float) Math.random());
				numMissingUV++;
			} else
			{
				verticeAttributes.put(vertex.t.u);
				verticeAttributes.put(vertex.t.v);
			}
		}

		System.err.println("Had " + numMIssingNormals + " missing normals and "
				+ numMissingUV + " missing UV coords");

		IntBuffer indices; // indices into the vertices, to specify triangles.
		indices = BufferUtils.createIntBuffer(indicesCount);
		for (Face face : triangles)
		{
			for (FaceVertex vertex : face.vertices)
			{
				int index = indexMap.get(vertex);
				indices.put(index);
			}
		}
		
		CrazyModel rval = new CrazyModel(verticeAttributes, indices, indicesCount); 
		rval.setTexture(textureID);
		rval.upload();

		return rval;
	}
}
