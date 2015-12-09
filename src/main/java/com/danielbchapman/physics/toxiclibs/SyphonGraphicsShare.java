package com.danielbchapman.physics.toxiclibs;

import codeanticode.syphon.SyphonServer;
import processing.core.PApplet;
import processing.core.PGraphics;

public class SyphonGraphicsShare implements IGraphicShare{

	SyphonServer server;
	@Override
	public void initialize(PApplet app) {
		System.out.println("SYPHON INITIALZIED");
		server = new SyphonServer(app, "Motion Syphon Server");
	}

	@Override
	public void cleanup() {
		server.stop();
		System.out.println("CLEANUP");
	}

	@Override
	public void send(PGraphics g) {
		server.sendImage(g);	
	}

}
