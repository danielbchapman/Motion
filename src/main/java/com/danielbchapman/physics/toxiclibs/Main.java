package com.danielbchapman.physics.toxiclibs;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import processing.core.PApplet;

public class Main
{
  public static void main(String ... args)
  {
    //Style JFrames
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
    {
      //Not critical
      e.printStackTrace();
    }
    
    PApplet.main(MotionEngine.class.getName());
  }
}
