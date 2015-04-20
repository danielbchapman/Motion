package com.danielbchapman.physics.ui;

import java.awt.Container;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public abstract class ComponentModel
{
  private JPanel panel;
  private boolean called = false;
  
  public int addByGridBag(Container comp, int rowStart)
  {
    if(called)
      return -1;
    
    return implementByGridBag(comp, rowStart);
  }
  
  abstract int implementByGridBag(Container comp, int rowStart);
  /**
   * return true
   * @return <Return Description>  
   * 
   */
  public boolean hasCalled()
  {
    return called;
  }
  
  public JPanel getJPanel()
  {
    if(panel != null)
      return panel;
    
    panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    
    addByGridBag(panel, 0);
    called = true;
    return panel;
  }
}
