package com.danielbchapman.video.onestream;

import processing.core.PApplet;
import processing.core.PConstants;

public class TestApp extends PApplet
{
  public void setup()
  {
    size(800,600, P3D);
    smooth();
    background(0);
    fill(color(0,0,255));
    rect(0,0, 400,400);
    int t = 250;
    int w = 400;
    int h = 400;
    int a =  color(0,255,0);
    int b = color(255,0,0);
    int grid = color(255, 255, 255, 128);
    int hS = h-t-t+2;
    int wS = w-t-t+2;
//    DrawingUtil.drawGradient(g, a, b, 0, t-1, t, hS, DrawingUtil.X_AXIS);//T
//    DrawingUtil.drawGradient(g, b, a, w-t, t-1, t, hS, DrawingUtil.X_AXIS);//B
//    DrawingUtil.drawGradient(g, a, b, t-1, 0, wS, t, DrawingUtil.Y_AXIS);//L
//    DrawingUtil.drawGradient(g, b, a, t-1, h-t, wS, t, DrawingUtil.Y_AXIS);//R
//    //Draw corners
//    DrawingUtil.drawGradientRadial(g, b, a, t, t, t, PConstants.PI, PConstants.PI+PConstants.HALF_PI);//TL
//    DrawingUtil.drawGradientRadial(g, b, a, w-t, t, t, PConstants.PI+PConstants.HALF_PI, PConstants.TWO_PI);//TR
//    DrawingUtil.drawGradientRadial(g, b, a, w-t, h-t,t, 0, PConstants.HALF_PI);//BL
//    DrawingUtil.drawGradientRadial(g, b, a, t, h-t, t, PConstants.HALF_PI, PConstants.PI);//BR
//    
    //DrawingUtil.drawGrid(g, 0, 0, width, height, 10, grid);    
    int white = color(0,0,0, 128f);
    //int black = parent.color(blendTop.y, blendTop.y, blendTop.y, 255);
    int black = color(0,0,0,0f);
    
    //DrawingUtil.drawGradient(g, white, black, 0, 0, w, t, DrawingUtil.Y_AXIS);
    DrawingUtil.drawGradient(g, black, white, 0, h - t, w, t, DrawingUtil.Y_AXIS);
    //DrawingUtil.drawGradient(g, white, black, 0, 0, t, h, DrawingUtil.X_AXIS);
    DrawingUtil.drawGradient(g, black, white, w - t, 0, t, h, DrawingUtil.X_AXIS);

  }
}
