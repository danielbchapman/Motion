package com.danielbchapman.physics.toxiclibs;

import java.util.function.Consumer;

public class LambdaBrushClassExample extends LambdaBrushClass
{
  @Override
  public Consumer<PointOLD> compile()
  {
    return (Consumer<PointOLD>)(x)->{ System.out.println("TEST");};
  }
}