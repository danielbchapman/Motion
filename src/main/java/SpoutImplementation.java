

import processing.core.PImage;
import processing.opengl.PGraphics3D;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.Texture;

import com.danielbchapman.logging.Log;

/**
 * Conversion of the SPOUT code | Daniel B. Chapman 
 *
 *
 *                  Spout.pde
 *
 *    Adds support to the basic functions of the 
 *    "JSpout" class and JNI dll, defined in JSpout.java
 *
 *    07.05.14 - updated for screen resize with sender change
 *             - updated Jspout ReadTexture as well
 *    06.08-14 - updated for Spout SDK
 *    05.09.14 - update with revised SDK
 *    12.10.14 - recompiled for release
 *    04.02.15 - receiver screen resize in sketch instead of in this class
 */
public class SpoutImplementation implements ISpoutAPI
{
  int[] dim = new int[2];
  int memorymode; // memorymode flag
  PGraphicsOpenGL pgl;

  public SpoutImplementation(PGraphicsOpenGL g)
  {
    pgl = (PGraphicsOpenGL) g;
    dim[0] = 0;
    dim[1] = 0;
    memorymode = -1; // default
  }

  //
  // Initialize a sender
  //

  // For texture sharing, the name provided
  // is registered in the list of senders
  // Texture share initialization only succeeds if
  // the graphic hardware is compatible, otherwise
  // it defaults to memoryshare mode
  /* (non-Javadoc)
   * @see jspout.ISpoutAPI#initSender(java.lang.String, int, int)
   */
  @Override
  public void initSender(String name, int Width, int Height)
  {
    // Try Texture share (mode 0)
    // If texture init fails, the mode returned is memoryshare
    // unless that fails too, then it returns -1
    memorymode = JSpout.InitSender(name, Width, Height, 0);
    if (memorymode == 0)
      Log.debug("Sender initialized texture sharing\n");
    else
      if (memorymode == 1)
        Log.debug("Sender texture sharing not supported - using memory sharing\n");
      else
        if (memorymode == -1)
          Log.debug("Sender sharing initialization failed\n");

  }

  // Write the sketch drawing surface texture to
  // an opengl/directx shared texture
  /* (non-Javadoc)
   * @see jspout.ISpoutAPI#sendTexture()
   */
  @Override
  public void sendTexture()
  {
    pgl.beginPGL();
    // Load the current contents of the renderer's
    // drawing surface into its texture.
    pgl.loadTexture();
    // getTexture returns the texture associated with the
    // renderer's. drawing surface, making sure is updated
    // to reflect the current contents off the screen
    // (or offscreen drawing surface).
    Texture tex = pgl.getTexture();
    // Processing Y axis is inverted with respect to OpenGL
    // so we need to invert the texture
    JSpout.WriteTexture(tex.glWidth, tex.glHeight, tex.glName, tex.glTarget, true); // invert
    pgl.endPGL();
  }
  
  @Override
  public void sendTexture2(PGraphics3D graphics)
  {
    graphics.beginPGL();
    // Load the current contents of the renderer's
    // drawing surface into its texture.
    graphics.loadTexture();
    // getTexture returns the texture associated with the
    // renderer's. drawing surface, making sure is updated
    // to reflect the current contents off the screen
    // (or offscreen drawing surface).
    Texture tex = graphics.getTexture();
    // Processing Y axis is inverted with respect to OpenGL
    // so we need to invert the texture
    JSpout.WriteTexture(tex.glWidth, tex.glHeight, tex.glName, tex.glTarget, true); // invert
    graphics.endPGL();
  }

  /* (non-Javadoc)
   * @see jspout.ISpoutAPI#closeSender()
   */
  @Override
  public void closeSender()
  {
    if (JSpout.ReleaseSender())
      Log.debug("Sender closed" + "\n");
    else
      Log.debug("No sender to close" + "\n");
  }

  //
  // Initialize a Receiver
  //
  // For texture sharing, the name provided
  // is searched in the list of senders and
  // used if it is there. If not, the receiver will
  // connect to the active sender selected by the user
  // or, if no sender has been selected, this will be
  // the first in the list if any are running.
  //
  /* (non-Javadoc)
   * @see jspout.ISpoutAPI#initReceiver(java.lang.String, processing.core.PImage)
   */
  @Override
  public void initReceiver(String name, PImage img)
  {

    // Image size values passed in are modified and passed back
    // as the size of the sender that the receiver connects to.
    // Then the screen has to be reset. The same happens when
    // receiving a texture if the sender or image size changes.
    dim[0] = img.width;
    dim[1] = img.height;

    // Already initialized ?
    if (memorymode > 0)
    {
      Log.debug("Receiver already initialized - teture sharing mode\n");
      return;
    }

    // Try Texture share (mode 0)
    // Returns 0 or 1 depending on capabilities or -1 on failure
    memorymode = JSpout.InitReceiver(name, dim, 0);

    // FAILURE
    if (memorymode == -1)
    {
      Log.debug("No sender running - start one and try again.\n");
      return;
    }
    else
      if (memorymode == 0)
        Log.debug("Receiver initialized texture sharing\n");
      else
        if (memorymode == 1)
          Log.debug("Receiver texture sharing not supported - using memory sharing\n");

    // Texture sharing succeeded and there was a sender running
    String newname = JSpout.GetSenderName();
    Log.debug("Receiver found sender : " + newname + " " + dim[0] + "x" + dim[1] + "\n");
    // dim will be returned with ths size of the sender it connected to

    // Reset the image size to the connected sender size if necessary
    if (dim[0] != img.width || dim[1] != img.height && dim[0] > 0 && dim[1] > 0)
    {
      // reset the image size to that of the sender
      img.resize(dim[0], dim[1]);
      img.updatePixels();
    }
    // All done
  } // end Receiver initialization

  /* (non-Javadoc)
   * @see jspout.ISpoutAPI#receiveTexture(processing.core.PImage)
   */
  @Override
  public PImage receiveTexture(PImage img)
  {
    // Quit if no server running
    if (memorymode < 0)
      return img;

    // Dimensions are sent as well as returned
    dim[0] = img.width;
    dim[1] = img.height;

    img.loadPixels();
    if (JSpout.ReadTexture(dim, img.pixels))
    {
      // If the sender read was OK, test the image
      // size returned and resize if it is different
      // Otherwise update the image for return
      if (dim[0] != img.width || dim[1] != img.height && dim[0] > 0 && dim[1] > 0)
      {
        img.resize(dim[0], dim[1]);
      }
      img.updatePixels();
    }
    return img;
  }

  /* (non-Javadoc)
   * @see jspout.ISpoutAPI#closeReceiver()
   */
  @Override
  public void closeReceiver()
  {
    if (JSpout.ReleaseReceiver())
      Log.debug("Receiver closed" + "\n");
    else
      Log.debug("No receiver to close" + "\n");
  }

}
