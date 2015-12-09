package com.danielbchapman.physics.ui;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import toxi.geom.Vec3D;

import com.danielbchapman.utility.UiUtility;


/**
 * An editor for a Vec3D. The Type T is used in the callbacks getter/setter
 * to update the model. In most cases T is the model containing the vector.
 */
public class Vec3DEditor<T> extends ComponentModel
{
  private Vec3D vec = new Vec3D();
  
  T object;
  BiConsumer<Vec3D, T> onUpdate;
  Function<T, Vec3D> onSynchronize;
  
  JLabel title;
  JTextField textX;
  JTextField textY;
  JTextField textZ;
  
  private boolean editing = false;
  public Vec3DEditor(
      String title,
      T object,
      BiConsumer<Vec3D, T> onUpdate,
      Function<T, Vec3D> onSynchronize
  )
  {
    this.object = object;
    this.onUpdate = onUpdate;
    this.onSynchronize = onSynchronize;
    this.title = new JLabel("<html><b>" + title + "</b></html>");
    textX = new JTextField();
    textY = new JTextField();
    textZ = new JTextField();
    
    sync();
    
    BiConsumer<JTextField, Consumer<Float>> addListener = 
        (j, x)->
    {
      UiUtility.addChangeListener(j, (e)->
      {
        if(editing)
          return;
        
        try
        {
          float f = Float.parseFloat(j.getText());
          if(!Float.isNaN(f))
          {
            x.accept(f);
            onUpdate.accept(vec, object);//Update the model
          }
          else
            sync();
        }
        catch(Throwable t)
        {
          sync();
        }  

        editing = false;
      });
      j.addPropertyChangeListener("text", 
      (e) ->
      {
        if(editing)
          return; //don't fire this
        
        editing = true;
        String s = (String) e.getNewValue();
        String o = (String) e.getOldValue();

        if(s != null){
          if(!s.equals(o))
          {
            try
            {
              float f = Float.parseFloat(s);
              if(!Float.isNaN(f))
              {
                x.accept(f);
                onUpdate.accept(vec, object);//Update the model
              }
              else
                j.setText((String) e.getOldValue());  
            }
            catch(Throwable t)
            {
              j.setText((String) e.getOldValue());
            }  
          }
        }
        editing = false;

      });
    };
    addListener.accept(
        textX, 
        (f)->{vec.x = f;}
        );
    addListener.accept(
        textY, 
        (f)->{vec.y = f;}
        );
    
    addListener.accept(
        textZ, 
        (f)->{vec.z = f;}
        );
  }
  
  public void sync()
  {
    vec = onSynchronize.apply(object);
    textX.setText(vec.x + "");
    textY.setText(vec.y + "");
    textZ.setText(vec.z + "");
  }
  
  public Vec3D getVector()
  {
    return vec;
  }

  @Override
  int implementByGridBag(Container comp, int row)
  {
    comp.add(title, UiUtility.getFillHorizontal(0, row));
    comp.add(new JLabel("X"), UiUtility.getFillHorizontal(1, row));
    comp.add(new JLabel("Y"), UiUtility.getFillHorizontal(2, row));
    comp.add(new JLabel("Z"), UiUtility.getFillHorizontal(3, row));
    
    comp.add(textX, UiUtility.getFillHorizontal(1, row+1));
    comp.add(textY, UiUtility.getFillHorizontal(2, row+1));
    comp.add(textZ, UiUtility.getFillHorizontal(3, row+1));
    
    return row + 2;
  }
  
  public static void main(String ... args)
  {
    JFrame f = new JFrame();
    f.setSize(200, 200);
    f.setLayout(new GridBagLayout());
    Vec3D test = new Vec3D(1,2,3);
    Vec3DEditor<Vec3D> ed = new Vec3DEditor<>(
        "Test Vec", 
        test, 
        (v,o)->{o.x = v.x; o.y = v.y; o.z = v.z; System.out.println(o);},
        (o)->{ return test;});
    
    ed.addByGridBag(f, 0);
    f.setVisible(true);
  }
}
