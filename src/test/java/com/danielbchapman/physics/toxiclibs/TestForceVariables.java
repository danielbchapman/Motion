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
    x.magnitude = rand.nextFloat();
    x.maxForce = rand.nextFloat();
    x.minForce = rand.nextFloat();
    x.timeStep = rand.nextFloat();
    x.userA = rand.nextFloat();
    x.userB = rand.nextFloat();
    x.userC = rand.nextFloat();
    x.enabled = !x.enabled;// Opposite of default
    x.running = !x.running;// Opposite of default
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

    ;
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
