package com.danielbchapman.physics.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class Spacer extends JPanel
{

  private static final long serialVersionUID = 1L;

  public Spacer(int x, int y)
  {
    setPreferredSize(new Dimension(x, y));
  }
}