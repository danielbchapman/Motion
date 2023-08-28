package com.danielbchapman.physics.toxiclibs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.danielbchapman.physics.ui.BehaviorSlider;
import com.danielbchapman.physics.ui.PropertySlider;
import com.danielbchapman.physics.ui.Spacer;
import com.danielbchapman.physics.ui.TitleField;
import com.danielbchapman.physics.ui.Vec3DEditor;
import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.UiUtility;

public class EnvironmentTools extends JFrame
{
  private static final long serialVersionUID = 1L;
  
  
  /**
   * A set of identifiers used for file consistency (if desired)
   * this should probably be converted to JSON.
   */
  public class FileArgs
  {
    public final static String DRAG = "Drag";
    public final static String GRAVITY = "Gravity";
    public final static String WIND = "Wind";
    public final static String HOME = "Home";
    public final static String HOME_LINEAR = "Home Linear";
  }
  
  public final static String ENVIRONMENT_FOLDER = "environment";
  public MotionEngine engine;
  
  BehaviorSlider<HomeBehavior3D> homeForce;
  BehaviorSlider<HomeBehavior3D> homeMax;
  BehaviorSlider<HomeBehaviorLinear3D> homeLinear;
  BehaviorSlider<HomeBehaviorLinear3D> homeLinearMax;
  BehaviorSlider<AngularGravityBehavior3D> gravity;
  PropertySlider<MotionEngine> dragSlider;
  Vec3DEditor<AngularGravityBehavior3D> gravityVector;
  
  JButton read;
  JButton hide;
  JButton save;
  JButton open;


  //TEST METHODS
  public static void main(String ... args)
  {
    JFrame f = new EnvironmentTools(null);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  public EnvironmentTools(MotionEngine engine)
  {
    this.engine = engine;
    read = new JButton("Read Scene");
    hide = new JButton("Hide");
    save = new JButton("Save");
    open = new JButton("Open");
    read.addActionListener((x)->{ read();});
    hide.addActionListener((x)->{ setVisible(false);});
    save.addActionListener((x)->{ attemptSave();});
    open.addActionListener((x)->{ loadGlobal();});;
    
    //Home
    homeForce = new BehaviorSlider<HomeBehavior3D>(
        this, "Home Euclidian Force", 
        0, 
        10000,
        1000f,
        ActionsOLD.home, 
        (b, f)->{ b.vars.magnitude = f; },
        (b, s)->{ s.set(b.vars.magnitude);},
        true
        );
    
    //Home
    homeMax = new BehaviorSlider<>(
        this, "Home Euclidian Max", 
        0, 
        10000,
        1000f,
        ActionsOLD.home, 
        (b, f)->{
          b.vars.maxForce = f;
        },
        (b, s)->{s.set(b.vars.maxForce);}
        );
    
    homeLinear = new BehaviorSlider<>(
        this, "Home Linear Easing", 
        0, 
        10000,
        10000f,
        ActionsOLD.homeLinear, 
        (b, f)->{b.vars.userA = f;},
        (b, s)->{s.set(b.vars.userA);},
        true
        );
    
    homeLinearMax = new BehaviorSlider<>(
        this, "Home Linear Max", 
        0, 
        10000,
        1000f,
        ActionsOLD.homeLinear, 
        (b, f)->{b.vars.maxForce = f;},
        (b, s)->{s.set(b.vars.maxForce);}
        );
    
    gravity = new BehaviorSlider<AngularGravityBehavior3D>(
        this, "Angular Gravity", 
        0, 
        10000,
        10000f,
        ActionsOLD.gravity, 
        (b, f)->{
          b.updateMagnitude(f);
          System.out.println("Scaling Gravity to -> " + f + " Gravity: " + ActionsOLD.gravity);
        },
        (b, s)->
        {
          s.set(b.getForce().magnitude());
        },
        true
        );    
    
    gravityVector = new Vec3DEditor<>("Gravity Vector", ActionsOLD.gravity, 
        (v, o)->{
          o.setGravity(v);
          },
        (o)->{return o.getForce().copy();}
        );
    
    dragSlider = new PropertySlider<MotionEngine>(
        "Drag", 
        0, 
        10000,
        10000f,
        ActionsOLD.engine, 
        (e, f)->{
          e.getPhysics().setDrag(f);
        },
        (s)->
        {
          return s.getPhysics().getDrag();
        }
        );  
    
    setPreferredSize(new Dimension(400, 650));
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    int i = 0;
    setLayout(new GridBagLayout());
    place(new TitleField("FORCES"), i++);
    
    place(dragSlider, i++);
    place(homeForce, i++);
    place(homeMax, i++);
    place(homeLinear, i++);
    place(homeLinearMax, i++);
    place(gravity, i++);
    
    i = gravityVector.addByGridBag(this, i++);
    
    place(new TitleField("VECTORS"), i++);
//    place(new VectorPanel("Some Vector"), 12);
//    place(new VectorPanel("Some Vector"), 13);
//    place(new VectorPanel("Some Vector"), 14);
    //Buttons
    place(new Spacer(100, 15), i++);
    JPanel group = new JPanel();
    group.setLayout(new GridBagLayout());
    group.add(read, UiUtility.getFillHorizontal(0, 0));
    group.add(hide, UiUtility.getFillHorizontal(1, 0));
    group.add(save, UiUtility.getFillHorizontal(2, 0));
    group.add(open, UiUtility.getFillHorizontal(3, 0));
    
    GridBagConstraints ggbc = UiUtility.getFillHorizontal(1, i++);
    ggbc.gridwidth = 2;
    add(group, ggbc);
    add(new JPanel(), UiUtility.getFillBoth(0, ++i));//Push up
    
    read();
    pack();
    
    addFocusListener(new FocusListener(){

      @Override
      public void focusGained(FocusEvent e)
      {
        read();
      }

      @Override
      public void focusLost(FocusEvent e)
      {
      }
    }
    );
  }
  
  /**
   * Pulls the data into the user interface.
   */
  public void read()
  {
    if(engine == null)
      return;
    
    pullData();
    //update valides
    //
  }
  
  public void pullData()
  {
    homeForce.sync();
    homeMax.sync();
    homeLinear.sync();
    homeLinearMax.sync();
    gravity.sync();
    gravityVector.sync();
    dragSlider.sync();
  }
  
  public void place(JComponent c, int y)
  {
    GridBagConstraints gbc = UiUtility.getFillHorizontal(0, y);
    gbc.gridwidth = 4;
    this.add(c, gbc);
  }
  
  public void attemptSave()
  {
    String disclaimer = "<html><h1>Do you really want to save?</h1></html>";
    String disclaimer2 = "<html><p>Saving this will create a file in the environment folder for future use.</p></html>";
    JFrame save = new JFrame();
    save.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    save.setTitle("Save Environment?");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
    JTextField name = new JTextField("environment-" + sdf.format(new Date()));
    JButton cancel = new JButton("Cancel");
    JButton doIt = new JButton("Save");
    
    save.setLayout(new GridBagLayout());
    GridBagConstraints x = UiUtility.getFillHorizontal(0, 0);
    x.gridwidth = 3;
    save.add(new JLabel(disclaimer), x);
    x.gridy = 1;
    save.add(new JLabel(disclaimer2), x);
    save.add(new JLabel("File Name: "), UiUtility.getNoFill(0, 5));
    save.add(name, UiUtility.getNoFill(1, 5));
    save.add(new JPanel(), UiUtility.getFillHorizontal(0, 99));
    save.add(doIt, UiUtility.getFillHorizontal(1, 99));
    save.add(cancel, UiUtility.getFillHorizontal(2, 99));
    
    save.setSize(400, 300);
    UiUtility.centerFrame(this, save);
    save.setVisible(true);
    name.selectAll();
    cancel.addActionListener((l)->{ save.setVisible(false); save.dispose();});
    doIt.addActionListener((l)->
    {
      if(saveVariables(name.getText()))
      {
        save.setVisible(false);
        save.dispose();
      }
      else
        JOptionPane.showMessageDialog(save, "Unable to save the file, please check the logs");
    });
  }
  
  public boolean saveVariables(String fileName)
  {
    //Save the data here!
    StringBuilder buffer = new StringBuilder();
    BiConsumer<String, String> write = (label, data)->
    {
      buffer.append(label);
      buffer.append("\n");
      buffer.append(data);
      buffer.append("\n");
    };
    //Prepare
    write.accept(FileArgs.DRAG, ActionsOLD.engine.getPhysics().getDrag()+"");
    write.accept(FileArgs.GRAVITY, ActionsOLD.gravity.save());
    write.accept(FileArgs.HOME, ActionsOLD.home.save());
    write.accept(FileArgs.HOME_LINEAR, ActionsOLD.homeLinear.save());
    write.accept(FileArgs.WIND, "not implemented...");
    
    try{
      FileUtil.makeDirs(new File(ENVIRONMENT_FOLDER));
      FileUtil.writeFile(ENVIRONMENT_FOLDER + "/" + fileName +".env", buffer.toString().getBytes());
      return true;
    } catch (Throwable t) {
      JOptionPane.showMessageDialog(this, t.getMessage());
      t.printStackTrace();
      return false;
    }
  }
  
  public void loadGlobal()
  {
    JFileChooser open = new JFileChooser(new File("environment"));
    int act = open.showOpenDialog(this);
    if(act == JFileChooser.APPROVE_OPTION)
    {
      File f = open.getSelectedFile();
      if(!loadVariables(f.getAbsolutePath()))
        JOptionPane.showConfirmDialog(this, "Unable to open " + f.getName(), "Error Opening File", JOptionPane.ERROR_MESSAGE);
    }
    
  }
  public boolean loadVariables(String fileName)
  {
    if(loadVariablesNoUi(fileName)){
      read();
      return true;
    }
    else
      return false;
  }
  
  public static boolean loadVariablesNoUi(String fileName)
  {
    ArrayList<String> lines = FileUtil.readLines(fileName);
    
    if(lines == null)
      return false;
    
    BiConsumer<SaveableParticleBehavior3D<?>, String> loadIt = (x, line)->
    {
      x.load(line);
      if(x.vars.running)
      {
        System.out.println("Loading -> " + x + " and setting to running");
        ActionsOLD.engine.addBehavior(x);
      }
      else
      {
        System.out.println("Loading -> " + x + " and setting to not running");
        ActionsOLD.engine.removeBehavior(x);
      }
        
    };
    
    for(int i = 0; i < lines.size(); i++)
    {
      switch(i)
      {
        case 1:
          ActionsOLD.engine.getPhysics().setDrag(Float.valueOf(lines.get(i)));
          break;
        case 3:
          loadIt.accept(ActionsOLD.gravity, lines.get(i));
          break;
        case 5:
          loadIt.accept(ActionsOLD.home, lines.get(i));
          break;
        case 7:
          loadIt.accept(ActionsOLD.homeLinear, lines.get(i));
          break;
        case 9:
          //wind
          //Actions.home.load(lines.get(i));
          break;
      }
    }
    
    return true;
  }
}
