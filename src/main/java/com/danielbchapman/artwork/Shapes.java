package com.danielbchapman.artwork;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

/**
 * A simple collection of primitive 3D objects for use in renderings. These
 * are borrowed from a variety of areas with credit given. Each object has
 * been refactored to return a PShape instance so that they can be cached.
 */
public class Shapes
{
  public static PShape axes(PGraphics g)
  {
    PShape ret = g.createShape();

    // X
    ret.beginShape(PConstants.LINES);
    ret.stroke(255, 0, 0);
    ret.fill(255, 0, 0);
    ret.vertex(-50, 0, 0);
    ret.vertex(50, 0, 0);
    ret.endShape();

    // Y
    ret.beginShape(PConstants.LINES);
    ret.stroke(0, 255, 0);
    ret.fill(0, 255, 0);
    ret.vertex(0, -50, 0);
    ret.vertex(0, 50, 0);
    ret.endShape();

    // Z
    ret.beginShape(PConstants.LINES);
    ret.stroke(0, 0, 255);
    ret.fill(0, 0, 255);
    ret.vertex(0, 0, -50);
    ret.vertex(0, 0, 50);
    ret.endShape();
    return ret;
  }

  /**
   * Simple cylinder borrowed from
   * @link{ http://vormplus.be/blog/article/drawing-a-cylinder-with-processing }
   * @param g
   * @param sides
   * @param r1
   * @param r2
   * @param h <Return Description>  
   * 
   */
  public static PShape drawCylinder(PGraphics g, int sides, float r1, float r2, float h)
  {
    PShape ret = g.createShape();

    float angle = 360 / sides;
    float halfHeight = h / 2;
    // top
    ret.beginShape();
    for (int i = 0; i < sides; i++)
    {
      float x = PApplet.cos(PApplet.radians(i * angle)) * r1;
      float y = PApplet.sin(PApplet.radians(i * angle)) * r1;
      ret.vertex(x, y, -halfHeight);
    }
    g.endShape(PConstants.CLOSE);
    // bottom
    g.beginShape();
    for (int i = 0; i < sides; i++)
    {
      float x = PApplet.cos(PApplet.radians(i * angle)) * r2;
      float y = PApplet.sin(PApplet.radians(i * angle)) * r2;
      ret.vertex(x, y, halfHeight);
    }
    ret.endShape(PConstants.CLOSE);
    // draw body
    ret.beginShape(PConstants.TRIANGLE_STRIP);
    for (int i = 0; i < sides + 1; i++)
    {
      float x1 = PApplet.cos(PApplet.radians(i * angle)) * r1;
      float y1 = PApplet.sin(PApplet.radians(i * angle)) * r1;
      float x2 = PApplet.cos(PApplet.radians(i * angle)) * r2;
      float y2 = PApplet.sin(PApplet.radians(i * angle)) * r2;
      ret.vertex(x1, y1, -halfHeight);
      ret.vertex(x2, y2, halfHeight);
    }
    ret.endShape(PConstants.CLOSE);
    return ret;
  }

  // Generic routine to draw textured sphere
  /**
   * Pulled from the processing examples:
   * 
   * Texture Sphere 
   * by Mike 'Flux' Chang (cleaned up by Aaron Koblin). 
   * Based on code by Toxi. 
   * 
   * A 3D textured sphere with simple rotation control.
   * Note: Controls will be inverted when sphere is upside down. 
   *    Use an "arc ball" to deal with this appropriately.
   * <JavaDoc>
   * @param g the PGraphics instance
   * @param r the radius
   * @param t the texture
   * @param sDetail the detail, default is 35
   * @return A new textured sphere as a PShape
   */
  public static PShape texturedSphere(PGraphics g, float r, PImage t, int sDetail)
  {

    float rotationX = 0;
    float rotationY = 0;
    float velocityX = 0;
    float velocityY = 0;
    float globeRadius = 400;
    float pushBack = 0;

    float[] sphereX, sphereY, sphereZ;
    float[] cx = new float[sDetail];
    float[] cz = new float[sDetail];
    float sinLUT[];
    float cosLUT[];
    float SINCOS_PRECISION = 0.5f;
    int SINCOS_LENGTH = (int) (360.0f / SINCOS_PRECISION);

    // Initialize sphere
    sinLUT = new float[SINCOS_LENGTH];
    cosLUT = new float[SINCOS_LENGTH];

    for (int i = 0; i < SINCOS_LENGTH; i++)
    {
      sinLUT[i] = (float) Math.sin(i * PApplet.DEG_TO_RAD * SINCOS_PRECISION);
      cosLUT[i] = (float) Math.cos(i * PApplet.DEG_TO_RAD * SINCOS_PRECISION);
    }

    float delta = (float) SINCOS_LENGTH / sDetail;

    // Calc unit circle in XZ plane
    for (int i = 0; i < sDetail; i++)
    {
      cx[i] = -cosLUT[(int) (i * delta) % SINCOS_LENGTH];
      cz[i] = sinLUT[(int) (i * delta) % SINCOS_LENGTH];
    }

    // Computing vertexlist vertexlist starts at south pole
    int vertCount = sDetail * (sDetail - 1) + 2;
    int currVert = 0;

    // Re-init arrays to store vertices
    sphereX = new float[vertCount];
    sphereY = new float[vertCount];
    sphereZ = new float[vertCount];
    float angle_step = (SINCOS_LENGTH * 0.5f) / sDetail;
    float angle = angle_step;

    // Step along Y axis
    for (int i = 1; i < sDetail; i++)
    {
      float curradius = sinLUT[(int) angle % SINCOS_LENGTH];
      float currY = -cosLUT[(int) angle % SINCOS_LENGTH];
      for (int j = 0; j < sDetail; j++)
      {
        sphereX[currVert] = cx[j] * curradius;
        sphereY[currVert] = currY;
        sphereZ[currVert++] = cz[j] * curradius;
      }
      angle += angle_step;
    }

    // Create Sphere
    PShape ret = g.createShape();
    int v1, v11, v2;

    r = (r + 240f) * 0.33f;
    ret.beginShape(PConstants.TRIANGLE_STRIP);
    if (t != null)
      ret.texture(t);
    float iu = (float) (t.width - 1) / (sDetail);
    float iv = (float) (t.height - 1) / (sDetail);
    float u = 0, v = iv;
    for (int i = 0; i < sDetail; i++)
    {
      ret.vertex(0, -r, 0, u, 0);
      ret.vertex(sphereX[i] * r, sphereY[i] * r, sphereZ[i] * r, u, v);
      u += iu;
    }
    ret.vertex(0, -r, 0, u, 0);
    ret.vertex(sphereX[0] * r, sphereY[0] * r, sphereZ[0] * r, u, v);
    ret.endShape();

    // Middle rings
    int voff = 0;
    for (int i = 2; i < sDetail; i++)
    {
      v1 = v11 = voff;
      voff += sDetail;
      v2 = voff;
      u = 0;
      ret.beginShape(PConstants.TRIANGLE_STRIP);

      if (t != null)
        ret.texture(t);

      for (int j = 0; j < sDetail; j++)
      {
        ret.vertex(sphereX[v1] * r, sphereY[v1] * r, sphereZ[v1++] * r, u, v);
        ret.vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2++] * r, u, v + iv);
        u += iu;
      }

      // Close each ring
      v1 = v11;
      v2 = voff;
      ret.vertex(sphereX[v1] * r, sphereY[v1] * r, sphereZ[v1] * r, u, v);
      ret.vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2] * r, u, v + iv);
      ret.endShape();
      v += iv;
    }
    
    return ret;
  }

}
