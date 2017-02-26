package shows.test;

import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PGraphics;
import processing.opengl.PGraphics2D;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.KeyCombo;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.physics.toxiclibs.Util;
import com.thomasdiewald.pixelflow.java.DwPixelFlow;
import com.thomasdiewald.pixelflow.java.fluid.DwFluid2D;

public class TestFluidScene extends BaseScene
{
  DwFluid2D fluid;
  boolean left;
  boolean right;
  boolean center;
  
  public float mouseX, mouseY, pmouseX, pmouseY;
  HashMap<KeyCombo, Consumer<Motion>> testKeys = new HashMap<>();
  
  @Override
  public boolean is2D()
  {
    return true;
  }
  
  @Override
  public void initialize(Motion motion)
  { 
    testKeys.put(new KeyCombo(' '), (m)->{
      motion.println("This calls the outer motion on go and is this->" + TestFluidScene.this);
    } );
    
    DwPixelFlow context = new DwPixelFlow(motion);
    fluid = new DwFluid2D(context, motion.width, motion.height, 1);
    fluid.param.dissipation_velocity = .7f;
    fluid.param.dissipation_density = .99f;
    fluid.param.temperature_ambient = -.5f;
    fluid.param.apply_buoyancy = true;
     
    fluid.addCallback_FluiData(new DwFluid2D.FluidData(){
      public void update(DwFluid2D fluid){
        float px = mouseX;
        float py = motion.height - mouseY;
        float vx = (mouseX - pmouseX) * 75 + 30;
        float vy = (mouseY - pmouseY) * -30;
        
        if(left)
        {
          fluid.addDensity(px, py, Util.rand(5, 20), 0, .4f, 1.0f, 1.0f);
          fluid.addDensity(px, py, Util.rand(5, 20), 1.0f, 1.0f, 1.0f, 1.0f);
          fluid.addDensity(px, py, Util.rand(5, 20), 1.0f, 0, 0, 0.5f);
        }
        
        if(right)
        {
          fluid.addVelocity(px, py, 14, vx, vy);
        }
        
        if(center)
        {
          fluid.addTemperature(px, py, 50, -.5f);
        }
          
      }
   });
  }

  @Override
  public void update(long time)
  { 
    fluid.update();
    left = false;
    center = false;
    right = false;
  }
  

  public void draw(PGraphics g) 
  {    
    g.beginDraw();
    g.background(0);//black
    g.endDraw();
    
    fluid.renderFluidTextures((PGraphics2D) g, 0);
    
  }

  @Override
  public void go()
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
    if(brush != null && brush.isDown())
    {
      //System.out.println(brush);
      left = point.left;
      right = point.right;
      center = point.center;
      mouseX = point.x;
      mouseY = point.y;
      pmouseX = point.pmouseX;
      pmouseY = point.pmouseY;
    }
    else
    {
      left = false;
      right = false;
      center = false;
    } 
    
    brush.applyBrush(g, point);
  }
}
