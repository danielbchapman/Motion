package com.danielbchapman.brushes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.PersistentVariables;

/**
 * A simple implementation of the String Serialize pattern that
 * loads the variables from a file or saves those variables to the
 * file.
 */
public abstract class SaveableBrush extends MotionInteractiveBehavior implements IBrush
{
  // Volatile variables
  @Getter
  protected boolean drawing = false;
  protected boolean firstPass = false;
  protected Vec3D drawLastPosition = null;
  protected long mouseDown;
  protected int framesDrawn;
//  /**
//   * The variables used for storage.
//   */
//  protected PersistentVariables vars = new PersistentVariables();

  /**
   * A method called one per drawing cycle for this brush. However
   * this can be called multiple times and it calculates the value.  
   */
  public void tick()
  {
    if (framesDrawn < 0)
      framesDrawn = 0;

    framesDrawn++;
  }

  @Override
  public void apply(VerletParticle3D p)
  {
  }

  @Override
  public void configure(float timeStep)
  {
    vars.timeStep = timeStep;
  }

  @Override
  public void setPosition(Vec3D location)
  {
    this.vars.position = location;
  }

  @Override
  public Vec3D getLastPosition()
  {
    return drawLastPosition;
  }

  @Override
  public Vec3D getCurrentPosition()
  {
    return this.vars.position;
  }

  /*
   * (non-Javadoc)
   * @see com.danielbchapman.brushes.IBrush#endDraw()
   */
  @Override
  public void endDraw()
  {
    drawing = false;
    firstPass = false;
    drawLastPosition = null;
    mouseDown = -1;
    framesDrawn = -1;
  }

  /*
   * (non-Javadoc)
   * @see com.danielbchapman.brushes.IBrush#startDraw()
   */
  @Override
  public void startDraw()
  {
    mouseDown = System.currentTimeMillis();
    framesDrawn = 0;
    drawing = true;
    firstPass = false;
    drawLastPosition = null;
  }

  /**
   * <em>
   * Please override this if you require the behavior. This method is an 
   * event handler
   * </em>
   * <p>
   * An abstract method that is called before the draw operation for this 
   * particular frame. Adjustments to color, opacity, etc... can be 
   * applied here. The actual implementation is dependent on the subclass.
   * </p>
   */
  public void update()
  {
  }

}
