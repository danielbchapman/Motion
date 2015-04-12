package com.danielbchapman.physics.vertlet;

import processing.core.PVector;

public class Point3D
{
  PVector force = new PVector(); //reset on each count
  float mass = 2.0f;  
  int pixelCentimeters = 100;
  //Public
  public PVector position = new PVector();
  public PVector velocity = new PVector();
  
  //Private
  private PVector acceleration = new PVector();
  private PVector lastAcceleration = new PVector();
  
  public Point3D(){
  }
  
  public Point3D(float x, float y, float z){
    position = new PVector(x, y, z);
  }
  //Apply a specific force to an object 
  public void addForce(PVector f){
    force.add(f);
  }
  
  //all things fall at the same rate!  
  public void addGravity(PVector f){
    addForce(PVector.mult(f, mass));
  }
  public void collision(){
    if(position.y > 500){
      velocity.y *= -0.5; //coefficient of a "bouncy" ball
    }
  }
  
  public void debug(){
    System.out.println("Debug information for Point " + position);
    System.out.println("velocity         ->" + velocity);
    System.out.println("force            ->" + force);
    System.out.println("Acceleration     ->" + acceleration);
    System.out.println("LastAcceleration ->" + lastAcceleration);
  } 
  
  public void drag(){
    //D= 1/2 * density * velocity^2 * dragCoefficient * cross-sectional-area
   force = PVector.mult(force, 0.5f * 50.0f * 0.47f * (float)Math.PI/1000.0f); 
  }
 
  public void euler(int dTime){
    //a = force / mass
    //velocity += a * step
    //position += velocity * dTime
  }
  
  public String posData(){
    return position.x + "\t" + position.y + "\t" + position.z;  
  }
  
  /** Time Corrected Verlet Integration */
  public void tcv(float step, float lastStep){
    if( lastStep < 1){
      lastStep = step; //identity correction faction
    }
    
    //THis is position Verlet, we need Velocity Verlet
    //xi+1 = xi + (xi - xi-1) * (dti / dti-1) + a * dti * dti
    //or
    //x = x + (x – xl)*h/hl + a*h*(h + hl)/2
    
    lastAcceleration = acceleration.get();
    
    //position += velocity * time_step + ( 0.5 * last_acceleration * time_step^2 )
    PVector vDeltaT = PVector.mult(velocity, step);
    PVector aDeltaT = PVector.mult(lastAcceleration, step * step * 0.5f);
    //position.add(vDeltaT);
    //position.add(aDeltaT);
    position.add(PVector.mult(PVector.add(vDeltaT, aDeltaT), pixelCentimeters));
    
    //Update acceleration
    acceleration = PVector.div(force, mass);
    PVector avgAccel = PVector.add(acceleration, lastAcceleration);
    avgAccel.div(2.0f);
    
    //Apply velocity
    avgAccel.mult(step);
    velocity.add(avgAccel);
    force = new PVector(); //clear data
  }
}
