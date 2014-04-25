/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.glu.GLU.*;

// I named this class "Main" just so java newbies can find the
// main method faster! Aren't I so nice? :)

public class Main
{
	MapData map;
	
	DebugCam cam;
	
	Texture debug = null;
	Texture grass_tex = null;
	Texture rock_tex = null;
	
	float sunDir = 0.2f;
	
	boolean wireFrame;
	boolean showNormals;
	
	int testShader;
	int shaderVert;
	int shaderFrag;
	
	public void run()
	{
		setupLWJGLDisplay();
		setupOpenGL();
		loadTextures();
		loadShaders();
		
		uploadVBOData();

	    setupLights();
	    setupCamera();
		
		while(!Display.isCloseRequested())
		{
			input();
			render();
		}

		cleanup();
		
		System.exit(0);
	}
	
	private void setupLWJGLDisplay()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setFullscreen(false);
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
			
			Display.destroy();
			System.exit(1);
		}
		
		glViewport(0, 0, 640, 480);
	}

	private void setupOpenGL()
	{
		// Enable something else
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		// Enable three-dee
		glEnable(GL_DEPTH_TEST);
		
		// Enable textures
		glEnable(GL_TEXTURE_2D);
		
		//
		glClearColor(93f / 255f, 155f / 255f, 217 / 255f, 0.0f);
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		//
		//glShadeModel(GL_FLAT);
	}

	private void loadTextures()
	{
		// Load textures
		debug = loadImage("resources/debug.png");
		grass_tex = loadImage("resources/grass.png");
		rock_tex = loadImage("donotinclude/rock128.png");
	}

	private void loadShaders()
	{
		testShader = glCreateProgram();
		shaderVert = glCreateShader(GL_VERTEX_SHADER);
		shaderFrag = glCreateShader(GL_FRAGMENT_SHADER);
		
		StringBuilder vertSrc = new StringBuilder();
		StringBuilder fragSrc = new StringBuilder();
		
		try
		{
			BufferedReader r = null;
			r = new BufferedReader(new FileReader("donotinclude/shader.vert"));
			String line;
			while((line = r.readLine()) != null)
			{
				vertSrc.append(line).append('\n');
			}
			r.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			BufferedReader r = null;
			r = new BufferedReader(new FileReader("donotinclude/shader.frag"));
			String line;
			while((line = r.readLine()) != null)
			{
				fragSrc.append(line).append('\n');
			}
			r.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		glShaderSource(shaderVert, vertSrc);
		glCompileShader(shaderVert);
		
		if(glGetShaderi(shaderVert, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.out.println("vertex shader didnt work");
		}
		
		glShaderSource(shaderFrag, fragSrc);
		glCompileShader(shaderFrag);
		
		if(glGetShaderi(shaderFrag, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.out.println("fragment shader didnt work");
		}
		
		glAttachShader(testShader, shaderVert);
		glAttachShader(testShader, shaderFrag);
		
		glLinkProgram(testShader);
		glValidateProgram(testShader);
	}

	private void uploadVBOData()
	{
		// Enable vertex buffer objects
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		// Map data
		map = new MapData();
		
		map.loadDataFromFile("foobar");
		map.makeModelFancy();
		map.grass.setTexture(grass_tex.getTextureID());
		map.rock.setTexture(rock_tex.getTextureID());
	
		map.grass.upload();
		map.rock.upload();
	}

	private void setupLights()
	{
		glEnable(GL_LIGHTING);
	    glEnable(GL_LIGHT0);
	    
	    glEnable(GL_COLOR_MATERIAL);
	    glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
	    
	    glLight(GL_LIGHT0, GL_DIFFUSE, floatBuffy(0.6f, 0.6f, 0.6f, 1.0f));
	    glLight(GL_LIGHT0, GL_AMBIENT, floatBuffy(0.0f, 0.0f, 0.0f, 1.0f));
	    glLight(GL_LIGHT0, GL_SPECULAR, floatBuffy(0.0f, 0.0f, 0.0f, 1.0f));
	    
	}

	private void setupCamera()
	{
		cam = new DebugCam(90, 640f / 480f, 0.1f, 1000f);
		cam.doLWJGLStuff();
		cam.doOpenGLStuff();
	}

	private void input()
	{
		//sunDir += 0.1;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_G))
		{
			wireFrame = true;
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		}
		else
		{
			wireFrame = false;
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
		showNormals = Keyboard.isKeyDown(Keyboard.KEY_F);
		
		if(showNormals)
		{
			cam.speed = 0.02f;
		}
		else
		{
			cam.speed = 0.2f;
		}
		
		cam.handleUserInput();
	}

	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		
		glPushMatrix();
		
			cam.applyMatrix();
	
			// If the W value is zero, it is like sunlight. Otherwise, it is lamplike
		    glLight(GL_LIGHT0, GL_POSITION, floatBuffy((float) Math.cos(sunDir), 1.5f, (float) Math.sin(sunDir), 0.0f));
			
			renderAxes(0.0f, 0.0f, 0.0f);
		
			if(showNormals)
			{
				drawNormals();
			}
			glDisable(GL_LIGHTING);
			map.grass.render();
			map.rock.render();
			glEnable(GL_LIGHTING);
		
		glPopMatrix();
	
		Display.update();
		Display.sync(60);
	}

	private void cleanup()
	{
		// We are no longer using VBOs
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		
		map.grass.cleanup();

		// Blow up display (Destroy it!)
		Display.destroy();
	}

	private FloatBuffer floatBuffy(float ... data)
	{
		FloatBuffer f = BufferUtils.createFloatBuffer(data.length);
		f.put(data);
		f.flip();
		return f;
	}
	
	private void drawNormals()
	{
		FloatBuffer geo = map.grass.verts;
		//geo.flip();

		float horzu = map.horzu;
		float vertu = map.vertu;
		
		float scale = 0.5f;

		glDisable(GL_LIGHTING);
		glBegin(GL_LINES);
		glColor3f(0.0f, 0.0f, 0.0f);
		for(int i = 0; i < 32 * 32 * 8 * 2; i += 8)
		{
			float x = geo.get(i + 0);
			float y = geo.get(i + 1);
			float z = geo.get(i + 2);
			float nx = geo.get(i + 3) * scale;
			float ny = geo.get(i + 4) * scale;
			float nz = geo.get(i + 5) * scale;
			
			glVertex3f(x, y, z);
			glVertex3f(x + nx, y + ny, z + nz);
		}
		float size = 32;
		for(int x = 0; x < size; ++ x)
		{
			for(int z = 0; z < size; ++ z)
			{
				/*
				 *     A                X ->
				 *                    
				 * C   M   D          Z
				 *       P            |
				 *     B   N          V
				 *
				 * M is the point defined by x, z
				 */
				
				// Calculate height of these points ===
				
				float m = map.map[x][z];
				float c = map.map[x > 0 ? x - 1 : x][z];
				float d = map.map[x < size - 1 ? x + 1 : x][z];
				float a = map.map[x][z > 0 ? z - 1 : z];
				float b = map.map[x][z < size - 1 ? z + 1 : z];
				float n = map.map[x < size - 1 ? x + 1 : x][z < size - 1 ? z + 1 : z];
				float p = (m + n + d + b) / 4.0f;
				
				// Calculate normals for M ===
				
				float cd_d = c - d;
				if(x == 0 || x == size - 1)
				{
					cd_d *= 2;
				}
				
				float ab_d = a - b;
				if(z == 0 || z == size - 1)
				{
					ab_d *= 2;
				}

				Vector3f m_n = new Vector3f(cd_d * vertu, 2 * horzu, ab_d * vertu);
				m_n.normalise();

				// Add M to data ===

				/*
				glVertex3f(x, m, z);
				glVertex3f(x + m_n.x, m + m_n.y, z + m_n.z);
				*/
				
				// Calculate normals for P ===
				
				Vector3f m2d = new Vector3f(vertu, d - m, 0);
				Vector3f m2b = new Vector3f(0, b - m, vertu);
				Vector3f b2n = new Vector3f(vertu, n - b, 0);
				Vector3f d2n = new Vector3f(0, n - d, vertu);
				
				Vector3f p_n_1 = new Vector3f();
				Vector3f p_n_2 = new Vector3f();
				
				Vector3f.cross(m2b, m2d, p_n_1);
				Vector3f.cross(d2n, b2n, p_n_2);
				
				Vector3f p_n = new Vector3f();
				Vector3f.add(p_n_1, p_n_2, p_n);
				p_n.normalise();

				// Add P to data ===

				glColor3f(1.0f, 1.0f, 0.0f);
				glVertex3f(((x + 0.5f) * horzu), (p * vertu), ((z + 0.5f) * horzu));
				glVertex3f(((x + 0.5f) * horzu) + p_n_1.x, (p * vertu) + p_n_1.y, ((z + 0.5f) * horzu) + p_n_1.z);
				glVertex3f(((x + 0.5f) * horzu), (p * vertu), ((z + 0.5f) * horzu));
				glVertex3f(((x + 0.5f) * horzu) + p_n_2.x, (p * vertu) + p_n_2.y, ((z + 0.5f) * horzu) + p_n_2.z);
				

				glColor3f(1.0f, 0.0f, 0.0f);
				glVertex3f(((x + 0.5f) * horzu), (p * vertu), ((z + 0.5f) * horzu));
				glVertex3f(((x + 0.5f) * horzu) + p_n.x, (p * vertu) + p_n.y, ((z + 0.5f) * horzu) + p_n.z);
			}
		}
		glEnd();
		glEnable(GL_LIGHTING);
		glColor3f(1.0f, 1.0f, 1.0f);
	}

	private Texture loadImage(String path)
	{
		Texture texture = debug;
		
		// Load textures
		try
		{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(path)));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return texture;
	}

	private void renderAxes(float x, float y, float z)
	{
		glPushMatrix();
		glTranslatef(x, y, z);

		glBindTexture(GL_TEXTURE_2D, 0);
		
		float thic = 0.5f;
		
		glColor3f(1.0f, 0.0f, 0.0f);
		for(float ox = 0.0f; ox < 10.0f; ox += 1.0f)
		{
			renderSphere(x + ox, y, z, thic);
		}
		glColor3f(0.0f, 1.0f, 0.0f);
		for(float oy = 0.0f; oy < 10.0f; oy += 1.0f)
		{
			renderSphere(x, y + oy, z, thic);
		}
		glColor3f(0.0f, 0.0f, 1.0f);
		for(float oz = 0.0f; oz < 10.0f; oz += 1.0f)
		{
			renderSphere(x, y, z + oz, thic);
		}
		glColor3f(1.0f, 1.0f, 1.0f);
		
		
		glPopMatrix();
	}
	
	private void renderSphere(float x, float y, float z, float radius)
	{
		glPushMatrix();
		glTranslatef(x, y, z);
		Sphere s = new Sphere();
		s.draw(radius, 16, 16);
		glPopMatrix();
	}

	// This is where the magic begins
	public static void main(String[] args)
	{
		Main m = new Main();
		m.run();
	}
}