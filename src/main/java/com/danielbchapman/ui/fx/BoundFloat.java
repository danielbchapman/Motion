package com.danielbchapman.ui.fx;

import java.text.DecimalFormat;
import java.util.function.Consumer;

import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;


/**
 * A simple text field that has a built in update for the
 * the decimal format as well as the 
 *
 */
public class BoundFloat extends TextField implements IBound<Float>
{
  @Getter
  @Setter
  private Consumer<Float> onUpdate;
  private float cache;
  
  @Getter
  @Setter
  private DecimalFormat format;
  
  public BoundFloat(float val, Consumer<Float> onUpdate)
  {
    this(val, null, onUpdate);
  }
  
  public BoundFloat(float val, DecimalFormat df, Consumer<Float> onUpdate)
  {
    super(df == null ? Float.toString(val) : df.format(val));
    if(df == null)
      this.format = new DecimalFormat("#.##");
    else
      this.format = df;
    
    this.onUpdate = onUpdate;
    cache = val;
    
    
    this.textProperty().addListener(
        (observe, oldVal, newVal)->
        {
          //if we don't have focus then fire an update
          if(!focusedProperty().get())
          {
            try
            {
              float f = Float.parseFloat(newVal);
              set(f);
              this.getStyleClass().removeAll(CSS.ERROR);
            }
            catch(NumberFormatException e)
            {
              this.getStyleClass().add(CSS.ERROR);
            }  
          }
        });
    
    this.focusedProperty().addListener(
      (observe, oldVal, newVal)->
      {
        if(!newVal && oldVal)//Focus lost
        {
          try
          {
            float f = Float.parseFloat(this.textProperty().get());
            set(f);
            this.getStyleClass().removeAll(CSS.ERROR);
          }
          catch(NumberFormatException e)
          {
            this.getStyleClass().add(CSS.ERROR);
          }  
        }
      });
  }
  
  /* (non-Javadoc)
   * @see com.danielbchapman.ui.fx.IBound#set(java.lang.Number)
   */
  public void set(Float number)
  {
    if(number != null)
    {
      float f = number.floatValue();
      if(f != cache)
      {
        cache = f;
        textProperty().set(format.format(number));
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
