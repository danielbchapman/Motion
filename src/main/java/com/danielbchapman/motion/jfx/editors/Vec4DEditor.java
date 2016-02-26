package com.danielbchapman.motion.jfx.editors;

import java.util.function.Consumer;

import toxi.geom.Vec4D;

import com.danielbchapman.motion.fx.BoundVec4D;


/**
 * A simple editor for a Vec4D object
 *
 */
public class Vec4DEditor extends BoundVec4D
{
  
  public Vec4DEditor(Vec4D v, Consumer<Vec4D> onUpdate)
  {
    super(v, onUpdate);
  }

}
