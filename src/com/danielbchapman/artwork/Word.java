package com.danielbchapman.artwork;

import java.util.Random;

import processing.core.PGraphics;
import processing.core.PImage;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.physics.toxiclibs.Point;

public class Word
{
  char[] letters;
  boolean split;
  Point[] points;
  Point parent;
  
  public Word(String word, Point parent)
  {
    letters = word.toCharArray();
    points = new Point[letters.length];
    character = letters[0];
    this.parent = parent;
    //calculate points
  }
  
  private char character;
  int size = 12;
  
  public void draw(PGraphics g, Point p)
  {
    if(split)
    {
      for(int i = 0; i < points.length; i++)
      {
        g.pushMatrix();
        g.translate(points[i].x, points[i].y, points[i].z);
        Point.rotation(g, points[i]);
        g.text(letters[i], 0, 0, 0);
        g.popMatrix();
      }
    }
    else
    {
      g.pushMatrix();
      g.translate(p.x,  p.y, p.z);
      Point.rotation(g, p);
      g.fill(255);
      g.stroke(255);
      for(int i = 0; i < letters.length; i++)
      {
        g.text(letters[i], i*size, 0, 0);  
      }
      
      g.popMatrix();  
    }
    
  }
  
  public void split(VerletPhysics3D physics, float mag)
  {
    if(split)
      return;
    split = true;
    //Add new points
    //remove old points
    for(int i = 0; i < letters.length; i++){
      Vec3D rand = Vec3D.randomVector().scale(mag);
      points[i] = parent.copyTranslate(i*size, 0, 0);
      points[i].addForce(rand);
      physics.addParticle(points[i]);
    }
  }
}
