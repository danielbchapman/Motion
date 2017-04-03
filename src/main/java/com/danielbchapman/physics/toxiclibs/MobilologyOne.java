package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.artwork.Paragraph;
import com.danielbchapman.utility.FileUtil;

public class MobilologyOne extends Layer
{
  //Static reads
  VerletPhysics3D local = new VerletPhysics3D();
  Paragraph wall;
  int lastIndex = 0;
  SectionOneCueStack stack;
  FloorSplitGravity gravity = new FloorSplitGravity(new Vec3D(0, 0.1f, 0));
  ArrayList<OLDEmitter<?>> emitters = new ArrayList<>();
  public void go(MotionEngine engine)
  {
    stack.go(engine, this);
  }
  
  public MobilologyOne()
  {
    local.setDrag(0f);
  }
  
  @Override
  public Point[] init()
  {
    //Setup Stack
    stack = new SectionOneCueStack();
    return new Point[]{}; //empty
  }

  @Override
  public void render(PGraphics g)
  { 
    g.background(0);
    g.fill(255);
    
    for(int i = 0; i < stack.active.size(); i++)
    {
      Paragraph p =stack.active.get(i);
      p.draw(g, p.parent);
    }
      
    for(OLDEmitter<?> e : emitters)
      e.draw(g);
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
        
        for(Point p : paragraph.points)
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
          cue("[ZERO] Environment Setup and Tweet", 
              Actions.homeTo(0.4f), 
              Actions.dragTo(0.12f),
              Actions.homeOn
          ),
              
          cue("[1] Flying Text",
              action(0, "Tweet 1", null, (x)->{tweet(x.getPhysics());} ),
              action(3300, "Tweet 2", null, (x)->{tweet(x.getPhysics());} ),
              action(6800, "Tweet 3", null, (x)->{tweet(x.getPhysics());} ),
              action(10300, "Tweet 4", null, (x)->{tweet(x.getPhysics());} ),
              action(13800, "Tweet 5", null, (x)->{tweet(x.getPhysics());} ),
              action(17300, "Tweet 6", null, (x)->{tweet(x.getPhysics());} ),
              action(20800, "Tweet 7", null, (x)->{tweet(x.getPhysics());} ),
              action(24300, "Tweet 8", null, (x)->{tweet(x.getPhysics());} ),
              action(27800, "Tweet 9", null, (x)->{tweet(x.getPhysics());} ),
              action(31300, "Tweet 10", null, (x)->{tweet(x.getPhysics());} ),
              action(34800, "Tweet 11", null, (x)->{tweet(x.getPhysics());} ),
              action(38300, "Tweet 12", null, (x)->{tweet(x.getPhysics());} ),
              action(41800, "Tweet 13", null, (x)->{tweet(x.getPhysics());} ),
              action(45300, "Tweet 14", null, (x)->{tweet(x.getPhysics());} ),
              action(48800, "Tweet 15", null, (x)->{tweet(x.getPhysics());} ),
              action(52300, "Tweet 16", null, (x)->{tweet(x.getPhysics());} ),
              action(55800, "Tweet 17", null, (x)->{tweet(x.getPhysics());} ),
              action(59300, "Tweet 18", null, (x)->{tweet(x.getPhysics());} ),
              action(62800, "Tweet 19", null, (x)->{tweet(x.getPhysics());} ),
              action(66300, "Tweet 20", null, (x)->{tweet(x.getPhysics());} )
              ),
//            cue("[2] Scanner--Not implemented"
//                ),
            cue("[3] CHAIN -> Start Emitters @ ~1:25, Gravity On1",
                Actions.loadEnvironment(new File("/content/scene_one/scene-one-pre-gravity.env")),
                action("Start Gravity", null, (e)->{e.addBehavior(gravity);})
                ),
//            load("[4] Cross after spin on Ground",
//                "content/scene_one/rec/1-cue-4",
//                "content/scene_one/brush/crosses",
//                action("Set Drag Low", null, e-> e.getPhysics().setDrag(.0402f))
//                ),
//            load("[5] Girls Entrance from Left (might move up",
//                "content/scene_one/rec/1-cue-5",
//                "content/scene_one/brush/dl-entrance"
//                ),
//            load("[6] Girls cross upstage center",
//                "content/scene_one/rec/1-cue-6",
//                "content/scene_one/brush/1-cue-6"
//                ),
//            load("[7] Girls Carry to Left, then to up right",
//                "content/scene_one/rec/1-cue-7",
//                "content/scene_one/brush/1-cue-6"
//                ),
//            load("[8] Other group crossing towards up-right"
//                ),
            cue("[9] Rainfall is started",
                action("Emitter", (l)-> {startFountain(7, 12, 72);}, null)),
            load("[10] Group at UP LEFT ",
                "content/scene_one/rec/grop-cross-ul",
                "content/scene_one/brush/scene-1-soft"
                ),
            cue("[11] Two break",
                action("Stop Playbacks", null, e->{e.clearPlaybacks();})
                ),
//            load("[12] Two more break",
//                "content/scene_one/rec/break-2",
//                "content/scene_one/brush/break"),
//            load("[13] Two more break",
//                "content/scene_one/rec/break-3",
//                "content/scene_one/brush/break"),
            load("[14] Everything  to Nikki @ cube",//Possibly add an point source here?
                "content/scene_one/rec/OneNikkiAtCube",
                "content/scene_one/brush/scene-1-soft"
                ),
            load("[15] Flying things behind Nikki",
                "content/scene_one/rec/nikki-levitate",
                "content/scene_one/brush/nikki-levitate"),
            cue("[16] Nikki ends pose--stuff falls",
                action("Clear Playbacks", null, (x)->{x.clearPlaybacks();})),
            load("[17] Nikki standing",
                "content/scene_one/rec/nikki-stands",
                "content/scene_one/brush/nikki-levitate"
                ),
            cue("[18] Music for two start",
                Actions.loadRecordingAsAction(
                    new File("content/scene_one/rec/push-away"), 
                    new File("content/scene_one/brush/max-explode")),
                action("Remove Gravity", null, e->{e.removeBehavior(gravity);}),
                Actions.homeOff,
                Actions.homeLinearOff,
                Actions.dragTo(0f)
                ),
            cue("[19] CHANGE SCENE", //MAKE A FOLLOW
                action("Scene Two Start", null, (e)->{e.advanceScene();}),
                action(300, "Scene Two GO", null, e->e.activeLayerGo()),
                action("STOP", null, e->{stopFountain();})
                ), 
            
          cue("Start Fountain!", 
              action("fountain", null, (x)->{startFountain(5,10,72);})),    
          //Turn on Gravity!
          cue("Gravity On, Home off", 
              Actions.homeOff, 
              Actions.dragToVeryLow, 
              Actions.gravityOn),
          cue("Home On, Gravity Off", 
              Actions.homeTo(0.2f), 
              Actions.homeOn, 
              Actions.gravityOff),
          //Go Home
          cue("Return Home Easing Mode", 
              Actions.dragToBasic, 
              Actions.homeOff, 
              Actions.homeLinearOn)
      );
      
    cue("CHAIN -> Start Emitters @ ~1:25, Gravity On1",
    Actions.loadEnvironment(new File("/content/scene_one/scene-one-pre-gravity.env")),
    action("Start Gravity", null, (e)->{e.addBehavior(gravity);})
    );
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
    
    public Cue load(String label, String file, String brushFile, Action ... post)
    {
      ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
      if(post != null)
        for(Action a : post)
          acts.add(a);
      return cue(label, acts);
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
      try
      {
        ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brush));
        return cue(label, acts);  
      }
      catch (Throwable t)
      {
        System.err.println("ARGS: " + label + ", " + file + ", " + brush);
        t.printStackTrace();
        throw t;
      }
      
    }
    
    public Cue load(String file, String brushFile)
    {
      return Actions.loadRecording(new File(file), new File(brushFile));
    }
    
    public Cue load(String label)
    {
      return cue(label);
    }

    public Action action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE, int delay)
    {
      return new Action(label, delay, fL, fE);
    }
    
    public Action action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
    {
      return new Action(label, 0, fL, fE);
    }
    public Action action(int delay, String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
    {
      return new Action(label, delay, fL, fE);
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
        Point p = new Point(x, y, 0, 1f);
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
      
      tweets.add(create(1, 0.60774976f, -0.8660331f, 0.3113848f));
      tweets.add(create(2, -0.88814126f, -0.79523945f, 0.30825332f));
      tweets.add(create(3, -0.29003507f, -0.89551735f, 0.38781804f));
      tweets.add(create(4, 0f, -0.83671033f, 0.40248105f));
      tweets.add(create(5, 0.2404893f, -0.7592367f, 0.2594211f));
      tweets.add(create(6, 0.47673875f, -0.8176122f, 0.27160722f));
      tweets.add(create(7, 0.23561329f, -0.8220054f, 0.38576248f));
      tweets.add(create(8, -0.969826f, -0.92680335f, 0.3300926f));
      tweets.add(create(9, -0.5420708f, -0.8197777f, 0.31014243f));
      tweets.add(create(10, -0.7764856f, -0.8661885f, 0.2753278f));
      tweets.add(create(11, -0.7423372f, -0.76972085f, 0.40629107f));
      tweets.add(create(12, 0.13760239f, -0.8734967f, 0.38589758f));
      tweets.add(create(13, 0.18477231f, -0.8238119f, 0.33576635f));
      tweets.add(create(14, 0.5980403f, -0.81536245f, 0.26064658f));
      tweets.add(create(15, 0.32537585f, -0.78095186f, 0.35657996f));
      tweets.add(create(16, -0.5967808f, -0.82131493f, 0.38780418f));
      tweets.add(create(17, 0.08435255f, -0.76120436f, 0.264645f));
      tweets.add(create(18, 0.75374967f, -0.7866781f, 0.41352078f));
      tweets.add(create(19, 0.122197926f, -0.7948279f, 0.30538788f));
      tweets.add(create(20, -0.32357234f, -0.750912f, 0.31922892f));

    }
    
    public Paragraph create(int fileNo, float x, float y, float w)
    {
      int width = Actions.engine.width;
      int height = Actions.engine.height;
      int pW = Transform.size(w, Actions.engine.width) / 3;
      
      int[] c = Transform.translate(x, y, width, height);
      System.out.println("Creating paragraph width: " + pW);
      String file = "content/scene_one/" + fileNo;
      
      Point p = new Point(c[0], c[1], 0, 1f);
      String toDisplay = FileUtil.readFile(file);
      if(toDisplay == null)
        toDisplay = "No Text";
      
      int size = 36;
      return new Paragraph(toDisplay, p, pW, size, size, 0, 3000, 10000, 0, 255, Paragraph.FadeType.LETTER_BY_LETTER);    }
  }
  
  public void stopFountain()
  {
    emitters.clear();
  }
  
  public void startFountain(int low, int high, int spacing)
  {
    emitters.clear();
    
    int base = spacing/2;
    for(int i = 0; i < high; i++)
    {
      LetterEmitter emitter = new LetterEmitter(
          "Buy it, use it, break it, fix it, Trash it, change it, mail - upgrade it, Charge it, point it, zoom it, press it,".replaceAll(",", " "),
          new Vec3D(Util.rand(10, base) + (spacing *i), Util.rand(-700, -400), -200),
          new Vec3D(0, 1f, 0),
          25000,
          50, 
          20f,
          200
        );
        emitter.physics = Actions.engine.getPhysics();
        
        emitters.add(emitter);
    }
    
  }

  @Override
  public void update()
  {
    long time = System.currentTimeMillis();
    for(OLDEmitter<?> e : emitters)
      e.update(time);
  }
  
  public String getName()
  {
    return "mobile1";  
  }
}
