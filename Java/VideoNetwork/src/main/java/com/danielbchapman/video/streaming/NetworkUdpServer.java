package com.danielbchapman.video.streaming;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class NetworkUdpServer extends PApplet
{
  private static final long serialVersionUID = 1L;

  int port = 9100;
  DatagramSocket socket;
  PGraphics video;

  public void setup()
  {
    size(1280, 720, P3D);
    video = new PGraphics();
    frameRate(60);
    try
    {
      socket = new DatagramSocket();
    }
    catch (SocketException e)
    {
      e.printStackTrace();
    }
  }

  public void draw()
  {
    background(50, 60, 70);
    fill(255);
    textSize(16);
    text("Frame rate: " + (frameRate), 10, 20);
    text(frameCount, 30, 30);

    broadcast(get());
  }

  public void broadcast(PImage data)
  {
    byte[] bytes = Compression.compress(data);
    
    try
    {
      socket.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName("192.168.254.25"), port));
      socket.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName("192.168.254.23"), port));
      socket.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName("localhost"), port));
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
