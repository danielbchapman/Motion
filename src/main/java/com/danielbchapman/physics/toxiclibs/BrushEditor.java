package com.danielbchapman.physics.toxiclibs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.UiUtility;

public class BrushEditor extends JFrame
{
  MotionInteractiveBehavior current;
  private static final long serialVersionUID = 1L;
  
  public static void main(String ... args)
  {
    BrushEditor be = new BrushEditor();
    be.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    be.setVisible(true);
  }
  
  ArrayList<JButton> brushDefaults = new ArrayList<JButton>();
  
  JButton save;
  JButton close;
  JButton update;
  public BrushEditor()
  {
    setSize(300, 668);
    setTitle("Brush Editor");
    save = new JButton("Save");
    close = new JButton("Close");
    update = new JButton("Meh?");
    
    int row = 0;
    title("Variables", row++);
    
    //Buttons
    add(new JPanel(), UiUtility.getFillHorizontal(0, 99));
    add(update, UiUtility.getFillHorizontal(3, 99));
    add(save, UiUtility.getFillHorizontal(1, 99));
    add(close, UiUtility.getFillHorizontal(2, 99));
    
    
  }
  
  public void populate(ForceVariables vars)
  {
    
  }
  
  public void saveBrush(String file)
  {
    //Line one == names
    //Line two == data
  }
  
  public void loadBrush(String file)
  {
    ArrayList<String> lines = FileUtil.readLines(file);
    if(lines == null)
      JOptionPane.showMessageDialog(this, "Unable to load the file, please check the logs.");
    
    if(lines.size() >= 2 && !lines.get(1).trim().isEmpty())
    {
      
    }
    else//use defaults
    {
      
    }
      
  }
  
  public void save()
  {
    String disclaimer = "<html><h1>Do you really want to save this brush?</h1></html>";
    String disclaimer2 = "<html><p>Saving this will create a file in the brush folder for future use.</p></html>";
    JFrame save = new JFrame();
    save.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    save.setTitle("Save Brush?");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
    JTextField name = new JTextField("brush-" + sdf.format(new Date()));
    JButton cancel = new JButton("Cancel");
    JButton doIt = new JButton("Save");
    
    save.setLayout(new GridBagLayout());
    GridBagConstraints x = UiUtility.getFillHorizontal(0, 0);
    x.gridwidth = 3;
    save.add(new JLabel(disclaimer), x);
    x.gridy = 1;
    save.add(new JLabel(disclaimer2), x);
    save.add(new JLabel("File Name: "), UiUtility.getNoFill(0, 5));
    save.add(name, UiUtility.getNoFill(1, 5));
    save.add(new JPanel(), UiUtility.getFillHorizontal(0, 99));
    save.add(doIt, UiUtility.getFillHorizontal(1, 99));
    save.add(cancel, UiUtility.getFillHorizontal(2, 99));
    
    save.setSize(400, 300);
    UiUtility.centerFrame(this, save);
    save.setVisible(true);
    name.selectAll();
    cancel.addActionListener((l)->{ save.setVisible(false); save.dispose();});
//    doIt.addActionListener((l)->
//    {
//      if(saveVariables(name.getText()))
//      {
//        save.setVisible(false);
//        save.dispose();
//      }
//      else
//        JOptionPane.showMessageDialog(save, "Unable to save the file, please check the logs");
//    });
  }
  
  private void title(String text, int row)
  {
    GridBagConstraints g = UiUtility.getFillHorizontal(0, row);
    g.gridwidth = 4;
    add(new JLabel("<html><h1><b>" + text + "</h1></html>"),g);
  }
  
  private void subtitle(String text, int row)
  {
    GridBagConstraints g = UiUtility.getFillHorizontal(0, row);
    g.gridwidth = 4;
    add(new JLabel("<html><h3><b>" + text + "</h3></html>"),g);
  }
}
