package com.danielbchapman.physics.toxiclibs;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.danielbchapman.utility.UiUtility;

public class OscDialog extends JFrame
{
  MotionEngine engine;
  JTextField ip = new JTextField("127.0.0.1");
  JTextField port = new JTextField("44321");
  JButton connect = new JButton("Connect");
  JButton cancel = new JButton("Cancel");
  public OscDialog(MotionEngine engine)
  {
    this.engine = engine;
    
    if(engine.liveDrawPort != null)
      port.setText(engine.liveDrawPort.toString());
    
    if(engine.liveDrawUrl != null)
      ip.setText(engine.liveDrawUrl);
    
    JPanel buttons = new JPanel();
    buttons.setLayout(new GridBagLayout());
    buttons.add(cancel, UiUtility.getNoFill(1, 0));
    buttons.add(connect, UiUtility.getNoFill(2, 0));
    
    setTitle("Select Motion Server");
    setPreferredSize(new Dimension(200, 200));
    setSize(new Dimension(200, 200));
    setLayout(new GridBagLayout());
    
    add(new JLabel("Motion Server IP"), UiUtility.getFillHorizontal(0, 0));
    add(ip, UiUtility.getFillHorizontal(0, 1));
    add(new JLabel("Port (default: 44321)"), UiUtility.getFillHorizontal(0, 2));
    add(port, UiUtility.getFillHorizontal(0, 3));    
    add(buttons, UiUtility.getNoFill(0, 4));
    
    cancel.addActionListener(a -> {
      OscDialog.this.setVisible(false);
      OscDialog.this.dispose();
    });
    
    connect.addActionListener(a -> {
      try
      {
        engine.liveDrawPort = Integer.parseInt(port.getText());
        engine.liveDrawUrl = ip.getText();
        
        engine.enableLiveDraw(); 
        OscDialog.this.setVisible(false);
        OscDialog.this.dispose();
      }
      catch(Throwable t)
      {
        JOptionPane.showMessageDialog(OscDialog.this, "Invalid Values", t.getMessage(), JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}
