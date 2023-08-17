package shows.troubledwater;

import com.danielbchapman.brushes.SmallBrush;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class OneLeafEnd extends PaintingLayer
{

  public OneLeafEnd()
  {
    stack.load();
  }

  @Override
  public String getSpecificName()
  {
    return "OneLeafEnd";
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

    @Override
    public void load()
    {
      SmallBrush pen = new SmallBrush();
      
      add(
          load(
              -300, 0, 800, 600,
            "One",
            "show/prologue/leaf-1",
            pen, Actions.follow(3000)),
          load(
              -250, 0, 800, 600,
              "Leaf",
              "show/prologue/leaf-2",
              pen)     
      );
    }
    
  }
}
