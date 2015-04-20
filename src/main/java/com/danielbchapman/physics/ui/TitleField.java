package com.danielbchapman.physics.ui;

import javax.swing.JLabel;

public class TitleField extends JLabel
{
  private static final long serialVersionUID = 1L;

  public TitleField(String label)
  {
    this.setText("<html><span style='font-size: 14px; font-weight:bold;text-decoration:underline;'>" + label + "</span></html>");
  }
}