package shows.tancredi;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

import shows.troubledwater.CharacterBrush;
import shows.troubledwater.PaintingLayer;
import shows.troubledwater.TWCueStack;

public class LetterProjection extends PaintingLayer
{

  public LetterProjection(MotionEngine engine)
  {
    super(engine);
    stack.load();
  }

  @Override
  public String getSpecificName()
  {
    return "letter-writing";
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
    	CharacterBrush pen = new CharacterBrush();
      
      add(
          load(
              40, 0, 533, 400,  
            "p141 Cursive Letter",
            "tancredi/path/letter-writing",
            pen)     
      );      
    }
    
  }
}
