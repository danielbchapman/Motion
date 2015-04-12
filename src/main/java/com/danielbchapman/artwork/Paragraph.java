package com.danielbchapman.artwork;

import java.util.ArrayList;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.physics.toxiclibs.Point;


/** 
 * A simple collection of words that constitutes a mono-spaced paragraph.
 */
public class Paragraph
{
  public Word[] words;
  public boolean split;
  public Point[] points;
  
//extend this an apply the force EQUALLY to all points, that's the way to distribute it.
  public Point parent;
  public int width;
  public int height;
  
  public Paragraph(String content, Point parent, int maxWidth, int wordSpace, int rowSpace)
  {
    this.width = maxWidth;
    
    String[] parts = content.split("\\s+");
    ArrayList<Word> wordList = new ArrayList<>();
    int col = 0;
    int row = 0;
    int index = 0;
    for(String s : parts)
    {
      int colNext = col + s.length() * wordSpace;
      
      if(colNext > maxWidth)
      {
        col = 0;
        colNext = s.length() * wordSpace;
        row++;
        System.out.println("Increasing column");
      }

      Point p = parent.copyTranslate(col, row*rowSpace, 0);
      Word w = new Word(s, p);
      wordList.add(w);
      colNext += wordSpace; //add a space
      col = colNext;
    }
    
    points = new Point[wordList.size()];
    words = new Word[wordList.size()];
    
    this.parent = parent;
    //calculate points
    this.width = maxWidth;
    this.height = (row +1) * rowSpace;
    
    for(int i = 0; i < wordList.size(); i++)
    {
      Word w = wordList.get(i);
      points[i] = w.parent;
      words[i] = w;
      System.out.println(words[i].letters + " @ " + points[i].x + ", " + points[i].y + ", " + points[i].z);
    }
  }
  
  private char character;
  int size = 12;
  
  public void draw(PGraphics g, Point p)
  {
      g.pushMatrix();
//      Point.rotation(g, p);
//      g.translate(p.x,  p.y, p.z);
      g.fill(255);
      g.stroke(255);
      for(int i = 0; i < words.length; i++)
        words[i].draw(g, words[i].parent);
      
      g.popMatrix();   
  }
  
  public void split(VerletPhysics3D physics, float mag)
  {
    System.out.println("Does nothing...");
    if(split)
      return;
    split = true;
    
    for(Word w : words)
      w.split(physics, mag);
//    
//    //FIXME Remove old points
//    for(int i = 0; i < words.length; i++){
//      Vec3D rand = null;
//      if(mag != 0f)
//        rand = Vec3D.randomVector().scale(mag);
//      else
//        rand = new Vec3D(0,0,0);
//      
//      Point add = words[i].parent;
//      Vec3D addHome = add.home.copy();
//      add.angular = parent.angular;
//      add = parent.copyTranslate(add.x, add.y, add.z);
//      add.home = addHome;
//      
//      words[i].parent = add;
//      points[i] = add;
//      points[i].addForce(rand);
//      physics.addParticle(points[i]);
//    }
  }
}
