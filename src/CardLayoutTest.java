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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardLayoutTest {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("CardLayout Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create CardLayout and main panel
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // First panel (Menu)
        JPanel menuPanel = new JPanel();
        JButton toGameButton = new JButton("Start Game");
        menuPanel.add(new JLabel("Main Menu"));
        menuPanel.add(toGameButton);

        // Second panel (Game)
        JPanel gamePanel = new JPanel();
        JButton toEndButton = new JButton("End Game");
        gamePanel.add(new JLabel("Game Screen"));
        gamePanel.add(toEndButton);

        // Third panel (End)
        JPanel endPanel = new JPanel();
        JButton toMenuButton = new JButton("Back to Menu");
        endPanel.add(new JLabel("Game Over"));
        endPanel.add(toMenuButton);

        // Add panels to cardPanel with names
        cardPanel.add(menuPanel, "MENU");
        cardPanel.add(gamePanel, "GAME");
        cardPanel.add(endPanel, "END");

        // Add card panel to frame
        frame.add(cardPanel);
        frame.setVisible(true);

        // Button actions to switch cards
        toGameButton.addActionListener(e -> cardLayout.show(cardPanel, "GAME"));
        toEndButton.addActionListener(e -> cardLayout.show(cardPanel, "END"));
        toMenuButton.addActionListener(e -> cardLayout.show(cardPanel, "MENU"));
    }
}
