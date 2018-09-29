package com.apress.gerber.spp_controller;

import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class esp8266_communication {
    private static final String IP = "192.168.4.1";
    private static final int PORT = 333;
    private Socket client = null;
    private PrintStream out = null;
    private int datanumbertosend=0;


    private DataInputStream in=null;
    private  Boolean stop=false;
    private byte[] receice;
    public Handler handler=null;


    public  esp8266_communication(Handler hand){
        this.handler=hand;
        new Thread(new Runnable() {
           @Override
           public void run() {
           try {
          client = new Socket(IP, PORT);
          client.setSoTimeout(100);
          out = new PrintStream(client.getOutputStream());


          } catch (IOException e) {
             e.printStackTrace();
          }
          }
          }).start();
        new receiveThread().start();
    }

    public void startreceive(){
        new receiveThread().start();
    }


    public void send(final String str){
        if(datanumbertosend==0){
            datanumbertosend++;
        new sendThread(str).start();}
    }

    private class sendThread extends  Thread{
        private String str;
        public sendThread(String stp){
            str=stp;
        }
        @Override
        public void run() {
                out.print(str);
                out.flush();
                datanumbertosend--;
        }
    }

    private class receiveThread extends  Thread{
        @Override
        public void run() {
            while (!stop)
            {

               try {
                   while(out==null){;}
                   in = new DataInputStream(client.getInputStream());
                   receice = new byte[50];
                   in.read(receice);
                   //client.close();
                  // in.close();
                   //server.close();

                Message message = new Message();
                message.what = 1;
                message.obj = new String(receice);
                handler.sendMessage(message);

               }catch (IOException e)
               {
                   e.printStackTrace();
               }

            }
        }
    }


    public void close()
    {
          if (client != null) {
              try{
            out.close();
            client.close();}catch (IOException e)
              {
                  e.printStackTrace();
              }
          }
          stop=true;
    }
}
