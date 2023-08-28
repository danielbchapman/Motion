package shows.troubledwater;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.danielbchapman.artwork.Paragraph;
import com.danielbchapman.physics.toxiclibs.ActionOLD;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.CueStack;
import com.danielbchapman.physics.toxiclibs.OLDEmitter;
import com.danielbchapman.physics.toxiclibs.FloorSplitGravity;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.LetterEmitter;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Transform;
import com.danielbchapman.physics.toxiclibs.Util;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class Scene7 extends Layer
{
 public static String WORDS = 
     "ã�‚ã‚‹äººã�®ã‚‚/ã�¨ã�«ã�¦ç´«å¼�éƒ¨ã�¨æ¸…å°‘ç´�è¨€ã�®ã‚ˆã�—ã�‚ã�—ã�„ã�‹ã�«ã�ªã�©ã�„ã�µäº‹ã�®ä¾�ã‚Šã�—ã€€äººã�¯å¼�éƒ¨ï¼�ï¼¼ã�¨ã�Ÿã‚žã�»ã‚�ã�«ã�»ã‚�ã�¬ã€€ã�—ã�‹ã�‚ã‚‰ã‚“ã��ã‚Œã�•ã‚‹äº‹ã�ªã�Œã‚‰æ¸…ã�¯ã‚‰ã�®ã�Šã‚‚ã�¨ã�¯ä¸–ã�«ã�‚ã�¯ã‚Œã�®äººä¹Ÿã€€å��å®¶ã�®æœ«ã�ªã‚Œã�°ä¸–ã�®ã�Šã�¼ã�ˆã‚‚ã�‹ã‚�ã�‹ã‚‰ã�–ã‚Šã�—ã‚„ã�—ã‚‰ã�šã€€ä¸‡ã�«å¥³ã�¯ã�¯ã�‹ã�ªã��ç‰©ã�ªã‚Œã�°ã�¯ã�‹ï¼�â€³ï¼¼ã�—ã��å¾Œè¦‹ã�ªã�©ã‚‚ã�ªã��ã�¦ã�¯ã�µã‚Œã�‘ã‚€ã�»ã�©ã�†ã�—ã�¤ã‚‰ã�—ã�ªã�©ã�¿ã�«ã�—ã�¿ã�¬ã�¹ã��äº‹ã�žå¤šã�‹ã‚Šã�‘ã‚‰ã�—ã€€ã‚„ã�†ï¼�ï¼¼å®®ã�¥ã�‹ã�¸ã�«å‡ºåˆ�ã�¬ã‚‹å¾Œå®®ã�®å¾¡ã�„ã�¤ã��ã�—ã�¿ã�«ã�•ã‚‹äººã�‚ã‚Šã�¨ã�—ã‚‰ã‚Œåˆ�ã�¦é¦™çˆ�å³¯ã�®é›ªã�«ç°¾ã‚’ã�¾ã��ã�ªã�©æ‰�ã�Ÿã�‘ã�Ÿã‚Šã�¨ã�¯ã�‹ã��ã�—ã�¦ã�žã�‚ã‚‰ã�¯ã‚Œã�¬ã€€å°‘ç´�è¨€ã�¯å¿ƒã�¥ã�‹ã‚‰ã�¨èº«ã‚’ã‚‚ã�¦ã�ªã�™ã‚ˆã‚Šã�¯ã�‹ã��ã�‚ã‚‹ã�¹ã��ç‰©ã�žã�‹ã��ã�‚ã‚Œã�¨ã‚‚æ•™ã‚†ã‚‹äººã�¯ã�‚ã‚‰ã�–ã‚Šã��ã€€å¼�éƒ¨ã�¯ã‚’ã�•ã�ªã��ã‚ˆã‚Šçˆ¶çˆ²æ™‚ã�Œã‚’ã�—ã�¸å…„ã‚‚ã�‚ã‚Šã�—ã�‹ã�°äººã�®ã�„ã‚‚ã�†ã�¨ã‚�ã�—ã�¦ã�‹ã�šï¼�ï¼¼ã�«ã�Šã�•ã‚†ã‚‹æ‰€ã‚‚æœ‰ã�Ÿã‚Šã�‘ã‚“ã€€ã�„ã�¯ã‚žå¯Œå®¶ã�«ç”Ÿã‚Œã�Ÿã‚‹å¨˜ã�®ã�™ã�ªã�»ã�«ã��ã� ã�¡ã�¦ã��ã�®ã�»ã�©ï¼�ï¼¼ã�®äººå¦»ã�«æˆ�ã�Ÿã‚‹ã‚‚ã�®ã�¨ã‚„ã�„ã�¯ã�¾ã�—ã€€å°‘ç´�è¨€ã�¯å¿ƒã�Ÿã�‹ã��èº«ã�®ã�¯ã�‹ï¼�â€³ï¼¼ã�—ã�‹ã‚‰ã�–ã‚Šã�—ã�‹ã�°ã�“ã�¨ã�«å‡ºã�§è¡Œã�²ã�«ã�‚ã‚‰ã�¯ã�•ã�§ã�¯ã�„ã�¥ã‚‰ã�„ã�‹ã�«ã�¨è¦‹ã‚‹äººã‚‚ã�‚ã‚‰ã�–ã‚Šã�‘ã‚“ã‚’å¼�éƒ¨ã�¯å¤©å�°ã�®ä¸€å¿ƒä¸‰è§€ã�¨ã‚„ã‚‰ã‚“ã�Šã�•ã‚€ã‚‹æ‰€ã�µã�‹ã��ä¾�ã‚Šã�—ã€€å°‘ç´�è¨€ã‚‚ä½›äººã�«ã�¦ã�¯ã�‚ã‚Šã�‘ã‚Œã�©ã�“ã‚�ã‚�ã�«æµªã�®ã�•ã‚�ã�Žã�¯ã�’ã�—ã�‘ã‚Œã�°ã��ã�¾ã�ªã��æœˆã�¯ç…§ã‚‰ã�•ã�šã‚„ä¾�ã‚Šã�‘ã‚“ã€€ã�‚ã�¯ã‚Œã�ªã‚‹ã�¯ã�‹ã��ã�‚ã‚Šã�‘ã‚‹èº«ã�žã�‹ã�—ã€€æ‰�ã�¯ã�Šã�®ã�¥ã�‹ã‚‰ã�«ã�—ã�¦å¾³ã�¯ã‚„ã�—ã�ªã�µã�¦å¾Œã�®ç‰©ã�«ã�“ã��ã€€é¢¨ã�—ã�¥ã�‹ã�«ã�—ã�¦ã�¯æ²–ã�«ã�¤ã‚ŠèˆŸã�®ã�‹ã�šã‚‚è¦‹ã‚‹ã�¹ã��é›²ã�•ã‚�ã�Žã�¦ã�¯å¤–å±±ã�®ã�‹ã�’ã‚‚ã�Šã�¼ã‚�ã�’ã�«æˆ�ã�¬ã�¹ã�—ã€€å¼�éƒ¨ã�Œæ—¥è¨˜ã�«å°‘ç´�è¨€ã‚’ã��ã�—ã‚Šã�—ã�¯ã�•ã‚‹äº‹ã�ªã�Œã‚‰æ­¤äººã�¯ã�†ã��ã‚ˆã�®ã�»ã�‹ç‰©ã�ªã‚Šã�‘ã‚‹ä¹Ÿã€€ã‚�ã�Œæ—¥ã�®æœ¬ã�«ä¸‰ç­†ã�®ã�²ã�¨ã�¤ã�¨ã�„ã�²ã�—ä¸–å°Šå¯ºã�®å�¿ã‚’ã�¯ã�˜ã‚�è¢–ã�®ã�†ã�¤ã‚Šé¦™ã‚†ã�‹ã�—ã�¨ã�—ã�Ÿã�²ã�—å�›ã�Ÿã�¡ã�®ã�†ã�¡ã�Ÿã‚Œã�‹ã�¯ã�¾ã�Œï¼�ï¼¼æ•·ã�—ã‚Œã‚‚ã�®ã�‚ã‚‰ã‚“ã‚„ã€€ã�•ã‚‹äººã€…ã�®æ­¤å�›ã�«åˆ¥ã‚Œã�¬ã‚‹å¾Œã�„ã�‹ã�ªã‚‰ã‚“ã�¤ã�¾ã‚’å¾—ã�Ÿã‚Šã�¨ã‚‚ã�‚ã�¯ã‚Œã�Ÿã�¡ã�¾ã�•ã‚Šã�¦ã�ªã�©ã�Šã‚‚ã�¸ã‚‹ã�¯ã�‚ã‚‰ã�˜ã€€ä¸€æ™‚ã�®æƒ…ã�«ä¸€ã�¨ã�Šã‚‚ã�¯ã‚Œã�¬ã‚‹ã�¯æ­¤äººã�®ã‚�ã�žã�¿ã�Ÿã‚Šã�¬ã‚‹æˆ�ã�‹ã�—ã€€é§¿é¦¬ã�®éª¨ã�¨ã�„ã�²ã�‘ã‚‹çµ‚ã‚Šã�®ã�•ã�¾ã‚’æ·ºã�¾ã�—ã�¨ã�¤ã�¾ã�¯ã�˜ã��ã�™ã‚‹ã�¯å…¶äººã‚’ã�—ã‚‰ã�­ã�°ã�žã�‹ã�—ã€€å®®ã�®å¾¡å‰�ã�™ã‚‰æ’«å­�ã�«æ¶™ã‚’ã��ã‚�ã�Žçµ¦ã�²ã�‘ã‚‹ã�»ã�©å°‘ç´�è¨€ã�®ã�‹ã��ã�‚ã‚Šã�‘ã‚‹ã�¯é�“ç�†ä¹Ÿã€€ã�Šã�ªã�˜ã�†ã�¯å¾¡å ‚ã�©ã�®ã�Œå‰�ã�«ã�¦çŒ¶ä»Šå°‘ã�—ã�„ã�¯ã�›ã�¾ã�»ã�—ã��äº‹ä¾�ã‚Šã€€æ­¤å�›ã‚’å¥³ã�¨ã�—ã�¦ã�‚ã�’ã�¤ã‚‰ã�µäººã�‚ã‚„ã�¾ã‚Œã‚Šã€€ã�¯ã‚„ã�†å¥³ã�®ã�•ã�‹ã�„ã‚’ã�¯ã�ªã‚Œã�¬ã‚‹äººã�ªã‚Œã�°ã�¤ã�²ã�®ä¸–ã�«ã�¤ã�¾ã‚‚ä¾�ã‚‰ã�–ã‚Šã��å­�ã‚‚ä¾�ã‚‰ã�–ã‚Šã��ã€€å�‡åˆ�ã�®ç­†ã�™ã�•ã�³æˆ�ã�‘ã‚‹æž•ã�®è�‰å­�ã‚’ã�²ã‚‚ã�¨ã��ä¾�ã‚‹ã�«ã�†ã�¯ã�¹ã�¯èŠ±ç´…è‘‰ã�®ã�†ã‚‹ã�¯ã�—ã�’ã�ªã‚‹ã�“ã�¨ã‚‚ã�µã�Ÿã‚�ã�³ä¸‰åº¦è¦‹ã‚‚ã�¦ã‚†ã��ã�«å“€ã‚Œã�«æ·‹ã�—ã��æ°£ã�žæ­¤ä¸­ã�«ã‚‚ã�“ã‚‚ã‚Šä¾�ã‚‹ã€€æº�æ°�ç‰©ã�Œã�Ÿã‚Šã‚’å�ƒå�¤ã�®å��ç‰©ã�¨ã�Ÿã‚�ã‚†ã‚‹ã�¯ã��ã�®æ™‚ã��ã�®äººã�®ã�†ã�¡ã�‚ã�²ã�¦ã�¤ã�²ã�«ã�•ã‚‹ã‚‚ã�®ã‚�å‡ºä¾†ã�«ã�‘ã‚“ã€€å°‘ç´�è¨€ã�«å¼�éƒ¨ã�®æ‰�ã�ªã�—ã�¨ã�„ã�µã�¹ã�‹ã‚‰ã�šã€€å¼�éƒ¨ã�Œå¾³ã�¯å°‘ç´�è¨€ã�«ã�¾ã�•ã‚Šã�Ÿã‚‹äº‹ã‚‚ã�¨ã‚ˆã‚Šã�ªã‚Œã�©ã�•ã‚Šã�¨ã�¦å°‘ç´�è¨€ã‚’ã‚’ã�¨ã�—ã‚�ã‚‹ã�¯ã�‚ã‚„ã�¾ã‚Œã‚Šã€€å¼�éƒ¨ã�¯å¤©ã�¤ã�¡ã�®ã�„ã�¨ã�—ã�”ã�«ã�¦å°‘ç´�è¨€ã�¯éœœã�µã‚‹é‡Žé‚Šã�«ã�™ã�¦å­�ã�®èº«ã�®ä¸Šæˆ�ã‚‹ã�¹ã�—ã€€ã�‚ã�¯ã‚Œã�ªã‚‹ã�¯æ­¤å�›ã�®ä¸Šã‚„ã�¨ã�„ã�²ã�—ã�«äººã€…ã�‚ã�–ã�¿ç¬‘ã�²ã�¬";
 public static void main(String ... args)
 {
   System.out.println(WORDS);
 }
 
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
 
 public Scene7()
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
//           cue("[2] Scanner--Not implemented"
//               ),
           cue("[3] CHAIN -> Start Emitters @ ~1:25, Gravity On1",
               Actions.loadEnvironment(new File("/content/scene_one/scene-one-pre-gravity.env")),
               action("Start Gravity", null, (e)->{e.addBehavior(gravity);})
               ),
//           load("[4] Cross after spin on Ground",
//               "content/scene_one/rec/1-cue-4",
//               "content/scene_one/brush/crosses",
//               action("Set Drag Low", null, e-> e.getPhysics().setDrag(.0402f))
//               ),
//           load("[5] Girls Entrance from Left (might move up",
//               "content/scene_one/rec/1-cue-5",
//               "content/scene_one/brush/dl-entrance"
//               ),
//           load("[6] Girls cross upstage center",
//               "content/scene_one/rec/1-cue-6",
//               "content/scene_one/brush/1-cue-6"
//               ),
//           load("[7] Girls Carry to Left, then to up right",
//               "content/scene_one/rec/1-cue-7",
//               "content/scene_one/brush/1-cue-6"
//               ),
//           load("[8] Other group crossing towards up-right"
//               ),
           cue("[9] Rainfall is started",
               action("Emitter", (l)-> {startFountain(7, 12, 72);}, null)),
           load("[10] Group at UP LEFT ",
               "content/scene_one/rec/grop-cross-ul",
               "content/scene_one/brush/scene-1-soft"
               ),
           cue("[11] Two break",
               action("Stop Playbacks", null, e->{e.clearPlaybacks();})
               ),
//           load("[12] Two more break",
//               "content/scene_one/rec/break-2",
//               "content/scene_one/brush/break"),
//           load("[13] Two more break",
//               "content/scene_one/rec/break-3",
//               "content/scene_one/brush/break"),
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
   
   public Cue cue(String label, ActionOLD ... actions)
   {
     return cue(label, null, actions);
   }
   public Cue cue(String label, List<ActionOLD> post, ActionOLD ... pre)
   {
     ArrayList<ActionOLD> list = new ArrayList<ActionOLD>();
     if(pre != null)
       for(ActionOLD a : pre)
         list.add(a);
     
     if(post != null)
       for(ActionOLD a : post)
         list.add(a);
     
     Cue cue = new Cue(label, list);
     return cue;
   }
   
   public Cue load(String label, String file, String brushFile, ActionOLD ... post)
   {
     ArrayList<ActionOLD> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
     if(post != null)
       for(ActionOLD a : post)
         acts.add(a);
     return cue(label, acts);
   }
   
   public Cue load(String label, String env, String file, String brushFile, ActionOLD ... post)
   {
     ArrayList<ActionOLD> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
     ActionOLD loadEnv = Actions.loadEnvironment(new File(env));
     if(post != null)
       for(ActionOLD a : post)
         acts.add(a);
     return cue(label, acts, loadEnv);
   }
   
   public Cue load(String label, String file, String brush)
   {
     try
     {
       ArrayList<ActionOLD> acts = Actions.loadRecordingAsAction(new File(file), new File(brush));
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

   public ActionOLD action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE, int delay)
   {
     return new ActionOLD(label, delay, fL, fE);
   }
   
   public ActionOLD action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
   {
     return new ActionOLD(label, 0, fL, fE);
   }
   public ActionOLD action(int delay, String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
   {
     return new ActionOLD(label, delay, fL, fE);
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
       String toDisplay = WORDS.substring(0, 50);
       if(toDisplay == null)
         toDisplay = "No Text";
       return new Paragraph(toDisplay, p, width, 12, 12, delay, fade, total, from, to, Paragraph.FadeType.LETTER_BY_LETTER);
     };
     
//     Integer[][] args = new Integer[][]
//         {
//                       //x,      y, wth, delay, fade,  total, from,  to
//           new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//1
//           new Integer[]{400,  100,  400,     0, 3000, 10000, 0, 255},//2
//           new Integer[]{200,  500,  400,     0, 3000, 10000, 0, 255},//3
//           new Integer[]{600,  400,  400,     0, 3000, 10000, 0, 255},//4
//           new Integer[]{350,  330,  400,     0, 3000, 10000, 0, 255},//5
//           new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//6
//           new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//7
//           new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//8
//           new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255},//9
//           new Integer[]{50,   50,  400,     0, 3000, 10000, 0, 255} //10
//         };
     
//     tweets.add(create(1, 0f,  -.5f, .3f));
//     tweets.add(create(2, -.8f, .2f, .35f));
//     tweets.add(create(3, .6f,    0, .29f));
//     tweets.add(create(4, -.9f,-.9f, .4f));
//     tweets.add(create(5, .5f,    0, .25f));
     
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
     
     Point p = new Point(c[0], c[1], 0, 1f);
     String toDisplay = WORDS.substring(0, 50);
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
   return "Scene7";
 }
}
