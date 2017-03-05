package com.danielbchapman.ui.fx;

import javafx.application.Application;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.danielbchapman.motion.fx.BoundFloat;
import com.danielbchapman.motion.fx.BoundSlider;
import com.danielbchapman.motion.fx.BoundVec4D;

import toxi.geom.Vec4D;

public class TestJavaFx extends Application
{
  @Override
  public void start(Stage arg0) throws Exception
  {
  
  }
  
  @BeforeClass
  public static void initJFX() {
      Thread t = new Thread("JavaFX Init Thread") {
          @Override
          public void run() {
              Application.launch(TestJavaFx.class, new String[0]);
          }
      };
      t.setDaemon(true);
      t.start();
  }
  
  @Test
  public void TestFloat()
  {
    final Pointer<Float> cache = new Pointer<>(10f);
    BoundFloat binding = new BoundFloat(10f, (f) -> { cache.value = f;});
    
    binding.setText("12");
    Assert.assertEquals(12f, cache.value, 0.01f);
    
    binding.setText("14.25");
    Assert.assertEquals(14.25f, cache.value, 0.01f);
    
    //error state
    binding.setText("abcd");
    Assert.assertEquals(14.25f, cache.value, 0.01f);
  }

  @Test
  public void TestSlider()
  {
    final Pointer<Float> cache = new Pointer<>(10f);
    BoundSlider binding = new BoundSlider(10f, 0f, 100f, 
        (f) -> { cache.value = f;});
    
    binding.set(12f);
    Assert.assertEquals(12f, cache.value, 0.01f);
    
    binding.set(14.25f);
    Assert.assertEquals(14.25f, cache.value, 0.01f);
    
    //error state
    binding.set(101f);
    Assert.assertEquals(100f, cache.value, 0.01f);
    
    //error state
    binding.set(-1f);
    Assert.assertEquals(0f, cache.value, 0.01f);
  }
  
  @Test
  public void TestVec4()
  {
    final Pointer<Vec4D> cache = new Pointer<>(new Vec4D(1, 2, 3, 4));
    BoundVec4D binding = new BoundVec4D(new Vec4D(1,1,1,1), 
        v -> 
        {
          cache.value = v;
        });

    binding.set(new Vec4D(5, 6, 7, 8));
    Assert.assertEquals(new Vec4D(5, 6, 7, 8), cache.value);
    
    binding.set(new Vec4D(-1, -2, -3, -4));
    Assert.assertEquals(new Vec4D(-1, -2, -3, -4), cache.value);
    
    //error state
    binding.set(null);
    Assert.assertEquals(new Vec4D(-1, -2, -3, -4), cache.value);
  }
}
