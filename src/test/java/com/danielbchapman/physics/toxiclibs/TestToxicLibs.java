package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Test;

import com.danielbchapman.artwork.Fadeable;
import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.Utility;

public class TestToxicLibs
{
  @Test
  public void testFadable()
  {
    long start = 1;
    long time = 5000;
    long delay = 2000;
    int color = 0xFFAA9977;
    int low = 0x11;
    int high = 0x55;
    int mid = 0x33;
    Fadeable fade = new Fadeable(low, high, time, delay);
    fade.start = 0L;//test
    
    byte[] zero = Utility.encodeBytes(fade.color(0L, color));
    byte[] fifty = Utility.encodeBytes(fade.color(delay + 2500, color));
    byte[] zeroT = Utility.encodeBytes(fade.color(delay, color));
    byte[] full = Utility.encodeBytes(fade.color(delay + time, color));
    
    //checking opacity here...
    Function<byte[], String> printBytes = (parts) ->
    {
      int x = Utility.decodeBytes(parts);
      StringBuilder out = new StringBuilder();
      out.append( Integer.toHexString(x) + "->[");
      for(int i = 0; i < parts.length; i++)
        if(i != 0)
          out.append(", " + Integer.toHexString(0x000000FF & parts[i]));
        else
          out.append(Integer.toHexString(0x000000FF & parts[i]));
      out.append("]\n");
      return out.toString();
    };
    
    BiFunction<Byte, Byte, Boolean> compare = (a, b)->{
      String x = Integer.toHexString(0xFF & a);
      String y = Integer.toHexString(0xFF & b);
      return x.equals(y);
    };
    
    assertTrue(printBytes.apply(zero) + " Expecting High 11", compare.apply(zero[0], (byte)low));
    assertTrue(printBytes.apply(zeroT) + " Expecting High 11", compare.apply(zeroT[0], (byte)low));
    assertTrue(printBytes.apply(fifty) + " Expecting High 33", compare.apply(fifty[0], (byte)mid));
    assertTrue(printBytes.apply(full) + " Expecting High 55", compare.apply(full[0], (byte)high));
  }
}
