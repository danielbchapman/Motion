package com.danielbchapman.physics.kinect;

import java.util.ArrayList;

import javax.media.opengl.GL;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PGraphicsOpenGL;

import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.PointWrapper;


public class KinectTracker extends Layer
{
  private MotionEngine engine;
  
  PShape mainEye;
  PShape littleEye;
  PImage eyeData;
  PointWrapper<PShape> eye;
  ArrayList<PointWrapper<PShape>> littleEyes;
  
  public KinectTracker(MotionEngine engine)
  {
    this.engine = engine;
  }
  
  public void draw()
  {
    
  }

  @Override
  public Point[] init()
  {
//    
//    
//    background(0);
//    translate(width / 2, height / 2);
//    rotateY(map(mouseX, 0, width, -PI, PI));
//    rotateZ(PI/6);
//    beginShape();
//    texture(img);
//    vertex(-100, -100, 0, 0, 0);
//    vertex(100, -100, 0, img.width, 0);
//    vertex(100, 100, 0, img.width, img.height);
//    vertex(-100, 100, 0, 0, img.height);
//    endShape();
    
    return new Point[]{};
  }

  boolean initialized = false;
  
  @Override
  public void render(PGraphics g)
  {
    if(!initialized)
    {
      initiailize();
      initialized = true;
    }
    PGraphicsOpenGL pgl = (PGraphicsOpenGL) g;
    pgl.gl
    GL gl = ((Object) pgl).beginGL();
    gl.glDisable( GL.GL_DEPTH_TEST );
    pgl.endGL();
    g.background(0);
    g.pushMatrix();
    g.translate(400,  400, -200);
    g.shape(mainEye);
    g.popMatrix();
  }

  public void initiailize()
  {
    
    System.out.println("Engine is null ? " + engine);
    eyeData = engine.loadImage("/skm/Eyeball.png");
    mainEye = engine.createShape();
    mainEye.beginShape();
    mainEye.tint(0,0);
    mainEye.texture(eyeData);
    mainEye.vertex(-100, -100, 0, 0, 0);
    mainEye.vertex(100, -100, 0, eyeData.width, 0);
    mainEye.vertex(100, 100, 0, eyeData.width, eyeData.height);
    mainEye.vertex(-100, -100, 0, 0, eyeData.height);
    mainEye.endShape();
  }
  @Override
  public void update()
  { 
  }

  @Override
  public void go(MotionEngine engine)
  { 
  }
}
