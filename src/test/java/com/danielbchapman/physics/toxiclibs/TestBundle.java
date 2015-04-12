package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import toxi.geom.Vec3D;

import com.danielbchapman.groups.Item;

public class TestBundle
{
  @Test //We can't use this, the classloading fails
  public void testLoadSave() throws ClassNotFoundException
  //public static void main(String ... args) throws ClassNotFoundException
  {
    MotionBrush x = new MotionBrush();
    x.setBehaviorClass(ExplodeBehavior.class);
    x.variables.direction = new Vec3D(1, 2, 3);
    x.variables.position = new Vec3D(3, 4, 5);
    x.variables.force = 2;
    x.variables.maxForce = 3;
    x.variables.minForce = 0.1f;
    x.variables.userA= 1f;
    x.variables.userB = 2f;
    x.variables.userC = 3f;
    
    Item save =  x.save(null);
    MotionBrush y = MotionBrush.load(save);
    
    assertEquals("IO Failed with loading/saving MotionBrush", x, y);
  }
  
  @Test
  public void testLinearHomeMath()
  {
    Point x = new Point(20, 20, 20, 1);
    x.home = new Vec3D(10, 10, 10);
    
    HomeBehaviorLinear3D tmp = new HomeBehaviorLinear3D(0.05f, 1f, 10f);
    
    tmp.apply(x);
    assertEquals(x + " -> " + x.x + ", " + x.y + ", " + x.z, Double.valueOf(x.x), Double.valueOf(19.5f)); 
  }
  
  @Test
  public void testOscillator()
  {
    FrequencyOscillationBehavior behavior = new FrequencyOscillationBehavior();
    Point x = new Point(0, 0, 0, 1);
    for(int i = 0; i < 100; i++)
    {
      behavior.update();
      behavior.apply(x);
      System.out.print("Offset => " + behavior.offset + " ");
      try
      {
        Thread.sleep(33);
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println(x.z);
    }
  }
}
