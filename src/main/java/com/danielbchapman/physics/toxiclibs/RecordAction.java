package com.danielbchapman.physics.toxiclibs;

import java.util.function.Consumer;

import lombok.Data;

import com.danielbchapman.text.Text;

@Data
public class RecordAction
{
  int stamp;
  String label;
  int x;
  int y;
  boolean leftClick;
  boolean rightClick;
  boolean keyEvent;

  public RecordAction(String label, int stamp, int x, int y, boolean left, boolean right, boolean keyEvent)
  {
    this.label = label;
    this.stamp = stamp;
    this.x = x;
    this.y = y;
    this.leftClick = left;
    this.rightClick = right;
    this.keyEvent = keyEvent;
  }
  
  private RecordAction()
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
    append.accept(leftClick);
    append.accept(rightClick);
    append.accept(keyEvent);
    
    return b.toString();
  }
  
  public static String toFloatFormat(RecordAction a, int w, int h)
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
    append.accept(a.leftClick);
    append.accept(a.rightClick);
    append.accept(a.keyEvent);
    
    return b.toString();
  }

  public static RecordAction fromFloatFormat(String s, int w, int h)
  {
    if(Text.isEmptyOrNull(s))
      return null;
    
    RecordAction result = new RecordAction();
    String[] parts = s.split(",");
    int i = 0;
    
    try
    {
      result.label = parts[i++];
      result.stamp = Integer.parseInt(parts[i++]);
      result.x = Transform.size(Float.parseFloat(parts[i++]), w);
      result.y = Transform.size(Float.parseFloat(parts[i++]), h);
      result.leftClick = Boolean.parseBoolean(parts[i++]);
      result.rightClick = Boolean.parseBoolean(parts[i++]);
      result.keyEvent = Boolean.parseBoolean(parts[i++]);
    }
    catch(Throwable t)
    {
      t.printStackTrace();
      return null;
    }

    return result;
  }
}
