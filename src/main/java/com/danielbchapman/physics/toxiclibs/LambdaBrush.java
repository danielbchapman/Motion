package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.danielbchapman.code.jdk.JavaSourceString;
import com.danielbchapman.code.jdk.ReloadingClassLoader;
import com.danielbchapman.utility.FileUtil;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class LambdaBrush extends MotionInteractiveBehavior
{
  public static int CLASS_COUNT = 0;

  //@formatter:off
  public final static String DEFAULT_LAMBDA =
          "(p)->{ " + "\n" +
          "      Vec3D distanceV = p.sub(vars.position); " + "\n" +
          "      float distance = distanceV.magnitude(); " + "\n" +
          "      float modifier = vars.magnitude / distance;// / mag; // 1 / n^2 " + "\n" +
          "       " + "\n" +
          "      if(modifier > vars.maxForce) " + "\n" +
          "        modifier =  vars.maxForce; " + "\n" +
          "       " + "\n" +
          "      if(modifier <  vars.minForce) " + "\n" +
          "        return;//skip " + "\n" +
          "      //noise " + "\n" +
          " " + "\n" +
          "      //Away and in the direction  " + "\n" +
          "      Vec3D vForce =  vars.force.normalizeTo(modifier); " + "\n" +
          "      distanceV = distanceV.normalizeTo(modifier);   " + "\n" +
          "      vForce = vForce.add(distanceV); " + "\n" +
          " " + "\n" +
          "      p.addAngularForce(vForce.scale(5f)); " + "\n" +
          "      p.addForce(vForce); " + "\n" +
          "  }; "
      ;
  public static final String CLASS_FILE = 
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
  //@formatter:on  
  public final static String CLASS_TOKEN = "CLASS_NAME";
  public final static String LAMBDA_TOKEN = "LAMBDA";
  public final static String LINE_BREAK = "--LINE--";
  public String name;
  public String className;
  public String lambda;
  private Consumer<Point> function;
  private LambdaBrushClass functionHost;

  public String compile() throws Exception
  {
    String packageName = "com.danielbchapman.physics.toxiclibs";
    className = "LambdaBrushDynamic" + name;
    JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    if (javac == null)
      throw new RuntimeException("Lambda Brushes are not available without the JDK");

    // Do the compilation
    String code = CLASS_FILE.replaceAll(CLASS_TOKEN, className).replaceAll(LAMBDA_TOKEN, lambda);

    JavaSourceString source = new JavaSourceString(className, code);

    className = "com.danielbchapman.physics.toxiclibs." + className;

    Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(source);
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("-d");
    arguments.add(new File("bin").getAbsolutePath());
    arguments.add("-classpath");

    StringBuilder b = new StringBuilder();
    URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();

    for (URL url : loader.getURLs())
      b.append(url.getFile()).append(File.pathSeparator);

    arguments.add(b.toString());

    StringWriter out = new StringWriter();
    boolean success = javac.getTask(out, null, null, arguments, null, fileObjects).call();

    if (success)
    {
      System.out.println("Compilation completed...");
      ReloadingClassLoader rcl = new ReloadingClassLoader(Thread.currentThread().getContextClassLoader(), new File("bin"));
      @SuppressWarnings("unchecked")
      Class<? extends LambdaBrushClass> clazz = (Class<? extends LambdaBrushClass>) rcl.loadClass(className);
      functionHost = clazz.newInstance();
      function = functionHost.compile();// Creates and returns a lambda
      return code;
    }
    else
    {
      throw new Exception("Failed to complile:" + out);
    }
  }

  @Override
  public void apply(VerletParticle3D p)
  {
    if (function != null && (p instanceof Point))
      function.accept((Point) p);
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

  public boolean isReady()
  {
    return function != null;
  }

  public static String toLines(LambdaBrush brush)
  {
    StringBuilder b = new StringBuilder();
    b.append(brush.name);
    b.append("\n");
    b.append(brush.className);
    b.append("\n");
    
    if(brush.lambda == null)
      b.append(DEFAULT_LAMBDA);
    else
      b.append(brush.lambda);
    return b.toString();
  }

  public static LambdaBrush fromFile(File f)
  {
    ArrayList<String> lines = FileUtil.readLines(f);
    ArrayList<String> code = new ArrayList<>();
    String name = lines.get(0);
    String className = lines.get(1);
    for(int i = 2; i < lines.size(); i++)
      code.add(lines.get(i));
    
    return fromLines(name, className, code);
  }
  
  public static LambdaBrush fromLines(String one, String two, ArrayList<String> code)
  {
    LambdaBrush brush = new LambdaBrush();
    brush.name = one.trim();
    brush.className = two.trim();
    StringBuilder b = new StringBuilder();
    for(String s : code)
    {
      b.append(s);
      b.append("\n");
    }
      
    brush.lambda = b.toString().trim();
    return brush;
  }

}
