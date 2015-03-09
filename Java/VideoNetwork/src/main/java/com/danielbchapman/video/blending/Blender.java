package com.danielbchapman.video.blending;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Blender
{
  Output one;
  Output two;
  Output three;
  Output four;
  
 
  public Blender()
  {
    new Controller();
    one = new Output("One");
    one.setVisible(true);
    
//    two = new Output("Two");
//    two.setVisible(true);
//    
//    three = new Output("Three");
//    three.setVisible(true);
//    
//    four = new Output("Four");
//    four.setVisible(true);
  }

  public static void showOnScreen( int screen, JFrame frame )
  {
      GraphicsEnvironment ge = GraphicsEnvironment
          .getLocalGraphicsEnvironment();
      GraphicsDevice[] gs = ge.getScreenDevices();
      if( screen > -1 && screen < gs.length )
      {
          gs[screen].setFullScreenWindow( frame );
      }
      else if( gs.length > 0 )
      {
          gs[0].setFullScreenWindow( frame );
      }
      else
      {
          throw new RuntimeException( "No Screens Found" );
      }
  }
  
  public static class Variables 
  { 
    Dimension size;
    Dimension location;
    int screen;
    boolean fullScreen;
  }
  
  public class Controller extends JFrame 
  {
    private static final long serialVersionUID = 1L;
    
    public Controller()
    {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Video Blender 0.0.1");
      add(new JButton("Go Fullscreen"){
        {
          addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
              System.out.println("Going Full Screen by rebuilding API");
            }
            
          });
        }
        });
    }
    
  }
}
