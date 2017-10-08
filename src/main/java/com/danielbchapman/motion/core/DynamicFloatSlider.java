package com.danielbchapman.motion.core;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.danielbchapman.functional.Procedure;
import com.danielbchapman.text.Safe;
import com.danielbchapman.utility.UiUtility;

public class DynamicFloatSlider extends JPanel
{
  private static final long serialVersionUID = 1L;

  protected JSlider slider;
  protected JTextField text;
  protected JLabel labelText;
  protected String label;
  protected double scalar;
  private Procedure<Float> onChange;
  private boolean _listenersBlocked = false;
  
  public DynamicFloatSlider(String label, float value, boolean negative, Procedure<Float> onChange)
  {
    this.onChange = onChange;
    this.label = label;
    slider = new JSlider(JSlider.HORIZONTAL);
    slider.setMaximum(100);
    slider.setMinimum(negative ? -100 : 100);
    
    labelText = new JLabel(label);
    text = new JTextField();
    text.setPreferredSize(new Dimension(50, 20));
    text.setSize(50, 20);
    text.setEditable(true);
    
    text.addActionListener((e) -> {
        if(!_listenersBlocked)
        {
          float newVal = Safe.parseFloat(text.getText(), (float) slider.getValue());
          set(newVal); 
        }
    });
    
    setPreferredSize(new Dimension(240, 75));
    setLayout(new GridBagLayout());
    
    add(labelText, UiUtility.getNoFill(0, 0));
    add(new JPanel(), UiUtility.getFillHorizontal(1, 0));
    
    add(text, UiUtility.getNoFill(2, 0));
    //add(scalarText, UiUtility.getNoFill(3, 0));
    
    GridBagConstraints gbc = UiUtility.getFillHorizontal(0, 1);
    gbc.gridwidth = 4;
    add(slider, gbc);
    
    slider.addChangeListener((e)->
        { 
          JSlider s = (JSlider) e.getSource();
          if(!s.getValueIsAdjusting() && !_listenersBlocked)
          {
            System.out.println("calling convert w/ " + s.getValue());
            convert(s.getValue());
            //onChange.call(get());
          }
        }
        );
    
    set(value);
  }
  
  public void setRange(double val)
  {
    double max = (double) slider.getMaximum();
    
    double abs = val < 0 ? val * -1.0f : val;
    //find base range
    if (abs <= 0.0075)
      scalar = 10000;
    else if (abs <= 0.075)
      scalar = 1000;
    else if (abs <= 0.75)
      scalar = 100;
    else if (abs <= 7.5)
      scalar = 10;
    else if (abs <= 75)
      scalar = 1;
    else if (abs <= 750)
      scalar = 0.1f;
    else if (abs <= 7500)
      scalar = 0.01f;
    
//    if ((abs / scalar) < 0.50) { //shrink
//      scalar = scalar / 10;
//    } 
//    else if ((abs / scalar) >= 0.80) 
//    { //expand
//      scalar = scalar * 10;
//    }
    
    //resize
    if (scalar >= 10000) 
    {
      scalar = 10000;
    } else if (scalar <= 0.01f) 
    {
      scalar = 0.01f;
    }
    
    System.out.println("scale resizing to [" + scalar + "] from abs [" + abs + "] (" + val + ")");
    
    slider.setMajorTickSpacing(25);
    slider.setMinorTickSpacing(5);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    
    slider.setValue((int) (val * scalar));
    DecimalFormat df = new DecimalFormat("0.#####");
    labelText.setText(label + " " + "x" + df.format((1 / scalar)));
  }
  /**
   * Set the float value;
   * @param f   
   * 
   */
  public void set(double f)
  {
    _listenersBlocked = true;
    DecimalFormat df = new DecimalFormat("0.#####");
    text.setText(df.format(f));
    setRange(f); 
    this.onChange.call((float)f); //broadcast while blocked
    _listenersBlocked = false;
  }
  
//  public float get()
//  {
//    System.out.println("get called " + val)
//    return (float) slider.getValue() / divisor;
//  }

  private void convert(int val)
  {
    System.out.println("convert called " + val + " scalar -> " + scalar);
    double x =  ((double) val) / scalar;//1-10 float
    set(x);
  }
}
