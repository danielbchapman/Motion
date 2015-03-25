package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import toxi.geom.Vec3D;

import com.danielbchapman.groups.Item;

public class TestBrushIo
{
  @Test //We can't use this, the classloading fails
  public void TestLoadSave() throws ClassNotFoundException
  //public static void main(String ... args) throws ClassNotFoundException
  {
    MotionBrush x = new MotionBrush();
    x.setBehaviorClass(ExplodeBehavior.class);
    x.variables.direction = new Vec3D(1, 2, 3);
    x.variables.position = new Vec3D(3, 4, 5);
    x.variables.force = 2;
    x.variables.maxForce = 3;
    x.variables.minForce = 0.1f;
    x.variables.noiseA= 1f;
    x.variables.noiseB = 2f;
    x.variables.noiseC = 3f;
    
    Item save =  x.save(null);
    MotionBrush y = MotionBrush.load(save);
    
    assertEquals("IO Failed with loading/saving MotionBrush", x, y);
  }
}
