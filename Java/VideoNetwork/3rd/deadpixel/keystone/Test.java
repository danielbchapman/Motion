package deadpixel.keystone;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


/**
 * A test application for keystoning using:
 * <url>http://keystonep5.sourceforge.net/</url>
 */

public class Test extends PApplet
{
  private static final long serialVersionUID = 1L;

  public static void main(String[] args)
  {
    PApplet.main(Test.class.getName());
  }
  
  PImage texture;
  Keystone key;
  @Override
  public void setup()
  { 
    size(800,600,P3D);
    PGraphics grid = createGraphics(800, 600);
    
    grid.beginDraw();
    grid.background(0);
    grid.strokeWeight(2f);
    grid.stroke(255);
    int spacing = 10;
    for(int i = 0; i < 80; i++)
      grid.line(i * spacing, 0, i * spacing, 600);
    
    for(int i = 0; i < 60; i++)
      grid.line(0, i * spacing, 800, i * spacing);
    grid.endDraw();
    
    texture = new PImage(800,600);
    texture.loadPixels();
    texture.pixels = grid.pixels;
    texture.updatePixels();
    key = new Keystone(this);
    key.createCornerPinSurface(400,400, 12);
    key.startCalibration();
    //Keystone.calibrate = true;
  }
  @Override
  public void draw()
  {
    background(0);
    key.getSurface(0).render(texture);
//    surface.render(texture);
  }
}
