package com.danielbchapman.artwork;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.physics.toxiclibs.PointOLD;

public class Word extends Fadeable
{
  @Getter
  boolean enableRotation = true;
  char[] letters;
  @Getter
  @Setter
  PointOLD parent;
  PointOLD[] points;
  
  int size = 36;
  boolean split;
  
  public Word(String word, PointOLD parent)
  {
    this(word, parent, 255, 255, 0, 0);
  }
  
  public Word(String word, PointOLD parent, int low, int high, int count, int delay)
  {
    this(word, parent, low, high, count, delay, 36);
  }
  
  public Word(String word, PointOLD parent, int low, int high, int count, int delay, int size)
  {
    super(low, high, count, delay);
    letters = word.toCharArray();
    points = new PointOLD[letters.length];
    this.parent = parent;
    this.size = size;
    //calculate points
  }
  public void draw(PGraphics g, PointOLD p)
  {
    float tSize = g.textSize;
    g.textSize(size);
    int reset = g.fillColor;
    int color = color(System.currentTimeMillis(), g.fillColor);
    if(split)
    {
      for(int i = 0; i < points.length; i++)
      {
        g.pushMatrix();
        g.translate(points[i].x, points[i].y, points[i].z);
        
        PointOLD.rotation(g, points[i]);
        
        g.fill(color);
        g.text(letters[i], 0, 0, 0);
        g.popMatrix();
      }
    }
    else
    {
      g.pushMatrix();
      g.translate(p.x,  p.y, p.z);
      
      PointOLD.rotation(g, p);
      g.fill(color);
      for(int i = 0; i < letters.length; i++)
      {
        g.text(letters[i], i*size, 0, 0);  
      }
      g.popMatrix();  
    }
    g.fill(reset);
    
    g.textSize(tSize);
  }
  
  public void setEnableRotation(boolean to)
  {
    this.enableRotation = to;
    
    if(points != null)
    for(PointOLD p : points)
      p.enableRotation = to;
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
  
  public void spread(float mag)
  {
    if(points != null)
      for(PointOLD p : points)
        p.addForce(Vec3D.randomVector().scaleSelf(mag));
  }
  
  /**
   * Moves all the points in this word by a certain amount. This will
   * also translate the forces so that this doesn't cause ridiculous acceleration.
   * 
   * @param vec 
   *          the translation amount  
   * 
   */
  public void translate(Vec3D vec)
  {
    parent.addSelf(vec);
    parent.clearForce();
    parent.clearVelocity();
    
    parent.y += vec.y;
    parent.z += vec.z;
    for(int i = 0; i < points.length; i++)
      if(points[i] != null)
      {
        points[i].addSelf(vec);
        points[i].clearForce();
        points[i].clearVelocity();
      }
        
  }
}
