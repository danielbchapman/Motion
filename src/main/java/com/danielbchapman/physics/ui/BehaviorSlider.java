package com.danielbchapman.physics.ui;

import java.util.function.BiConsumer;

import com.danielbchapman.physics.toxiclibs.EnvironmentTools;

import toxi.physics3d.behaviors.ParticleBehavior3D;

public class BehaviorSlider<T extends ParticleBehavior3D> extends FloatSlider
{
  /**
   * A reference back to the toolkit used for this, this is really not a great 
   * design but it will work.
   */
  private final EnvironmentTools environmentTools;
  private static final long serialVersionUID = 1L;
  private BiConsumer<T, Float> onChange;
  private BiConsumer<T, BehaviorSlider<T>> get;
  private T behavior;
  private boolean useEnabled;
  
  public BehaviorSlider(EnvironmentTools environmentTools, String label, int min, int max, float divisor, T behavior, BiConsumer<T, Float> onChange, BiConsumer<T, BehaviorSlider<T>> get)
  {
    this(environmentTools, label, min, max, divisor, behavior, onChange, get, false);
  }
  
  public BehaviorSlider(EnvironmentTools environmentTools, String label, int min, int max, float divisor, T behavior, BiConsumer<T, Float> onChange, BiConsumer<T, BehaviorSlider<T>> get, boolean useEnabled)
  {
    super(label, min, max, divisor);
    this.environmentTools = environmentTools;
    this.onChange = onChange;
    this.get = get;
    this.behavior = behavior;
    this.useEnabled = useEnabled;
    if(useEnabled)
      enabled.addItemListener((e)->
      {
        boolean selected = enabled.isSelected();
        if(selected){
          this.environmentTools.engine.addBehavior(behavior);
        }
        else
          this.environmentTools.engine.removeBehavior(behavior);
      });
    else
      enabled.setVisible(false);
  }
  
  @Override
  public void set(float f)
  {
    super.set(f);
    onChange.accept(behavior, f);
  }    
  
  public void sync()
  {
    if(useEnabled && this.environmentTools.engine != null)
      enabled.setSelected(this.environmentTools.engine.isActive(behavior));
    get.accept(behavior, this);
  }
}