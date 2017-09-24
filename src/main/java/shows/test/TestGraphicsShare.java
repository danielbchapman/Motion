package shows.test;

import processing.core.PConstants;
import processing.core.PGraphics;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionGraphicsClient;

public class TestGraphicsShare extends BaseScene
{
  MotionGraphicsClient client;

  @Override
  public void initialize(Motion motion)
  {
    client = MotionGraphicsClient
        .CreateClient(
            motion,
            //Explicitly link to an OpenGL context
            motion.createGraphics(motion.width, motion.height, PConstants.P3D), 
            "motion", 
            "Spout DX11 Sender");
  }

  @Override
  public void update(long time)
  {
    client.update();
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

      g.image(client.getImage(), 100, 100, 400, 300);      
    }
    else
    {
      g.text("No context for " + client.getAppName() + "::" + client.getServerName(), 50, 50); 
    }
  }
}
