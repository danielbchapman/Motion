package com.danielbchapman.ui.fx;

import java.util.function.Consumer;

import javafx.scene.control.Slider;
import lombok.Getter;
import lombok.Setter;


/**
 * A simple extension of the Slider class that automatically updates when the value is changed.
 */
public class BoundSlider extends Slider implements IBound
{
  @Getter
  @Setter
  private Consumer<Double> onUpdate;
  
  /**
   * The cached value of the double
   */
  @Getter
  private double cache;
  
  /**
   * Binds the slider to a listener (Consumer&lt;double&gt;) that fires
   * whenever the value of this slider is not equal to the previous value
   * that was cached.
   * @param value
   * @param min
   * @param max
   * @param onUpdate
   */
  public BoundSlider(double value, double min, double max, Consumer<Double> onUpdate)
  {
    super(min, max, value);
    cache = value;
    valueProperty().addListener(
        (observed, oldVal, newVal) -> 
        {
          if(onUpdate == null)
            return;
          else 
            set(newVal);
        } );
  }
  
  /* (non-Javadoc)
   * @see com.danielbchapman.ui.fx.IBound#set(java.lang.Number)
   */
  public void set(Number number)
  {
    if(number != null)
    {
      double d = number.doubleValue();
      if(d != cache)
      {
        cache = d;
        onUpdate.accept(cache);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see com.danielbchapman.ui.fx.IBound#get()
   */
  public double get()
  {
    return cache;
  }
}
