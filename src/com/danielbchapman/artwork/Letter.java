package com.danielbchapman.artwork;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import com.danielbchapman.physics.vertlet.Point3D;

public class Letter
{
  PImage texture;
  
  private char character;
  int size = 16;
  PVector rotation = new PVector();
  
  public Letter(char c, int size)
  {
    character = c;
    this.size = size;
//    PGraphics g = new PGraphics();
//    g.text("" + character, 0, 0);
//    texture = g.get();
//    g.dispose();
  }
  
  public void draw(PGraphics g, Point3D point)
  {
    PVector p = point.position;
    g.pushMatrix();
    g.fill(255);
    g.stroke(255);
    //g.texture(texture);
    //g.rect(p.x, p.y, size, size);
    g.text(character, p.x, p.y, p.z);
    g.popMatrix();
  }
}
