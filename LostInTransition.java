import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class LostInTransition extends JFrame{


  private JTextArea message;
  private JTextField inputField;
  private JButton sendButton;
  private PrintWriter toServer;
  private BufferedReader fromServer;
  private JFrame window;

  public LostInTransition(){
    window = new JFrame("Lost In Transition");
    window.setSize(900,600);
    window.setLocationRelativeTo(null);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setVisible(true);
    window.setLayout(new BorderLayout());

    message = new JTextArea();
    window.add(new JScrollPane(message), BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    inputField = new JTextField();
    sendButton = new JButton("Send");
    bottomPanel.add(inputField, BorderLayout.CENTER);
    bottomPanel.add(sendButton, BorderLayout.EAST);
    window.add(bottomPanel, BorderLayout.SOUTH);

    ActionListener send = e -> {
      String text = inputField.getText().trim();
      if(!text.isEmpty()){
        toServer.println(text);
      }
    };
    sendButton.addActionListener(send);
    inputField.addActionListener(send);

    new Thread(() -> connectAndListen()).start();
  }

  private void connectAndListen(){
    try{
      JOptionPane.showMessageDialog(window, "Connecting to server...");
      Socket connection = new Socket("localhost", 1728);
      toServer = new PrintWriter(connection.getOutputStream(),true);
      fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      JOptionPane.showMessageDialog(window, "Connected - waiting for game to start");

      String line;
      while ((line = fromServer.readLine()) != null){
        JOptionPane.showMessageDialog(window, line);
        message.append(line + "\n");
      }

      JOptionPane.showMessageDialog(window, "Game over.");
      connection.close();

    } catch(Exception e){
      JOptionPane.showMessageDialog(window, "Connection Was Lost");
    }
  }
  public static void main(String[] args) {
        new LostInTransition();
  }

}
