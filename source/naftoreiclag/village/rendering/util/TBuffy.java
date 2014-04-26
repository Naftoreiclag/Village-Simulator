package naftoreiclag.village.rendering.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class TBuffy
{
	public static FloatBuffer floaty(float ... data)
	{
		FloatBuffer f = BufferUtils.createFloatBuffer(data.length);
		f.put(data);
		f.flip();
		return f;
	}
}
