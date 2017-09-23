package com.danielbchapman.motion.core;

import java.net.SocketException;
import java.util.List;
import java.util.stream.Collectors;

import com.danielbchapman.motion.livedraw.RemoteDrawMouseEvent;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;


/**
 * A port of the OSC method from Motion 0.1, this encapsulates it
 * so that it can be isolated.
 *
 * @author Daniel B. Chapman 
 * @since Apr 3, 2017
 */
public class OscModule
{
  //Live Drawing (sending to Remote)
  public boolean liveDrawEnabled = false;
  public String liveDrawUrl;
  public Integer liveDrawPort;
  public OSCPortOut oscSender;
  
  // SHOW CONTROL
  public static int OSC_PORT = 44321;
  public OSCPortIn oscReceiver;
  
  Motion motion;
  public OscModule(Motion motion)
  {
    this.motion = motion;
  }
  /**
   * Start the OSC listeners for this instance
   */
  public void startOSC()
  {
    try
    {
      oscReceiver = new OSCPortIn(OSC_PORT);
      OSCListener listener = new OSCListener()
      {
        public void acceptMessage(java.util.Date time, OSCMessage message)
        {
          List<Object> args = message.getArguments();
          Log.info("MESSAGE RECEIVED | size: [" + args.size() + "] message:[" + message.getArguments().stream().map(x -> x.toString()).collect(Collectors.joining(" | ")) + "]");
          if (args == null || args.size() < 1)
          {
            System.out.println("Args: " + args);
            if (args != null)
              for (int i = 0; i < args.size(); i++)
                Log.debug(i + " -> " + args.get(i));
          }
          else
          {
            String command = (String) args.get(0);
            if ("go".equalsIgnoreCase(command))
            {
              Log.debug("FIRING GO");
              try
              {
                Integer cue = (Integer) args.get(1);
                motion.goTo(cue);
              }
              catch (Throwable t)
              {
                t.printStackTrace();
                Log.severe("Unable to fire cue!\n" + t.getMessage());
              }
            }
            else
              if ("advance".equalsIgnoreCase(command))
              {
                if (args.size() < 2)
                {
                  motion.advanceScene();
                }
                else
                {
                  try
                  {
                    String name = (String) args.get(1);
                    motion.advanceSceneTo(name);
                  }
                  catch (Throwable t)
                  {
                    Log.severe("Unable to process scene name: " + args.get(1));
                  }
                }
                return;
              }
              else
                if ("play".equalsIgnoreCase(command))
                {
                  System.out.println("Play...");
                  motion.runPlayback();
                }
                else
                  if ("clear".equalsIgnoreCase(command))
                  {
                    motion.advanceSceneTo("clear");
                    motion.go();
                  }
                  else //FIXME LIVE DRAW IS NOT WORKING
                    if ("live".equalsIgnoreCase(command))
                    {
                      if (args.size() < 2)
                      {
                        System.out.println("Invalid command: " + args.stream().map(x -> x.toString()).collect(Collectors.joining(", ")));
                        return;
                      }

                      try
                      {
                        String type = (String) args.get(1);
                        if (type.equalsIgnoreCase("mouse") && args.size() > 4)
                        {
                          float x = (float) args.get(2);
                          float y = (float) args.get(3);
                          int down = (int) args.get(4);

                          RemoteDrawMouseEvent e = new RemoteDrawMouseEvent(x, y, down != 0);
//                          System.out.println("Adding event: " + e);
                          //remoteDrawQueue.add(e);
                        }
                        else if(type.equalsIgnoreCase("key") && args.size() > 3)
                        {
                          char key = (char) args.get(2);
                          int code = (int) args.get(3);
                          
                          //virtualKeyPressed(key, code);
                        }
                      }
                      catch (Throwable t)
                      {
                        System.err.println("INVALID COMMAND");
                        t.printStackTrace(System.err);
                      }
                    }
                    else
                    {
                      System.out.println("UNKNOWN COMMAND " + command);
                      return;
                    }
          }
        }
      };
      oscReceiver.addListener("/motion", listener);
      oscReceiver.startListening();
      System.out.printf("STARTING OSC ON PORT %d%n", OSC_PORT);
      System.out.println(oscReceiver.toString());
    }
    catch (SocketException e)
    {
      e.printStackTrace();
    }
  }
}
