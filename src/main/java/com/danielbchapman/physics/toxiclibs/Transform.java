package com.danielbchapman.physics.toxiclibs;

/**
 * A collection of functions aimed at making content resolution flexible
 * by using the model from OpenGL
 */
public class Transform
{
  /**
   * Position this object as the following coordinates (-1.0, 1.0)
   * inside the following container. This will then return 
   * @param x
   * @param y
   * @param width
   * @param height
   * @return [xPixels, yPixels]  
   * 
   */
  public static int[] translate(float x, float y, int width, int height)
  {
    float tX = x * ((float) width) / 2f;
    float tY = y * ((float) height) / 2f;
    return new int[] { (int) tX + width/2, (int)tY + height /2 };
  }

  public static int[] translate(float x, float y, float z, int width, int height)
  {
    float tX = x * ((float) width) / 2f;
    float tY = y * ((float) height) / 2f;
    float tZ = z * ((float) width) / 2f;
    return new int[] { (int) tX + width/2, (int)tY + height /2, (int) tZ + width/2};
  }
  
  /**
   * <JavaDoc>
   * @param x
   * @param y
   * @param width
   * @param height
   * @return [xRelative, yRelative]
   */
  public static float[] translate(int x, int y, int width, int height)
  {
    float fX = (float) x;
    float fY = (float) y;
    float w = (float) width;
    float h = (float) height;
    float tX = (fX - width/2) / (w /2);
    float tY = (fY - height/2) / (h /2);;
    return new float[] { tX, tY };
  }
  
  public static float[] translate(int x, int y, int z, int width, int height)
  {
    float fX = (float) x;
    float fY = (float) y;
    float fZ = (float) z;
    float w = (float) width;
    float h = (float) height;
    float tX = (fX - width/2) / (w /2);
    float tY = (fY - height/2) / (h /2);
    float tZ = (fZ - width/2) / (w /2);
    return new float[] { tX, tY, tZ };
  } 
  
  public static float size(int x, int dim)
  {
    float fX = (float) (x - dim / 2);
    float dCenter = (float)dim /2f; 
    return fX / dCenter;
  }
  
  public static int size(float x, int dim)
  {
    float dF = (float) dim;
    if(x == 0f)
      return (int) (dF / 2f); //scale by zero
    else
      return (int)( dF /2f * x ) + (dim / 2); //scale 
  }
}
