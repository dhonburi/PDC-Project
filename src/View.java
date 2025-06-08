
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class View extends JFrame implements ModelListener {

    private final int ROWS = 6;
    private final int COLS = 5;
    private JTextField inputField;
    public JButton submitButton;
    private JPanel gridPanel;

    public View() {
        // Basic JFrame setup
        setTitle("Wordle");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen

        // Top panel for input
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(5);
        submitButton = new JButton("Submit");
        inputPanel.add(new JLabel("Guess:"));
        inputPanel.add(inputField);
        inputPanel.add(submitButton);
        add(inputPanel, BorderLayout.NORTH);

        // Center panel for tile grid
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel = new JPanel(new GridLayout(ROWS, COLS, 5, 5));

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JLabel tile = new JLabel("", SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setFont(new Font("Arial", Font.BOLD, 24));
                tile.setBackground(Color.DARK_GRAY);
                tile.setForeground(Color.WHITE);
                tile.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                gridPanel.add(tile);
            }
        }
        
        outerPanel.add(gridPanel, BorderLayout.CENTER);
        add(outerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void addSubmitListener(java.awt.event.ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    @Override
    public void onModelChanged(String newData) {
        
    }
}
