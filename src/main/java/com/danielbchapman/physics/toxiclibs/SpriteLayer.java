package com.danielbchapman.physics.toxiclibs;

import java.io.File;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PGraphicsOpenGL;

public class SpriteLayer extends Layer
{

  public PImage image; 
  public PShape quad;
  public SpriteLayer(PApplet app)
  {
    image = app.loadImage("test/sprite/sprite.png");
    quad = app.createShape();
    quad.beginShape();
    quad.noFill();
    quad.noStroke();
    quad.tint(255, 64);
    quad.texture(image);
    quad.vertex(-50, -50, 0, 0, 0);
    quad.vertex(50, -50, 0, image.width, 0);
    quad.vertex(50, 50, 0, image.width, image.height);
    quad.vertex(-50, 50, 0, 0, image.height);
    quad.endShape();
  }
  
  @Override
  public Point[] init()
  {
    //Load Variables
    MotionEngine.brush = MotionInteractiveBehavior.load(new File("test/sprite/test-sprite.brush"));
    ActionOLD x = ActionsOLD.loadEnvironment(new File("test/sprite/test-sprite.env"));
    x.engine = engine;
    x.layer = this;
    try
    {
      x.call();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    
    int rows = 10;
    int cols = 10;
    int spacing = 20;
    Point[] ret = new Point[rows * cols];
    
    for(int i = 0; i < rows; i++)
      for(int j = 0; j < cols; j++)
      {
        ret[i * cols + j] = new Point((float) i * spacing, (float)j * spacing, 0f, 1f);
      }
         
    return ret;
  }

  @Override
  public void render(PGraphics g)
  {
    //for(Point p : points)
    PGraphicsOpenGL gl = (PGraphicsOpenGL) g;
    gl.beginPGL();
    gl.hint(PGraphicsOpenGL.DISABLE_DEPTH_TEST);
    gl.endPGL();
    
    g.pushMatrix();
    g.fill(0, 16);
    g.rect(0,0, g.width, g.height);
    //g.background(0, 16);
    g.noStroke();
    g.noFill();
    
    for(Point p : points)
    {
      g.pushMatrix();
      g.translate(p.x, p.y , p.z);
      Point.rotation(g, p);
      g.shape(quad); 
      g.popMatrix();
    }
    
    g.popMatrix();
    gl.beginPGL();
    gl.hint(PGraphicsOpenGL.ENABLE_DEPTH_TEST);
    gl.endPGL();
    
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
