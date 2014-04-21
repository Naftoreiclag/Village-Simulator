package naftoreiclag.village;

import org.lwjgl.opengl.GL11;

public class CameraTest
{
	float x = 0.0f;
	float y = 0.0f;
	float z = 0.0f;

	float yaw = 0.0f;
	float pitch = 0.0f;

	public void look()
	{
		GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(x, y, z);
	}
}
