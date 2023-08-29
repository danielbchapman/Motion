package com.danielbchapman.motion.core;

import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PGraphics;
import toxi.physics3d.VerletPhysics3D;


/**
 * A copy of the Scene class that implements all abstract methods
 * to cut down on verbosity.
 */
public class BaseScene extends Scene
{
  public HashMap<KeyCombo, Consumer<Motion>> baseKeyMap = new HashMap<>();
  public HashMap<String, Class<?>> basePropTypes = new HashMap<>();
  public SceneProperties props = new SceneProperties();
  
  public long currentTime = -1;
  public void mapKey(Character key, Consumer<Motion> action)
  {
    KeyCombo keyCombo = new KeyCombo(key);
    baseKeyMap.put(keyCombo, action);
  }
  
  public BaseScene()
  {
    mapKey('y', (app) -> {
      Log.info("Scene Properties");
      app.displayProperties(this.props, basePropTypes);
    });
    
    basePropTypes.put("demo", Float.class);
  }
  
  public void afterBrushes(PGraphics g)
  { 
  }

  @Override
  public String getName()
  {
    return getClass().getSimpleName();
  }

  @Override
  public void initialize(Motion motion)
  {
  }

  @Override
  public HashMap<KeyCombo, Consumer<Motion>> getKeyMap()
  {
    return baseKeyMap;
  }

  @Override
  public void update(long time)
  {
	  currentTime = time;
  }

  @Override
  public void draw(PGraphics g)
  {
  }

  @Override
  public void shutdown()
  {
  }

  @Override
  public boolean isPersistent()
  {
    return false;
  }

  @Override
  public void go(Motion motion)
  {
  }

  @Override
  public boolean applyBrushesAfterDraw()
  {
    return true;
  }

  @Override
  public void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point)
  {
    brush.applyBrush(g, point);
  }

  @Override
  public HashMap<String, Class<?>> getPropertyTypes()
  {
    return basePropTypes;
  }

	@Override
	public VerletPhysics3D get3DPhysics() {
		return null;
	}

	@Override
	public boolean isVerletPhysic3D()
	{
		return false;
	}
}
