package shows.test;

import com.danielbchapman.motion.core.Log;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.core.PhysicsBrush;
import com.danielbchapman.motion.core.Point;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class TestInverseExplodeBrush extends PhysicsBrush
{

  public TestInverseExplodeBrush()
  {
    this(new Vec3D(), 100f);
  }

  public TestInverseExplodeBrush(Vec3D direction, float magnitude)
  {
    vars.force = direction;
    vars.magnitude = magnitude;
    vars.maxForce = 10f;
    vars.minForce = 0.1f;
    
    //Default Brush Data
    vars.maxForceMin = 0f;
    vars.maxForceMax = 100f;
    
    vars.minForceMin = 0f;
    vars.minForceMin = 1f;
    
    vars.magnitudeMin = 0f;
    vars.magnitudeMax = 100f;
  }

  public Vec3D getPosition()
  {
    return this.vars.position;
  }

  public void apply(VerletParticle3D p)
  {
    Vec3D distanceV = p.sub(vars.position);
    float distance = distanceV.magnitude();
    float modifier = vars.magnitude / distance;// / mag; // 1 / n^2
    
    if(modifier > vars.maxForce)
      modifier =  vars.maxForce;
    
    if(modifier <  vars.minForce)
      return;//skip
    //noise

    //Away and in the direction 
    Vec3D vForce =  vars.force.normalizeTo(modifier);
    distanceV = distanceV.normalizeTo(modifier);  
    vForce = vForce.add(distanceV);
    if(p instanceof Point)
    {
      ((Point)p).addAngularForce(vForce.scale(5f).invert());
    }
    p.addForce(vForce.invert());
  }

  public void applyBrush(PGraphics g, MotionMouseEvent point)
  {
  	g.pushMatrix();
  	g.strokeWeight(10);
  	g.stroke(255);
  	g.point(point.x, point.y, point.z);
  	g.popMatrix();
  	//System.out.println("Test explore applyBrush");
    this.setPosition(new Vec3D(point));
    //FIXME, this is where it needs to apply
  }

  public void update(long time)
  {
  }
  
  public void setMinimum(float force)
  {
    vars.minForce = force;
  }
  
  public float getMinimum()
  {
    return vars.minForce;
  }
  
  public void setMax(float force)
  {
    vars.maxForce = force;
  }
  
  public float getMax()
  {
    return vars.maxForce;
  }
}
