package com.danielbchapman.physics.toxiclibs;

import java.util.Random;

import processing.core.PGraphics;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.artwork.Word;

public class WordLayer extends ParticleLayer
{
  Word[] words;
  
  @Override
  public Point[] init()
  {
    Point[] result = super.init();
    words = new Word[result.length];
    for(int i = 0; i < result.length; i++)
    {
      words[i] = new Word(randomWord(), result[i]);
    }
    
    return result;
  }
  
  String[] possible = new String[]
      {
        "Cats",
        "Love",
        "Dishes",
        "Short",
        "Turtle"
      };
  
  Random rand = new Random();
  
  public String randomWord()
  {
    return "Cat";
//    int index = rand.nextInt(possible.length);
//    return possible[index];
  }
  @Override
  public void render(PGraphics g)
  {
    g.background(0); //black
    g.strokeWeight(2f);
    g.stroke(255);
    g.fill(255, 255, 255);
    g.pushMatrix();
    g.translate(10, 10, 0);//Offset to see correctly
    for(int i = 0; i < points.length; i++)
    {
      words[i].draw(g, points[i]);
    }
    
    g.popMatrix();
  }
  
  public void randomSplits(VerletPhysics3D physics)
  {
    Random rand = new Random();
    for(Word w : words)
      if((rand.nextInt(5) % 5) == 0)
        w.split(physics, 5f);
  }
  
}
