package shows.test;

import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PGraphics;
import processing.opengl.PGraphics2D;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.KeyCombo;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.thomasdiewald.pixelflow.java.DwPixelFlow;
import com.thomasdiewald.pixelflow.java.fluid.DwFluid2D;

public class TestFluidScene extends BaseScene
{
  DwFluid2D fluid;
  boolean mousePressed = false;
  public int mouseX, mouseY, pmouseX, pmouseY;
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
        if(mousePressed){
          float px = mouseX;
          float py = motion.height - mouseY;
          float vx = (mouseX - pmouseX) * 75 + 30;
          float vy = (mouseY - pmouseY) * -30;
          
          fluid.addVelocity(px, py, 14, vx, vy);
          fluid.addDensity(px, py, 20, 0, .4f, 1.0f, 1.0f);
          fluid.addDensity(px, py, 8, 1.0f, 1.0f, 1.0f, 1.0f);
          fluid.addDensity(px, py, 12, 1.0f, 0, 0, 0.5f);
          fluid.addTemperature(px, py, 50, -.5f);
         }
      }
   });
  }
  
  @Override
  public void applyBrushBeforeDraw(MotionBrush brush, PGraphics g)
  {
    if(brush != null && brush.isActive())
    {
      System.out.println(brush);
      mousePressed = brush.mouseDown;
      mouseX = brush.mouseX;
      mouseY = brush.mouseY;
      pmouseX = brush.pmouseX;
      pmouseY = brush.pmouseY;
    }
    else
    {
      mousePressed = false;
    }
  }
  @Override
  public void update(long time)
  { 
    fluid.update();
    mousePressed = false;
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
}
