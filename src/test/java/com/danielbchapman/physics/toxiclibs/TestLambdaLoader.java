package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.Utility;

public class TestLambdaLoader
{
  @Test
  public void testIO() throws Exception
  {
    String fileName = "test-data-delete.tmp";
    File tmp = new File(fileName);
    try
    {
    //FIXME this needs a custom class loader to work
      LambdaBrush brush = new LambdaBrush();
      brush.lambda = "(x)->{System.out.println(\"TEST\");};";
      brush.name = "TestBrushBeta";
      System.out.println(brush.compile());
      Point x = new Point(1,2,3,1);
      brush.apply(x);
      assertTrue("Lambda exists:", brush.isReady());
      
      LambdaBrush a = new LambdaBrush();
      a.name = "TestBrushBeta";
      a.lambda = "(x)->{System.out.println(\"TEST\");};";
      System.out.println(a.compile());
      
      String data = LambdaBrush.toLines(a);
      
      FileUtil.writeFile(fileName, data.getBytes());
      
      LambdaBrush b = LambdaBrush.fromFile(new File(fileName));
      b.compile();
      
      if(tmp.exists())
        tmp.delete();
      
      assertTrue("Name Identical?", Utility.compareToNullSafe(a.name, b.name) == 0);
      assertTrue("Class Name Identical?", Utility.compareToNullSafe(a.className, b.className) == 0);
      assertTrue("Lambda Code Identical?", Utility.compareToNullSafe(a.lambda, b.lambda) == 0);
      
    }
    catch (Throwable t)
    {
      //cleanup
      if(tmp.exists())
        tmp.delete();
      throw t;
    }
    

  }
}
