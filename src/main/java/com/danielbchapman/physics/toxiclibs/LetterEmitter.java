package com.danielbchapman.physics.toxiclibs;

import com.danielbchapman.artwork.Letter;
import com.danielbchapman.artwork.Word;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class LetterEmitter extends Emitter<PointWrapper<Word>>
{

  String[] text;
  int index;
  int max; 
  
  /**
   * @param text
   * @param position
   * @param heading
   * @param lifeSpan
   * @param rate
   * @param randomVector
   * @param randomTime
   */
  //FIXME Java Doc Needed
  public LetterEmitter(String text, Vec3D position, Vec3D heading, int lifeSpan, int rate, float randomVector, int randomTime)
  {
    super(position, heading, lifeSpan, rate, randomVector, randomTime);
    setText(text);
  }

  public void setText(String text)
  {
    char[] chars = text.toCharArray();
    
    this.text = new String[chars.length];
    for(int i = 0; i < chars.length;i++)
      this.text[i] = "" + chars[i];
    index =0;
    max = this.text.length;
  }
  
  @Override
  public void draw(PGraphics g)
  {
    for(PointWrapper<Word> p : children)
      p.getWrap().draw(g, p);
      
  }

  @Override
  public PointWrapper<Word> createPoint(float x, float y, float z, float w)
  {
    PointWrapper<Word> p = new PointWrapper<Word>(x,y,z,w);
    if(index >= max)
      index = 0;
    
    p.setWrap(new Word(text[index], p));//could use the "word here" to set the death time...
    index++;
    return p;
  }



}
