/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the MIT License (http://opensource.org/licenses/mit-license.html)
 * See accompanying file LICENSE
 */

package naftoreiclag.village;

import naftoreiclag.village.slide.GuiNode;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

//I named this class "Main" just so java newbies can find the
//main method faster! Aren't I so nice? :)

public class Main
{
	public void run()
	{
		EasyGraphics graphics = new EasyGraphics()
		{
			GuiNode baseNode;
			
			@Override
			void sendStuffToGPU()
			{
				renderGL();
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

	// This is where the magic begins
	public static void main(String[] args)
	{
		Main a = new Main();
		a.run();
	}
}