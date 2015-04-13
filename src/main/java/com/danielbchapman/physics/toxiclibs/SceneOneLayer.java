package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.danielbchapman.artwork.Paragraph;
import com.danielbchapman.utility.FileUtil;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class SceneOneLayer extends Layer
{
  //Static reads
  
  ArrayList<Paragraph> tweets = new ArrayList<>();
  ArrayList<Paragraph> active = new ArrayList<>();
  Paragraph wall;

  int lastIndex = 0;
  public void tweet(VerletPhysics3D physics)
  {
    if(lastIndex < tweets.size())
    {
      Paragraph paragraph = tweets.get(lastIndex);
      lastIndex++;
      active.add(paragraph);
      paragraph.translate(new Vec3D(0,0, -50));
      
      for(Point p : paragraph.points)
        physics.addParticle(p);
    }
    else
      System.out.println("Can't Tweet Nothing!");
  }
  
  public SceneOneLayer()
  {
    //Layout Variables
    //String all = FileUtil.readLines("content/scene_one/wall.txt").stream().collect(Collectors.joining(" "));
    
    final ArrayList<String> data = new ArrayList<>();
    
    Consumer<String> read = (x)->
    {
      data.add(FileUtil.readLines(x).stream().collect(Collectors.joining("\n")));
    };
    
    //creates a paragraph based on the arguments
    BiFunction<Integer, Integer[], Paragraph> create = (s, a) ->
    {
      int x = a[0];
      int y = a[1];
      int width = a[2];
      int delay = a[3];
      int fade = a[4];
      int total = a[5];
      int from = a[6];
      int to = a[7];

      String file = "content/scene_one/" + s;
      Point p = new Point(x, y, 0, 1f);
      String toDisplay = FileUtil.readFile(file);
      if(toDisplay == null)
        toDisplay = "No Text";
      return new Paragraph(toDisplay, p, width, 12, 12, delay, fade, total, from, to, Paragraph.FadeType.LETTER_BY_LETTER);
    };
    
    Integer[][] args = new Integer[][]
        {
                      //x,      y, wth, delay, fade,  total, from,  to
          new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//1
          new Integer[]{400,  100,  400,     0, 3000, 10000, 0, 255},//2
          new Integer[]{200,  500,  400,     0, 3000, 10000, 0, 255},//3
          new Integer[]{600,  400,  400,     0, 3000, 10000, 0, 255},//4
          new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//5
          new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//6
          new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//7
          new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//8
          new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//9
          new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255} //10
        };
    
    tweets.add(create.apply(1, args[0]));
    tweets.add(create.apply(2, args[1]));
    tweets.add(create.apply(3, args[2]));
    tweets.add(create.apply(4, args[3]));
//    tweets.add(create.apply(5, args[4]));
//    tweets.add(create.apply(6, args[5]));
//    tweets.add(create.apply(7, args[6]));
//    tweets.add(create.apply(8, args[7]));
//    tweets.add(create.apply(9, args[8]));
//    tweets.add(create.apply(10, args[9]));
      
  }
  @Override
  public Point[] init()
  {
    return new Point[]{}; //empty
  }

  @Override
  public void render(PGraphics g)
  { 
    g.background(0);
    g.fill(255);
    for(Paragraph p : active)
      p.draw(g, p.parent);
  }
}
