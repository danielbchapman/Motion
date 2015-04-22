package com.danielbchapman.physics.toxiclibs;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombok.Data;
import lombok.Getter;
import processing.core.PConstants;
import processing.event.MouseEvent;

import com.danielbchapman.text.Utility;
import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.UiUtility;

@Data
public class Recorder
{
  public static void main(String ... args)
  {
    RecordUI r =  new RecordUI(800, 600);
    r.setVisible(true);
    r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  public final static String CAPTURE_FOLDER = "captures";

  public static void save(String name, ArrayList<RecordAction> actions, int w, int h)
  {
    StringBuilder b = new StringBuilder();

    for (int i = 0; i < actions.size(); i++)
    {
      b.append(RecordAction.toFloatFormat(actions.get(i), w, h));
      b.append("\n");
    }

    FileUtil.makeDirs(new File(CAPTURE_FOLDER));
    FileUtil.writeFile(CAPTURE_FOLDER + "/" + name, b.toString().getBytes());
  }

  public static ArrayList<RecordAction> load(String file, int w, int h)
  {
    ArrayList<RecordAction> results = new ArrayList<>();

    ArrayList<String> lines = FileUtil.readLines(file);

    for (String s : lines)
      results.add(RecordAction.fromFloatFormat(s, w, h));

    return results;
  }

  ArrayList<RecordAction> actions = new ArrayList<RecordAction>();
  @Getter
  boolean recording = false;
  long start = -1L;

  public void capture(MouseEvent e)
  {
    if (!recording)
      return;

    int y = e.getY();
    int x = e.getX();
    int time = (int) (System.currentTimeMillis() - start);

    boolean left = false;
    boolean right = false;
    // boolean center = false;
    int button = e.getButton();
    if (button == PConstants.LEFT)
    {
      left = true;
    }
    else
      if (button == PConstants.RIGHT)
      {
        right = true;
      }
    // Key events are disabled...
    RecordAction tmp = new RecordAction("Captured", time, x, y, left, right, false);
    actions.add(tmp);
    System.out.println("Adding action " + tmp);
  }

  public ArrayList<RecordAction> stop()
  {
    recording = false;
    start = -1L;
    ArrayList<RecordAction> cp = new ArrayList<>();
    for (RecordAction a : actions)
      cp.add(a);
    actions.clear();
    return cp;
  }

  public void start()
  {
    recording = true;
    start = System.currentTimeMillis();
  }

  /**
   * Take a list of actions and turn it into a cue stack
   * @param actions the actions to prepare
   * @param layer the layer to use (future hook)
   * @param engine the engine to use
   * @return A constructed CueStack object
   * 
   */
  public static Playback playback(ArrayList<RecordAction> actions, Layer layer, MotionEngine engine)
  {
    System.out.println("Creating stack: " + actions.size());

    return new Playback("playback", actions);
  }

  public static class RecordUI extends JFrame
  {
    private static final long serialVersionUID = 1L;

    int row = 0;
    String name;

    JButton open;
    JButton close;
    JButton save;
    
    JPanel content;
    public RecordUI(int w, int h)
    {
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setLayout(new GridBagLayout());
      open = new JButton("Open");
      save = new JButton("Save");
      close = new JButton("Close");
      
      open.addActionListener((x)->
      {
        open(Actions.engine.width, Actions.engine.height);
      });
      
      close.addActionListener((e)->
      {
        this.setVisible(false);
        this.dispose();
      });
      
      save.addActionListener((x)->
      {
        ArrayList<RecordAction> actions = Actions.engine.getCapture();
        if(actions != null && !actions.isEmpty())
          save(actions, Actions.engine.width, Actions.engine.height);
        else
          warn("Unable to Save", "Unable to save empty list" );
      });
      content = new JPanel();
      add(content, UiUtility.getFillBoth());
      setSize(250, 400);
      setVisible(true);
    }

    public void populate(String name, ArrayList<RecordAction> actions)
    {
      content.removeAll();
      content.setLayout(new GridBagLayout());
      row = 0;
      title(this, name == null ? "No Cues Loaded" : name, row++);
      if(actions == null || actions.isEmpty())
      {
        subtitle(content, "No Actions In File", row++);
      }
      else
        subtitle(content, "Total Events " + actions.size(), row++);
      
      content.add(save, UiUtility.getFillHorizontal(0, row));
      content.add(open, UiUtility.getFillHorizontal(1, row));
      content.add(close, UiUtility.getFillHorizontal(2, row++));
      content.add(new JPanel(), UiUtility.getFillBoth(0, row++));
      
      content.revalidate();
      content.repaint();
    }

    public void open(int w, int h)
    {
      JFileChooser open = new JFileChooser(CAPTURE_FOLDER);
      int act = open.showOpenDialog(this);
      if(act == JFileChooser.APPROVE_OPTION)
      {
        File f = open.getSelectedFile();
        ArrayList<RecordAction> actions = Recorder.load(f.getAbsolutePath(), w, h);
        if(actions == null || actions.isEmpty())
        {
          warn("Failed to Load", "Unable to open " + f.getName());
          return;
        }
        
        populate(f.getName(), actions);
        Actions.engine.setCapture(actions);
      }
    }

    public void save(ArrayList<RecordAction> actions, int w, int h)
    {
      String disclaimer = "<html><h1>Do you really want to save this sequence?</h1></html>";
      String disclaimer2 = "<html><p>Saving this will create a file in the capture folder for future use.</p></html>";
      JFrame save = new JFrame();
      save.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      save.setTitle("Save Sequence?");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
      JTextField name = new JTextField("capture-" + sdf.format(new Date()));
      JButton cancel = new JButton("Cancel");
      JButton doIt = new JButton("Save");

      save.setLayout(new GridBagLayout());
      save.add(new JLabel(disclaimer), UiUtility.getFillHorizontal(0, 0).size(3, 1));
      save.add(new JLabel(disclaimer2), UiUtility.getFillHorizontal(0, 0).size(3, 1));

      save.add(new JLabel("File Name: "), UiUtility.getNoFill(0, 6));
      save.add(name, UiUtility.getFillHorizontal(1, 6).size(2, 1));

      save.add(new JPanel(), UiUtility.getFillHorizontal(0, 99));
      save.add(doIt, UiUtility.getFillHorizontal(1, 99));
      save.add(cancel, UiUtility.getFillHorizontal(2, 99));

      save.setSize(400, 300);
      UiUtility.centerFrame(this, save);
      save.setVisible(true);
      name.selectAll();

      cancel.addActionListener((l) -> {
        save.setVisible(false);
        save.dispose();
      });
      doIt.addActionListener((l) -> {
        try
        {
          Recorder.save(name.getText(), actions, w, h);
          save.setVisible(false);
          save.dispose();
          populate(name.getText(), actions);// refresh with names
        }
        catch (Throwable t)
        {
          warn("Error Saving", "Unable to save the file, please check the logs");
        }
      });
    }

    private void title(Container c, String text, int row)
    {
      GridBagConstraints g = UiUtility.getFillHorizontal(0, row);
      g.gridwidth = 4;
      c.add(new JLabel("<html><h2><b>" + text + "</h2></html>"), g);
    }

    private void subtitle(Container c, String text, int row)
    {
      GridBagConstraints g = UiUtility.getFillHorizontal(0, row);
      g.gridwidth = 4;
      c.add(new JLabel("<html><h3><b>" + text + "</h3></html>"), g);
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

    public void close()
    {
      setVisible(false);
      dispose();
    }
  }
}
