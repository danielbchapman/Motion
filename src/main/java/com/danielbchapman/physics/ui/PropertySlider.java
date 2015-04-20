package com.danielbchapman.physics.ui;

import java.util.function.BiConsumer;

public class PropertySlider<Source> extends FloatSlider
{
  private static final long serialVersionUID = 1L;
  private BiConsumer<Source, Float> onChange;
  private BiConsumer<FloatSlider, Source> get;
  private Source source;
  public PropertySlider(String label, int min, int max, float divisor, Source source, BiConsumer<Source, Float> onChange, BiConsumer<FloatSlider, Source> get)
  {
    this(label, min, max, divisor, source, onChange, get, false);
  }
  
  public PropertySlider(String label, int min, int max, float divisor, Source source, BiConsumer<Source, Float> onChange, BiConsumer<FloatSlider, Source> get, boolean useEnabled)
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
    get.accept(this, source);
  }
}