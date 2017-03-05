package shows.gravitationalwaves;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.motion.utility.GraphicsUtility;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.utility.Utility;

import processing.core.PGraphics;

public class BleedingGrid extends BleedingLayer
{
  int columns;
  int rows;
  int spacing;
  
  public BleedingGrid(float width, float height, float spacing)
  {
    columns = 1 + (int) ((width / spacing));
    rows = 1 + (int)((height / spacing));
    this.spacing = (int)spacing;
    
    //add 3 padding
    columns += 3;
    rows += 3;
    //overrides init()
    ArrayList<Point> p = GraphicsUtility.createMotionGrid(columns, rows , this.spacing, this.spacing, 0, 1, GraphicsUtility::point);
    super.points = p.toArray(new Point[p.size()]);  
    System.out.printf("Constructing grid at columns %d rows %d spacing %d", columns, rows, this.spacing );
  }

  @Override
  public void reanderAfterBleed(PGraphics g)
  { 
    g.pushMatrix();
    g.translate(-spacing * 3,  -spacing * 3); //fill outside by + 3 
    g.noFill();
    g.stroke(255);
    g.strokeWeight(3f);
    GraphicsUtility.drawMotionGridAsLines(g, points, columns);
    
    g.popMatrix();
  }

  @Override
  public Point[] init()
  { 
    return null; //not used
  }

  @Override
  public void update()
  { 
  }

  @Override
  public void go(MotionEngine engine)
  {     
    try
    {
      Cue q = Cue.create(
          "load environment", 
          Actions.loadEnvironment(new File("gravity/bleed-grid-balanced.env"))
      );
      clear();
      q.go(this, engine);
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override
  public String getName()
  {
    return "bleeding-grid";
  }
}
