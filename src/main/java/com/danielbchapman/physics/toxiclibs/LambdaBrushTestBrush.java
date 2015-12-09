package com.danielbchapman.physics.toxiclibs; 
 
import java.util.function.Consumer;
 
public class LambdaBrushTestBrush extends LambdaBrushClass 
{ 
  @Override 
  public Consumer<Point> compile() 
  { 
    return (Consumer<Point>)(x)->{System.out.println("TEST");}; 
  } 
} 
