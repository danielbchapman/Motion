package com.danielbchapman.motion;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.tools.Tool;

import com.danielbchapman.application.Application;
import com.danielbchapman.application.models.ItemTableView;
import com.danielbchapman.fx.builders.Fx;
import com.danielbchapman.groups.Group;
import com.danielbchapman.groups.Groups;
import com.danielbchapman.groups.Item;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.physics.toxiclibs.Main;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.val;

public class UI extends Application
{
	public MotionEngine motion;
	
	public MotionTools toolBar;
	
	Group test = Groups.getGroup("TestCues");
	
  public static void main(String... args)
  {
    launch(UI.class);
  }

  public void register(MotionEngine motion)
  {
  	this.motion = motion;
  }
  
  @Override
  public void start(Stage stage) throws Exception
  {
//  	Platform.setImplicitExit(false);
  	//Main container
    VBox workspace = new VBox();
  	MotionTools tools = new MotionTools();
  	MotionStatus status = new MotionStatus();
  	MotionTabs tabs = new MotionTabs();
    MotionCueList cues = new MotionCueList();
    MotionCueList2 cues2 = new MotionCueList2();
  	HBox controls = new HBox();
  	
  	controls.getChildren().add(tabs);
    VBox cueList = new VBox();
    cueList.setMinWidth(300);
    cueList.getChildren().add(new Label("Cue List"));
    
    
    ArrayList<MotionCue> data = new ArrayList<MotionCue>(); 
    
//    ScrollPane cueScroll = new ScrollPane(cueList);
//    cueScroll.setMinWidth(300);
//    cueScroll.setContent(cueList);
    
    
    for(int i = 1; i < 11; i++)
    {
      ContentCue cue = new ContentCue();
      cue.setLabel("Test Cue " + i);
      cue.setCueNumber(new BigDecimal(i));      
      cueList.getChildren().add(cue);
      
      Item x = new Item();
      x.setValue("label", "Test Cue " + i);
      x.setValue("description", "TMrrrrpppp....");
      
      final int ii = i;
      data.add(
          new MotionCue()
          {{
              setId(""+ii);
              setLabel(""+ ii);
              setDescription("Test Cue " + ii);  
          }});
      test.put(x);
    }
    
    TableView<MotionCue> table = Fx.table(MotionCue.class);
    val list = FXCollections.observableArrayList(data);
    table.setItems(list);
    
    
    
//    ItemTableView itb = ItemTableView
//        .builder()
//        .columns("id", "label", "description")
//        .editable(true)
//        .edi
//        .items(test.sort("id"))
//        .build();
    
    Button printIt = new Button("Print Data");
    
    printIt.setOnAction(x -> 
    {
      table.getItems().forEach(System.out::println);
    });
    
    workspace.getChildren()
      .addAll(
          tools,
          cues,
          cues2,
          //itb,
          table,
          controls,
          printIt,
          status
          );
    
    stage.setMinWidth(800);
    stage.setMinHeight(600);
    stage.setScene(new Scene(workspace));
    stage.setTitle(MessageUtility.GLOBAL.get("motion-version"));
    stage.show();
    
  }

	@Override
	public MenuBar getMenuBar()
	{
		return new MenuBar();
	}

	@SuppressWarnings("Engine is only partially initialized")
	@Override
	protected void initialize()
	{
		motion = Main.ENGINE; //static pointer

	}

	@SuppressWarnings("Shutdown is not implemented at all.")
	@Override
	public void shutdown()
	{
		motion = null;
		System.out.println("[NOT IMPLEMENTED] shutdown NOT CALLED ");
	}

	@Override
	protected File getPropertiesFile()
	{
		return new File("settings.properties");
	}

	@SuppressWarnings("ADD EXCEPTION LOGGING HERE IN THE FRAMEWORK")
	@Override
	protected void logException(Throwable t)
	{
		t.printStackTrace();
	}

	@SuppressWarnings("Implement the CSS if we go this route!")
	@Override
	protected String getStyleSheets()
	{
		return null;
	}

	@Override
	public Scene initializeScene()
	{
		//TODO Auto Generated Sub
		throw new RuntimeException("Not Implemented...");
		
	}
  
  
}

