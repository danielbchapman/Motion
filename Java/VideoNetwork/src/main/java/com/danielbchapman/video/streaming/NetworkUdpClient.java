package com.danielbchapman.video.streaming;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PImage;

public class NetworkUdpClient extends PApplet
{

  private static final long serialVersionUID = 1L;

  DatagramSocket socket;
  int port = 9100;
  PImage video;
  byte[] buffer = new byte[65536];
  
  public void setup()
  {
    size(800, 600, P3D);
    try {
      socket = new DatagramSocket(port);
    } catch (SocketException e) {
      System.out.println("Socket Exception...");
      e.printStackTrace(System.out);
    }
    video = createImage(320, 240, RGB); //Source size...
  }
  
  public void draw()
  {
    readImage();
    
    background(255, 0, 0);
    imageMode(CENTER);
    image(video, width/2, height/2);
  }
  
  public void readImage(){
    DatagramPacket p = new DatagramPacket(buffer, buffer.length);
    try {
      socket.receive(p);
    } catch (IOException e){
      e.printStackTrace(System.out);
    }
    
    byte[] data = p.getData();
    
    Compression.decompress(data, video);
  }
}
