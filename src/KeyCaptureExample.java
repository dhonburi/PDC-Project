/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dhonl
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeyCaptureExample {
    private StringBuilder typedText = new StringBuilder();

    public KeyCaptureExample() {
        JFrame frame = new JFrame("Key Capture");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JLabel displayLabel = new JLabel("", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputField.setHorizontalAlignment(SwingConstants.CENTER);

        // Always keep focus
        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                inputField.requestFocusInWindow();
            }
        });

        // Key handling
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (Character.isLetter(e.getKeyChar())) {
                    // Add uppercase letter
                    if (typedText.length() < 5) {
                        typedText.append(Character.toUpperCase(e.getKeyChar()));
                    }
                } else if (code == KeyEvent.VK_BACK_SPACE) {
                    // Remove last letter
                    if (typedText.length() > 0) {
                        typedText.deleteCharAt(typedText.length() - 1);
                    }
                } else if (code == KeyEvent.VK_ENTER) {
                    // Clear all
                    typedText.setLength(0);
                }

                displayLabel.setText(typedText.toString());
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(inputField, BorderLayout.SOUTH);
        frame.add(displayLabel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(KeyCaptureExample::new);
    }
}
