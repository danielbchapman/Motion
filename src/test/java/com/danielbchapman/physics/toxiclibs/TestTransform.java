package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestTransform
{
  @Test
  public void testTransform()
  {
    int w = 800;
    int h = 600;
    
    {//Zero/Center
      int x = 400;
      int y = 300;
      //Zero
      float fX = 0f;
      float fY = 0f;
      float[] rl = Transform.translate(x, y, w, h);
      int[] pix = Transform.translate(fX, fY, w, h);
      //Zero
      assertTrue("X == " + x + "?-> " + pix[0], pix[0] == x);
      assertTrue("Y == " + y + "?-> " + pix[1], pix[1] == y);
      assertTrue("fX == " + fY + "?-> " + rl[0], rl[0] == fX);
      assertTrue("fY == " + fY + "?-> " + rl[1], rl[1] == fY);
    }
    
    {//TR
      int x = 800;
      int y = 0;
      //Zero
      float fX = 1f;
      float fY = -1f;
      float[] rl = Transform.translate(x, y, w, h);
      int[] pix = Transform.translate(fX, fY, w, h);
      //Zero
      assertTrue("X == " + x + "?-> " + pix[0], pix[0] == x);
      assertTrue("Y == " + y + "?-> " + pix[1], pix[1] == y);
      assertTrue("fX == " + fY + "?-> " + rl[0], rl[0] == fX);
      assertTrue("fY == " + fY + "?-> " + rl[1], rl[1] == fY);  
    }
    
    {//BR
      int x = 800;
      int y = 600;
      //Zero
      float fX = 1f;
      float fY = 1f;
      float[] rl = Transform.translate(x, y, w, h);
      int[] pix = Transform.translate(fX, fY, w, h);
      //Zero
      assertTrue("X == " + x + "?-> " + pix[0], pix[0] == x);
      assertTrue("Y == " + y + "?-> " + pix[1], pix[1] == y);
      assertTrue("fX == " + fY + "?-> " + rl[0], rl[0] == fX);
      assertTrue("fY == " + fY + "?-> " + rl[1], rl[1] == fY);  
    }
    
    {//BL
      int x = 0;
      int y = 600;
      //Zero
      float fX = -1f;
      float fY = 1f;
      float[] rl = Transform.translate(x, y, w, h);
      int[] pix = Transform.translate(fX, fY, w, h);
      //Zero
      assertTrue("X == " + x + "?-> " + pix[0], pix[0] == x);
      assertTrue("Y == " + y + "?-> " + pix[1], pix[1] == y);
      assertTrue("fX == " + fY + "?-> " + rl[0], rl[0] == fX);
      assertTrue("fY == " + fY + "?-> " + rl[1], rl[1] == fY);  
    }
    
    {//TL
      int x = 0;
      int y = 0;
      //Zero
      float fX = -1f;
      float fY = -1f;
      float[] rl = Transform.translate(x, y, w, h);
      int[] pix = Transform.translate(fX, fY, w, h);
      //Zero
      assertTrue("X == " + x + "?-> " + pix[0], pix[0] == x);
      assertTrue("Y == " + y + "?-> " + pix[1], pix[1] == y);
      assertTrue("fX == " + fY + "?-> " + rl[0], rl[0] == fX);
      assertTrue("fY == " + fY + "?-> " + rl[1], rl[1] == fY);  
    }
    
    {//quarter/quarter
      int x = 200;
      int y = 150;
      //Zero
      float fX = -0.5f;
      float fY = -0.5f;
      float[] rl = Transform.translate(x, y, w, h);
      int[] pix = Transform.translate(fX, fY, w, h);
      //Zero
      assertTrue("X == " + x + "?-> " + pix[0], pix[0] == x);
      assertTrue("Y == " + y + "?-> " + pix[1], pix[1] == y);
      assertTrue("fX == " + fY + "?-> " + rl[0], rl[0] == fX);
      assertTrue("fY == " + fY + "?-> " + rl[1], rl[1] == fY);  
    }
    
  }
  
  @Test
  public void testSingle()
  {
    int dim = 800;
    { //zero
      int x = 400;
      float f = 0f;
      int r = Transform.size(f, dim);
      float fR = Transform.size(x, dim);
      assertTrue("Zero Test << " + r + " == " + x, r == x);
      assertTrue("Zero Test << " + fR + " == " + f, fR == f);
    }
    
    { //75%
      int x = 600;
      float f = .5f;
      int r = Transform.size(f, dim);
      float fR = Transform.size(x, dim);
      assertTrue("75% Test << " + r + " == " + x, r == x);
      assertTrue("75% Test << " + fR + " == " + f, fR == f);
    }
    
    { //25%
      int x = 200;
      float f = -.5f;
      int r = Transform.size(f, dim);
      float fR = Transform.size(x, dim);
      assertTrue("? == " + f + "?-> ", r == x);
      assertTrue("? == " + f + "?-> ", fR == f);
    }
    
  }
}
