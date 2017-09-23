package com.danielbchapman.motion.core;

import java.util.function.BiConsumer;

import com.danielbchapman.physics.ui.FloatSlider;

import toxi.physics3d.behaviors.ParticleBehavior3D;

public class BehaviorSlider2017<T extends ParticleBehavior3D> extends FloatSlider
{
  /**
   * A reference back to the toolkit used for this, this is really not a great 
   * design but it will work.
   */
  private final EnvironmentTools2017 environmentTools;
  private static final long serialVersionUID = 1L;
  private BiConsumer<T, Float> onChange;
  private BiConsumer<T, BehaviorSlider2017<T>> get;
  private T behavior;
  private boolean useEnabled;
  
  public BehaviorSlider2017(EnvironmentTools2017 environmentTools, String label, int min, int max, float divisor, T behavior, BiConsumer<T, Float> onChange, BiConsumer<T, BehaviorSlider2017<T>> get)
  {
    this(environmentTools, label, min, max, divisor, behavior, onChange, get, false);
  }
  
  public BehaviorSlider2017(EnvironmentTools2017 environmentTools, String label, int min, int max, float divisor, T behavior, BiConsumer<T, Float> onChange, BiConsumer<T, BehaviorSlider2017<T>> get, boolean useEnabled)
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
          this.environmentTools.scene.addBehavior(behavior);
        }
        else
          this.environmentTools.scene.removeBehavior(behavior);
      });
    else
      enabled.setVisible(false);
  }
  
  @Override
  public void set(float f)
  {
    super.set(f);
    System.out.println("Updateding Property " + behavior.getClass());
    onChange.accept(behavior, f);
  }    
  
  public void sync()
  {
    if(useEnabled && this.environmentTools.getScene() != null)
      enabled.setSelected(this.environmentTools.getScene().isActive(behavior));
    get.accept(behavior, this);
  }
}