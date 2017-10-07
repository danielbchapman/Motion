package com.danielbchapman.motion.core;

import java.util.Map;

import javax.swing.JFrame;

public class SceneTools<T> extends JFrame
{
  private static final long serialVersionUID = 1L;
 
  //TEST METHODS
  public static void main(String ... args)
  {
    JFrame f = new EnvironmentTools2017(null);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  public SceneTools(Motion motion, Class<T> clazz)
  {
    
  }
}
