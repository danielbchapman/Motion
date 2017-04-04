package shows.oz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PGraphics;
import processing.opengl.PGraphics2D;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.KeyCombo;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.utility.GraphicsUtility;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Util;
import com.thomasdiewald.pixelflow.java.DwPixelFlow;
import com.thomasdiewald.pixelflow.java.fluid.DwFluid2D;

public class WitchSmokeRed extends BaseScene
{
  DwFluid2D fluid;
  HashMap<KeyCombo, Consumer<Motion>> testKeys = new HashMap<>();
  
  ArrayList<MotionMouseEvent> eventsSinceUpdate = new ArrayList<>();
  
  @Override
  public boolean is2D()
  {
    return true;
  }
  
  @Override
  public void initialize(Motion motion)
  { 
    testKeys.put(new KeyCombo(' '), (m)->{
      motion.println("This calls the outer motion on go and is this->" + WitchSmokeRed.this);
    } );
    
    DwPixelFlow context = new DwPixelFlow(motion);
    fluid = new DwFluid2D(context, motion.width, motion.height, 1);
    fluid.param.dissipation_velocity = .7f;
    fluid.param.dissipation_density = .99f;
    fluid.param.temperature_ambient = -.5f;
    fluid.param.apply_buoyancy = true;
     
    fluid.addCallback_FluiData(new DwFluid2D.FluidData(){
      public void update(DwFluid2D fluid){
        for(MotionMouseEvent e : eventsSinceUpdate)
        {
          float px = e.x;
          float py = motion.height - e.y;
          float vx = (e.x - e.pmouseX) * 75 + 30;
          float vy = (e.y - e.pmouseY) * -30;
          
          if(e.left)
          {
            fluid.addDensity(px, py, 8, .5f, .1f, 0f, 1.0f);
            fluid.addDensity(px, py, 20, 1f, 0f, 0f, 0f);
            fluid.addDensity(px, py, 5, 1.0f, 0, 0, 0.5f);
          }
          
          if(e.right)
          {
            fluid.addVelocity(px, py, 14, vx, vy);
          }
          
          if(e.center)
          {
            fluid.addTemperature(px, py, 50, -.5f);
          } 
        }  
      }
   });
  }

  @Override
  public void update(long time)
  { 
    fluid.update();
    eventsSinceUpdate.clear();
  }
  
  public void draw(PGraphics g) 
  {    
    
    g.beginDraw();
    g.background(0,0,0, 32);
    g.stroke(255, 255, 255);
    g.strokeWeight(3);
    //g.background(0);//black
    g.endDraw();
    
    fluid.renderFluidTextures((PGraphics2D) g, 0);
    
  }

  @Override
  public void go(Motion motion)
  {    
  }
  

  @Override
  public HashMap<KeyCombo, Consumer<Motion>> getKeyMap()
  {
    return testKeys;
  }
  
  @Override
  public void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point)
  {
    if(brush.isDown())
    {
      eventsSinceUpdate.add(point);
    }
    brush.applyBrush(g, point);
  }
}
