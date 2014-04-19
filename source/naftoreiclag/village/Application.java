/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import naftoreiclag.village.slide.GuiNode;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Application
{
	public void run()
	{
		EasyGraphics graphics = new EasyGraphics()
		{
			GuiNode baseNode;
			
			@Override
			void sendStuffToGPU()
			{
			}
		};
		
		try
		{
			graphics.initialize(800, 600);
		} catch (LWJGLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		while (!Display.isCloseRequested())
		{
			graphics.updateDisplay();
			renderGL();
		}
		
		if(Display.isCloseRequested())
		{
			graphics.cleanup();
		}
	}
	
	public void renderGL()
    {
            GL11.glColor3f(0.5f, 0.5f, 1.0f);

            GL11.glPushMatrix();

            GL11.glBegin(GL11.GL_QUADS);
                    GL11.glVertex2f(100 - 50, 100 - 50);
                    GL11.glVertex2f(100 + 50, 100 - 50);
                    GL11.glVertex2f(100 + 50, 100 + 50);
                    GL11.glVertex2f(100 - 50, 100 + 50);
            GL11.glEnd();

            
    }

	public static void main(String[] args)
	{
		Application application = new Application();
		application.run();
	}
}