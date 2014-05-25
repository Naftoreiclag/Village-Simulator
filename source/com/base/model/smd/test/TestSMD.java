/* Copyright (c) 2010 "serser" http://code.google.com/u/103273266243665308417
 *
 * Distributed under the MIT License (http://www.opensource.org/licenses/mit-license.php)
 */

package com.base.model.smd.test;

import com.base.model.jme.SMDNode;
import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.app.SimpleGame;
import com.jme.scene.shape.AxisRods;
import com.jme.util.BoneDebugger;

public class TestSMD extends SimpleGame
{

	private SMDNode node;
	AxisRods ejes;

	public TestSMD()
	{
	}

	/**
	 * Entry point for the test,
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		TestSMD app = new TestSMD();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	/**
	 * builds the trimesh.
	 * 
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame()
	{
		ejes = new AxisRods("ejes", true, 5.0f);
		ejes.setLocalTranslation(0, 0, 0);
		rootNode.attachChild(ejes);
		node = new SMDNode("C:\\JME\\Models\\player\\eightball\\eightball.qc", display);
		node = new SMDNode("C:\\JME\\Models\\player\\terror\\terror.qc", display);
		node.getController().setActiveAnimation("run");
		rootNode.attachChild(node);
	}

	@Override
	protected void simpleRender()
	{
		// super.simpleRender();
		BoneDebugger.drawBones(rootNode, display.getRenderer(), true);
	}

	@Override
	protected void updateInput()
	{
		super.updateInput();
	}

	@Override
	public void simpleUpdate()
	{
		super.simpleUpdate();
		timer.update();
	}
}
