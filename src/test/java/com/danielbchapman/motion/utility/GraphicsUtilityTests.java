package com.danielbchapman.motion.utility;

import java.util.ArrayList;

import org.junit.Test;

import com.danielbchapman.physics.toxiclibs.PointOLD;

public class GraphicsUtilityTests
{
  @Test
  public void TestCreateMotionGrid()
  {
    ArrayList<PointOLD> points = GraphicsUtility.createMotionGrid(20, 20, 15, 15, -10, 1, 
      (f)->
      {
        return new PointOLD(f[0], f[1], f[2], f[3]);
      });
  }
  
}
