package com.danielbchapman.video;

import java.io.IOException;
import java.net.Socket;

public class NetworkClientThread implements Runnable 
{
  Object lock;
  boolean live = true;
  String host = "localhost";
  int port = 20123;
  int size;
  int[] cache;
  public NetworkClientThread(int size, Object lock, int[] cache) {
    this.size = size * 3; //packet data 
    this.lock = lock == null ? new Object() : lock;//use an internal lock else use the external lock
    this.cache = cache;
  }
  @Override
  public void run() {
    
    try
    {
      Socket socket = new Socket(host, port);
      long time = 0;
      long lastTime = System.currentTimeMillis();
      byte[] img = new byte[size + 16];
      while((time - lastTime) < 60000){        
        int remainder = socket.getInputStream().read(img, 0, size + 16); //packet size in bytes
        while(remainder < size){
          Thread.yield();
//          try {
//            Thread.sleep(100);
//          } catch (Throwable t){
//          }
//          Thread.yield();
          remainder += socket.getInputStream().read(img, remainder, size - remainder + 16);
          //System.out.println("Waiting for input: remainder is -> " + (remainder + 16));
        }
        
        byte[] data = StreamProtocol.decodeBytes(img);
        
        if(data == null) {
          throw new RuntimeException("Data was null, packet error!!");
        }
        
        if(data.length != (img.length - 16)){
          String x = "data " + data.length + " != " + img.length + " size variable is " + size;
          throw new RuntimeException("Data mismatch " + x);
        }
        
        synchronized (lock) {
          for(int i = 0, j = 0; i < cache.length; i++, j+=3){
            int x = StreamProtocol.decode((byte) 0x00, data[j], data[j +1], data[j + 2]);
            cache[i] = x;
          }
        }
        
        time = System.currentTimeMillis();
        //System.out.println("byte[] size is: " + img.length + " >> " + (time - lastTime) + "ns");
        Thread.yield();
      }      
      
      socket.close();
      System.exit(0);
    } 
    catch (IOException io)
    {
      io.printStackTrace();
      live = false;
      System.out.println("Error Client Read: io");
    }

  }

}