package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import toxi.geom.Vec3D;

public class TestForceVariables
{
  @Test
  public void testLineSerialization()
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
    x.enabled = false; //not default
    x.running = true; //not default
    
    String line = ForceVariables.toLine(x);
    ForceVariables y = ForceVariables.fromLine(line);
    String debug = 
        "\n\n" +
        x.toString() + "\n" +
        "==\n" +
        y.toString() + "\n" +
        ForceVariables.toLine(x) + "\n" +
        ForceVariables.toLine(y) + "\n";
    
        ;
    assertTrue(debug, x.equals(y));
  }
}
