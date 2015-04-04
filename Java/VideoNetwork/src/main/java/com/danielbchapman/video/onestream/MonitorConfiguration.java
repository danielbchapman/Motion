package com.danielbchapman.video.onestream;

import java.awt.GridBagLayout;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.danielbchapman.utility.UiUtility;
import com.danielbchapman.video.blending.Vec2;

public class MonitorConfiguration extends JFrame
{
  private static final long serialVersionUID = 1L;

  public static void main(String ... args)
  {
    Monitor t = new Monitor(null, 0, 1);
    t.contentStart = new Vec2(400, 0);
    t.contentDimensions = new Vec2(800, 600);
    new MonitorConfiguration(t, null);
  }
  private Monitor monitor;
  private CaptureApp app;
  
  //Input
  JTextField contentX = new JTextField();
  JTextField contentY = new JTextField();
  JTextField contentW = new JTextField();
  JTextField contentH = new JTextField();
  JTextField monitorResX = new JTextField();
  JTextField monitorResY = new JTextField();
  
  JTextField tLx = new JTextField();
  JTextField tLy = new JTextField();
  JTextField tRx = new JTextField();
  JTextField tRy = new JTextField();
  JTextField bRx = new JTextField();
  JTextField bRy = new JTextField();
  JTextField bLx = new JTextField();
  JTextField bLy = new JTextField();
  
  
  @SuppressWarnings("serial")
  public MonitorConfiguration(Monitor m, CaptureApp app)
  {
    this.monitor = m;
    this.app = app;
    setTitle("Configure" + monitor.name);
    setLayout(new GridBagLayout());
    setSize(300, 200);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    //Settings
    UiUtility.centerFrame(this);
    add(new JLabel("X/W"), UiUtility.getNoFill(1, 0));
    add(new JLabel("Y/H"), UiUtility.getNoFill(2, 0));
    
    add(new JLabel("Content Start (x/y)"), UiUtility.getNoFill(0, 1));
    add(contentX, UiUtility.getFillHorizontal(1, 1));
    add(contentY, UiUtility.getFillHorizontal(2, 1));
    
    add(new JLabel("Content Width"), UiUtility.getNoFill(0, 2));
    add(contentW, UiUtility.getFillHorizontal(1, 2));
    add(contentH, UiUtility.getFillHorizontal(2, 2));

    add(new JLabel("Monitor Resolution"), UiUtility.getNoFill(0, 3));
    add(monitorResX, UiUtility.getFillHorizontal(1, 3));
    add(monitorResY, UiUtility.getFillHorizontal(2, 3));
    
//    add(new JLabel("Top Left"), UiUtility.getNoFill(0, 4));
//    add(tLx, UiUtility.getFillHorizontal(1, 4));
//    add(tLy, UiUtility.getFillHorizontal(2, 4));
//    
//    add(new JLabel("Top Right"), UiUtility.getNoFill(0, 5));
//    add(tRx, UiUtility.getFillHorizontal(1, 5));
//    add(tRy, UiUtility.getFillHorizontal(2, 5));
//    
//    add(new JLabel("Bottom Right"), UiUtility.getNoFill(0, 6));
//    add(bRx, UiUtility.getFillHorizontal(1, 6));
//    add(bRy, UiUtility.getFillHorizontal(2, 6));
//    
//    add(new JLabel("Bottom Left"), UiUtility.getNoFill(0, 7));
//    add(bLx, UiUtility.getFillHorizontal(1, 7));
//    add(bLy, UiUtility.getFillHorizontal(2, 7));
    
    add(new JButton("Save"){{addActionListener((x)->{save();});}}, UiUtility.getNoFill(1,98));
    add(new JButton("Cancel"){{addActionListener((x)->{cancel();});}}, UiUtility.getNoFill(2,98));
    
    add(new JPanel(), UiUtility.getFillBoth(2, 99));

    //Update content
    contentX.setText("" + monitor.contentStart.x);
    contentY.setText("" + monitor.contentStart.y);
    
    contentW.setText("" + monitor.contentDimensions.x);
    contentH.setText("" + monitor.contentDimensions.y);    

    monitorResX.setText(monitor.cache == null ? "null" : "" + monitor.cache.width);
    monitorResY.setText(monitor.cache == null ? "null" : "" + monitor.cache.height);
    setVisible(true);
  }
  
  public void save()
  {
    System.out.println("Saving...");
    
    Function<JTextField, Integer> as = (x)->{return Integer.valueOf(x.getText());};
    BiFunction<JTextField, JTextField, Vec2> asx = (x,y)->
    {
      return new Vec2(as.apply(x), as.apply(y));
    };
    int x = as.apply(contentX);
    int y = as.apply(contentY);
    int w = as.apply(contentW);
    int h = as.apply(contentH);

    monitor.contentStart.x = x;
    monitor.contentStart.y = y;
        
    monitor.contentDimensions.x = w;
    monitor.contentDimensions.y = h;
    
//    monitor.topLeft = asx.apply(tLx, tLy);
//    monitor.topRight = asx.apply(tRx, tRy);
//    monitor.bottomRight = asx.apply(bRx, bRy);
//    monitor.bottomLeft = asx.apply(bLx, bLy);
    
    //Blend is next!
    app.updateMeshAndBlend();
    this.setVisible(false);
    this.dispose();
  }
  
  public void cancel()
  {
    this.setVisible(false);
    this.dispose();
  }
 
}
