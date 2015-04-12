package com.danielbchapman.debug;

public class Debug
{
  public static boolean debug = false;
  public static DebugInstance d = new DebugInstance();
  
  public static DebugInstance outln(Object s)
  {
    return d.outln(s == null ? "null" : s.toString());
  }
  
  public static DebugInstance errln(Object s)
  {
    return d.errln(s == null ? "null" : s.toString());
  }
  
  public static DebugInstance out(Object s)
  {
    return d.out(s == null ? "null" : s.toString());
  }
  
  public static DebugInstance err(Object s)
  {
    return d.err(s == null ? "null" : s.toString());
  }
  
  static class DebugInstance
  {
    public DebugInstance outln(String s)
    {
      out(s);
      out("\n");
      return this;
    }
    
    public DebugInstance errln(String s)
    {
      err(s);
      err("\n");
      return this;
    }
    
    public DebugInstance out(String s)
    {
      System.out.print(s);
      return this;
    }
    
    public DebugInstance err(String s)
    {
      System.err.print(s);
      return this;
    }
  }
}
