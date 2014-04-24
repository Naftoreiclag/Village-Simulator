package naftoreiclag.village;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Main3
{
	MapData map;
	
	Texture debug = null;
	Texture texture = null;
	
	private void run()
	{
		loadTextures();
		setupLWJGLDisplay();
		setupOpenGL();
		
		uploadVBOData();
		
		setupLights();
		setupCamera();
		//setupAction();
		
		while(!Display.isCloseRequested())
		{
			input();
			render();
		}

		cleanup();
		
		System.exit(0);
	}
	
	private void input()
	{
	}

	private void render()
	{
		
	}

	private void loadTextures()
	{
		debug = loadImage("resources/debug.png");
		texture = loadImage("donotinclude/eeeeeeenicegrassscaled.png");
	}

	private void setupLWJGLDisplay()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
			
			Display.destroy();
			System.exit(1);
		}
	}

	private void setupOpenGL()
	{
	}

	private void uploadVBOData()
	{
		map = new MapData();
		map.loadDataFromFile("foobar");
	}

	private void setupLights()
	{
	}

	private void setupCamera()
	{
	}

	private void cleanup()
	{
		Display.destroy();
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

	public static void main(String[] args)
	{
		Main3 m = new Main3();
		m.run();
	}
}
