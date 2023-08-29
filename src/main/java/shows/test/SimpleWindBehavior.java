package shows.test;

import com.danielbchapman.motion.core.SaveableParticleBehavior3D;
import com.danielbchapman.physics.toxiclibs.PersistentVariables;
import com.danielbchapman.physics.toxiclibs.PointOLD;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class SimpleWindBehavior extends SaveableParticleBehavior3D<SimpleWindBehavior>
{
  PApplet applet;
  
  
  public SimpleWindBehavior()
  {
    this(new Vec3D(1, 0, 0));
  }
  
  public SimpleWindBehavior(Vec3D direction)
  {
    this.vars.force = direction.copy();
  }
  
  @Override
  public void apply(VerletParticle3D p)
  {   
    if (p instanceof PointOLD)
    {
      PointOLD px = (PointOLD) p;
    }
    
    if(applet != null){
      p.addForce(vars.scaledForce.scale(applet.noise(p.x, p.y, p.z)) );      
    } else {
      p.addForce(vars.scaledForce);      
    }

  }

  @Override
  public void configure(float timeStep)
  {    
    vars.timeStep = timeStep;
    setForce(vars.force);
  }

  public void setForce(Vec3D force) {
    this.vars.force = force;
    this.vars.scaledForce = force.scale(vars.timeStep * vars.timeStep);
  }
  
  @Override
  public String save()
  {
    System.out.println(this + " AngularGravity? " + vars.running);
    return PersistentVariables.toLine(vars);
  }

  @Override
  public SimpleWindBehavior load(String data)
  {
    SimpleWindBehavior copy = new SimpleWindBehavior();
    copy.vars = this.vars.copy();
    return copy;
  }
  
  public void updateMagnitude(float newValue)
  {
    Vec3D f = vars.backup.copy().normalize();
    f.scaleSelf(newValue);
    setForce(f);
  }
  
  public void update(PApplet app, long time)
  {
    
  }

}
