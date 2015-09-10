package com.danielbchapman.physics.toxiclibs;

public interface StringSerialize<T>
{
  public String save();
  public T load(String data);
  
  public static String write(PersistentVariables vars)
  {
    
    return "not implemented";
  }
  public static String write(Object ... args)
  {
    StringBuffer buf = new StringBuffer();
    
    for(int i = 0; i < args.length; i++)
    {
      buf.append(args[i]);
      if(i + 1 < args.length)
        buf.append(",");
    }
    
    return buf.toString();
  }
  public static float readFloat(String part)
  {
    return Float.parseFloat(part);
  }
  public static int readInt(String part)
  {
    return Integer.parseInt(part);
  }
}
