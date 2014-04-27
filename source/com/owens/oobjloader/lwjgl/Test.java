/* Written by Sean R. Owens, sean at guild dot net, released to the
 * public domain.
 * See [https://github.com/seanrowens/oObjLoader] or [http://darksleep.com/oobjloader/] for details.
 */

package com.owens.oobjloader.lwjgl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import naftoreiclag.village.rendering.model.CrazyModel;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.owens.oobjloader.builder.Build;
import com.owens.oobjloader.builder.Face;
import com.owens.oobjloader.builder.FaceVertex;
import com.owens.oobjloader.builder.Material;
import com.owens.oobjloader.parser.Parse;

public class Test
{
	public static void maain(String[] args)
	{
		String filename = "donotinclude/stanrabbit.obj";
		String defaultTextureMaterial = "resources/debug.png";

		try
		{
			init();
			run(filename, defaultTextureMaterial);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			cleanup();
		}
		System.exit(0);
	}

	// iterate over face list from builder, and break it up into a set of face
	// lists by material, i.e. each for each face list, all faces in that
	// specific list use the same material
	private static ArrayList<ArrayList<Face>> createFaceListsByMaterial(Build builder)
	{
		ArrayList<ArrayList<Face>> facesByTextureList = new ArrayList<ArrayList<Face>>();
		Material currentMaterial = null;
		ArrayList<Face> currentFaceList = new ArrayList<Face>();
		for (Face face : builder.faces)
		{
			if (face.material != currentMaterial)
			{
				if (!currentFaceList.isEmpty())
				{
					System.err.println("Adding list of "
							+ currentFaceList.size()
							+ " triangle faces with material "
							+ currentMaterial
							+ "  to our list of lists of faces.");
					facesByTextureList.add(currentFaceList);
				}
				System.err.println("Creating new list of faces for material "
						+ face.material);
				currentMaterial = face.material;
				currentFaceList = new ArrayList<Face>();
			}
			currentFaceList.add(face);
		}
		if (!currentFaceList.isEmpty())
		{
			System.err.println("Adding list of " + currentFaceList.size()
					+ " triangle faces with material " + currentMaterial
					+ "  to our list of lists of faces.");
			facesByTextureList.add(currentFaceList);
		}
		return facesByTextureList;
	}

	// @TODO: This is a crappy way to calculate vertex normals if we are missing
	// said normals. I just wanted
	// something that would add normals since my simple VBO creation code
	// expects them. There are better ways
	// to generate normals, especially given that the .obj file allows
	// specification of "smoothing groups".
	private static void calcMissingVertexNormals(ArrayList<Face> triangleList)
	{
		for (Face face : triangleList)
		{
			face.calculateTriangleNormal();
			for (int loopv = 0; loopv < face.vertices.size(); loopv++)
			{
				FaceVertex fv = face.vertices.get(loopv);
				if (face.vertices.get(0).n == null)
				{
					FaceVertex newFv = new FaceVertex();
					newFv.v = fv.v;
					newFv.t = fv.t;
					newFv.n = face.faceNormal;
					face.vertices.set(loopv, newFv);
				}
			}
		}
	}

	// load and bind the texture we will be using as a default texture for any
	// missing textures, unspecified textures, and/or
	// any materials that are not textures, since we are pretty much
	// ignoring/not using those non-texture materials.
	//
	// In general in this simple test code we are only using textures, not
	// 'colors' or (so far) any of the other multitude of things that
	// can be specified via 'materials'.
	private static int setUpDefaultTexture(TextureLoaderB textureLoader, String defaultTextureMaterial)
	{
		int defaultTextureID = -1;
		try
		{
			defaultTextureID = textureLoader.load(defaultTextureMaterial, true);
		} catch (IOException ex)
		{
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
			System.err
					.println("ERROR: Got an exception trying to load default texture material = "
							+ defaultTextureMaterial + " , ex=" + ex);
			ex.printStackTrace();
		}
		System.err.println("INFO:  default texture ID = " + defaultTextureID);
		return defaultTextureID;
	}

	// Get the specified Material, bind it as a texture, and return the OpenGL
	// ID. Returns he default texture ID if we can't
	// load the new texture, or if the material is a non texture and hence we
	// ignore it.
	private static int getMaterialID(Material material, int defaultTextureID,
			Build builder, TextureLoaderB textureLoader)
	{
		int currentTextureID;
		if (material == null)
		{
			currentTextureID = defaultTextureID;
		} else if (material.mapKdFilename == null)
		{
			currentTextureID = defaultTextureID;
		} else
		{
			try
			{
				File objFile = new File(builder.objFilename);
				File mapKdFile = new File(objFile.getParent(),
						material.mapKdFilename);
				System.err.println("Trying to load  "
						+ mapKdFile.getAbsolutePath());
				currentTextureID = textureLoader.load(
						mapKdFile.getAbsolutePath(), true);
			} catch (IOException ex)
			{
				Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null,
						ex);
				System.err
						.println("ERROR: Got an exception trying to load  texture material = "
								+ material.mapKdFilename + " , ex=" + ex);
				ex.printStackTrace();
				System.err.println("ERROR: Using default texture ID = "
						+ defaultTextureID);
				currentTextureID = defaultTextureID;
			}
		}
		return currentTextureID;
	}

	// VBOFactory can only handle triangles, not faces with more than 3
	// vertices. There are much better ways to 'triangulate' polygons, that
	// can be used on polygons with more than 4 sides, but for this simple test
	// code justsplit quads into two triangles
	// and drop all polygons with more than 4 vertices. (I was originally just
	// dropping quads as well but then I kept ending up with nothing
	// left to display. :-) Or at least, not much. )
	private static ArrayList<Face> splitQuads(ArrayList<Face> faceList)
	{
		ArrayList<Face> triangleList = new ArrayList<Face>();
		for (Face face : faceList)
		{
			if (face.vertices.size() == 3)
			{
				triangleList.add(face);
			} else if (face.vertices.size() == 4)
			{
				FaceVertex v1 = face.vertices.get(0);
				FaceVertex v2 = face.vertices.get(1);
				FaceVertex v3 = face.vertices.get(2);
				FaceVertex v4 = face.vertices.get(3);
				Face f1 = new Face();
				f1.map = face.map;
				f1.material = face.material;
				f1.add(v1);
				f1.add(v2);
				f1.add(v3);
				triangleList.add(f1);
				Face f2 = new Face();
				f2.map = face.map;
				f2.material = face.material;
				f2.add(v1);
				f2.add(v3);
				f2.add(v4);
				triangleList.add(f2);
			} else
			{
			}
		}
		return triangleList;
	}

	private static void init() throws Exception
	{
		Display.setFullscreen(false);
		Display.setVSyncEnabled(true);
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, 640 / 480, 0.5f, 400.0f);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glViewport(0, 0, 640, 480);
	}
	
	private static void run(String filename, String defaultTextureMaterial)
	{
		Scene scene = new Scene();

		Build builder = new Build();
		try
		{
			new Parse(builder, filename);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ArrayList<ArrayList<Face>> facesByTextureList = createFaceListsByMaterial(builder);

		TextureLoaderB textureLoader = new TextureLoaderB();
		int defaultTextureID = setUpDefaultTexture(textureLoader, defaultTextureMaterial);

		int currentTextureID = -1;
		for (ArrayList<Face> faceList : facesByTextureList)
		{
			if (faceList.isEmpty())
			{
				continue;
			}
			currentTextureID = getMaterialID(faceList.get(0).material, defaultTextureID, builder, textureLoader);
			ArrayList<Face> triangleList = splitQuads(faceList);
			calcMissingVertexNormals(triangleList);

			if(triangleList.size() <= 0)
			{
				continue;
			}

			CrazyModel vbo = VBOFactory.build(currentTextureID, triangleList);

			scene.addVBO(vbo);
		}

		float anglex = 0;
		float angley = 0;
		float anglez = 0;
		float anglexInc = .25f;
		float angleyInc = .005f;
		float anglezInc = .25f;
		float translatex = 0;
		float translatey = 0f;
		float translatez = -200f;
		float incrementx = 0;
		float incrementy = 0;
		float incrementz = -0.1f;
		float zmax = .10f;
		float zmin = -300f;

		while(!Display.isCloseRequested())
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			// add some arbitrary rotation and translation of the viewpoint just
			// to make things less boring
			anglex = (anglex + anglexInc) % 360;
			angley = (angley + angleyInc) % 360;
			anglez = (anglez + anglezInc) % 360;
			if (anglez > 360)
			{
				anglez = 0;
			}
			translatex += incrementx;
			translatey += incrementy;
			translatez += incrementz;
			if (translatez <= zmin || translatez >= zmax)
			{
				incrementz = -incrementz;
			}
			GL11.glTranslated(0, 0, translatez);
			GL11.glTranslated(0, translatey, 0);
			GL11.glTranslated(translatex, 0, 0);
			GL11.glRotatef(anglez, 0.0f, 0.0f, 1.0f);
			GL11.glRotatef(angley, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(anglex, 1.0f, 0.0f, 0.0f);
			
			scene.render();
			
			Display.update();
			Display.sync(60);
		}
	}

	private static void cleanup()
	{
		Display.destroy();
	}
}
