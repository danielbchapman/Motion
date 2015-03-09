package com.danielbchapman.video;

import processing.core.PApplet;

public class ServerWindow extends PApplet
{
  private static final long serialVersionUID = 1L;

  public ServerWindow()
  {
  }
  
  StreamProtocol prot = new StreamProtocol();
  NetworkServer server = new NetworkServer();

  public void setup()
  {
    size(800, 600);

    // Writing to the depth buffer is disabled to avoid rendering
    // artifacts due to the fact that the particles are semi-transparent
    // but not z-sorted.
    frameRate(60);
    server.startServer();
    loadPixels();
    System.out.println("Packet size is: " + pixels.length * 3);
  }

  public void draw(){
    //System.out.println("Frame is" + frameCount);
    

    //--------------------Draw 
    background(50, 60, 70);
    fill(255);
    textSize(16);
    text("Frame rate: " + (frameRate), 10, 20);
    text(frameCount, 30, 30);

    //--------------------Start Network
    loadPixels();
    
    byte[] data = new byte[pixels.length * 3];
    
    for(int i = 0, j =0; i < pixels.length; i++, j+=3){
      int col = pixels[i];
      data[j] = (byte) (col >>> 16);
      data[j+1] = (byte) (col >>> 8);
      data[j+2] = (byte) (col);
    }
    
    if(server.clients.length > 0){
      server.send(data);
    }
  }
}
