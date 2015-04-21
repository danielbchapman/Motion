package com.danielbchapman.physics.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import lombok.Getter;
import lombok.Setter;

import com.danielbchapman.utility.UiUtility;

public class FloatSlider extends JPanel
{
  private static final long serialVersionUID = 1L;
  
  @Getter
  @Setter
  protected float value = 0f;
  
  protected JSlider slider;
  protected JTextField text;
  protected JCheckBox enabled;
  protected float divisor;
  
  public FloatSlider(String label, int min, int max, float divisor)
  {
    this.divisor = divisor;
    enabled = new JCheckBox();
    enabled.setText("Enabled");
    slider = new JSlider(JSlider.HORIZONTAL);
    slider.setMinimum(0);
    slider.setMaximum(10000);
    slider.setMajorTickSpacing(1000);
    slider.setMinorTickSpacing(100);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    text = new JTextField();
    text.setPreferredSize(new Dimension(50, 20));
    text.setSize(50, 20);
    text.setEditable(false);
    setPreferredSize(new Dimension(240, 75));
    setLayout(new GridBagLayout());
    
    add(new JLabel(label), UiUtility.getNoFill(0, 0));
    add(new JPanel(), UiUtility.getFillHorizontal(1, 0));
    
    add(text, UiUtility.getNoFill(2, 0));
    add(enabled, UiUtility.getNoFill(3, 0));
    
    GridBagConstraints gbc = UiUtility.getFillHorizontal(0, 1);
    gbc.gridwidth = 4;
    add(slider, gbc);
    
    slider.addChangeListener((e)->
        { 
          JSlider s = (JSlider) e.getSource();
          if(!s.getValueIsAdjusting())
          {
            convert(s.getValue());
          }
        }
        );
  }
  /**
   * Set the float value;
   * @param f   
   * 
   */
  public void set(float f)
  {
    DecimalFormat df = new DecimalFormat("0.0000");
    text.setText(df.format(f));
    slider.setValue((int) (f * divisor));//Convert to an int
  }
  
  private void convert(int val)
  {
    float x =  ((float) val) / divisor;//1-10 float
    set(x);
  }
}