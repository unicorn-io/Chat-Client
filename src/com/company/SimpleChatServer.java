package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class SimpleChatServer {

  private ArrayList clientOutputStream;

  public static void main(String[] args) {
    new SimpleChatServer().go();
  }

  public class ClientHandler implements Runnable {
    BufferedReader reader;
    Socket sock;

    private ClientHandler(Socket clientSocket) {
      sock = clientSocket;
      try {
        InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
        reader = new BufferedReader(isReader);
      } catch(IOException ex) {
        ex.printStackTrace();
      }
    }

    public void run() {
      String messege;
      try {
        while ((messege = reader.readLine()) != null) {
          System.out.println("read " + messege);
          tellEveryone(messege);
        }
      } catch(IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  private void go() {
    clientOutputStream = new ArrayList();
    try {
      ServerSocket serverSock = new ServerSocket(5000);

      while (true) {
        Socket clientSocket = serverSock.accept();
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
        clientOutputStream.add(writer);

        Thread t = new Thread(new ClientHandler(clientSocket));
        t.start();
        System.out.println("got a connection");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void tellEveryone(String messege) {
    Iterator it = clientOutputStream.iterator();
    while (it.hasNext()) {
      try{
        PrintWriter writer = (PrintWriter) it.next();
        writer.println(messege);
        writer.flush();
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

}
