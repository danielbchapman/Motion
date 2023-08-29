package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.artwork.Paragraph;
import com.danielbchapman.utility.FileUtil;

public class SceneOneLayer extends Layer
{
  //Static reads
  
  Paragraph wall;

  int lastIndex = 0;
  SectionOneCueStack stack;
  
  public void go(MotionEngine engine)
  {
    System.out.println("Firing Cue Scene One Stack...");
    stack.go(engine, this);
  }
  
  public SceneOneLayer()
  {
  }
  
  @Override
  public PointOLD[] init()
  {
    //Setup Stack
    stack = new SectionOneCueStack();
    return new PointOLD[]{}; //empty
  }

  @Override
  public void render(PGraphics g)
  { 
    g.background(0);
    g.fill(255);
    for(Paragraph p : stack.active)
      p.draw(g, p.parent);
  }
  
  public class SectionOneCueStack extends CueStack
  {
    ArrayList<Paragraph> tweets = new ArrayList<>();
    ArrayList<Paragraph> active = new ArrayList<>();

    public void tweet(VerletPhysics3D physics)
    {
      if(lastIndex < tweets.size())
      {
        Paragraph paragraph = tweets.get(lastIndex);
        lastIndex++;
        active.add(paragraph);
        paragraph.translate(new Vec3D(0,0, -20));
        
        for(PointOLD p : paragraph.points)
          physics.addParticle(p);
      }
      else
        System.out.println("Can't Tweet Nothing!");
    }
    
    public SectionOneCueStack()
    {
      makeTweets();
      
      add(
          //Tweet!
          cue("Environment Setup and Tweet", 
              ActionsOLD.homeTo(0.4f), 
              ActionsOLD.dragTo(0.12f),
              ActionsOLD.homeOn,
              action("Tweet 1", null, (x)->{tweet(x.getPhysics());} )),
//          cue("Tweet 2", action("Tweet 2", null, (x)->{tweet(x.getPhysics());} )),
//          cue("Tweet 3",action("Tweet 3", null, (x)->{tweet(x.getPhysics());} )),
//          cue("Tweet 4", action("Tweet 4", null, (x)->{tweet(x.getPhysics());} )),
//          cue("Tweet 5", action("Tweet 5", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 2", action("Tweet 2", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 3", action("Tweet 3", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 4", action("Tweet 4", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 5", action("Tweet 5", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 6", action("Tweet 6", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 7", action("Tweet 7", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 8", action("Tweet 8", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 9", action("Tweet 9", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 10", action("Tweet 10", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 11", action("Tweet 11", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 12", action("Tweet 12", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 13", action("Tweet 13", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 14", action("Tweet 14", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 15", action("Tweet 15", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 16", action("Tweet 16", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 17", action("Tweet 17", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 18", action("Tweet 18", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 19", action("Tweet 19", null, (x)->{tweet(x.getPhysics());} )),
              cue("Tweet 20", action("Tweet 20", null, (x)->{tweet(x.getPhysics());} )),
              
          //Turn on Gravity!
          cue("Gravity On, Home off", 
              ActionsOLD.homeOff, 
              ActionsOLD.dragToVeryLow, 
              ActionsOLD.gravityOn),
          cue("Home On, Gravity Off", 
              ActionsOLD.homeTo(0.2f), 
              ActionsOLD.homeOn, 
              ActionsOLD.gravityOff),
          //Go Home
          cue("Return Home Easing Mode", 
              ActionsOLD.dragToBasic, 
              ActionsOLD.homeOff, 
              ActionsOLD.homeLinearOn)
      );
    }
    
    public Cue cue(String label, ActionOLD ... actions)
    {
      ArrayList<ActionOLD> list = new ArrayList<ActionOLD>();
      for(ActionOLD a : actions)
        list.add(a);
      Cue cue = new Cue(label, list);
      return cue;
    }
    
    public ActionOLD action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
    {
      return new ActionOLD(label, 0, fL, fE);
    }
    public void makeTweets()
    { 
      //creates a paragraph based on the arguments
      @SuppressWarnings("unused")
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
        PointOLD p = new PointOLD(x, y, 0, 1f);
        String toDisplay = FileUtil.readFile(file);
        if(toDisplay == null)
          toDisplay = "No Text";
        return new Paragraph(toDisplay, p, width, 12, 12, delay, fade, total, from, to, Paragraph.FadeType.LETTER_BY_LETTER);
      };
      
//      Integer[][] args = new Integer[][]
//          {
//                        //x,      y, wth, delay, fade,  total, from,  to
//            new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//1
//            new Integer[]{400,  100,  400,     0, 3000, 10000, 0, 255},//2
//            new Integer[]{200,  500,  400,     0, 3000, 10000, 0, 255},//3
//            new Integer[]{600,  400,  400,     0, 3000, 10000, 0, 255},//4
//            new Integer[]{350,  330,  400,     0, 3000, 10000, 0, 255},//5
//            new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//6
//            new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//7
//            new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//8
//            new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//9
//            new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255} //10
//          };
      
//      tweets.add(create(1, 0f,  -.5f, .3f));
//      tweets.add(create(2, -.8f, .2f, .35f));
//      tweets.add(create(3, .6f,    0, .29f));
//      tweets.add(create(4, -.9f,-.9f, .4f));
//      tweets.add(create(5, .5f,    0, .25f));
      
      tweets.add(create(1, -0.8484037f, -0.7767497f, 0.30237955f));
      tweets.add(create(2, -0.32622182f, -0.08932668f, 0.410291f));
      tweets.add(create(3, -0.6360295f, -0.24247438f, 0.26332414f));
      tweets.add(create(4, -0.86493295f, -0.24452251f, 0.33556807f));
      tweets.add(create(5, -0.27467936f, -0.7601769f, 0.278355f));
      tweets.add(create(6, -0.034897566f, 0.15360034f, 0.3512485f));
      tweets.add(create(7, 0.015447915f, -0.6243588f, 0.40551943f));
      tweets.add(create(8, 0.7235648f, -0.116715014f, 0.3204156f));
      tweets.add(create(9, -0.74228424f, 0.22538579f, 0.37197044f));
      tweets.add(create(10, 0.09099883f, 0.24332011f, 0.26577008f));
      tweets.add(create(11, -0.59968406f, -0.15510714f, 0.41845268f));
      tweets.add(create(12, -0.016927779f, -0.3144101f, 0.26566678f));
      tweets.add(create(13, -0.31929725f, -0.014933825f, 0.27539426f));
      tweets.add(create(14, 0.7354534f, -0.71851176f, 0.3970907f));
      tweets.add(create(15, -0.4252262f, -0.57359636f, 0.326428f));
      tweets.add(create(16, -0.50721383f, 0.07806498f, 0.3798647f));
      tweets.add(create(17, -0.8192794f, -0.09218466f, 0.33783633f));
      tweets.add(create(18, -0.765166f, 0.19136155f, 0.387854f));
      tweets.add(create(19, -0.81006783f, -0.6293552f, 0.3905328f));
      tweets.add(create(20, -0.8023385f, -0.38700843f, 0.41174376f));
    }
    
    public Paragraph create(int fileNo, float x, float y, float w)
    {
      int width = ActionsOLD.engine.width;
      int height = ActionsOLD.engine.height;
      int pW = Transform.size(w, ActionsOLD.engine.width);
      
      int[] c = Transform.translate(x, y, width, height);
      System.out.println("Creating paragraph width: " + pW);
      String file = "content/scene_one/" + fileNo;
      
      PointOLD p = new PointOLD(c[0], c[1], 0, 1f);
      String toDisplay = FileUtil.readFile(file);
      if(toDisplay == null)
        toDisplay = "No Text";
      return new Paragraph(toDisplay, p, pW, 12, 12, 0, 3000, 10000, 0, 255, Paragraph.FadeType.LETTER_BY_LETTER);    }
  }

  @Override
  public void update()
  {
  }
}
