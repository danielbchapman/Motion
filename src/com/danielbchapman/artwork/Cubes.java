package com.danielbchapman.artwork;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import com.danielbchapman.physics.vertlet.Point3D;

public class Cubes
{
  PImage texture;
  
  private char character;
  int size = 16;
  PVector rotation = new PVector();
  
  public Cubes(int size)
  {
    this.size = size;
  }
  
  public void draw(PGraphics g, Point3D point)
  {
    PVector p = point.position;
    g.pushMatrix();
    g.fill(255);
    g.stroke(255);
    g.translate(p.x, p.y, p.z);
    //g.scale(1f);
    //g.texture(texture);
    //g.rect(p.x, p.y, size, size);
    g.box((float) size);
    g.popMatrix();
  }
}
