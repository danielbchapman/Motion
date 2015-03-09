package com.danielbchapman.video;

import java.net.ServerSocket;
import java.net.Socket;

class NetworkServer {

  ServerSocket server;
  Socket[] clients = new Socket[10];
  boolean running = true;
  int FILE_SIZE = 0; 
 
 public void send(byte[] bytes){
   byte[] data = StreamProtocol.encodeBytes(bytes);
   for(int i = 0; i < clients.length; i++){
     if(clients[i] != null){
       try {
         //Write data
         clients[i].getOutputStream().write(data);
         clients[i].getOutputStream().flush();
       } catch (Throwable t){
         try {
           clients[i].close();
         } catch (Throwable t2){
           t2.printStackTrace();
         }
       }
     }
   }  
 }
 
 public  void startServer(){
   new Thread(){
     public void run(){

       try {
         server = new ServerSocket(20123);
         
       } catch (Throwable t) {
         t.printStackTrace();
       }
               
       while(running && server != null){
         Thread.yield();
         System.out.println("Waiting for client....");
         try {
           Socket client = server.accept();
           for(int i = 0; i < clients.length; i++){
             if(clients[i] == null){
               clients[i] = client;
               break;
             }
           }
         } catch (Throwable t) {
           t.printStackTrace(); 
         }
       }
     }
   }.start();  }
}