package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.scene.input.KeyCode;
import processing.core.PApplet;
import processing.event.KeyEvent;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.AttractionBehavior3D;
import toxi.physics3d.behaviors.GravityBehavior3D;

public class MotionEngine extends PApplet
{
  private static final long serialVersionUID = 1L;

  private VerletPhysics3D physics = new VerletPhysics3D();
  private ArrayList<Layer> layers = new ArrayList<>();

  private static Cue testCue;
  private static Cue gravityOneSecond;
  
  private static AttractionBehavior3D sucker = 
      new AttractionBehavior3D(new Vec3D(1f, 1f, 1f), 45f, 1f, 1f); 

  private static HomeBehavior3D home = new HomeBehavior3D(new Vec3D(0, 0, 0));
  
  static
  {
    ArrayList<Action> test = new ArrayList<>();

    for (int i = 0; i < 1000; i++)
    {
      Action a = new Action("Action " + i, i * 16);
      test.add(a);
    }

    ArrayList<Action> gravityOne = new ArrayList<>();
    Action start = new Action("Start Gravity", 0);
    final GravityBehavior3D gravity = new GravityBehavior3D(new Vec3D(0f, 0.01f, 0f));

    Action stop = new Action("Stop Gravity", 1000);

    start.motionFunction = (MotionEngine e) -> {
      e.physics.addBehavior(gravity);
    };

    stop.motionFunction = (MotionEngine e) -> {
      e.physics.removeBehavior(gravity);
    };
    
    gravityOne.add(start);
    gravityOne.add(stop);

    gravityOneSecond = new Cue(null, null, gravityOne);
    testCue = new Cue(null, null, test);
  }

  // public Force gravity;
  // public Force wind;
  // public Force
  //
  public void add(Layer layer)
  {
    layer.applet = this;
    layers.add(layer);
    for (Point p : layer.points)
      physics.addParticle(p);
  }

  public void remove(Layer layer)
  {
    layers.remove(layer);
  }

  public void draw()
  {
    // Model updates
    physics.update();

    
    if (layers != null)
      for (Layer l : layers)
      {
        // Apply forces...
        g.pushMatrix();
        //Rotate world
        //rotateX(45f);
        l.render(g);
        g.popMatrix();
      }

    // Framerate
    pushMatrix();
    translate(0, 0, 50);
    noStroke();
    fill(0);
    rect(50, height - 50, 150, 25, 0);
    fill(255, 0, 0);
    text("Frame Rate: " + frameRate, 50, height - 50 + 15, 1);
    popMatrix();
  }

  public void setup()
  {
    size(1024, 768, OPENGL);
    frameRate(60);
    physics.setDrag(0.5f);
    postSetup();
  }

  public void postSetup()
  {
    GridLayer grid = new GridLayer();
    add(grid);

  }

  @Override
  public void keyPressed(KeyEvent event)
  {
    if (event.getKey() == ' ')
    {
      testCue.go(layers.get(0), this);
    }

    if (event.getKey() == 'g')
    {
      // Gravity for one second.
      gravityOneSecond.go(layers.get(0), this);
    }
    
    if (event.getKey() == '1')
    {
      sucker.setStrength(-1f);
    }
    
    if(event.getKey() == '2')
    {
      sucker.setStrength(1f);
    }
    
    if(event.getKeyCode() == LEFT)
    {
      float drag = physics.getDrag();
      drag -= 0.01;
      if(drag < 0)
        drag = 0;
      
      System.out.println("Setting drag to -> " + drag);
      physics.setDrag(drag);
    }
    
    if(event.getKey() == 'h')
    {
      System.out.println("Turning on home force!");
      if(home.enabled)
      {
        home.enabled = false;
        physics.removeBehavior(home);
      }
        
      else
      {
        home.enabled = true;
        physics.addBehavior(home);
      }
    }
    
    if(event.getKeyCode() == RIGHT)
    {
      float drag = physics.getDrag();
      drag += 0.01;
      if(drag > 2)
        drag = 2;
      
      System.out.println("Setting drag to -> " + drag);
      physics.setDrag(drag);
    }
  }
  
  @Override
  public void mouseDragged()
  {
    sucker.setAttractor(new Vec3D(mouseX, mouseY, 0));
  }
  
  @Override
  public void mousePressed()
  {
    System.out.println("Mouse Down!");
    physics.addBehavior(sucker);
  }
  public void mouseReleased() 
  {
    physics.removeBehavior(sucker);
  };
}
