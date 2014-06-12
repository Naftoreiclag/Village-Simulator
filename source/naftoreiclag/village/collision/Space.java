/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the BSD 2-Clause License (http://opensource.org/licenses/BSD-2-Clause)
 * See accompanying file LICENSE
 */

package naftoreiclag.village.collision;

import java.util.ArrayList;
import java.util.List;

public class Space
{
	// The objects which reside in this space
	public List<Circle> circles = new ArrayList<Circle>();
	public List<Line> lines = new ArrayList<Line>();
	
	// Increment physics simulation by a certain amount
	public void simulate(long delta)
	{
		for(Circle circle : circles)
		{
			// Move it
			circle.loc.addLocalMultiplied(circle.velocity, delta);
			
			// Remember whether the current state is suspected to be dirty (circles in illegal positions)
			boolean suspectedDirty = true;
			
			// Repeat until it is proven clean
			while(suspectedDirty)
			{
				// If nothing illegal is found on the circle, then this value will stay false.
				suspectedDirty = false;
				
				// Check relation of this circle to surrounding lines.
				for(Line line : lines)
				{
					// What
					Vector2d AC = circle.loc.subtract(line.a);
					Vector2d AB = line.b.subtract(line.a);
	
					// I don't even
					double AB_distsq = (AB.a * AB.a) + (AB.b * AB.b);
					
					// What is a dot product
					double AC_dot_AB = (AC.a * AB.a) + (AC.b * AB.b);
					
					// What is this
					double magic = AC_dot_AB / AB_distsq;
	
					// Past B
					if(magic > 1)
					{
						continue;
					}
					
					// Past A
					else if(magic < 0)
					{
						double AC_distsq = AC.magnitudeSquared();
						
						if(AC_distsq <= circle.radsq)
						{
							circle.loc.addLocal(AC.divide(Math.sqrt(AC_distsq)).multiplyLocal(circle.rad + 0.5d)).subtractLocal(AC);
							
							suspectedDirty = true;
							break;
						}
					}
					
					// Within the line
					else
					{
						Vector2d D = line.a.add(AB.multiply(magic));
						Vector2d DC = circle.loc.subtract(D);
						double DC_distsq = DC.magnitudeSquared();
						
						if(DC_distsq <= circle.radsq)
						{
							circle.loc.addLocal(DC.divide(Math.sqrt(DC_distsq)).multiplyLocal(circle.rad + 0.5d)).subtractLocal(DC);
	
							suspectedDirty = true;
							break;
						}
					}
				}
			}
			
			circle.velocity.setZero();
		}
	}
	
	// I had to use uppercase letters here to avoid (more) confusion
	public boolean collides(Vector2d A, Vector2d B, Vector2d C, double rad)
	{
		// TODO: put checks for horizontal / vertical lines for max efficiency over 9000
		
		// What
		Vector2d AC = C.subtract(A);
		Vector2d AB = B.subtract(A);

		// I don't even
		double AB_distsq = (AB.a * AB.a) + (AB.b * AB.b);
		
		// What is a dot product
		double AC_dot_AB = (AC.a * AB.a) + (AC.b * AB.b);
		
		// What is this
		double magic = AC_dot_AB / AB_distsq;

		if(magic > 1) return false;
		else if(magic < 0)
		{
			double AC_distsq = AC.magnitudeSquared();
			
			return AC_distsq <= (rad * rad);
		}
		
		return A.add(AB.multiply(magic)).distanceSquared(C) <= (rad * rad);
	}
}
