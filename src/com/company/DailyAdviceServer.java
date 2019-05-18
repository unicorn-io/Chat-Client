package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DailyAdviceServer {

  String[] adviceList = {"Take smaller bites","Go for the tight jeans. No they do not make you look fat", "One word: Inapropriate",
                            "Just for Today Be honest", "Tell Your boss what You think"};
  public void go() {
    try{
      ServerSocket socket = new ServerSocket(4242);


      while(true) {
        Socket sock = socket.accept();

        PrintWriter writer = new PrintWriter(sock.getOutputStream());
        String advice = getAdvice();
        writer.println(advice);
        writer.close();
        System.out.println(advice);
      }

    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  private String getAdvice() {
    int random = (int) (Math.random() * adviceList.length);
    return adviceList[random];
  }

  public static void main(String[] args) {
    DailyAdviceServer testClient = new DailyAdviceServer();
    testClient.go();
  }
}
