package com.danielbchapman.physics.vertlet;

import org.junit.Assert;
import org.junit.Test;

public class Matrix
{
  public static float[] rotate(float[] vec)
  {
    /*
     * cos(a) + u^2(1 - cos(a))
     * 
     * 
     * 
     */
    throw new RuntimeException("Not implemented yet...");
  }
  
  public static float[] scale(float[] vec)
  {
    int size = vec.length + 1;
    float[] matrix = new float[size * size];
    
    for(int i = 0; i < size; i++)
      for(int j = 0; j < size; j++)
      {
        if(i == j && i < vec.length)
          matrix[i * size + j] = vec[i];
        else
          matrix[i * size + j] = 0f;
      }
    
    matrix[size*size -1] = 1f;
    return matrix;
  }
  
  public static float[] translate(float[] vec)
  {
   return null; 
  }
  
  public static float[] identity(int dimensions)
  {
    if(dimensions == 2)
    {
      return new float[]
          {
            1f, 0f, 
            0f, 1f
          };
    }
    
    if(dimensions == 3)
    {
      return new float[]
          {
            1f, 0f, 0f, 
            0f, 1f, 0f,
            0f, 0f, 1f
          };
    }
    
    if(dimensions == 4)
    {
      return new float[]
          {
            1f, 0f, 0f, 0f, 
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
          };
    }
    
    //Use algorithm for it...
    throw new RuntimeException("Unsupported operation..");
  }
  
  public static float[] cross()
  {
    throw new RuntimeException("Not implemented");
  }
  
  public static float[] mult(float[] matrix, float[] vector)
  {
    //should use mod but close enough...
    int v = vector.length;
    int m = matrix.length;
    
    float[] result = new float[vector.length];
    for(int i = 0; i < vector.length; i++)
    {
      result[i] = 0f;
    }
    
    if((int)(matrix.length / vector.length) == vector.length)
    {
      
      for(int i = 0; i < matrix.length; i++)
      {
        int index = i / v;
        result[index] += vector[index] * matrix[i]; 
      }
      
      return result;
    }
    
    throw new RuntimeException("Unsupported operation..");
  }
  
  @Test
  public void TestMatrixContructors()
  {
    //Pretty hard to fail here but whatever...
    float[] identity2 = identity(2);
    float[] identity3 = identity(3);
    float[] identity4 = identity(4);
    
    Assert.assertTrue(identity2.length == 4);
    Assert.assertTrue(identity3.length == 9);
    Assert.assertTrue(identity4.length == 16);
  }
  
  @Test
  public void TestMatrixMult()
  {
    float[] identity2 = identity(2);
    float[] identity3 = identity(3);
    float[] identity4 = identity(4);
    
    float[] vec2 = new float[]{2f, 4f};
    float[] vec3 = new float[]{2f, 4f, 8f};
    float[] vec4 = new float[]{2f, 4f, 8f, 16f};
    
    float[] vec2Mult = Matrix.mult(identity2, vec2);
    float[] vec3Mult = Matrix.mult(identity3, vec3);
    float[] vec4Mult = Matrix.mult(identity4, vec4);
    
    Assert.assertTrue(compMatrix(vec2Mult, vec2));
    Assert.assertTrue(compMatrix(vec3Mult, vec3));
    Assert.assertTrue(compMatrix(vec4Mult, vec4));
  }
  
  private static boolean compMatrix(float[] a, float[] b)
  {
    if(a == null && b == null)
      return true;
    
    if(a == null && b != null)
      return false;
    
    if(a != null && b == null)
      return false;
    
    if(a.length != b.length)
      return false;
    
    for(int i = 0; i < a.length; i++)
      if(a[i] != b[i])
        return false;
    
    return true;
  }
  
  @Test
  public void TestScale()
  {
    float[] a = scale(new float[]{2f, 2f});
    float[] b = scale(new float[]{2f, 2f, 2f});
    float[] c = scale(new float[]{2f, 2f, 2f, 2f});
    
    float[] a1 = 
      {
        2f, 0f, 0f,
        0f, 2f, 0f,
        0f, 0f, 1f
      };
    
    float[] b1 = 
      {
        2f, 0f, 0f, 0f,
        0f, 2f, 0f, 0f,
        0f, 0f, 2f, 0f,
        0f, 0f, 0f, 1f
      };
    
    float[] c1 = 
      {
        2f, 0f, 0f, 0f, 0f,
        0f, 2f, 0f, 0f, 0f,
        0f, 0f, 2f, 0f, 0f,
        0f, 0f, 0f, 2f, 0f,
        0f, 0f, 0f, 0f, 1f
      };
    Assert.assertTrue(compMatrix(a, a1));
    Assert.assertTrue(compMatrix(b, b1));
    Assert.assertTrue(compMatrix(c, c1));
  }
}