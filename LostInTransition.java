import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class LostInTransition extends JFrame {

    private JTextArea message;
    private JTextField inputField;
    private JButton sendButton;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private JFrame window;
    private JLabel promptLabel;

    public LostInTransition() {
        window = new JFrame("Lost In Transition");
        window.setSize(900, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);

        promptLabel = new JLabel(" ", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        promptLabel.setForeground(new Color(30, 30, 30));
        promptLabel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        promptLabel.setOpaque(true);
        promptLabel.setBackground(new Color(245, 245, 245));
        window.add(promptLabel, BorderLayout.NORTH);

        message = new JTextArea();
        message.setEditable(false);
        message.setFont(mainFont);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        message.setBackground(Color.WHITE);
        message.setForeground(new Color(30, 30, 30));
        window.add(new JScrollPane(message), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(6, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        inputField = new JTextField();
        inputField.setFont(mainFont);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sendButton.setBackground(new Color(50, 50, 50));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setPreferredSize(new Dimension(80, 36));

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        window.add(bottomPanel, BorderLayout.SOUTH);

        ActionListener send = e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                toServer.println(text);
                inputField.setText("");
            }
        };
        sendButton.addActionListener(send);
        inputField.addActionListener(send);

        window.setVisible(true);
        new Thread(() -> connectAndListen()).start();
    }

    private void connectAndListen() {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.background", Color.WHITE);

        try {
            int playerCount = 0;

            int choice = JOptionPane.showConfirmDialog(null, "Are you the first player?", "Game Setup", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                while (playerCount < 2 || playerCount % 2 != 0) {
                    String input = JOptionPane.showInputDialog(null, "How many players will join? (even number, min 2)");
                    if (input == null) continue;
                    try {
                        playerCount = Integer.parseInt(input.trim());
                        if (playerCount < 2 || playerCount % 2 != 0) {
                            JOptionPane.showMessageDialog(null, "Must be an even number (2, 4, 6...).");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Enter a number.");
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "Connecting to server...");
            Socket connection = new Socket("localhost", 1728);
            toServer = new PrintWriter(connection.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            if (playerCount > 0) {
                toServer.println(playerCount);
            }

            JOptionPane.showMessageDialog(null, "Connected - waiting for game to start");

            String line;
            while ((line = fromServer.readLine()) != null) {
                final String msg = line;

                if (msg.startsWith("PLAYER:")) {
                    int num = Integer.parseInt(msg.replace("PLAYER:", "").trim());
                    SwingUtilities.invokeLater(() -> window.setTitle("Lost In Transition — Player " + num));
                    continue;
                }

                if (msg.startsWith("Round")) {
                    SwingUtilities.invokeLater(() -> promptLabel.setText(msg));
                }

                SwingUtilities.invokeLater(() -> message.append(msg + "\n"));
            }

            JOptionPane.showMessageDialog(null, "Game over.");
            connection.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Was Lost: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LostInTransition();
    }
}