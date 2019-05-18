package com.company;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class testClient {
  public static void main(String[] args) {
    try {
      Socket newSocket = new Socket("127.0.0.1", 4242);
      InputStreamReader stream = new InputStreamReader(newSocket.getInputStream());
      BufferedReader reader = new BufferedReader(stream);
      String messege = reader.readLine();
      System.out.println("Today You should" + messege);
      reader.close();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
