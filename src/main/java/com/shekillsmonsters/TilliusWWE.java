package com.shekillsmonsters;

import shows.troubledwater.CharacterBrush;
import shows.troubledwater.PaintingLayer;
import shows.troubledwater.TWCueStack;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class TilliusWWE extends PaintingLayer
{

  public TilliusWWE(MotionEngine engine)
  {
    super(engine);
    stack.load();
  }

  @Override
  public String getSpecificName()
  {
    return "TilliusWWE";
  }
  
  public void go(MotionEngine engine)
  {
    stack.go(engine, this);
  }
  
  Interlude0Stack stack = new Interlude0Stack(this);  
  public class Interlude0Stack extends TWCueStack
  {
    public Interlude0Stack(Layer layer)
    {
      super(layer);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void load()
    {
    	CharacterBrush pen = new CharacterBrush();
      
      add(
          load(  
            "T",
            "show/scene4/tillius/t",
            pen),
          load(
              "Leaf",
              "show/scene4/tillius/i",
              pen),
              
          load(
	          "Leaf",
	          "show/scene4/tillius/l1",
	          pen),
          load(
              "Leaf",
              "show/scene4/tillius/l2",
              pen),
          load(
              "Leaf",
              "show/scene4/tillius/i2",
              pen),
          load(
              "Leaf",
              "show/scene4/tillius/u",
              pen),
          load(
              "Leaf",
              "show/scene4/tillius/s",
              pen)
      );     
    }
    
  }
}
