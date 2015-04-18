package com.danielbchapman.physics.toxiclibs;

import java.util.function.Function;

import toxi.geom.Vec3D;
import toxi.physics3d.behaviors.GravityBehavior3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;


/**
 * A collection of actions and forces that can be 
 * used by other layers and scenes. These are all "defaults"
 * and can be adjusted as needed. Ideally this allows scenes
 * to share fundamental environmental variables without doubling 
 * up accidentally.
 */
public class Actions
{
  public static MotionEngine engine;
  //Forces
  
  public static Action dragToVeryLow = new Action("Drag to 0.01f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.01f);
      });
  public static Action dragToNone = new Action("Drag to 0.0f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.0f);
      });
  public static Action dragToBasic = new Action("Drag to 0.4f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.4f);
      });
  
  public static AngularGravityBehavior3D gravity = new AngularGravityBehavior3D(new Vec3D(0f, 0.01f, 0f));
  
  public static Action gravityOff = new Action("Gravity Off", 0, null, 
      (x)->{
        x.removeBehavior(gravity);
      });
  
  public static Action gravityOn = new Action("Gravity On", 0, null, 
      (x)->{
        x.addBehavior(gravity);
      });
  public static HomeBehavior3D home = new HomeBehavior3D(new Vec3D(0, 0, 0));
  public static HomeBehaviorLinear3D homeLinear = new HomeBehaviorLinear3D(0.005f, .1f, 10f);
  
  public static Action homeOff = new Action("Home Off", 0, null, 
      (x)->{
        x.removeBehavior(home);
      });
  
  public static Action homeOn = new Action("Home On", 0, null, 
      (x)->{
        x.addBehavior(home);
      });
 
  public static Action homeLinearOff = new Action("Home Linear Off", 0, null, 
      (x)->{
        x.removeBehavior(homeLinear);
      });
  
  public static Action homeLinearOn = new Action("Home Linear On", 0, null, 
      (x)->{
        x.addBehavior(homeLinear);
      });
  
  public static Action homeTo(float f)
  {
    return new Action("Home to " + f, 0, null, 
        (x)->{
          home.vars.maxForce = f;
        });
  }
  
  public static Action dragTo(float f)
  {
    return new Action("Drag to " + f, 0, null, 
        (x)->{
          x.getPhysics().setDrag(f);
        });
  }
  
  public static Action homeTo0dot2f = new Action("Home to 0.2f", 0, null, 
      (x)->{
        home.vars.maxForce = 0.2f;
      });
  public static Action homeTo01f = new Action("Home to 1f", 0, null, 
      (x)->{
        home.vars.maxForce = 1f;
      });
  
  public static Action homeTo2f = new Action("Home to 2f", 0, null, 
      (x)->{
        home.vars.maxForce = 2f;
      });
  
  public static Action homeTo10f = new Action("Home to 10f", 0, null, 
      (x)->{
        home.vars.maxForce = 10f;
      });
}
