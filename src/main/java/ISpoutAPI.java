

import processing.core.PImage;

public interface ISpoutAPI
{

  // For texture sharing, the name provided
  // is registered in the list of senders
  // Texture share initialization only succeeds if
  // the graphic hardware is compatible, otherwise
  // it defaults to memoryshare mode
  public abstract void initSender(String name, int Width, int Height);

  // Write the sketch drawing surface texture to
  // an opengl/directx shared texture
  public abstract void sendTexture();

  public abstract void closeSender();

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
  public abstract void initReceiver(String name, PImage img); // end Receiver initialization

  public abstract PImage receiveTexture(PImage img);

  public abstract void closeReceiver();

}