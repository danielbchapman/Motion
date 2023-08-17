package com.danielbchapman.brushes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.PersistentVariables;
import com.danielbchapman.physics.toxiclibs.Util;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

public abstract class VectorBrushPORT extends MotionBrush
{
  static final Map<String, String> FIELD_NAMES;
  static {
    HashMap<String, String> m = new HashMap<>();
    m.put(PersistentVariables.Fields.MAGNITUDE, "Magniture");
    m.put(PersistentVariables.Fields.MAX_FORCE, "Max Force");
    m.put(PersistentVariables.Fields.MIN_FORCE, "Min Force");
    m.put(PersistentVariables.Fields.POS_X, "Position");
    m.put(PersistentVariables.Fields.POS_Y, "Position");
    m.put(PersistentVariables.Fields.POS_Z, "Position");
    m.put(PersistentVariables.Fields.FOR_X, "Force");
    m.put(PersistentVariables.Fields.FOR_Y, "Force");
    m.put(PersistentVariables.Fields.FOR_Z, "Force");
    m.put(PersistentVariables.Fields.USER_C, "Image");
    
    FIELD_NAMES = Collections.unmodifiableMap(m);
  }
  
  float splitSize = 2f;
  boolean idle = false;
  @Getter
  @Setter
  protected int color;
  @Getter
  @Setter
  public int opacityStart = 255;
  public int opacityEnd = 16;
  public int minOpacity = 16;
  public int maxOpacity = 255;
  
  public float sizeStart = 1.0f;
  public float sizeEnd = 0.2f;
  public float minSize = 0.1f;
  public float maxSize = 2.0f;
  
  protected long fadeTime = 3000; // 1 second
  protected long sizeTime = 2000;

  //FIXME - Vector Brushes draw between lines..I think This needs to be defined
  protected boolean firstPass = false;
  protected Vec3D drawLastPosition = null;
  /**
   * A method that can be called before any drawing operations occur but 
   * after the update method is called to set opacity
   */
  public void beforeDraw()
  {
  }
  /**
   * A method to draw this brush at a specific point. This 
   * will be called multiple times per large vector so 
   * speed would be advised.
   * @param g the graphics context
   * @param p the point to draw at (world based, not change based)  
   * @param opacity the opacity to draw this stroke with
   * @param sizeModifier a 0f-1f percentage modifier for the size larger values are
   * permitted. 
   */
  public abstract void applyBrush(PGraphics g, Vec3D p, int opacity, float sizeModifier);

  @Override
  public boolean isVectorBrush() 
  {
	  return true;
  }
  
  @Override
  public void applyBrush(PGraphics g, MotionMouseEvent point)
  {
    //tick();
    if(vars.position == null)
      return;
    g.pushMatrix();
    g.stroke(255);
    g.fill(255);
    
    int currentOpacity = opacityStart;
    long elapsed = currentTime - startTime;
    if(isFadingBrush())
    {
      currentOpacity =
          (int) Util.fade(
              opacityStart, 
              opacityEnd, 
              fadeTime, 
              elapsed, 
              minOpacity, 
              maxOpacity);
    }
    
    float sizeModifier = sizeStart;
    if(isVariableSizeBrush())
    {
      sizeModifier = Util.fade(
          sizeStart, 
          sizeEnd, 
          sizeTime,
          elapsed,
          minSize,
          maxSize);
    }
    if(drawLastPosition == null || !firstPass) //start the brush
    {
      this.drawLastPosition = vars.position;
      if(!idle)
        applyBrush(g, vars.position, currentOpacity, sizeModifier);
      firstPass = true;
      idle = true;
      //System.out.println("idle");
    }
    else
    {
      Vec3D scalar = new Vec3D(vars.position.x, vars.position.y, vars.position.z);
      scalar.subSelf(drawLastPosition);
      float length = scalar.magnitude();
      int steps = (int) (length / splitSize);
      //Paint a single point
      if(steps <= 1)
      {
        if(vars.position.equals(drawLastPosition))
        {
          //System.out.println( vars.position.x + " ->" + drawLastPosition);
          idle = true;
        }
        if(!idle)
        {
          applyBrush(g, vars.position, currentOpacity, sizeModifier);
          drawLastPosition = vars.position;
        }
      }
      else //Paint multiple points
      {
        //first point already exists, skip it (last)
        for(int i = 1; i < steps; i++)
        {
          float subset = i * splitSize;
          Vec3D newSub = scalar.getNormalizedTo(subset);
          Vec3D newPos = drawLastPosition.add(newSub);
          applyBrush(g, newPos, currentOpacity, sizeModifier);
        }
        //Draw the new point
        applyBrush(g, vars.position, currentOpacity, sizeModifier);
        drawLastPosition = this.vars.position;
//        System.out.println("Paining multiple!");
      }
    }
    
    g.popMatrix();
    
    //Update position
  }

  @Override
  public String getName()
  {
    return "Vector Brush";
  } 
  
  @Override
  public Map<String, String> getFieldNames()
  {
    return FIELD_NAMES;
  }
  
  @Override
  public void update(long time) {
  }
  /**
   * Copy the brush variables for cloning. If you need it saved
   * this is where you put it.
   * @return
   */
  public abstract VectorBrushPORT copyBrushVariables();
  
  //FIXME this is terrible, we should use reflection or something
  @Override
  public MotionBrush deepCopy() 
  {
	VectorBrushPORT copy = copyBrushVariables();
	copy.color = color;
	if(drawLastPosition != null) 
	{
		copy.drawLastPosition = drawLastPosition.copy();	
	}
	
	copy.fadeTime = fadeTime;
	copy.firstPass = firstPass;
	copy.framesDrawn = framesDrawn;
	copy.idle = idle;
	copy.maxOpacity = maxOpacity;
	copy.maxSize = maxSize;
	copy.minOpacity = minOpacity;
	copy.minSize = minSize;
	copy.opacityEnd = opacityEnd;
	copy.opacityStart = opacityStart;
	
	return copy;	
  }
}
