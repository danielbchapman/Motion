package com.danielbchapman.physics.toxiclibs;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{
  public static JFrame MAIN_CONTAINER;
  public static MotionEngine ENGINE;
  
//  public static int getScreen()
//  {
//    MAIN_CONTAINER.getSCre
//    int screen = 0;
//    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//    GraphicsDevice[] gs = ge.getScreenDevices();
//    if( screen > -1 && screen < gs.length ) {
//        gs[screen].setFullScreenWindow(frame);
//    } else if( gs.length > 0 ) {
//        gs[0].setFullScreenWindow(frame);
//    } else {
//        throw new RuntimeException( "No Screens Found" );
//    }
//  }
  
  public static void setUndecorated()
  {
    GraphicsConfiguration ge = MAIN_CONTAINER.getGraphicsConfiguration();
    ge.getBounds();
    System.out.println("BOUNDS ARE -> " + ge.getBounds());
    ENGINE.stop();
    MAIN_CONTAINER.remove(ENGINE);
    MAIN_CONTAINER.setVisible(false);
    MAIN_CONTAINER.dispose();
    MAIN_CONTAINER.setUndecorated(true);
    MAIN_CONTAINER.setVisible(true);
    
    ENGINE = new MotionEngine();
    MAIN_CONTAINER.add(ENGINE, BorderLayout.CENTER);
    ENGINE.init();
    Rectangle monitor = MAIN_CONTAINER.getGraphicsConfiguration().getBounds();
    MAIN_CONTAINER.setLocation(monitor.x, 0);
//    Point screen = MAIN_CONTAINER.getLocationOnScreen();
//    Point real = MAIN_CONTAINER.getLocation();
//    Toolkit.getDefaultToolkit().get
//    MAIN_CONTAINER.setLocation(0, 0);
  }
  
  public static void main(String ... args)
  {
    MAIN_CONTAINER = new JFrame();
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
    MAIN_CONTAINER.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    MAIN_CONTAINER.setSize(Actions.WIDTH, Actions.HEIGHT);
    MAIN_CONTAINER.setLayout(new BorderLayout());
    
    ENGINE = new MotionEngine();
    MAIN_CONTAINER.add(ENGINE, BorderLayout.CENTER);
    ENGINE.init();
    MAIN_CONTAINER.setVisible(true);
    //PApplet.main(MotionEngine.class.getName());
  }
}
