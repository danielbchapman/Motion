package com.danielbchapman.physics.toxiclibs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class SyphonGraphicsShare implements IGraphicShare
{

	public static String SYPHON_INSTANCE_NAME = "Motion Syphon Server";

	Object syphonServer;
	Method stop;
	Method sendImage;
	Constructor<?> syphonConst;

	@Override
	public synchronized void initialize(PApplet app)
	{
		Class<?> syphonServerClass = null;
		//SyphonServer x = new SyphonServer(app, "Motion");
		// I hate cross platform code!
		// server = new SyphonServer(app, "Motion Syphon Server");
		try
		{
			syphonServerClass = Class.forName("codeanticode.syphon.SyphonServer");
			syphonConst = syphonServerClass.getConstructor(PApplet.class, String.class);
			stop = syphonServerClass.getDeclaredMethod("stop");
			sendImage = syphonServerClass.getDeclaredMethod("sendImage", PImage.class);
			syphonServer = syphonConst.newInstance(app, SYPHON_INSTANCE_NAME);

			System.out.println("SYPHON INITIALZIED");
		}
		catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void cleanup()
	{
		if (syphonServer != null && stop != null)
		{
			try
			{
				stop.invoke(syphonServer);
				System.out.println("CLEANUP");
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				syphonConst = null;
				stop = null;
				sendImage = null;
				syphonServer = null;

				e.printStackTrace();

			}
		}
	}

	@Override
	public void send(PGraphics g) {
		if(syphonServer != null && sendImage != null)
		{
			try
			{
				sendImage.invoke(syphonServer, g);
				//sendImage = null;
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				cleanup();
				e.printStackTrace();
			}
		}
	}
}
