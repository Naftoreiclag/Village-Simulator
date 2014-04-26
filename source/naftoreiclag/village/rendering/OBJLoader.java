package naftoreiclag.village.rendering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class OBJLoader
{
	public static Model fromObjFile(String filename) throws IOException
	{
		int vertCount = 0;
		int indCount = 0;

		String line;

		BufferedReader read1 = new BufferedReader(new FileReader(new File(filename)));
		while((line = read1.readLine()) != null)
		{
			String[] data = line.split(" ");
			
			if(data[0].equals("v"))
			{
				vertCount ++;
			}
			else if(data[0].equals("f"))
			{
				indCount += 3;
			}
		}
		read1.close();

		FloatBuffer verts = BufferUtils.createFloatBuffer(vertCount * 8);
		IntBuffer indices = BufferUtils.createIntBuffer(indCount);
		
		int numNorms;
		int numTexs;
		
		BufferedReader read2 = new BufferedReader(new FileReader(new File(filename)));
		while((line = read2.readLine()) != null)
		{
			String[] data = line.split(" ");
			
			if(data[0].equals("#"))
			{
			    continue;
			}
			else if(data[0].equals("v"))
			{
				verts.put(Float.valueOf(data[1])).put(Float.valueOf(data[2])).put(Float.valueOf(data[3]));
			}
			else if(data[0].equals("vt"))
			{
				verts.put(Float.valueOf(data[1])).put(Float.valueOf(data[2])).put(Float.valueOf(data[3]));
			}
			else if(data[0].equals("vn"))
			{
				verts.put(Float.valueOf(data[1])).put(Float.valueOf(data[2])).put(Float.valueOf(data[3]));
			}
			else if(data[0].equals("f"))
			{
			}
			else
			{
				System.err.println("Corrupt line in .obj! Skipping that line and hoping for best...");
				continue;
			}
		}
		read2.close();

		return new Model(verts, indices, indCount, Model.WeaveType.nVnTnN);
	}
}
