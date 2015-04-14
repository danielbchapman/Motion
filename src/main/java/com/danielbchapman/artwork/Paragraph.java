package com.danielbchapman.artwork;

import java.util.ArrayList;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.physics.toxiclibs.Point;

/** 
 * A simple collection of words that constitutes a mono-spaced paragraph and 
 * extends Fadeable.
 */
public class Paragraph
{
  public enum FadeType
  {
    /**
     * Fade everything at once
     */
    TOGETHER,
    /**
     * Fade each line together (new line separated) as a cascade
     */
    LINE_BY_LINE,
    /**
     * Fade each word as a cascade
     */
    WORD_BY_WORD,
    /**
     * Fade each letter as a cascade (automatically splits the word)
     */
    LETTER_BY_LETTER
  }

  public final String content;
  public Word[] words;
  public boolean split;
  public Point[] points;

  // extend this an apply the force EQUALLY to all points, that's the way to distribute it.
  public Point parent;
  public int width;
  public int height;

  public Paragraph(String content, Point parent, int maxWidth, int wordSpace, int rowSpace, int delay, int fadeCount, int totalTime, int from, int to, FadeType type)
  {
    this.content = content;
    this.width = maxWidth;
    
    
    if (FadeType.LETTER_BY_LETTER == type)
    {
      String[] parts = content.split("\\s+");
      ArrayList<Character> characters = new ArrayList<>();
      
      for(String s : parts)
      {
        for(char c : s.toCharArray())
          characters.add(c);
      }
        
      ArrayList<Word> wordList = new ArrayList<>();
      int col = 0;
      int row = 0;

      int count = fadeCount;
      int currentDelay = delay;
      int increment = totalTime / characters.size();

      for (String s : parts)
      {

        int colNext = col + s.length() * wordSpace;

        if (colNext > maxWidth)
        {
          col = 0;
          colNext = s.length() * wordSpace;
          row++;
          //System.out.println("Increasing column");
        }

        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++)
        {
          Point p = parent.copyTranslate(col + (wordSpace * i), row * rowSpace, 0);
          Word w = new Word(""+chars[i], p, 0, 255, count, currentDelay);
          wordList.add(w);
          currentDelay += increment;
        }

        colNext += wordSpace; // add a space
        col = colNext;
      }

      points = new Point[wordList.size()];
      words = new Word[wordList.size()];

      this.parent = parent;
      // calculate points
      this.width = maxWidth;
      this.height = (row + 1) * rowSpace;

      for (int i = 0; i < wordList.size(); i++)
      {
        Word w = wordList.get(i);
        points[i] = w.parent;
        words[i] = w;
        //System.out.println(words[i].letters + " @ " + points[i].x + ", " + points[i].y + ", " + points[i].z);
      }
    }
    else if (FadeType.LINE_BY_LINE == type)
    {
      String[] lines = content.split("\n");
      ArrayList<String> parts = new ArrayList<>();
      
      for(String s : lines)
      {
        String[] strs =  s.split("\\s+");
        for(String s1 : strs)
          parts.add(s1);
        
        parts.add(null);//newline marker
      }
        
      ArrayList<Word> wordList = new ArrayList<>();
      int col = 0;
      int row = 0;

      int count = fadeCount;
      int currentDelay = delay;
      int increment = totalTime / lines.length;

      for (String s : parts)
      {
        if(s == null)
        {
          currentDelay += increment;
          col = 0;
          row++;
          //System.out.println("Increasing column");
          continue;
        }
        
        int colNext = col + s.length() * wordSpace;

        if (colNext > maxWidth)
        {
          col = 0;
          colNext = s.length() * wordSpace;
          row++;
          //System.out.println("Increasing column");
        }

        Point p = parent.copyTranslate(col, row * rowSpace, 0);

        Word w = new Word(s, p, 0, 255, count, currentDelay);
        wordList.add(w);
        colNext += wordSpace; // add a space
        col = colNext;
      }

      points = new Point[wordList.size()];
      words = new Word[wordList.size()];

      this.parent = parent;
      // calculate points
      this.width = maxWidth;
      this.height = (row + 1) * rowSpace;

      for (int i = 0; i < wordList.size(); i++)
      {
        Word w = wordList.get(i);
        points[i] = w.parent;
        words[i] = w;
        //System.out.println(words[i].letters + " @ " + points[i].x + ", " + points[i].y + ", " + points[i].z);
      }
    }
    else if (FadeType.WORD_BY_WORD == type || FadeType.TOGETHER == type)
    {
      String[] parts = content.split("\\s+");
      ArrayList<Word> wordList = new ArrayList<>();
      int col = 0;
      int row = 0;

      int count = fadeCount;
      int currentDelay = delay;
      int increment = totalTime / parts.length;

      if(type == FadeType.TOGETHER)
        increment = 0;
      
      int debugCount = 0;
      for (String s : parts)
      {

        int colNext = col + s.length() * wordSpace;

        if (colNext > maxWidth)
        {
          col = 0;
          colNext = s.length() * wordSpace;
          row++;
          System.out.println("Increasing column");
        }

        Point p = parent.copyTranslate(col, row * rowSpace, 0);

        Word w = new Word(s, p, 0, 255, count, currentDelay);
        if (debugCount == 50)
          w.debug = true;
        wordList.add(w);
        colNext += wordSpace; // add a space
        col = colNext;
        currentDelay += increment;
        debugCount++;
      }

      points = new Point[wordList.size()];
      words = new Word[wordList.size()];

      this.parent = parent;
      // calculate points
      this.width = maxWidth;
      this.height = (row + 1) * rowSpace;

      for (int i = 0; i < wordList.size(); i++)
      {
        Word w = wordList.get(i);
        points[i] = w.parent;
        words[i] = w;
        System.out.println(words[i].letters + " @ " + points[i].x + ", " + points[i].y + ", " + points[i].z);
      }
    }
    else
    {
      throw new IllegalArgumentException("Fade type was invalid: " + type);
    }
  }

  /**
   * Construct a paragraph with the following constraints on width and the monospaced fonts.
   * @param content the text
   * @param parent the point to draw at (top left)
   * @param maxWidth the maximum width (pixels)
   * @param wordSpace the spacing between letters
   * @param rowSpace the spacing between rows
   */
  public Paragraph(String content, Point parent, int maxWidth, int wordSpace, int rowSpace)
  {
    this.content = content;
    this.width = maxWidth;

    String[] parts = content.split("\\s+");
    ArrayList<Word> wordList = new ArrayList<>();
    int col = 0;
    int row = 0;

    for (String s : parts)
    {

      int colNext = col + s.length() * wordSpace;

      if (colNext > maxWidth)
      {
        col = 0;
        colNext = s.length() * wordSpace;
        row++;
        System.out.println("Increasing column");
      }

      Point p = parent.copyTranslate(col, row * rowSpace, 0);

      Word w = new Word(s, p);
      wordList.add(w);
      colNext += wordSpace; // add a space
      col = colNext;
    }

    points = new Point[wordList.size()];
    words = new Word[wordList.size()];

    this.parent = parent;
    // calculate points
    this.width = maxWidth;
    this.height = (row + 1) * rowSpace;

    for (int i = 0; i < wordList.size(); i++)
    {
      Word w = wordList.get(i);
      points[i] = w.parent;
      words[i] = w;
      System.out.println(words[i].letters + " @ " + points[i].x + ", " + points[i].y + ", " + points[i].z);
    }
  }

  int size = 12;

  /**
   * Render this paragraph to this point in space (top left)
   * @param g
   * @param p  
   * 
   */
  public void draw(PGraphics g, Point p)
  {
    g.pushMatrix();
    // Point.rotation(g, p);
    // g.translate(p.x, p.y, p.z);
    for (int i = 0; i < words.length; i++)
      words[i].draw(g, words[i].parent);

    g.popMatrix();
  }

  /**
   * Moves all the points in this paragraph by a certain amount. This will
   * also translate the forces so that this doesn't cause ridiculous acceleration.
   * 
   * @param vec 
   *          the translation amount  
   * 
   */
  public void translate(Vec3D vec)
  {
    for(Word w : words)
      w.translate(vec);
  }
  
  public void split(VerletPhysics3D physics, float mag)
  {
    System.out.println("Does nothing...");
    if (split)
      return;
    split = true;

    for (Word w : words)
      w.split(physics, mag);
    //
    // //FIXME Remove old points
    // for(int i = 0; i < words.length; i++){
    // Vec3D rand = null;
    // if(mag != 0f)
    // rand = Vec3D.randomVector().scale(mag);
    // else
    // rand = new Vec3D(0,0,0);
    //
    // Point add = words[i].parent;
    // Vec3D addHome = add.home.copy();
    // add.angular = parent.angular;
    // add = parent.copyTranslate(add.x, add.y, add.z);
    // add.home = addHome;
    //
    // words[i].parent = add;
    // points[i] = add;
    // points[i].addForce(rand);
    // physics.addParticle(points[i]);
    // }
  }
}
