package com.danielbchapman.motion.jfx.editors;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import toxi.geom.Vec4D;

import com.danielbchapman.motion.fx.BoundFloat;
import com.danielbchapman.motion.fx.BoundSlider;
import com.danielbchapman.motion.fx.BoundVec4D;

public class ComponentTest extends Application
{
  public static void main(String ... args)
  {
    launch(ComponentTest.class);
  }
  @Override
  public void start(Stage stage) throws Exception
  {
    VBox test = new VBox();
    
    test.getChildren()
      .addAll(
          new NamedEditor<BoundFloat>("Float 1", new BoundFloat(10f, System.out::println)),
          new NamedEditor<BoundFloat>("Float 2", new BoundFloat(11f, System.out::println)),
          new NamedEditor<BoundFloat>("Float 3", new BoundFloat(12f, System.out::println)),
          new NamedEditor<BoundSlider>("Slider 1", new BoundSlider(10f, -10f, 100f, System.out::println)),
          new NamedEditor<BoundSlider>("Slider 2", new BoundSlider(0.5f, 0f, 1f, System.out::println)),
          new NamedEditor<BoundVec4D>("Vector 1", new BoundVec4D(new Vec4D(1, 2, 3, 4), System.out::println))
        ); 
    
    stage.setMinWidth(800);
    stage.setMinHeight(600);
    Scene scene = new Scene(test);
    scene.getStylesheets().add(getClass().getClassLoader().getResource("com/danielbchapman/ui/fx/controls.css").toString());
    stage.setScene(scene);
    stage.setTitle("FX Layout Tests");
    stage.show();
  }

}
