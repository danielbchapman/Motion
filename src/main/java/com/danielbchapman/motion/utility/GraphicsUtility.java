package com.danielbchapman.motion.utility;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import processing.core.PGraphics;

import com.danielbchapman.physics.toxiclibs.Point;

public class GraphicsUtility
{
  /**
   * Creates a grid of Point[rows(y)][columns(x)] based on the dimensions
   * provided. This will create one more column than expected as there is a "zero" for
   * the grid. Each row will be columns+1 in size and there is an additional row
   * @param width the width of the grid to create
   * @param height the height of the grid to create
   * @param vSpace the vertical spacing
   * @param hSpace the horizontal spacing
   * @param constructor the constructor to used to create the points giving a fine 
   * control over how and what is created. This takes x, y, z, w as the arguments
   * @return ArrayList in row order (row 1, then row 2 etc...)
   * 
   */
  public static <T extends Point> ArrayList<T> createMotionGrid(int columns, int rows, int vSpace, int hSpace, int depth, int weight, Function<float[], T> constructor)
  { 
    ArrayList<T> points = new ArrayList<T>();
    T x = constructor.apply(new float[]{0,0,0,1});
    
    //Construct the Point[];
    for(int r = 0; r < rows; r++)
    {
      for(int c = 0; c < columns; c++)
      {
        points.add(constructor.apply(
            new float[]
            {
              c * hSpace, 
              r * vSpace, 
              depth, 
              weight
            })); 
      }
    }
    
    return points;
  }
  
  public static void drawMotionGridAsLinesOffset(PGraphics p, Point[] grid, int rowLength)
  {
    int rows = grid.length / rowLength;
    
    //Draw rows
    for(int r = 0; r < rows; r++)
    {
      for(int c = 0; c < (rowLength - 1); c++) //we draw to the end, but don't draw the last point
      {
        Point a = grid[r * rowLength + c];
        Point b = grid[r * rowLength + c + 1];
        p.line(a.x, a.y, a.z, b.x, b.y, b.z);
      }
    }
    
    //Draw columns
    for(int c = 0; c < rowLength; c++)
    {
      for(int r = 0; r < (rows -1); r++)
      {
        Point a = grid[(r) * rowLength + c];
        Point b = grid[(r + 1) * rowLength + c];
        p.line(a.x, a.y, a.z, b.x, b.y, b.z);
      }
    }
  }
  
  public static void drawMotionGridAsLines(PGraphics p, Point[] grid, int rowLength)
  {
    //System.out.println("drawing grid: " + grid + " of length " + grid.length + " rows: " + rowLength);
    int rows = grid.length / rowLength;
    
    //Draw rows
    for(int r = 0; r < rows; r++)
    {
      for(int c = 0; c < rowLength -1; c++) //we draw to the end, but don't draw the last point
      {
        Point a = grid[r * rowLength + c];
        Point b = grid[r * rowLength + c + 1];
        p.line(a.x, a.y, a.z, b.x, b.y, b.z);
      }
    }
    
    //Draw columns
    for(int c = 0; c < rowLength; c++)
    {
      for(int r = 0; r < (rows -1); r++)
      {
        Point a = grid[(r) * rowLength + c];
        Point b = grid[(r + 1) * rowLength + c];
        p.line(a.x, a.y, a.z, b.x, b.y, b.z);
      }
    }
  }
  
  public static Point point(float[] vals)
  {
    if(vals == null || vals.length < 1)
      return new Point(0, 0, 0, 1);
    
    if(vals.length >= 4)
      return new Point(vals[0], vals[1], vals[2], vals[3]);
    
    if(vals.length == 3)
      return new Point(vals[0], vals[1], vals[2], 1);
    
    if(vals.length == 2)
      return new Point(vals[0], vals[1], 0, 1);
    
    if(vals.length == 1)
      return new Point(vals[0], 0, 0, 1);
    
    return new Point(0, 0, 0, 1);
  }
}
