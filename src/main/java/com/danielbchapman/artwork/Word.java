package com.danielbchapman.artwork;

import java.util.Random;

import processing.core.PGraphics;
import processing.core.PImage;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.physics.toxiclibs.Point;

public class Word extends Fadeable
{
  char[] letters;
  boolean split;
  Point[] points;
  Point parent;
  
  public Word(String word, Point parent, int low, int high, int count, int delay)
  {
    super(low, high, count, delay);
    letters = word.toCharArray();
    points = new Point[letters.length];
    character = letters[0];
    this.parent = parent;
    //calculate points
  }
  
  public Word(String word, Point parent)
  {
    this(word, parent, 255, 255, 0, 0);
  }
  
  private char character;
  int size = 12;
  
  public void draw(PGraphics g, Point p)
  {
    int reset = g.fillColor;
    int color = color(System.currentTimeMillis(), g.fillColor);
    if(split)
    {
      for(int i = 0; i < points.length; i++)
      {
        g.pushMatrix();
        g.translate(points[i].x, points[i].y, points[i].z);
        Point.rotation(g, points[i]);
        g.fill(color);
        g.text(letters[i], 0, 0, 0);
        g.popMatrix();
      }
    }
    else
    {
      g.pushMatrix();
      g.translate(p.x,  p.y, p.z);
      Point.rotation(g, p);
      g.fill(color);
      for(int i = 0; i < letters.length; i++)
      {
        g.text(letters[i], i*size, 0, 0);  
      }
      g.popMatrix();  
    }
    g.fill(reset);
  }
  
  public void split(VerletPhysics3D physics, float mag)
  {
    if(split)
      return;
    split = true;
    //Add new points
    //FIXME Remove old points
    for(int i = 0; i < letters.length; i++){
      Vec3D rand = Vec3D.randomVector().scale(mag);
      points[i] = parent.copyTranslate(i*size, 0, 0);
      points[i].addForce(rand);
      physics.addParticle(points[i]);
    }
  }
}
