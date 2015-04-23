package com.danielbchapman.physics.toxiclibs;

import java.util.Map;
import java.util.function.Consumer;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.danielbchapman.code.jdk.JavaSourceString;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class LambdaBrush extends MotionInteractiveBehavior
{
  public String name;
  public String className;
  public String lambda;
  private Consumer<Point> function;
  private LambdaBrushClass functionHost;
  private String classFile = 
      "package com.danielbchapman.physics.toxiclibs; " + "\n" +
      " " + "\n" +
      "import java.util.function.*; " + "\n" +
      "import com.danielbchapman.physics.toxiclibs.*; " + "\n" +
      "import java.util.*; " + "\n" +
      " " + "\n" +
      "public class CLASS_NAME extends LambdaBrushClass " + "\n" +
      "{ " + "\n" +
      "  @Override " + "\n" +
      "  public Consumer<Point> compile() " + "\n" +
      "  { " + "\n" +
      "    return (Consumer<Point>)LAMBDA " + "\n" +
      "  } " + "\n" +
      "} ";
  public String compile()
  {
    String packageName = "com.danielbchapman.physics.toxiclibs";
    className = "LambdaBrush" + name;
    JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    if(javac == null)
      throw new RuntimeException("Lambda Brushes are not available without the JDK");
    
    //Do the compilation
    String code = 
        classFile
          .replaceAll("CLASS_NAME", className)
          .replaceAll("LAMBDA", lambda);
    
    JavaSourceString source = new JavaSourceString("lambda"+System.currentTimeMillis(), code);
    
    className = "com.danielbchapman.physics.toxiclibs." + className;
    
    return code;
    
  }
  @Override
  public void apply(VerletParticle3D p)
  {
    if(function != null && (p instanceof Point))
      function.accept((Point)p);
  }

  @Override
  public void configure(float timeStep)
  {
    vars.timeStep = timeStep;
  }

  @Override
  public Map<String, String> getFieldNames()
  {
    return null;
  }

  @Override
  public MotionInteractiveBehavior copy()
  {
    LambdaBrush copy = new LambdaBrush();
    copy.lambda = lambda;
    copy.function = function;
    copy.vars = vars.copy();
    return copy;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setPosition(Vec3D location)
  {
    vars.position = location;
  }

//  public static String toLines(LambdaBrush brush)
//  {
//    StringBuilder b = new StringBuilder();
//    b.append(brush.name);
//    b.append(",");
//  }
//  
//  public static LambdaBrush fromLines(String one, String two)
//  {
//    LambdaBrush brush = new LambdaBrush();
//    return brush;
//  }

}
