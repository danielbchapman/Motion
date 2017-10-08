package com.danielbchapman.motion.core;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.danielbchapman.functional.Action;
import com.danielbchapman.functional.Procedure;
import com.danielbchapman.physics.ui.FloatSlider;
import com.danielbchapman.physics.ui.TitleField;
import com.danielbchapman.utility.UiUtility;

public class SceneTools extends JFrame
{
  private static final long serialVersionUID = 1L;
  
  private SceneProperties properties;
  
  //TEST METHODS
  public static void main(String ... args)
  {
    JFrame f = new EnvironmentTools2017(null);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  SceneProperties props;
  ArrayList<DynamicFloatSlider> sliders = new ArrayList<>();
  
  public SceneTools(SceneProperties props, Map<String, Class<?>> types)
  {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(300, 400));
    setTitle("Scene Properties");
    //setup types
    ArrayList<String> sorted = new ArrayList<>();
    
    for(String key: types.keySet())
      sorted.add(key);
      
    Collections.sort(sorted);
    
    //We assume a float if not listed as we generally alter floats
    for(String key: sorted)
    {
      float current = props.getFloat(key, 0);
      DynamicFloatSlider slider = new DynamicFloatSlider(key, current, true, (val)->{
        System.out.println("::change::" + key + "->" + val);
      });
      sliders.add(slider);
    }
    int i = 0;
    setLayout(new GridBagLayout());
    
    place(new TitleField("FORCES"), i++);
    
    for(DynamicFloatSlider f : sliders)
      place(f, i++);
    
  }

  public void sync()
  {
    
  }
  
  public void place(JComponent c, int y)
  {
    GridBagConstraints gbc = UiUtility.getFillHorizontal(0, y);
    gbc.gridwidth = 4;
    this.add(c, gbc);
  }
  
}
