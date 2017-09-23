package com.danielbchapman.motion.core;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.danielbchapman.physics.toxiclibs.PersistentVariables;
import com.danielbchapman.physics.toxiclibs.PersistentVariables.Fields;
import com.danielbchapman.physics.ui.CheckBoxProperty;
import com.danielbchapman.physics.ui.PojoComboBox;
import com.danielbchapman.physics.ui.PropertySlider;
import com.danielbchapman.physics.ui.SelectItem;
import com.danielbchapman.physics.ui.Spacer;
import com.danielbchapman.physics.ui.Vec3DEditor;
import com.danielbchapman.text.Text;
import com.danielbchapman.text.Utility;
import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.UiUtility;

import shows.test.TestExplodeBrush;
import shows.test.TestFalloffAttractorBrush;
import shows.test.TestInverseExplodeBrush;

public class BrushEditor2017 extends JFrame
{
  public static final String BRUSH_FOLDER = "brushes";
  MotionBrush current;
  Motion motion;
  private int row = 0;
  private static final long serialVersionUID = 1L;
  
  public static void main(String ... args)
  {
    BrushEditor2017 be = new BrushEditor2017(null);
    be.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    be.setVisible(true);
  }
  
  ArrayList<JButton> brushDefaults = new ArrayList<JButton>();
  PojoComboBox<Class<? extends MotionBrush>> brushClass;
  JButton reload;
  JButton save;
  JButton load;
  JButton close;
  JButton update;
  JPanel content = new JPanel();

  //Fields
  private PersistentVariables variables;

  CheckBoxProperty<BrushEditor2017> enabled;
  
  Vec3DEditor<BrushEditor2017> position;
  Vec3DEditor<BrushEditor2017> force;
  Vec3DEditor<BrushEditor2017> backup;
  Vec3DEditor<BrushEditor2017> scaledForce;
  
  PropertySlider<BrushEditor2017> magnitude;
  PropertySlider<BrushEditor2017> radius;
  PropertySlider<BrushEditor2017> userA;
  PropertySlider<BrushEditor2017> userB;
  PropertySlider<BrushEditor2017> userC;
  PropertySlider<BrushEditor2017> maxForce;
  PropertySlider<BrushEditor2017> minForce;

  public BrushEditor2017(Motion motion)
  {
    this.motion = motion;
    setLayout(new GridBagLayout());
    setPreferredSize(new Dimension(400, 650));
    setSize(new Dimension(400, 650));
    setTitle("Brush Editor");
    save = new JButton("Save");
    load = new JButton("Load");
    close = new JButton("Close");
    update = new JButton("Sync");
    
    save.addActionListener(x -> { save();});
    load.addActionListener(x -> { load();});
    close.addActionListener(x -> { this.setVisible(false); this.dispose();});
    update.addActionListener(x -> { sync();});
    
    
    brushClass = new PojoComboBox<>();
    reload = new JButton("Reload");
    BiConsumer<String, Class<? extends MotionBrush>> addItem = (name, clazz)->
    {
      brushClass.addItem(
          new SelectItem<Class<? extends MotionBrush>>(name, clazz));
    };
    
    addItem.accept("Explode Brush", TestExplodeBrush.class);
    addItem.accept("Inverse Explode Brush", TestInverseExplodeBrush.class);
    addItem.accept("Falloff Attractor", TestFalloffAttractorBrush.class);
    //addItem.accept("Inverse Falloff Attractor", FalloffAttractionBehaviorInverse.class);
    //addItem.accept("Lambda Brush", LambdaBrush.class);
    
    Consumer<Void> loadFromCombo = (Void)->
    {
      System.out.println("Combo Box: " + brushClass.getSelectedValue());
      if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "Load brush? " + brushClass.getSelectedName()))
          {
            Class<? extends MotionBrush> clazz = brushClass.getSelectedValue();
            MotionBrush beh;
            try
            {
              beh = clazz.newInstance();
              populate(beh);
              motion.setCurrentBrush(beh);
            }
            catch (Exception e)
            {
              e.printStackTrace();
              warn("Unable to load brush", "The brush " + clazz + " could not be loaded.");
            }
          }
    };
    brushClass.addActionListener((x)->
    {
      loadFromCombo.accept(null);
    });
    
    reload.addActionListener(x->{loadFromCombo.accept(null);});
    
    add(brushClass, UiUtility.getFillHorizontal(0, 0).size(2, 1));
    add(reload, UiUtility.getFillHorizontal(3, 0));
    
    content.setLayout(new GridBagLayout());
    GridBagConstraints gbc = UiUtility.getFillHorizontal(0, 50);
    gbc.gridwidth = 5;
    add(content, gbc);    
    //Buttons
    add(load, UiUtility.getFillHorizontal(0, 99));
    add(update, UiUtility.getFillHorizontal(1, 99));
    add(save, UiUtility.getFillHorizontal(2, 99));
    add(close, UiUtility.getFillHorizontal(3, 99));
    
    add(new Spacer(10,10), UiUtility.getFillBoth(2, 250));
    MotionBrush brush = motion.currentBrush;
    if(brush == null)
    {
      brush = new TestExplodeBrush();
      motion.setCurrentBrush(brush);
    }
      
    populate(brush);
    setVisible(true);
  }

  public void populate(MotionBrush brush)
  {
    if(brush == null)
    {
      warn("Unable to Load", "The variables for this brush are null");
      return;
    }
    this.current = brush;
    this.variables = brush.vars;
    
    Map<String, String> names = brush.getFieldNames();
    Function<String, String> checkName = (x)->
    {
      String result = names.get(x);
      return result == null ? x : result;
    };
    
    /*
     * Create the components and bind them to the reference
     */
    enabled = new CheckBoxProperty<BrushEditor2017>(
        checkName.apply(Fields.ENABLED), 
        this, 
        (o)->{
          System.out.println("Selected ->" + o.variables.enabled);
          return o.variables.enabled;
        }, 
        (b, o)->
        {
          System.out.println("Selected ->" + b);
          o.variables.enabled = b;
        }
    );
    
    position = new Vec3DEditor<>(
        checkName.apply(Fields.POS_X),
        this,
        (v, o)-> //update
        {
          o.variables.position = v.copy();
        },
        (o)-> //sync
        {
          return o.variables.position.copy();
        }
        );
    
    force = new Vec3DEditor<>(
        checkName.apply(Fields.FOR_X),
        this,
        (v, o)-> //update
        {
          o.variables.force = v.copy();
        },
        (o)-> //sync
        {
          return o.variables.force.copy();
        }
        );
    
    backup = new Vec3DEditor<>(
        checkName.apply(Fields.BAK_X),
        this,
        (v, o)-> //update
        {
          o.variables.backup = v.copy();
        },
        (o)-> //sync
        {
          return o.variables.backup.copy();
        }
        );
    scaledForce= new Vec3DEditor<>(
        checkName.apply(Fields.SCL_X),
        this,
        (v, o)-> //update
        {
          o.variables.scaledForce = v.copy();
        },
        (o)-> //sync
        {
          return o.variables.scaledForce.copy();
        }
        );
    
    float steps = 1000f;
    magnitude = new PropertySlider<>(
        checkName.apply(Fields.MAGNITUDE), 
        (int)(variables.magnitudeMin * steps), 
        (int)(variables.magnitudeMax * steps), 
        steps, 
        this, 
        (o, f)->
        {
          o.variables.magnitude = f;
        }, 
        (o)->
        {
          return o.variables.magnitude;
        });

    //FixME implement a min/max for each variable
    radius = new PropertySlider<>(
        checkName.apply(Fields.RADIUS), 
        (int)(variables.radiusMin * steps), 
        (int)(variables.radiusMax * steps), 
        steps, 
        this, 
        (o, f)->
        {
          o.variables.setRadius(f);
        }, 
        (o)->
        {
          return o.variables.getRadius();
        });
    userA = new PropertySlider<>(
        checkName.apply(Fields.USER_A), 
        0, 
        10000, 
        10000f, 
        this, 
        (o, f)->
        {
          o.variables.userA = f;
        }, 
        (o)->
        {
          return o.variables.userA;
        });
    userB = new PropertySlider<>(
        checkName.apply(Fields.USER_B), 
        0, 
        10000, 
        10000f, 
        this, 
        (o, f)->
        {
          o.variables.userB = f;
        }, 
        (o)->
        {
          return o.variables.userB;
        });
    userC = new PropertySlider<>(
        checkName.apply(Fields.USER_C), 
        0, 
        10000, 
        10000f, 
        this, 
        (o, f)->
        {
          o.variables.userC = f;
        }, 
        (o)->
        {
          return o.variables.userC;
        });
    maxForce = new PropertySlider<>(
        checkName.apply(Fields.MAX_FORCE), 
        0, 
        10000, 
        10000f, 
        this, 
        (o, f)->
        {
          o.variables.maxForce = f;
        }, 
        (o)->
        {
          return o.variables.maxForce;
        });
    minForce = new PropertySlider<>(
        checkName.apply(Fields.MIN_FORCE), 
        0, 
        10000, 
        10000f, 
        this, 
        (o, f)->
        {
          o.variables.minForce = f;
        }, 
        (o)->
        {
          return o.variables.minForce;
        });
    
    row = 0;
    Function<String, Boolean> checkItem = (x)->
    {
      String result = names.get(x);
      return result != null;
    };
    
    BiConsumer<String, Vec3DEditor<BrushEditor2017>> addVec = (x, y)->
    {
      if(checkItem.apply(x)){
        row = y.addByGridBag(content, row++);
      }
    };
    
    BiConsumer<String, PropertySlider<BrushEditor2017>> addProp = (x, y)->
    {
      if(checkItem.apply(x)){
        place(content, y, row++);
      }
    };
    /*
     * Add the components that are needed
     */
    content.removeAll();    
    if(!Text.isEmptyOrNull(brush.vars.getPetName()))
      title(content, brush.vars.getPetName(), row++);
    
    title(content, "Class: " + brush.getName(), row++);
    
    if(checkItem.apply(Fields.ENABLED)){
       content.add(enabled, UiUtility.getFillHorizontal(0, row++).anchor(GridBagConstraints.EAST));
    }
    
    addVec.accept(Fields.POS_X, position);
    addVec.accept(Fields.FOR_X, force);
    addVec.accept(Fields.SCL_X, scaledForce);
    addVec.accept(Fields.BAK_X, backup);
    
    addProp.accept(Fields.MAGNITUDE, magnitude);
    addProp.accept(Fields.RADIUS, radius );
    addProp.accept(Fields.MIN_FORCE, minForce);
    addProp.accept(Fields.MAX_FORCE, maxForce);
    addProp.accept(Fields.USER_A, userA);
    addProp.accept(Fields.USER_B, userB);
    addProp.accept(Fields.USER_C, userC);

    sync();
    
    content.revalidate();
    content.repaint();
  }
  
  public void sync()
  {
    enabled.setSelected(variables.enabled);
    
    position.sync();
    force.sync();
    backup.sync();
    scaledForce.sync();
    
    magnitude.sync();
    radius.sync();
    userA.sync();
    userB.sync();
    userC.sync();
    maxForce.sync();
    minForce.sync();
  }
  
  public boolean saveBrush(String file)
  {
    //Line zero == Class
    //Line one == data
    try
    {
      StringBuilder b = new StringBuilder();
      b.append(current.getClass().getName());
      b.append("\n");
      b.append(PersistentVariables.toLine(current.vars));
      
      FileUtil.makeDirs(new File(BRUSH_FOLDER));
      FileUtil.writeFile(BRUSH_FOLDER + "/" + file, b.toString().getBytes());
      return true;
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      warn("Unable to save", "The file " + file + " could not be saved. Please check the name and try again");
      return false;
    }
  }
  
  @SuppressWarnings("unchecked")
  public boolean loadBrush(String file)
  {
    ArrayList<String> lines = FileUtil.readLines(file);
    if(lines == null)
      return false;
    
    if(lines.size() >= 2 && !lines.get(1).trim().isEmpty())
    {
      try
      {
        String clazzName = lines.get(0).trim();
        String data = lines.get(1).trim();
        
        
        Class<? extends MotionBrush> clazz = (Class<? extends MotionBrush>) getClass().getClassLoader().loadClass(clazzName);
        MotionBrush loaded = clazz.newInstance();
        
        PersistentVariables vars = PersistentVariables.fromLine(data);
        loaded.vars = vars;
        
        populate(loaded);
        motion.setCurrentBrush(loaded);
        return true;
      }
      catch (Throwable t)
      {
        t.printStackTrace();
        return false;
      }
      
    }
    return false; 
  }
  
  public void load()
  {
    JFileChooser open = new JFileChooser(new File(BRUSH_FOLDER));
    int act = open.showOpenDialog(this);
    if(act == JFileChooser.APPROVE_OPTION)
    {
      File f = open.getSelectedFile();
      if(!loadBrush(f.getAbsolutePath()))
        warn("Failed to Load", "Unable to open " + f.getName());
    }
  }
  
  public void save()
  {
    String disclaimer = "<html><h1>Do you really want to save this brush?</h1></html>";
    String disclaimer2 = "<html><p>Saving this will create a file in the brush folder for future use.</p></html>";
    JFrame save = new JFrame();
    save.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    save.setTitle("Save Brush?");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
    JTextField name = new JTextField("brush-" + sdf.format(new Date()));
    JTextField brush = new JTextField(Text.isEmptyOrNull(current.vars.petName) ? "My " + current.getName() : current.vars.petName);
    JButton cancel = new JButton("Cancel");
    JButton doIt = new JButton("Save");
    
    save.setLayout(new GridBagLayout());
    save.add(new JLabel(disclaimer), UiUtility.getFillHorizontal(0, 0).size(3, 1));
    save.add(new JLabel(disclaimer2), UiUtility.getFillHorizontal(0, 0).size(3, 1));
    
    save.add(new JLabel("Brush Label: "), UiUtility.getNoFill(0, 5));
    save.add(brush, UiUtility.getFillHorizontal(1, 5).size(2, 1));
    
    save.add(new JLabel("File Name: "), UiUtility.getNoFill(0, 6));
    save.add(name, UiUtility.getFillHorizontal(1, 6).size(2, 1));    
    
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
      if(!Text.isEmptyOrNull(brush.getText()))
        current.vars.petName = brush.getText();
      if(saveBrush(name.getText()))
      {
        save.setVisible(false);
        save.dispose();
        populate(current);//refresh with names
      }
      else
        warn("Error Saving", "Unable to save the file, please check the logs");
    });
  }
  
  private void title(Container c, String text, int row)
  {
    GridBagConstraints g = UiUtility.getFillHorizontal(0, row);
    g.gridwidth = 4;
    c.add(new JLabel("<html><h2><b>" + text + "</h2></html>"),g);
  }
  
  void subtitle(Container c, String text, int row)
  {
    GridBagConstraints g = UiUtility.getFillHorizontal(0, row);
    g.gridwidth = 4;
    c.add(new JLabel("<html><h3><b>" + text + "</h3></html>"),g);
  }
  
  /**
   * @param title
   * @param message  
   * 
   */
  private void warn(String title, String message)
  {
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
  }
  
  public void place(JComponent parent, JComponent c, int y)
  {
    GridBagConstraints gbc = UiUtility.getFillHorizontal(0, y);
    gbc.gridwidth = 4;
    parent.add(c, gbc);
  }
}
