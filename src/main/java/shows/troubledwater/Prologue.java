package shows.troubledwater;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class Prologue extends PaintingLayer
{

  public Prologue(MotionEngine engine)
  {
    super(engine);
    stack.load();
  }

  @Override
  public String getSpecificName()
  {
    return "Prologue";
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
      
//      add(
//          load(
//              -300, 0, 800, 600,  
//            "One",
//            "show/prologue/leaf-1",
//            pen),
//          load(
//              -250, 0, 800, 600,
//              "Leaf",
//              "show/prologue/leaf-2",
//              pen, Actions.follow(3000))     
//      );
      
      add(
          load("One",
            "show/prologue/leaf-1",
            pen,
            Actions.follow(3000)),
          load("Leaf",
              "show/prologue/leaf-2",
              pen)     
      );
      
    }
    
  }
}
