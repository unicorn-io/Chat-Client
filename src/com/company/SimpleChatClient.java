package com.company;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class SimpleChatClient {
  private JTextArea incoming;
  private JTextField outgoing;
  private PrintWriter writer;
  private BufferedReader reader;

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ex) { ex.printStackTrace(); }
    SimpleChatClient client = new SimpleChatClient();
    client.go();
  }

  private void go() {
    JFrame frame = new JFrame("Simple Chat Client");
    JPanel mainPanel = new JPanel();
    outgoing = new JTextField(20);
    JButton sendButton = new JButton("Send");
    sendButton.addActionListener(new SendButtonListener());
    incoming = new JTextArea(15, 50);
    incoming.setLineWrap(true);
    incoming.setWrapStyleWord(true);
    incoming.setEditable(false);
    JScrollPane qScroller = new JScrollPane(incoming);
    qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    mainPanel.add(qScroller);
    mainPanel.add(outgoing);
    mainPanel.add(sendButton);
    frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
    setupNetworking();
    frame.setSize(438, 500);
    frame.setVisible(true);

    Thread readerThread = new Thread(new IncomingReader());
    readerThread.start();
  }

  private void setupNetworking() {
    try {
      Socket sock = new Socket("127.0.0.1", 5000);
      writer = new PrintWriter(sock.getOutputStream());
      InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
      reader = new BufferedReader(streamReader);
      System.out.println("Networking Established");
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  public class IncomingReader implements Runnable {
    public void run() {
      String messege;
      try {
        while ((messege = reader.readLine()) != null) {
          System.out.println("read " + messege);
          incoming.append(messege + "\n");
        }
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public class SendButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent ev) {
      try {
        writer.println(outgoing.getText());
        writer.flush();

      } catch(Exception ex) {
        ex.printStackTrace();
      }
      outgoing.setText("");
      outgoing.requestFocus();
    }
  }
}
