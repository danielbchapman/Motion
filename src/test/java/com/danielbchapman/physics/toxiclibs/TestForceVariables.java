package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Random;

import org.junit.Test;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class TestForceVariables
{
  private ForceVariables createTestData()
  {
    Random rand = new Random();
    ForceVariables x = new ForceVariables();
    x.force = Vec3D.randomVector();
    x.position = Vec3D.randomVector();
    x.backup = Vec3D.randomVector();
    x.scaledForce = Vec3D.randomVector();
    
    x.radius = rand.nextFloat();
    x.radiusMin = rand.nextFloat();
    x.radiusMax = rand.nextFloat();
    
    x.magnitude = rand.nextFloat();
    x.magnitudeMin = rand.nextFloat();
    x.magnitudeMax = rand.nextFloat();
    
    x.maxForce = rand.nextFloat();
    x.maxForceMin = rand.nextFloat();
    x.maxForceMax = rand.nextFloat();
    
    x.minForce = rand.nextFloat();
    x.minForceMin = rand.nextFloat();
    x.minForceMax = rand.nextFloat();
    
    x.userA = rand.nextFloat();
    x.userAMin = rand.nextFloat();
    x.userAMax = rand.nextFloat();
    
    x.userB = rand.nextFloat();
    x.userBMin = rand.nextFloat();
    x.userBMax = rand.nextFloat();
    
    x.userC = rand.nextFloat();
    x.userCMin = rand.nextFloat();
    x.userCMax = rand.nextFloat();
    
    x.timeStep = rand.nextFloat();
    x.enabled = !x.enabled;// Opposite of default
    x.running = !x.running;// Opposite of default
    
    x.petName = "test";//There's a problem here when there's a null we get "" or null
    return x;
  }

  @Test
  public void testLineSerialization()
  {
    Random rand = new Random();
    ForceVariables x = createTestData();
    String line = ForceVariables.toLine(x);
    ForceVariables y = ForceVariables.fromLine(line);
    String debug = "\n\n" + x.toString() + "\n" + "==\n" + y.toString() + "\n" + ForceVariables.toLine(x) + "\n" + ForceVariables.toLine(y) + "\n";


    String a = ForceVariables.toLine(x);
    String b = ForceVariables.toLine(y);
    if(!a.equals(b))
    {
        for(int i = 0; i <a.length(); i++)
        {
          if(a.charAt(i) != b.charAt(i))
            System.out.println("[" + i + "]> " + a.charAt(i) + " != " + b.charAt(i));
        }   
    }
    assertTrue("Data equivilence", a.equals(b));
    assertTrue(debug, x.equals(y));
  }

  @Test
  public void testMotionBrushIO()
  {
    ExplodeBehavior inst = new ExplodeBehavior();
    
    inst.vars = createTestData();
    String data = MotionInteractiveBehavior.save(inst);
    MotionInteractiveBehavior load = MotionInteractiveBehavior.load(data);

    assertTrue(inst.vars.equals(load.vars));
  }
}
