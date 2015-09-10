package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

import com.danielbchapman.artwork.Fadeable;
import com.danielbchapman.brushes.SaveableBrush;


public class MobilologyTwo extends Layer
{
  int gridX;
  int gridY;
  int spacing;
  Line[] xAxis;
  Line[] yAxis;
  
  boolean startFade = false;
  Fadeable fader = new Fadeable(0, 255, 3000, 0);
  CueStack stack;
  
  int color = 0x00FFFFFF;
  
  
  public MobilologyTwo()
  {
  }
   
  public Point[] init()
  {
    int w = 720;
    int h = 1280;
    gridX = w/20;//w/20;//124/2;//;120;
    gridY = h/20;//h/20;//76/2;//76;
    spacing = 20;
    Point[] pointsX = new Point[gridX * gridY];
    Point[] pointsY = new Point[gridX * gridY];
    
//    int total = gridX + gridY;
    xAxis = new Line[gridY]; //lines
    yAxis = new Line[gridX];
    
    for(int i = 0; i < gridY; i++)
    {
      for(int j = 0; j < gridX; j++)
      {
        //Y-Axis
        pointsX[i * gridX + j] = new Point(j * spacing, i * spacing, 0, 1f);
        //X-Axis (separate points) so the lines can "fly in"
        pointsY[i * gridX + j] = new Point(j * spacing, i * spacing, 0, 1f);
      }
    }
    
    //X-Axis 
    for(int i = 0; i < gridY; i++)
    {
      Point[] row = new Point[gridX];
      for(int j = 0; j < gridX; j++)
      {
        row[j] = pointsX[i*gridX + j];
      }
      Line line = new Line(row);
      xAxis[i] = line;
    }
    
    
    //Y-Axis
    for(int i = 0; i < gridX; i++)
    {
      Point[] col = new Point[gridY];
      for(int j = 0; j < gridY; j++)
      {
        col[j] = pointsY[i  + gridX * j];
      }
      Line line = new Line(col);
      yAxis[i] = line;
    }
    
    System.out.println(yAxis.length);
    for(int i = 0; i < yAxis.length; i++)
      if(yAxis[i] == null)
        System.out.println("No line @ " + i);
    
    Point[] grid = new Point[pointsX.length + pointsY.length];
    
    for(int i = 0; i < pointsX.length; i++)
    {
      grid[i] = pointsX[i];
    }
    
    for(int i = 0; i < pointsY.length; i++)
    {
      grid[i+pointsY.length] = pointsY[i];
    }
    
    prepareCues();
    return grid;
  }

  public void debugXAxis()
  {
    
    for(int i = 0 ; i < xAxis.length; i++)
    {
      System.out.println("X Line from -> " + xAxis[i].points[0] + " to " + xAxis[i].points[xAxis[i].points.length -1]);
      for(Point p : xAxis[i].points)
        System.out.println("\tPoint:" + p);
    }
  }
  /* (non-Javadoc)
   * @see com.danielbchapman.physics.toxiclibs.Scene#render(processing.core.PGraphics)
   */
  @Override
  public void render(PGraphics g)
  {
    g.background(0); //black
    if(startFade)
      g.stroke(fader.color(System.currentTimeMillis(), 0xFFFFFFFF));
    else
      g.stroke(color);
    g.strokeWeight(4f);
    g.pushMatrix();
    g.translate(10, 10, 0);//Offset to test with
    for(int i = 0; i < xAxis.length; i++)
    {
      //System.out.println("r: " + xAxis[i]);
      xAxis[i].render(g); 
    }
    
    for(int i = 0; i < yAxis.length; i++)
    {
      //System.out.println("r: " + lines[i]);
      yAxis[i].render(g); 
    }
    
    g.popMatrix();
  }
  
  /**
   * A random preset for animation that forces all the particles to one
   * point offscreen so that they can "crawl" onto the screen.  
   */
  public void offscreen()
  {
    startFade = false;
    fader = new Fadeable(fader.opacityStart, fader.opacityEnd, fader.count, fader.delay);
    Random rand = new Random();
    boolean direction = false;
    int padding = 0;
    int height = gridY * spacing + padding;
    int width = gridX * spacing + padding;
    
    for(Line l : xAxis)
    {
      int count = 0;
      for(Line l2 : xAxis)
      {
        if(l.equals(l2))
          count++;
      }
      if(count > 1)
        System.out.println("DUPLICATE LINES");
    }
    
    for(int i = 0; i < xAxis.length; i++)
    {
      for(Line l : xAxis)
      {
        direction = rand.nextBoolean();
        if(direction)
        {
          System.out.println("x -> right");
          for(Point p : l.points)
          {
            p.clearForce();
            p.clearVelocity();
            p.x = width / 2;
          }
        }
        else
        {

          System.out.println("x -> left");
          for(Point p : l.points)
          {
            p.clearForce();
            p.clearVelocity();
            p.x = 50;//less than zero
          }
        }
      }
    }
    
    for(int i = 0; i < yAxis.length; i++)
    {
     
      for(Line l : yAxis)
      {
        direction = rand.nextBoolean();
        if(direction)
          for(Point p : l.points)
            p.y = height;
        else
          for(Point p : l.points)
            p.y = padding;         
      }
    }
  }
  
  public void go(MotionEngine engine)
  {
    stack.go(engine, this);
  }
  
  public void prepareCues()
  {
    stack = new CueStack();
    stack.add(
        cue("[PRESET] Prepare Scene", 
            Actions.homeOff,
            Actions.gravityOff,
            Actions.homeLinearOff,
            Actions.dragTo(.5f),
            //Need to set the forces I like here...
            action("Send Lines Out", (x)->{offscreen(); lockAll();}, null),
            action("addLinear", null, (e)->{e.addBehavior(Actions.homeLinear);}, 50),
            action("Start Lines", (x)->{runFades();}, null, 100)
            ),
        load("[31] SLAP!",
            "content/scene_two/env/slap-2-final.env",
            "content/scene_two/rec/slap-2-final", 
            "content/scene_two/brush/slap-2-final"
//            Actions.homeLinearOff,
//            Actions.homeOff,
//            Actions.dragTo(0.01f)
//            action("Restore", (x)->{
//              Actions.dragTo(.25f).motionFunction.accept(Actions.engine);
//              Actions.homeLinearOn.motionFunction.accept(Actions.engine);
//              }, null, 1500)
            ), 
         cue("[32] on Throw Restore Gravity",
             Actions.homeLinearOn,
             Actions.homeOn,
             Actions.dragTo(0.05173f)
             ),
    
         load("[33] Nikki Hands on Cube", //Restore Gravity on the throw
             "content/scene_two/rec/nikki-hands-on-cube",
             "content/scene_two/brush/scene-two-squeeze",
             //hActions.homeOff,
             Actions.homeLinearOff),
         cue("[34] Return Home", Actions.dragTo(.25f), Actions.homeLinearOn),
                      
         load("[37] Nikki Hits the Wall",
             "content/scene_two/rec/punching-wall",
             "content/scene_two/brush/scene-two-squeeze",
             Actions.dragTo(0.01f)
          ),
         cue("[38] No home with the suck down", Actions.homeLinearOff), //need some environment here
         load("[39] Jitter Run", 
             "content/scene_two/rec/scene-2-jitter",
             "content/scene_two/brush/scene-two-squeeze",
             Actions.homeLinearOn
             ),
         load("[40] Jitter Run", 
             "content/scene_two/rec/scene-2-jitter",
             "content/scene_two/brush/scene-two-squeeze",
             Actions.homeLinearOff
             ),
         load("[41] Line Enters",
             "content/scene_two/rec/2-line-enters",
             "content/scene_two/brush/scene-two-squeeze"),   
         load("[42] We're Bored",
             "content/scene_two/rec/cyc-dance",
             "content/scene_two/brush/scene-2-jitter"),
         load("[43] We're Bored",
             "content/scene_two/rec/cyc-dance",
             "content/scene_two/brush/scene-2-jitter"),               
             //jitter -> 
         cue("[45] Restore Grid", 
             Actions.stopPlayback(), 
             Actions.homeLinearOn, 
             Actions.homeLinearOn),
              
         cue("[49] Suck Offstage",
             Actions.dragToNone,
             Actions.stopPlayback(),
             Actions.homeOff,
             Actions.homeLinearOff,
             action("clear", null, 
                 (e)->{
                   try{
                   e.getPhysics().behaviors.clear();
                   } catch (Throwable t){
                     t.printStackTrace();
                   }
                 }),
             Actions.follow(100)),
             
         load("[49.1] Suck Offstage",
             "content/scene_two/env/suck",  
             "content/scene_two/rec/suck",
             "content/scene_two/brush/suck",
             Actions.homeLinearOff,
             Actions.homeOff
             ),
         cue("[50] 5 Transition to 3",
             action("Scene 3", null, (x)->{x.advanceScene(); x.activeLayer.go(x);})
//             Actions.go(100)
             )
    );
//        load("captures/test-motion", "brushes/implode-brush"),
//        load("captures/test-motion", "brushes/implode-brush"),
//        cue("Slap! One",
//            Actions.gravityOff,
//            Actions.homeLinearOff,
//            Actions.homeOff,
//            Actions.dragToNone,
//            action("SLAP", (x)->{slap();}, null),
//            action("Restore", (x)->{
//              Actions.dragTo(.5f).motionFunction.accept(Actions.engine);
//              Actions.homeLinearOn.motionFunction.accept(Actions.engine);
//              }, null, 1000)
//            )      
    
  }
  
  public void slap()
  {
    if(engine == null)
    {
      System.out.println("Slap Failed!");
      return;
    }
      
    int[] where = Transform.translate(0f, 0f, 0f, Actions.engine.width, Actions.engine.height);
    Slap force = new Slap(new Vec3D(where[0], where[1], where[2]), new Vec3D(0, 0, -1f), 100f);
    force.maxForce = 10f;
    Cue slap = cue("Slap",
          new Action("Slap Start", 0,null, 
              (x)->{
                x.addBehavior(force);
              }),
          new Action("Slap End", 1500,null, 
              (x)->{
                x.removeBehavior(force);
              })    
        );
    //Copy current forces (global)
    slap.go(this, engine);
    
  }
  public Cue cue(String label, Action ... actions)
  {
    return cue(label, null, actions);
  }
  public Cue cue(String label, List<Action> post, Action ... pre)
  {
    ArrayList<Action> list = new ArrayList<Action>();
    if(pre != null)
      for(Action a : pre)
        list.add(a);
    
    if(post != null)
      for(Action a : post)
        list.add(a);
    
    Cue cue = new Cue(label, list);
    return cue;
  }
  
  public Cue load(String label, String env, String file, String brushFile, Action ... post)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
    Action loadEnv = Actions.loadEnvironment(new File(env));
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts, loadEnv);
  }
  
  public Cue load(String label, String file, String brush)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brush));
    return cue(label, acts);
  }
  
  public Cue load(String label, String file, String brushFile, Action ... post)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts);
  }
  
  public Cue load(String file, String brushFile)
  {
    return Actions.loadRecording(new File(file), new File(brushFile));
  }
  
  public Cue load(String label)
  {
    return cue(label);
  }
  public Action action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
  {
    return new Action(label, 0, fL, fE);
  }
  public Action action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE, int delay)
  {
    return new Action(label, delay, fL, fE);
  }
  public void lockAll()
  {
    for(Point p : points)
      p.lock();
  }
  
  public void runFades()
  {
    startFade = true;  
    System.out.println("Running Fades");
    final ArrayList<Line> schedule = new ArrayList<>();
    
    for(Line l : xAxis)
      schedule.add(l);
    
    for(Line l : yAxis)
      schedule.add(l);
    
    Collections.shuffle(schedule);
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

    Random rand = new Random();
    long total = 0L;
    int count = 28000 / ((720/20) + (1280/20) + 10); // 35 seconds
  
    System.out.println("Firing -> " + count);
    for(int i = 0; i < schedule.size(); i++)
    {
      final FadeThread t = new FadeThread();
      final Line line = schedule.get(i);
      t.line = line;
      long delay = rand.nextLong() % count; //35 second?
      if(delay < 0)
        delay = -delay;
      total += delay + 200;
      
      executor.schedule(t, total, TimeUnit.MILLISECONDS);
    }
    
    executor.schedule(()->{System.out.println("DONE!");}, total, TimeUnit.MILLISECONDS);
  }
  
  public static class FadeThread implements Runnable
  {
    public Line line;
    
    @Override
    public void run()
    {
      for(Point p : line.points)
        p.unlock();
    }
    
  }

  @Override
  public void update()
  {
  }
}
