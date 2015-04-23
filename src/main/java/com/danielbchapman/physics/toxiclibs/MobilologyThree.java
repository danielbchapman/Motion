package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.Random;

import com.danielbchapman.artwork.Word;
import com.danielbchapman.utility.FileUtil;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class MobilologyThree extends Layer
{
  int gridX;
  int gridY;
  int spacing;
  
  ArrayList<Emitter> emitters = new ArrayList<>();
  ArrayList<Word> words = new ArrayList<>();
  ArrayList<String> data = new ArrayList<>();
  
  ArrayList<Word> toAdd = new ArrayList<>();
  int dataIndex;
  int dataMax;
  
  Vec3D location = new Vec3D();
  Vec3D low = new Vec3D();
  Vec3D high = new Vec3D();
  
  Random rand = new Random();
  
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
  
  public void setText(String val)
  {
    String[] words = val.split("\\s+");
    dataIndex = 0;
    dataMax = words.length;
    for(String s : words)
      data.add(s);
    
  }
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
//     String data2 = "These are some letters that we should emit";
//     for(int i = 0; i < count; i++)
//     {
//       float x = ((float) Actions.engine.width -75f) / (float)count;
//       x *= i+1;
//       emitters.add(new LetterEmitter(data2, new Vec3D(x, -100, 0), Vec3D.randomVector(), 15000, 1000, 2f, 25));
//     }
//     
//     for(int i = 0; i < count; i++)
//     {
//       float x = ((float) Actions.engine.width -45f) / (float)count;
//       x *= i+1;
//       emitters.add(new LetterEmitter(data2, new Vec3D(x, -100, 150), Vec3D.randomVector(), 15000, 1000, 2f, 25));
//     }
  }
  
  public Point[] init()
  {
    gridX = 120/2;
    gridY = 76/2;
    spacing = 40;
    Point[] grid = new Point[0];
    
    return grid;
  }

  /* (non-Javadoc)
   * @see com.danielbchapman.physics.toxiclibs.Scene#render(processing.core.PGraphics)
   */
  @Override
  public void render(PGraphics g)
  {

    float size = g.textSize;
    g.background(0); //black
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
      w.draw(g, w.getParent());
    g.textSize(size);
    for(Emitter e : emitters)
      e.draw(g);
    
    g.popMatrix();
  }

  public Vec3D randomVec()
  {
    int x = rand(low.x, high.x);
    int y = rand(low.y, high.y);
    int z = rand(low.z, high.z);
    return new Vec3D(x, y, z);
  }
  
  @Override
  public void go(MotionEngine engine)
  {
    //Create random vector
    Vec3D location = randomVec();
    Vec3D force = randomVec().normalizeTo(1f);
    emit(nextWord(), location, force);
  }

  public String nextWord()
  {
    if(data.isEmpty())
      return "null";
    if(dataIndex >= dataMax)
      dataIndex = 0;
    
    return data.get(dataIndex++);
  }
  @Override
  public void update()
  {
    for(Emitter e : emitters)
      e.update(System.currentTimeMillis());
    
    for(Word w : toAdd)
    {
      words.add(w);
      w.split(engine.getPhysics(), 0f);
    }
    
    toAdd.clear();
  }
  
  public void emit(String letters, Vec3D position, Vec3D force)
  {
    PointWrapper<Word> p = new PointWrapper<>(position.x, position.y, position.z, 1f);
    Word word = new Word(letters, p);
    p.setEnableRotation(false);
    toAdd.add(word);
  }
}
