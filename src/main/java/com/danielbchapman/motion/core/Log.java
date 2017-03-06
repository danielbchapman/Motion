package com.danielbchapman.motion.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Log
{
  public static Logger log = LogManager.getLogger(Log.class);
  
  static {
    String logging = System.getProperties().getProperty("log");
    if(logging == null)
      logging = "debug";
 
    if("debug".equalsIgnoreCase(logging))
      Configurator.setLevel(System.getProperty("log4j.logger"), Level.INFO);
    
    else if("info".equalsIgnoreCase(logging))
      Configurator.setLevel(System.getProperty("log4j.logger"), Level.INFO);
    
    else if("warn".equalsIgnoreCase(logging))
      Configurator.setLevel(System.getProperty("log4j.logger"), Level.WARN);
    
    else if("error".equalsIgnoreCase(logging))
      Configurator.setLevel(System.getProperty("log4j.logger"), Level.ERROR);
    
    else if("severe".equalsIgnoreCase(logging))
      Configurator.setLevel(System.getProperty("log4j.logger"), Level.ERROR);
    
    else
      Configurator.setLevel(System.getProperty("log4j.logger"), Level.DEBUG); 
  }
  public static void debug(Object obj)
  {
    log.debug(obj);
  }
  
  public static void info(Object obj)
  {
    log.info(obj);    
  }
  
  public static void warning(Object obj)
  {
    log.warn(obj);
  }
  
  public static void severe(Object obj)
  {
    log.error(obj); 
  }
}
