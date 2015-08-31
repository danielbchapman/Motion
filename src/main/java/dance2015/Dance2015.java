package dance2015;

import processing.core.PApplet;
import processing.core.PVector;

import com.danielbchapman.artwork.Cubes;
import com.danielbchapman.artwork.Letter;
import com.danielbchapman.artwork.Line;
import com.danielbchapman.debug.Debug;
import com.danielbchapman.physics.vertlet.Point3D;

import dance2015.DrawUtil.FrameCounter;

public class Dance2015 extends PApplet {
  private static final long serialVersionUID = 1L;
  FrameCounter counter = new FrameCounter();
  public boolean debug = false;
  

  //Model A variables
  boolean fullscreen = false;
  float rotation = 0.0f;
  int rows = 80;
  int cols = 60;
  Point3D[] points = new Point3D[rows * cols];
  Letter[] letters = new Letter[rows * cols];
  int lastTime = 0;
  float lastDTime = 0.0f;
  int start;

  
	public void setup() {
	  Debug.debug = true;
	  
	  //size(1920, 1080, P3D);
	  size(800, 600, P3D);
	  fullscreen = true;
	  counter = DrawUtil.getCounter();
	  setupA();
	}

	@Override
	public boolean sketchFullScreen()
	{
	  return false;
	}
	
	public void draw() {
	  background(0);
	  
	  pushMatrix();
	  draw3D();
	  popMatrix();
	  
	  pushMatrix();
	  draw2D();
	  popMatrix();
	  
	  pushMatrix();
	  printRate();
	  popMatrix();
	}
	
	public void draw3D()
	{
	  drawA();
	}
	
	public void draw2D()
	{
	  pushMatrix();
	  
	  printRate();
	  popMatrix();
	}
	
	void setupA()
	{
	  frameRate(60);
	  smooth();
	  int row = -1;
	  for(int i = 0; i < rows * cols; i++){
	    if(i % rows == 0)
	      row++;
	      
	    points[i] = new Point3D((i % rows) * 10, row * 10, (i % 40) + 20);
	    letters[i] = new Letter((char) random(20, 96) , 16);
	    Debug.outln(points[i].position);
	  }
	  
	  lastTime = millis();
	  start = millis();
	  //blurWind = loadShader("blur.glsl");
	  background(0);
	}
	
	Line lineRender = new Line();
	Cubes cubes = new Cubes(2);
	void drawA()
	{
	  //filter(blurWind);
	  background(0, 40);
	  translate(400, 0, -200);
	  rotation += 0.01;
	  rotateY(rotation);
	  //camera(xRotation+= 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);  
	  //Limiter
	  if(frameCount > 20000){
	    return;
	  }
	 	  //Step
	  int time = millis();
	  float dTime = ((float)time - (float) lastTime)/ 1000.0f;
	  
	  stroke(255);
	  strokeWeight(1);
	  PVector gravity = new PVector(0f, 9.8f, 0f);
	  PVector wind = new PVector(0.05f, 0f, 0f);
	  Point3D p;
	  PVector pos;
	  for(int i = 0; i < points.length; i++){
	    p = points[i];
	    pos = points[i].position;  
	    //Forces
	    p.addGravity(gravity);
	    p.addForce(wind);
	    p.addForce(new PVector(random(0), random(0), random(-10.0f, 10.0f)));
	    
	    p.drag();
	    p.tcv(dTime, lastDTime);
	    p.collision();
	    
	    //Draw
	    //letters[i].draw(g, p);
	    point(pos.x, pos.y, pos.z);
	    //cubes.draw(g, p);
	    //lineRender.drawGrid(g, points, rows, cols, 40);
	    
	    //filter(blurWind); 
	    
	    if(i == 0){
	      //Debug.outln(frameCount);
	      //Debug.outln(dTime);
	      //p.debug();
	      Debug.outln((start - time) + "\t" + p.posData());
	    }
	  }  
	  
	  //Update times
	  lastTime = time;
	  lastDTime = dTime;
	    
	}
	
	public void printRate()
	{ 
	  color(255);
	  text("Frame Rate: " + frameRate, 20, 50);
	}
	public static void main(String _args[]) {
		PApplet.main(new String[] { dance2015.Dance2015.class.getName() });
	}
}
