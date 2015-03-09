package com.danielbchapman.video;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class NetworkClient extends PApplet
{
  private static final long serialVersionUID = 1L;
  StreamProtocol prot = new StreamProtocol();
  NetworkClientThread client;
  Object lock = new Object();
  int[] cache;

  public NetworkClient()
  {
  }
  public void setup(){
    size(800, 600, P3D);
    frameRate(30);
    loadPixels();
    cache = new int[800 * 600];//pixels.length];
    
    //Connect
    client = new NetworkClientThread(cache.length, lock, cache);
    new Thread(client).start();
  }

  public void draw(){
    background(0, 20, 20);
    PImage image = new PImage(800, 600);
    

    //System.out.println("Frame " + frameCount);
    loadPixels();
    
    if(cache != null)
      synchronized (lock) {
        for(int i = 0; i < cache.length; i++){
          image.pixels[i] = cache[i];
        }
        
        //System.out.println(cache[0]);
      }
 
    image.updatePixels();
    this.image(image, 0f, 0f);
    
    text("Client: " + frameCount, 30, 90);
  }
}







