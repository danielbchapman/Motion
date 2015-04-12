package com.danielbchapman.physics.toxiclibs;

import toxi.geom.Vec3D;


/**
 * A GlobalForce is a collection of data that represents a force that is applied
 * to the whole world as opposed to a point. These are forces like Low Frequency Oscillation 
 * as well as forces like wind or gravity. A localized force based on a point should utilize
 * the "point force". 
 *
 */
public class GlobalForce
{
  Vec3D direction;
}
