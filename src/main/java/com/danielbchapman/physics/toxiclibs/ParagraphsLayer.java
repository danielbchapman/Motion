package com.danielbchapman.physics.toxiclibs;

import processing.core.PGraphics;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.artwork.Paragraph;
import com.danielbchapman.artwork.Paragraph.FadeType;
import com.danielbchapman.brushes.SaveableBrush;

public class ParagraphsLayer extends Layer
{
  Paragraph paragraph;
  Point origin;
  public boolean isSplit()
  {
    return paragraph.split;
  }
  @Override
  public Point[] init()
  {
    Point origin = new Point(10,10,0, 1);
    String text = 
          "Buy it, use it, break it, fix it, "+ "\n"+
          "Trash it, change it, mail - upgrade it,"+ "\n"+
          "Charge it, point it, zoom it, press it,"+ "\n"+
          "Snap it, work it, quick - erase it,"+ "\n"+
          "Write it, cut it, paste it, save it,"+ "\n"+
          "Load it, check it, quick - rewrite it,"+ "\n"+
          "Plug it, play it, burn it, rip it,"+ "\n"+
          "Drag and drop it, zip - unzip it,"+ "\n"+
          "Lock it, fill it, call it, find it,"+ "\n"+
          "View it, code it, jam - unlock it,"+ "\n"+
          "Surf it, scroll it, pause it, click it,"+ "\n"+
          "Cross it, crack it, switch - update it,"+ "\n"+
          "Name it, rate it, tune it, print it,"+ "\n"+
          "Scan it, send it, fax - rename it,"+ "\n"+
          "Touch it, bring it, pay it, watch it,"+ "\n"+
          "Turn it, leave it, start - format it."+ "\n";
    
    //paragraph = new Paragraph(text, origin, 400, 12, 12);
    paragraph = new Paragraph(text, origin, 500, 12, 12, 0, 0, 10000, 0, 255, FadeType.LINE_BY_LINE);
    origin.setWeight(1f);
    //points = new Point[1];
    //points[0] = paragraph.parent;//origin
    points = paragraph.points;
    return points;
  }
  
  @Override
  public void render(PGraphics g)
  {
    g.background(0); //black
    g.strokeWeight(2f);
    g.stroke(255);
    g.fill(255, 255, 255);
    g.pushMatrix();
    paragraph.draw(g, paragraph.parent); 
    g.popMatrix();
  }
  
  public void split(VerletPhysics3D physics, float mag)
  {
    if(paragraph.split)
      return;
   
    paragraph.split(physics, mag);;
  }
  @Override
  public void go(MotionEngine engine)
  {
  }
  @Override
  public void update()
  {
  }  
}
