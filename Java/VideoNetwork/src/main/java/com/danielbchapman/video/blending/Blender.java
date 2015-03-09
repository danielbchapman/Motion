package com.danielbchapman.video.blending;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.danielbchapman.groups.Groups;
import com.danielbchapman.groups.Groups.GroupFormatException;

public class Blender
{
  Output[] outputs = new Output[8];
  String file = "settings.xml";
  public Blender()
  {
    try
    {
      System.out.println("Loading settings");
      Groups.fromXml(new FileInputStream("settings.xml"));
    }
    catch (ParserConfigurationException | GroupFormatException | SAXException | IOException e)
    {
      e.printStackTrace();
    }
    
    Controller main = new Controller();
    main.setSize(400, 400);
    main.setVisible(true);
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    for(int i = 0; i < 1; i++){
      Output x = new Output("Output: " + i);
      outputs[i] = x;
      x.setVisible(true);  
    }
    
    //Save on exit
    main.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
          if (JOptionPane.showConfirmDialog(main, 
              "Do you want to save on exit?", "Save State", 
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
          {
            try
            {
              Groups.toXml(new FileOutputStream(new File(file)), "Blender", "Version 0.0.1", Groups.getKnownGroups());
            }
            catch (FileNotFoundException | ParserConfigurationException | TransformerException e)
            {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            System.exit(0);
          }
      }
  });    
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
    
    public void goWindowed()
    {
      setSize(1280, 720);
    }
    public void goFullScreen()
    {
//      System.out.println("Full Screen API");
//      if(full){
//        setVisible(false);
//        invalidate();
//        setSize(Toolkit.getDefaultToolkit().getScreenSize());
//        //setUndecorated(true);
//        revalidate();
//        setVisible(true);
//      }
//      else 
//      {
//        //setUndecorated(false);
//        
//      }
    }
    
  }
}
