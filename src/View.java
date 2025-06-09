
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

    Color backroundCol = new Color(18, 18, 19);
    Color borderCol = new Color(58, 58, 60);
    Color keysCol = new Color(129, 131, 132);

    public View() {
        // Basic JFrame setup
        setTitle("Wordle");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen

        // Top panel for input (temporary while keyboards not implemented)
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(5);
        submitButton = new JButton("Submit");
        inputPanel.add(new JLabel("Guess:"));
        inputPanel.add(inputField);
        inputPanel.add(submitButton);
        add(inputPanel, BorderLayout.NORTH);

        // Center panel for tile grid
        gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 1000, 30, 1000));
        gridPanel.setBackground(backroundCol);
        gridPanel.setPreferredSize(new Dimension(280, 335));

        for (int i = 0; i < ROWS; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            rowPanel.setBackground(backroundCol);
            for (int j = 0; j < COLS; j++) {
                JLabel tile = new JLabel("", SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setFont(new Font("", Font.BOLD, 24));
                tile.setPreferredSize(new Dimension(50, 50));
                tile.setBackground(backroundCol);
                tile.setForeground(Color.WHITE);
                tile.setBorder(BorderFactory.createLineBorder(borderCol));
                rowPanel.add(tile);
            }
            gridPanel.add(rowPanel);
        }

        add(gridPanel, BorderLayout.CENTER);

        // Bottom Panel for Keyboard 
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridLayout(3, 1, 5, 5));
        keyboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        keyboardPanel.setBackground(backroundCol);

        String[] rows = {
            "QWERTYUIOP",
            "ASDFGHJKL",
            "+ZXCVBNM-"
        };

        for (String row : rows) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 1));
            rowPanel.setBackground(backroundCol);
            for (char c : row.toCharArray()) {
                JButton key;
                switch (c) {
                    case '+':
                        key = new JButton("ENTER");
                        key.setFont(new Font("Helvetica", Font.BOLD, 16));
                        key.setPreferredSize(new Dimension(70, 60));
                        break;
                    case '-':
                        key = new JButton("<");
                        key.setFont(new Font("Helvetica", Font.BOLD, 24));
                        key.setPreferredSize(new Dimension(70, 60));
                        break;
                    default:
                        key = new JButton(String.valueOf(c));
                        key.setFont(new Font("Helvetica", Font.BOLD, 22));
                        key.setPreferredSize(new Dimension(45, 60));
                        break;
                }
                key.setFocusable(false);
                key.setBackground(keysCol);
                key.setForeground(Color.WHITE);
                key.setBorder(BorderFactory.createLineBorder(borderCol));
                rowPanel.add(key);
            }
            keyboardPanel.add(rowPanel);
        }

        add(keyboardPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void addSubmitListener(java.awt.event.ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    @Override
    public void onModelChanged(String newData) {

    }
}
