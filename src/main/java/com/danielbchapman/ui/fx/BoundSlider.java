package com.danielbchapman.ui.fx;

import java.util.function.Consumer;

import javafx.scene.control.Slider;
import lombok.Getter;
import lombok.Setter;


/**
 * A simple extension of the Slider class that automatically updates when the value is changed.
 */
public class BoundSlider extends Slider implements IBound<Float>
{
  @Getter
  @Setter
  private Consumer<Float> onUpdate;
  
  /**
   * The cached value of the Float
   */
  @Getter
  private Float cache;
  
  /**
   * Binds the slider to a listener (Consumer&lt;Float&gt;) that fires
   * whenever the value of this slider is not equal to the previous value
   * that was cached.
   * @param value
   * @param min
   * @param max
   * @param onUpdate
   */
  public BoundSlider(float value, float min, float max, Consumer<Float> onUpdate)
  {
    super(min, max, value);
    cache = value;
    this.onUpdate = onUpdate;
    valueProperty().addListener(
        (observed, oldVal, newVal) -> 
        {
          if(onUpdate == null)
            return;
          else
            set(newVal.floatValue());
        } );
  }
  
  /* (non-Javadoc)
   * @see com.danielbchapman.ui.fx.IBound#set(java.lang.Number)
   */
  public void set(Float number)
  {
    if(number != null)
    {
      Float d = number.floatValue();
      if(d != cache)
      {
        this.setValue(number);
        cache = (float) this.getValue();
        onUpdate.accept(cache);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see com.danielbchapman.ui.fx.IBound#get()
   */
  public Float get()
  {
    return cache;
  }
  
  /* (non-Javadoc)
   * @see javafx.scene.Node#toString()
   */
  public String toString()
  {
    return super.toString() + " [" + cache + "]";
  }
}
