package com.danielbchapman.physics.kinect;

import java.util.ArrayList;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PGraphicsOpenGL;
import SimpleOpenNI.SimpleOpenNI;

import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.PointWrapper;


public class KinectTracker extends Layer
{
  private MotionEngine engine;
  SimpleOpenNI  context; 
  Point[] points;
  PShape dragon;
  PShape mainEye;
  PShape littleEye;
  PImage eyeData;
  PointWrapper<PShape> eye;
  ArrayList<PointWrapper<PShape>> littleEyes = new ArrayList<>();
  
  public KinectTracker(MotionEngine engine)
  {
    this.engine = engine;
  }

  @Override
  public Point[] init()
  {
    points = new Point[0];
    return points;
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
    //SET GL
    PGraphicsOpenGL gl = (PGraphicsOpenGL) g;
    gl.beginPGL();
    gl.hint(PGraphicsOpenGL.DISABLE_DEPTH_TEST);
    gl.endPGL();
    
    g.noStroke();
    g.noFill();

    g.fill(0, 16);
    g.rect(0,0, g.width, g.height);
    
    g.stroke(255, 100, 0);
    g.noFill();
    g.strokeWeight(4f);
    for(PointWrapper<?> p : littleEyes)
    {
      g.line(eye.x,  eye.y,  100f, p.x, p.y, p.z);
    }
    
    g.noStroke();
    g.noFill();
    g.pushMatrix();
    g.translate(eye.x, eye.y, eye.z+100f);
    g.shape(eye.getWrap());
    g.popMatrix();
    
    for(PointWrapper<PShape> p : littleEyes)
    {
      g.pushMatrix();
      g.translate(p.x, p.y, p.z);
      g.shape(p.getWrap());
      g.popMatrix();
    }
    
    //Restore GL
    gl.beginPGL();
    gl.hint(PGraphicsOpenGL.ENABLE_DEPTH_TEST);
    gl.endPGL();
    
//    g.pushMatrix();
//    g.translate(200, 200);
//    g.shape(dragon);
//    g.popMatrix();
  }

  public void initiailize()
  {
//    dragon = engine.loadShape("/skm/dragon/Dragon-head_obj1.OBJ");
    //Kinect Connection
//    context = new SimpleOpenNI(engine); 
//    
//    //asks OpenNI to initialize and start receiving depth sensor's data
//    context.enableDepth(); 
//    context.enableRGB();
//    context.alternativeViewPointDepthToImage();
//    context.enableUser();
//    context.setMirror(true);
    
    //------------------POINTS
    System.out.println("Engine is null ? " + engine);
    
    eyeData = engine.loadImage("skm/Eyeball.png");
    mainEye = engine.createShape();
    mainEye.beginShape();
    mainEye.noFill();
    mainEye.noStroke();
    mainEye.tint(255, 64);
    mainEye.texture(eyeData);
    mainEye.vertex(-200, -200, 0, 0, 0);
    mainEye.vertex(200, -200, 0, eyeData.width, 0);
    mainEye.vertex(200, 200, 0, eyeData.width, eyeData.height);
    mainEye.vertex(-200, 200, 0, 0, eyeData.height);
    mainEye.endShape();
    
    eye = new PointWrapper<>(400, 400, 0, 0.1f);
    eye.setWrap(mainEye);
    
    float[][] locations = new float[][]
        {
          new float[]{ 243, 260, 0},
          new float[]{ 264, 154, 0},
          new float[]{ 391, 209, 0},
          new float[]{ 446, 142, 0},
          new float[]{ 545, 212, 0},
          new float[]{ 570, 182, 0},
          new float[]{ 585, 264, 0},
          new float[]{ 348, 85, 0},
          new float[]{ 450, 124, 0},
          new float[]{ 500, 170, 0}
        };
    
    for(int i = 0; i < 10; i++)
    {
      PShape tmp = engine.createShape();
      tmp = engine.createShape();
      tmp.beginShape();
      tmp.noFill();
      tmp.noStroke();
      tmp.tint(255, 64);
      tmp.texture(eyeData);
      tmp.vertex(-20, -20, 0, 0, 0);
      tmp.vertex(20, -20, 0, eyeData.width, 0);
      tmp.vertex(20, 20, 0, eyeData.width, eyeData.height);
      tmp.vertex(-20, 20, 0, 0, eyeData.height);
      tmp.endShape();
      
      float[] p = locations[i];
      PointWrapper<PShape> pTmp = new PointWrapper<>(p[0], p[1], p[2], 1);
      pTmp.setWrap(tmp);
      littleEyes.add(pTmp);
    }
    
    engine.getPhysics().addParticle(eye);
    for(PointWrapper<?> p : littleEyes)
      engine.getPhysics().addParticle(p);
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
