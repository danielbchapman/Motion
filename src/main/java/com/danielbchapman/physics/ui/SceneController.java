package com.danielbchapman.physics.ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.utility.UiUtility;

public class SceneController extends JFrame
{
  JLabel info;
  JLabel current;
  JButton go;
  JButton stop;
  JButton doSomethignToCyc;
  JButton resize;
  
  MotionEngine engine;
  public SceneController(MotionEngine engine)
  {
    this.engine = engine;
    setVisible(true);
    setLocation(50, 50);
    setTitle("Motion Controller");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    addKeyListener(new KeyListener(){

      @Override
      public void keyTyped(KeyEvent e)
      {
      }

      @Override
      public void keyPressed(KeyEvent e)
      {
        if(e.getKeyChar() == ' ')
        {
          System.out.println("Go");
          go();
        }
      }

      @Override
      public void keyReleased(KeyEvent e)
      {    
      }});
    
    
    setLayout(new GridBagLayout());
    int i = 0;
    
    info = new JLabel("Information");
    current = new JLabel("Current Text");
    go = new JButton("<html><h1>GO</h1></html>");
    go.setPreferredSize(new Dimension(200, 200));
    go.setSize(200, 200);
    stop = new JButton("STOP");
    doSomethignToCyc = new JButton("DO SOMETHING");
    add(info, UiUtility.getFillHorizontal(0, i++));
    add(current, UiUtility.getFillHorizontal(0, i++));
    add(go, UiUtility.getFillHorizontal(0, i++));
    add(stop, UiUtility.getFillHorizontal(0, i++));
    add(info, UiUtility.getFillHorizontal(0, i++));
    
    go.addActionListener(x->go());
    stop.addActionListener(x->stop());
    go.addActionListener(x->
    {
//      Actions.loadRecording("", brush)
//      engine.activeLayer.play
    });
    setSize(400, 400);
  }
  
  public void go()
  {
    if(engine != null)
      engine.activeLayer.go(engine);
  }
  
  public void stop()
  {
    if(engine != null)
      engine.clearPlaybacks();
  }
  
  
}
