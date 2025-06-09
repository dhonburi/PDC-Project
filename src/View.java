
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
    public JTextField inputField;
    public JButton submitButton;
    private JLabel[][] tiles;
    public String input;
    private int attempts;
    private HashMap<Character, JButton> keyButtons = new HashMap<>();
    private HashMap<Character, Integer> keyColours = new HashMap<>();

    Color backroundCol = new Color(18, 18, 19);
    Color borderCol = new Color(58, 58, 60);
    Color keysCol = new Color(129, 131, 132);
    Color green = new Color(106, 170, 100);
    Color yellow = new Color(201, 180, 88);
    Color gray = new Color(58, 58, 60);

    public View() {

        // Basic JFrame setup
        setTitle("Wordle");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        attempts = 0;

        // Top panel for input (temporary while keyboards not implemented)
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(5);
        inputField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                // If not a letter, consume the event (ignore input)
                if (!Character.isLetter(c)) {
                    e.consume();
                    return;
                }

                // If length is already 5, consume the event (ignore input)
                if (inputField.getText().length() >= 5) {
                    e.consume();
                }
            }
        });
        submitButton = new JButton("Submit");
        inputPanel.add(new JLabel("Guess:"));

        inputPanel.add(inputField);
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel for tile grid
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 1000, 30, 1000));
        gridPanel.setBackground(backroundCol);
        gridPanel.setPreferredSize(new Dimension(280, 335));
        tiles = new JLabel[ROWS][COLS];

        inputField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateLabel();
            }

            public void removeUpdate(DocumentEvent e) {
                updateLabel();
            }

            public void changedUpdate(DocumentEvent e) {
                updateLabel(); // rarely used for plain text
            }

            private void updateLabel() {
                input = inputField.getText();
                for (int i = 0; i < input.length(); i++) {
                    setTile(attempts, i, input.charAt(i));
                }
                for (int i = 4; i >= input.length(); i--) {
                    setTile(attempts, i, ' ');
                }
            }
        });

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
                tiles[i][j] = tile;
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
                        keyButtons.put(c, key);
                        keyColours.put(c, 0);
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

    public void setTile(int row, int col, Color background) {
        tiles[row][col].setBackground(background);
    }

    public void setTile(int row, int col, char letter) {
        tiles[row][col].setText(String.valueOf(letter));
    }

    public void feedbackRow(String feedback, int attempt) {
        for (int i = 0; i < 5; i++) {
            char c = Character.toUpperCase(input.charAt(i));
            switch (feedback.charAt(i)) {
                case 'V':
                    setTile(attempt - 1, i, green);
                    setKeyColor(input.charAt(i), green);
                    keyColours.put(c, 3);
                    break;
                case '?':
                    setTile(attempt - 1, i, yellow);
                    if (keyColours.get(c) <= 1) { // default 0, grey 1, yellow 2, green 3.
                        setKeyColor(input.charAt(i), yellow);
                        keyColours.put(c, 2);
                    }
                    break;
                case 'X':
                    setTile(attempt - 1, i, gray);
                    if (keyColours.get(c) == 0) {
                        setKeyColor(input.charAt(i), gray);
                        keyColours.put(c, 1);
                    }
                    break;
                default:
                    setTile(attempt - 1, i, Color.RED); //only if error
                    break;
            }
        }
        inputField.setText("");
    }

    public void setKeyColor(char c, Color background) {
        JButton key = keyButtons.get(Character.toUpperCase(c));
        if (key != null) {
            key.setBackground(background);
        }
    }

    public void addSubmitListener(java.awt.event.ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    @Override
    public void onModelChanged(String feedback, int attempt) {
        attempts = attempt;
        feedbackRow(feedback, attempt);
    }
}
