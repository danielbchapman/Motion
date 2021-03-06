package shows.gravitationalwaves;

import java.io.File;
import java.util.ArrayList;

import processing.core.PGraphics;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.motion.utility.GraphicsUtility;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class BleedingPointGrid extends BleedingLayer
{
  int columns;
  int rows;
  int spacing;
  private final String name;
  public BleedingPointGrid(float width, float height, float spacing, String name)
  {
    this.name = name;
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
    for(Point p : points)
      g.point(p.x,  p.y,  p.z);
    
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
    return name;
  }
}
