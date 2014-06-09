package naftoreiclag.village.rendering.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class TBuffy
{
	public static FloatBuffer floaty(float ... data)
	{
		FloatBuffer b = BufferUtils.createFloatBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
	
	public static IntBuffer inty(int ... data)
	{
		IntBuffer b = BufferUtils.createIntBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
}
