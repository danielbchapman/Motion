package com.danielbchapman.physics.ui;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PropertySlider<Source> extends FloatSlider
{
  private static final long serialVersionUID = 1L;
  private BiConsumer<Source, Float> onChange;
  private Function<Source, Float> get;
  private Source source;
  
  
  /**
   * @param label
   * @param min
   * @param max
   * @param divisor
   * @param source
   * @param onChange
   * @param get
   */
  public PropertySlider(String label, int min, int max, float divisor, Source source, BiConsumer<Source, Float> onChange, Function<Source, Float> get)
  {
    this(label, min, max, divisor, source, onChange, get, false);
  }
  
  public PropertySlider(String label, int min, int max, float divisor, Source source, BiConsumer<Source, Float> onChange, Function<Source, Float> get, boolean useEnabled)
  {
    super(label, min, max, divisor);
    this.onChange = onChange;
    this.get = get;
    this.source = source;
    enabled.setVisible(false);
  }
  
  @Override
  public void set(float f)
  {
    super.set(f);
    onChange.accept(source, f);
  }    
  
  public void sync()
  {
    super.set(get.apply(source));
  }
}