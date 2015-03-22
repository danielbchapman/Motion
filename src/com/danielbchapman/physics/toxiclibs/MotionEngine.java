package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import com.danielbchapman.physics.vertlet.Force;

import processing.core.PApplet;
import processing.event.KeyEvent;

public class MotionEngine extends PApplet
{
  private static final long serialVersionUID = 1L;
  
  private ArrayList<Layer> layers = new ArrayList<>();
  
  private static Cue testCue;
  
  static {
    ArrayList<Action> test = new ArrayList<Action>();
    
    for(int i = 0; i < 1000; i++)
    {
      Action a = new Action("Action " + i, i * 16);
      test.add(a);
    }
    
    testCue = new Cue(null, null, test);
  }
  
//  public Force gravity;
//  public Force wind;
//  public Force 
//  
  public void add(Layer layer)
  {
    layer.applet = this;
    layers.add(layer);
  }
  
  public void remove(Layer layer)
  {
    layers.remove(layer);
  }
  
  public void draw()
  {
    if(layers != null)
      for(Layer l : layers)
      {
        //Apply forces...
        l.render(g);
      }
        
    pushMatrix();
    translate(0, 0, 50);
    noStroke();
    fill(0);
    rect(50, height-50, 150, 25, 0);
    fill(255, 0, 0);
    text("Frame Rate: " + frameRate, 50, height-50+15, 1);
    popMatrix();
  }
  
  public void setup()
  {
    size(1024, 768, P3D);
    frameRate(60);
    
    postSetup();
  }
  
  public void postSetup()
  {
    add(new GridLayer());

  }
  
  @Override
  public void keyPressed(KeyEvent event)
  {
    if(event.getKey() == ' ')
    {
      testCue.go();
    }
  }
}
