

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;

import com.danielbchapman.logging.Log;
import com.sun.jna.Platform;

public class SpoutProvider
{
  public static ISpoutAPI getInstance(PGraphics gl) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    if(Platform.isWindows() || Platform.isWindowsCE())
    {
      PGraphicsOpenGL pglInstance = null;
      if(gl instanceof PGraphicsOpenGL)
        pglInstance = (PGraphicsOpenGL) gl;
      
      if(pglInstance == null)
      {
        Log.severe("The grapghics instance is not OpenGL");
        return null;
      }
      //This prevents this from being called on another operating system
      Class<?> windows = Class.forName("SpoutImplementation");
      Constructor<?>[] cons = windows.getDeclaredConstructors();
      Constructor<?> pgl = null;
      for(int i = 0; i < cons.length ; i++)
      {
        pgl = cons[i];
        if(pgl.getGenericParameterTypes().length == 1) //one arg
          break;
      }
      
      pgl.setAccessible(true);
      
      Object x = pgl.newInstance(gl);
      return (ISpoutAPI) x; //create and load this class
    }
    else
      return null;
    
  }
}
