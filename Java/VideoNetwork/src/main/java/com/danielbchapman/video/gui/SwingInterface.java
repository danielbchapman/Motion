package com.danielbchapman.video.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.danielbchapman.video.NetworkClient;
import com.danielbchapman.video.ServerWindow;

public class SwingInterface extends JFrame
{
  public final static int MODE_CLIENT = 1;
  public final static int MODE_SERVER = 2;

  private int mode = 0;
  private static final long serialVersionUID = 1L;

  public SwingInterface()
  {
    setTitle("Video Network Node by Daniel B. Chapman");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Need to implement the "full screen API"
    setSize(800, 600);
    setLayout(new FlowLayout());
    add(new JButton("Launch Client"){

      {
        addActionListener((ActionEvent e) -> 
        {
          System.out.println("Launching Client");
          new NetworkClient();
        });
      }
    });
    
    add(new JButton("Launch Server"){

      {
        addActionListener((ActionEvent e) -> 
        {
          System.out.println("Launching Server");
          new ServerWindow();
        });
      }
    });    
    setVisible(true);

  }
}
