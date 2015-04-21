package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import javax.swing.JFrame;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class RecordPallet extends JFrame
{
  private static final long serialVersionUID = 1L;

  ArrayList<RecordAction> actions = new ArrayList<RecordAction>();
  boolean recording = false;
  long start = -1L;
  public void capture(MouseEvent e)
  {
    
  }
  
  public void stop()
  {
    
  }
  
  public void start()
  {
    recording = true;
    start = System.currentTimeMillis();
  }
}
