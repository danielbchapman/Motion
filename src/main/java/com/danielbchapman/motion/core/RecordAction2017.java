package com.danielbchapman.motion.core;

import java.util.function.Consumer;

import lombok.Data;

import com.danielbchapman.physics.toxiclibs.Transform;
import com.danielbchapman.text.Text;

@Data
public class RecordAction2017
{
  int stamp;
  String label;
  int x;
  int y;
  int px;
  int py;
  int z;
  int pz;
  boolean leftClick;
  boolean rightClick;
  boolean centerClick;

  public RecordAction2017(String label, int stamp, int x, int y, int z, int px, int py, int pz, boolean left, boolean right, boolean center)
  {
    this.label = label;
    this.stamp = stamp;
    this.x = x;
    this.y = y;
    this.z = z;
    this.px = px;
    this.py = py;
    this.pz = pz;
    this.leftClick = left;
    this.rightClick = right;
    this.centerClick = center;
  }
  
  private RecordAction2017()
  {
  }
  
  @Override
  public String toString()
  {
    StringBuilder b = new StringBuilder();
    Consumer<Object> append = (x)->
    {
      b.append(",");
      b.append(x);
    };
    
    b.append(label.replaceAll(",", ""));
    append.accept(stamp);
    append.accept(x);
    append.accept(y);
    append.accept(z);    
    append.accept(px);
    append.accept(py);
    append.accept(pz);
    append.accept(leftClick);
    append.accept(rightClick);
    append.accept(centerClick);
    
    return b.toString();
  }
  
  public static String toFloatFormat(RecordAction2017 a, int w, int h)
  {
    StringBuilder b = new StringBuilder();
    Consumer<Object> append = (x)->
    {
      b.append(",");
      b.append(x);
    };
    
    b.append(a.label.replaceAll(",", ""));
    append.accept(a.stamp);
    append.accept(Transform.size(a.x, w));
    append.accept(Transform.size(a.y, h));
    append.accept(Transform.size(a.z, w));
    append.accept(Transform.size(a.px, w));
    append.accept(Transform.size(a.py, h));
    append.accept(Transform.size(a.pz, w));
    append.accept(a.leftClick);
    append.accept(a.rightClick);
    append.accept(a.centerClick);
    
    return b.toString();
  }

  public static RecordAction2017 fromFloatFormat(String s, int w, int h)
  {
    if(Text.isEmptyOrNull(s))
      return null;
    
    RecordAction2017 result = new RecordAction2017();
    String[] parts = s.split(",");
    int i = 0;
    
    try
    {
      result.label = parts[i++];
      result.stamp = Integer.parseInt(parts[i++]);
      result.x = Transform.size(Float.parseFloat(parts[i++]), w);
      result.y = Transform.size(Float.parseFloat(parts[i++]), h);
      result.z = Transform.size(Float.parseFloat(parts[i++]), w);
      result.px = Transform.size(Float.parseFloat(parts[i++]), w);
      result.py = Transform.size(Float.parseFloat(parts[i++]), h);
      result.pz = Transform.size(Float.parseFloat(parts[i++]), w)
      result.leftClick = Boolean.parseBoolean(parts[i++]);
      result.rightClick = Boolean.parseBoolean(parts[i++]);
      result.centerClick = Boolean.parseBoolean(parts[i++]);
    }
    catch(Throwable t)
    {
      t.printStackTrace();
      return null;
    }

    return result;
  }
}
