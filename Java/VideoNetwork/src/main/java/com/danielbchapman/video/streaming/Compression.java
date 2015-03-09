package com.danielbchapman.video.streaming;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import processing.core.PImage;

public class Compression
{
  public static byte[] compress(PImage image)
  {
    BufferedImage b = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB);
    
    image.loadPixels();
    b.setRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);
    
    try(
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedOutputStream buf = new BufferedOutputStream(out);

    ){
      ImageIO.write(b, "jpg", buf);
      buf.flush();
      byte[] result = out.toByteArray(); 
      return result;
    } 
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }
  
  public static PImage decompress(byte[] bytes, PImage target)
  {
    try( ByteArrayInputStream in = new ByteArrayInputStream(bytes); )
    {
      BufferedImage img = ImageIO.read(in);
      if(target == null)
        target = new PImage(img.getWidth(), img.getHeight());
      
      target.loadPixels();
      img.getRGB(0, 0, target.width, target.height, target.pixels, 0, target.width);
      target.updatePixels();
      return target;
    } catch (IOException e){
      e.printStackTrace();
      return null;
    }
  }
}
