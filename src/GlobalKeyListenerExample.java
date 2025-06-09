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
import java.awt.event.KeyEvent;

public class GlobalKeyListenerExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Global Key Listener");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JLabel outputLabel = new JLabel("Press a letter key...", SwingConstants.CENTER);
        frame.add(outputLabel);

        // Add global key listener
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            if (event.getID() == KeyEvent.KEY_PRESSED) {
                char c = event.getKeyChar();

                if (Character.isLetter(c)) {
                    System.out.println("You pressed: " + c);
                    outputLabel.setText("Letter: " + Character.toUpperCase(c));
                }
            }
            return false; // Allow event to propagate
        });

        frame.setVisible(true);
    }
}
