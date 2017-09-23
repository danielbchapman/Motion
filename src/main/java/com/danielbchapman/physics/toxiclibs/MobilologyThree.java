package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

import com.danielbchapman.artwork.Word;
import com.danielbchapman.utility.FileUtil;

public class MobilologyThree extends Layer
{
  int gridX;
  int gridY;
  int spacing;
  
  ArrayList<OLDEmitter<?>> emitters = new ArrayList<>();
  ArrayList<Word> words = new ArrayList<>();
  ArrayList<String> data = new ArrayList<>();
  ArrayList<Word> toAdd = new ArrayList<>();
  
  public static FloorSplitGravity gravity = new FloorSplitGravity(new Vec3D(0, 0.1f, 0));
  public static FloorSplitGravity gravityNew = new FloorSplitGravity(new Vec3D(0, 0.07f, 0));
  int dataIndex;
  int dataMax;
  
  long started = -1L;
  long lastCount = -1L;
  
  Vec3D location = new Vec3D();
  Vec3D low = new Vec3D();
  Vec3D high = new Vec3D();
  
  Random rand = new Random();
  public boolean STOP = false;
  public MobilologyThree()
  {
    setText(FileUtil.readFile("content/scene_three/text.txt"));
    
    location = new Vec3D(
        Transform.size(0f, Actions.engine.width),
        Transform.size(-1.02f, Actions.engine.height),
        0
      );
     
   low = new Vec3D(
       Transform.size(-.8f, Actions.engine.width),
       -50,
       -100
     );
   high = new Vec3D(
       Transform.size(.8f, Actions.engine.width),
       50,
       100
     );
    
   int count = 10;
   String data2 = "These are some letters that we should emit";
   for(int i = 0; i < count; i++)
   {
     float x = ((float) Actions.engine.width -75f) / (float)count;
     x *= i+1;
     emitters.add(new LetterEmitter(data2, new Vec3D(x, -100, 0), Vec3D.randomVector(), 15000, 1000, 2f, 25));
   }
   
   for(int i = 0; i < count; i++)
   {
     float x = ((float) Actions.engine.width -45f) / (float)count;
     x *= i+1;
     emitters.add(new LetterEmitter(data2, new Vec3D(x, -100, 150), Vec3D.randomVector(), 15000, 1000, 2f, 25));
   }
   
   prepareCues();
  }
  
  public void emit(String letters, Vec3D position, Vec3D force)
  {
    PointWrapper<Word> p = new PointWrapper<>(position.x, position.y, position.z, 1f);
    Word word = new Word(letters, p);
    p.setEnableRotation(false);
    toAdd.add(word);
  }
  
  @Override
  public void go(MotionEngine engine)
  {
    stack.go(Actions.engine, this);
//    if(!started)
//    {
//      started = true;
//      //Start gravity
//      ;
//      System.out.println("Adding forces!");
//    }
    //Create random vector
//    Vec3D location = randomVec();
//    Vec3D force = randomVec().normalizeTo(1f);
//    emit(nextWord(), location, force);
  }
  
  CueStack stack = new CueStack();
  public void prepareCues()
  {
    stack.add(
        cue("[50] Start Scene",
             action("Add Behaviors", null,
                 (x)->{
                   started = System.currentTimeMillis();
                   System.out.println("============================ SCENE 3=====================");
                   System.out.println(" Adding Gravity " + Actions.engine.addBehavior(gravityNew));
                   Actions.engine.getPhysics().setDrag(0.015f);
                   Actions.engine.addBehavior(Actions.home);
                   MotionEngine.brush = MotionInteractiveBehavior.load(new File("content/scene_three/brush/leaves"));
                   for(Object o : Actions.engine.getPhysics().behaviors)
                     System.out.println(o);
                   
                   System.out.println("============================ END SCENE 3=====================");
                 }
             , 400)),
             
         load("[51] w/Music",
             "content/scene_three/rec/3-51-music",
             "content/scene_three/brush/leaves"),
//         load("[52] w/Lift"),
//         load("[53] Swimming"),
         load("[54] Buttocks Moment",
             "content/scene_three/rec/buttocks",
             "content/scene_three/brush/leaves"),
//         load("[55] With Everyone Sitting",
//             "content/scene_three/rec/sitting", 
//             "content/scene_three/brush/leaves"),
         load("[56] Entrance", //"Let that Absorb"
              "content/scene_three/rec/find_this", 
              "content/scene_three/brush/leaves"),
         cue("[57] Forward Gather \"Already Has\"",
             Actions.stopPlayback()
             ),
         load("[58] With Locking",
             "content/scene_three/rec/3-locking",
             "content/scene_three/brush/soft-leaves"),//"Social Media Platforms"
         cue("[59] On Unlock", //"proud warrior too!
             Actions.stopPlayback()),
         load("[59.5] Googling",
             "content/scene_three/rec/googling",
             "content/scene_three/brush/scene-3-implode"
             ),
         cue("[59.6] DISCONNECTING", 
             Actions.stopPlayback()
             ),
         load("[60] With Savasana",
             "content/scene_three/rec/sequence-60-good", 
             "content/scene_three/brush/leaves"),
//         load(
//             "[61] With Nikki running throug",
//             "content/scene_three/rec/nikki-end", 
//             "content/scene_three/brush/leaves"
//         ),
//         cue("[62] With Downward Motion"),
         cue("[63] With Hands and lights", 
             Actions.stopPlayback()
             ),
         load("[70] Blow Away",
             "content/scene_three/rec/3-explode", 
             "content/scene_three/brush/max-explode",
             action("stop emitters", null, x->{stopEmitters();}),
             Actions.gravityOff,
             Actions.homeOff,
             Actions.homeLinearOff,
             action("gravity off", null, x->{x.removeBehavior(gravityNew);}),
             Actions.follow(50),
             action("STOP", null, x->{STOP = true;}),
             Actions.follow(0)
             ),
         cue("ADVANCE TO BLANK", Actions.advanceScene(3500))
        );    
  }
  
  public void fadeOut()
  {
    
  }
  public void stopEmitters()
  {
    for(int i = 0; i < emitters.size(); i++)
      emitters.get(i).stop(true);
  }
  public Point[] init()
  {
    Point[] grid = new Point[0];
    
    return grid;
  }
  
  public String nextWord()
  {
    if(data.isEmpty())
      return "null";
    if(dataIndex >= dataMax)
      dataIndex = 0;
    
    return data.get(dataIndex++);
  }

  public int rand(float low, float high)
  {
    return rand((int)low, (int)high);
  }

  public int rand(int low, int high)
  {
    int delta = high - low;
    if(delta < 0)
      delta = -delta;
    
    int dif = rand.nextInt(delta);
    return dif + low;
  }
  
  public Vec3D randomVec()
  {
    int x = rand(low.x, high.x);
    int y = rand(low.y, high.y);
    int z = rand(low.z, high.z);
    return new Vec3D(x, y, z);
  }

  /* (non-Javadoc)
   * @see com.danielbchapman.physics.toxiclibs.Scene#render(processing.core.PGraphics)
   */
  @Override
  public void render(PGraphics g)
  {
    float size = g.textSize;
      g.background(0);
    g.strokeWeight(2f);
    g.stroke(255);
    g.fill(255, 255, 255);
    g.pushMatrix();
//    g.translate(10, 10, 0);//Offset to see correctly
//    for(int i = 0; i < points.length; i++)
//    {
//      g.pushMatrix();
//      Point p  = points[i];
//      
//      g.translate(p.x, p.y, p.z);
//      Point.rotation(g, p);
//      //g.rotate((p.angular.magnitude())/360f, p.angular.x, p.angular.y, p.angular.z);
//      g.text("Word", 0,0,0);
//      //Rotation based on angular
////      g.translate(-p.x, -p.y, -p.z);
////      g.rotate(-p.angular.x);
//      g.popMatrix();
//      //g.point(p.x, p.y, p.z); 
//    }
    

    g.textSize(20);
    for(Word w : words)
    {
      w.draw(g, w.getParent());
    }
      
    g.textSize(size);
    for(OLDEmitter<?> e : emitters)
      e.draw(g);
    
    g.popMatrix();
  }
  public void setText(String val)
  {
    String[] words = val.split("\\s+");
    dataIndex = 0;
    dataMax = words.length;
    for(String s : words)
      data.add(s);
    
  }
  
  @Override
  public void update()
  {
    if(started > 0)
    {
      long now = System.currentTimeMillis();
      
      if(now - lastCount > 800)
      {
        lastCount = now;
        
        Vec3D location = randomVec();
        Vec3D force = randomVec().normalizeTo(1f);
        emit(nextWord(), location, force);
      }
          
    }
    for(OLDEmitter<?> e : emitters)
      e.update(System.currentTimeMillis());
    
    for(Word w : toAdd)
    {
      words.add(w);
      w.split(engine.getPhysics(), 0f);
    }
    
    toAdd.clear();
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
  
  public Cue load(String file, String brushFile)
  {
    return Actions.loadRecording(new File(file), new File(brushFile));
  }
  
  public Cue load(String label, String file, String brushFile, Action ... post)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts);
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
}
