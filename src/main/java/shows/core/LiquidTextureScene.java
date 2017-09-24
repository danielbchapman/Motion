package shows.core;

import java.util.ArrayList;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.opengl.PGraphics2D;

import com.danielbchapman.code.Pair;
import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionGraphicsClient;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.sun.jna.Platform;
import com.thomasdiewald.pixelflow.java.DwPixelFlow;
import com.thomasdiewald.pixelflow.java.dwgl.DwGLSLProgram;
import com.thomasdiewald.pixelflow.java.fluid.DwFluid2D;

public class LiquidTextureScene extends BaseScene
{ 
  //Pointers
  Motion motion;
  
  //Syphon or Spout client
  MotionGraphicsClient client;
  
  //Pixel Flow Variables
  /** We buffer the fluid in this context (don't draw on it) */
  PGraphics2D fluidBuffer;
  /** We draw to this context */
  PGraphics2D fluidImage;
  DwPixelFlow pixelFlow;
  DwFluid2D fluid;
  FluidDataCallback callback;
  
  //Variables
  int gridScale = 1;
  float vScale = 15;
  
  //Event Handling (Motion)
  ArrayList<Pair<MotionMouseEvent, MotionBrush>> eventsSinceUpdate = new ArrayList<>();
  
  @Override
  public void initialize(Motion motion)
  {
    this.motion = motion;
    
    client = MotionGraphicsClient
        .CreateClient(
            motion,
            //Explicitly link to an OpenGL context
            motion.createGraphics(motion.width, motion.height, PConstants.P3D), 
            Platform.isWindows() ? "motion" : "QLab", 
            Platform.isWindows() ? "Spout DX11 Sender" : "TestSurface");
    
    fluidBuffer = (PGraphics2D) motion.createGraphics(motion.width, motion.height, PConstants.P2D);
    fluidBuffer.smooth(4);
    
    //Where we draw
    fluidImage = (PGraphics2D) motion.createGraphics(motion.width, motion.height, PConstants.P2D);
    fluidImage.noSmooth();
    
    pixelFlow = new DwPixelFlow(motion);
    pixelFlow.print();
    pixelFlow.printGL();
    
    fluid = new DwFluid2D(pixelFlow, motion.width, motion.height, gridScale);
    fluid.param.dissipation_density     = 1.00f;
    fluid.param.dissipation_velocity    = 0.95f;
    fluid.param.dissipation_temperature = 0.70f;
    fluid.param.vorticity               = 0.50f;
    
    callback = new FluidDataCallback();
    fluid.addCallback_FluiData(callback);
  }
  
  @Override
  public boolean is2D()
  {
    return true;
  }
  
  @Override
  public void update(long time)
  {
    client.update();
    fluid.update();
    eventsSinceUpdate.clear();
  }

  @Override
  public void debug(PGraphics g)
  {
    float y = 50;
    g.text(String.format("dissipation_density:     %2f", fluid.param.dissipation_density), 50, y += 20);
    g.text(String.format("dissipation_velocity:    %2f", fluid.param.dissipation_velocity), 50, y += 20);
    g.text(String.format("dissipation_temperature: %2f", fluid.param.dissipation_temperature), 50, y += 20);
    g.text(String.format("vScale:                  %2f", vScale), 50, y += 20);
  }
  int count = 0;
  @Override
  public void draw(PGraphics g)
  {
    g.fill(0, 255, 0);
    g.rect(0, 0, g.width, g.height);
    g.fill(0);
    g.text("TEST GRAPHICS SHARE " + client.getAppName() + "::" + client.getServerName(), 10, 10);

    if (client.getGraphics() != null)
    {
      if (count++ % 120 == 0) 
      {
        System.out.println(" drawing " + client);        
      }

      //g.image(client.getImage(), 100, 100, 400, 300);
      fluidImage.beginDraw();
      fluidImage.image(client.getImage(), 0, 0, fluidImage.width, fluidImage.height);
      fluidImage.endDraw();
    }
    else
    {
      g.text("No context for " + client.getAppName() + "::" + client.getServerName(), 50, 50); 
    }
    
    fluidBuffer.beginDraw();
    fluidBuffer.background(64,64,0);
    fluidBuffer.endDraw();
    
    fluid.renderFluidTextures(fluidBuffer, 0); //Texture mode 0 | Fluid
    g.image(fluidBuffer, 0, 0, g.width, g.height);
  }

  private class FluidDataCallback implements DwFluid2D.FluidData
  {
    @Override
    // this is called during the fluid-simulation update step.
    public void update(DwFluid2D fluid) {
    
      for(Pair<MotionMouseEvent, MotionBrush> event : eventsSinceUpdate) 
      {
        MotionBrush brush = event.two;
        MotionMouseEvent mouse = event.one;
        
        System.out.println(mouse);
        float radius = 15;
        float px = mouse.x;
        float py = motion.height - mouse.y;
        float vx = (mouse.x - mouse.pmouseX) * (vScale);
        float vy = (mouse.y - mouse.pmouseY) * (-vScale);
        
        if (mouse.left)
        {
          radius = 20;
          fluid.addVelocity(px, py, radius, vx, vy);
        }
        
        if (mouse.center)
        {
          radius = 50;
          fluid.addDensity (px, py, radius, 1.0f, 0.0f, 0.40f, 1f, 1);
        }
        
        if (mouse.right)
        {
          radius = 15;
          fluid.addTemperature(px, py, radius, 15f);
        }
      }
  
      // use the text as input for density
      float mix = fluid.simulation_step == 0 ? 1.0f : 0.01f;
      addDensityTexture(fluid, fluidImage, mix);
    }
    
    // custom shader, to add density from a texture (PGraphics2D) to the fluid.
    public void addDensityTexture(DwFluid2D fluid, PGraphics2D pg, float mix){
      int[] pg_tex_handle = new int[1];
      pg_tex_handle[0] = pg.getTexture().glName;
      pixelFlow.begin();
      pixelFlow.getGLTextureHandle(pg, pg_tex_handle);
      pixelFlow.beginDraw(fluid.tex_density.dst);
      DwGLSLProgram shader = pixelFlow.createShader(this, "shaders/addDensity.frag");
      shader.begin();
      shader.uniform2f     ("wh"        , fluid.fluid_w, fluid.fluid_h);                                                                   
      shader.uniform1i     ("blend_mode", 6);   
      shader.uniform1f     ("mix_value" , mix);     
      shader.uniform1f     ("multiplier", 1);     
      shader.uniformTexture("tex_ext"   , pg_tex_handle[0]);
      shader.uniformTexture("tex_src"   , fluid.tex_density.src);
      shader.drawFullScreenQuad();
      shader.end();
      pixelFlow.endDraw();
      pixelFlow.end("app.addDensityTexture");
      fluid.tex_density.swap();
    }
  } 
  
  //MARK: - Motion Event Handling
  @Override
  public void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point)
  {
    if(brush.isDown())
    {
      eventsSinceUpdate.add(new Pair<>(point, brush));
    }
    brush.applyBrush(g, point);
  }  
}
