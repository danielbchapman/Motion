package com.danielbchapman.physics.toxiclibs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import lombok.Getter;
import lombok.Setter;
import toxi.geom.Vec3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

import com.danielbchapman.utility.UiUtility;

public class EnvironmentTools extends JFrame
{
  private static final long serialVersionUID = 1L;
  
  MotionEngine engine;
  
  BehaviorSlider<HomeBehavior3D> homeForce;
  BehaviorSlider<HomeBehavior3D> homeMax;
  BehaviorSlider<HomeBehaviorLinear3D> homeLinear;
  BehaviorSlider<HomeBehaviorLinear3D> homeLinearMax;
  BehaviorSlider<AngularGravityBehavior3D> gravity;
  PropertySlider<MotionEngine> dragSlider;
  VectorPanel<AngularGravityBehavior3D> gravityVector;
  
  JButton read;
  JButton hide;
  private boolean apply = false;

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
    read.addActionListener((x)->{ read();});
    hide.addActionListener((x)->{ setVisible(false);});
    
    //Home
    homeForce = new BehaviorSlider<>(
        "Home Euclidian Force", 
        0, 
        10000,
        1000f,
        Actions.home, 
        (b, f)->{ b.strength = f; },
        (b, s)->{ s.set(b.strength);},
        true
        );
    
    //Home
    homeMax = new BehaviorSlider<>(
        "Home Euclidian Max", 
        0, 
        10000,
        1000f,
        Actions.home, 
        (b, f)->{
          b.max = f;
        },
        (b, s)->{s.set(b.max);}
        );
    
    homeLinear = new BehaviorSlider<>(
        "Home Linear Easing", 
        0, 
        10000,
        10000f,
        Actions.homeLinear, 
        (b, f)->{b.vars.userA = f;},
        (b, s)->{s.set(b.vars.userA);},
        true
        );
    
    homeLinearMax = new BehaviorSlider<>(
        "Home Linear Max", 
        0, 
        10000,
        1000f,
        Actions.homeLinear, 
        (b, f)->{b.vars.maxForce = f;},
        (b, s)->{s.set(b.vars.maxForce);}
        );
    
    gravity = new BehaviorSlider<AngularGravityBehavior3D>(
        "Angular Gravity", 
        0, 
        10000,
        10000f,
        Actions.gravity, 
        (b, f)->{
          b.updateMagnitude(f);
          System.out.println("Scaling Gravity to -> " + f + " Gravity: " + Actions.gravity);
        },
        (b, s)->
        {
          s.set(b.getForce().magnitude());
        },
        true
        );    
    
    gravityVector = new VectorPanel<>(
        "Gravity Vector",
        Actions.gravity,
        (b, g)->{
          System.out.println("Called Set");
          //b.set(g.getForce());
        },
        (g, b)->{
          System.out.println("Called change");
          //g.setForce(b.get());
          }
        );
    
    dragSlider = new PropertySlider<MotionEngine>(
        "Drag", 
        0, 
        10000,
        10000f,
        Actions.engine, 
        (e, f)->{
          e.getPhysics().setDrag(f);
        },
        (s, e)->
        {
          s.set(e.getPhysics().getDrag());
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
    place(gravityVector, i++);
    
    place(new TitleField("VECTORS"), i++);
//    place(new VectorPanel("Some Vector"), 12);
//    place(new VectorPanel("Some Vector"), 13);
//    place(new VectorPanel("Some Vector"), 14);
    //Buttons
    place(new Spacer(100, 15), i++);
    add(read, UiUtility.getNoFill(1, i));
    add(hide, UiUtility.getNoFill(2, i));
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
    homeForce.pullData();
    homeMax.pullData();
    homeLinear.pullData();
    homeLinearMax.pullData();
    gravity.pullData();
    gravityVector.pullData();
    dragSlider.pullData();
  }
  
  public class VectorPanel<T> extends JPanel
  {
    private static final long serialVersionUID = 1L;
    private Vec3D vector = new Vec3D();
    
    JTextField xText = new JTextField();
    JTextField yText = new JTextField();
    JTextField zText = new JTextField();
    T object;
    BiConsumer<VectorPanel<T>, T> read;
    BiConsumer<T, VectorPanel<T>> change;
    public VectorPanel(String label, T object, BiConsumer<VectorPanel<T>, T> read, BiConsumer<T, VectorPanel<T>> change)
    {
      this.read = read;
      this.change = change;
      this.object = object;
      setLayout(new GridBagLayout());
      GridBagConstraints g = UiUtility.getFillHorizontal(0, 0);
      g.gridwidth = 4;
      add(new JLabel(label), g);
      add(new JLabel("x:"), UiUtility.getFillHorizontal(0, 1));
      add(new JLabel("y:"), UiUtility.getFillHorizontal(1, 1));
      add(new JLabel("z:"), UiUtility.getFillHorizontal(2, 1));
     
      add(xText, UiUtility.getFillHorizontal(0, 2));
      add(yText, UiUtility.getFillHorizontal(1, 2));
      add(zText, UiUtility.getFillHorizontal(2, 2));
      
      read.accept(this, object);
      
      
      FocusListener f = new FocusListener()
      {
        @Override
        public void focusLost(FocusEvent e)
        {
          change.accept(object, VectorPanel.this);
        }

        @Override
        public void focusGained(FocusEvent e)
        {
        }
      };
      
      xText.addFocusListener(f);
      yText.addFocusListener(f);
      zText.addFocusListener(f);
//      xText.addPropertyChangeListener((e)->{change.accept(object, this);});
//      yText.addPropertyChangeListener((e)->{change.accept(object, this);});
//      zText.addPropertyChangeListener((e)->{change.accept(object, this);});
    }
    
    public void pullData()
    {
      read.accept(this, object);
    }
    public void set(Vec3D vec)
    {
//      this.vector = vec;
//      DecimalFormat df = new DecimalFormat("000.0000");
//      xText.setText(df.format(vec.x));
//      yText.setText(df.format(vec.y));
//      zText.setText(df.format(vec.z));
    }
    
    public Vec3D get()
    {
      float x = Float.valueOf(xText.getText());
      float y = Float.valueOf(yText.getText());
      float z = Float.valueOf(zText.getText());
      vector = new Vec3D(x,  y,  z);
      return vector.copy();
    }
  }
  
  public class PropertySlider<Source> extends FloatSlider
  {
    private static final long serialVersionUID = 1L;
    private BiConsumer<Source, Float> onChange;
    private BiConsumer<FloatSlider, Source> get;
    private Source source;
    public PropertySlider(String label, int min, int max, float divisor, Source source, BiConsumer<Source, Float> onChange, BiConsumer<FloatSlider, Source> get)
    {
      this(label, min, max, divisor, source, onChange, get, false);
    }
    
    public PropertySlider(String label, int min, int max, float divisor, Source source, BiConsumer<Source, Float> onChange, BiConsumer<FloatSlider, Source> get, boolean useEnabled)
    {
      super(label, min, max, divisor);
      this.onChange = onChange;
      this.get = get;
      this.source = source;
      enabled.setVisible(false);
    }
    
    @Override
    public void set(float f)
    {
      super.set(f);
      onChange.accept(source, f);
    }    
    
    public void pullData()
    {
      get.accept(this, source);
    }
  }
  
  public class BehaviorSlider<T extends ParticleBehavior3D> extends FloatSlider
  {
    private static final long serialVersionUID = 1L;
    private BiConsumer<T, Float> onChange;
    private BiConsumer<T, BehaviorSlider<T>> get;
    private T behavior;
    private boolean useEnabled;
    
    public BehaviorSlider(String label, int min, int max, float divisor, T behavior, BiConsumer<T, Float> onChange, BiConsumer<T, BehaviorSlider<T>> get)
    {
      this(label, min, max, divisor, behavior, onChange, get, false);
    }
    
    public BehaviorSlider(String label, int min, int max, float divisor, T behavior, BiConsumer<T, Float> onChange, BiConsumer<T, BehaviorSlider<T>> get, boolean useEnabled)
    {
      super(label, min, max, divisor);
      this.onChange = onChange;
      this.get = get;
      this.behavior = behavior;
      this.useEnabled = useEnabled;
      if(useEnabled)
        enabled.addItemListener((e)->
        {
          boolean selected = enabled.isSelected();
          if(selected){
            engine.addBehavior(behavior);
          }
          else
            engine.removeBehavior(behavior);
        });
      else
        enabled.setVisible(false);
    }
    
    @Override
    public void set(float f)
    {
      super.set(f);
      onChange.accept(behavior, f);
    }    
    
    public void pullData()
    {
      if(useEnabled && engine != null)
        enabled.setSelected(engine.isActive(behavior));
      get.accept(behavior, this);
    }
  }
  
  public class FloatSlider extends JPanel
  {
    private static final long serialVersionUID = 1L;
    
    @Getter
    @Setter
    protected float value = 0f;
    
    protected JSlider slider;
    protected JTextField text;
    protected JCheckBox enabled;
    protected float divisor;
    
    public FloatSlider(String label, int min, int max, float divisor)
    {
      this.divisor = divisor;
      enabled = new JCheckBox();
      enabled.setText("Enabled");
      slider = new JSlider(JSlider.HORIZONTAL);
      slider.setMinimum(0);
      slider.setMaximum(10000);
      slider.setMajorTickSpacing(1000);
      slider.setMinorTickSpacing(100);
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);
      text = new JTextField();
      text.setPreferredSize(new Dimension(50, 20));
      text.setSize(50, 20);
      setPreferredSize(new Dimension(240, 75));
      setLayout(new GridBagLayout());
      
      add(new JLabel(label), UiUtility.getNoFill(0, 0));
      add(new JPanel(), UiUtility.getFillHorizontal(1, 0));
      
      add(text, UiUtility.getNoFill(2, 0));
      add(enabled, UiUtility.getNoFill(3, 0));
      
      GridBagConstraints gbc = UiUtility.getFillHorizontal(0, 1);
      gbc.gridwidth = 4;
      add(slider, gbc);
      
      slider.addChangeListener((e)->
          { 
            JSlider s = (JSlider) e.getSource();
            if(!s.getValueIsAdjusting())
            {
              convert(s.getValue());
            }
          }
          );
    }
    /**
     * Set the float value;
     * @param f   
     * 
     */
    public void set(float f)
    {
      DecimalFormat df = new DecimalFormat("0.0000");
      text.setText(df.format(f));
      slider.setValue((int) (f * divisor));//Convert to an int
    }
    
    private void convert(int val)
    {
      float x =  ((float) val) / divisor;//1-10 float
      set(x);
    }
  }
  public class TitleField extends JLabel
  {
    private static final long serialVersionUID = 1L;

    public TitleField(String label)
    {
      this.setText("<html><span style='font-size: 14px; font-weight:bold;text-decoration:underline;'>" + label + "</span></html>");
    }
  }
  
  public class Spacer extends JPanel
  {
   
    private static final long serialVersionUID = 1L;

    public Spacer(int x, int y)
    {
      setPreferredSize(new Dimension(x, y));
    }
  }
  public void place(JComponent c, int y)
  {
    GridBagConstraints gbc = UiUtility.getFillHorizontal(0, y);
    gbc.gridwidth = 4;
    this.add(c, gbc);
  }
}
