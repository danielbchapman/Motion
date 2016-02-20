package com.danielbchapman.ui.fx;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.function.Consumer;



import com.danielbchapman.utility.Utility;

import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;


/**
 * A simple text field that has a built in update for the
 * the decimal format as well as the 
 *
 */
public class BoundDecimal extends TextField implements IBound
{
  @Getter
  @Setter
  private Consumer<Double> onUpdate;
  private double cache;
  
  @Getter
  @Setter
  private DecimalFormat format;
  
  public BoundDecimal(double val, Consumer<Double> onUpdate)
  {
    this(val, null, onUpdate);
  }
  
  public BoundDecimal(double val, DecimalFormat df, Consumer<Double> onUpdate)
  {
    super(df == null ? Double.toString(val) : df.format(val));
    if(df == null)
      this.format = new DecimalFormat("#.##");
    else
      this.format = df;
    
    this.onUpdate = onUpdate;
    cache = val;
    
    this.textProperty().addListener(
      (observe, oldVal, newVal)->
      {
        try
        {
          double d = Double.parseDouble(newVal);
        }
        catch(NumberFormatException e)
        {
          this.setText(oldVal);//revert
          Utility.logWarningException(this.getClass().getSimpleName() + "] Number format exception, this should highlight or be more useful...", e);
        }
      });
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
