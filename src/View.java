
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
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
    private JLabel[][] tiles;
    public String input;
    private int attempts;
    private HashMap<Character, JButton> keyButtons = new HashMap<>();
    private HashMap<Character, Integer> keyColours = new HashMap<>();
    private String currentWord;
    private JButton tutorialButton;
    private JButton statsButton;
    private JButton closeButton;
    private JButton closeButton2;

    Color backroundCol = new Color(18, 18, 19);
    Color borderCol = new Color(58, 58, 60);
    Color keysCol = new Color(129, 131, 132);
    Color green = new Color(106, 170, 100);
    Color yellow = new Color(201, 180, 88);
    Color gray = new Color(58, 58, 60);

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public View() {

        // Basic JFrame setup
        setTitle("Wordle");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        attempts = 0;

        // Create CardLayout and main panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // First panel (Game)
        JPanel gamePanel = new JPanel(new BorderLayout());
        // Top panel for How-to-Play & Stats
        JPanel topPannel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        topPannel.setBackground(backroundCol);
        topPannel.setBorder(BorderFactory.createLineBorder(borderCol));

        tutorialButton = new JButton("How to Play");
        tutorialButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        tutorialButton.setFocusable(false);
        tutorialButton.setBackground(backroundCol);
        tutorialButton.setForeground(Color.WHITE);
        tutorialButton.setPreferredSize(new Dimension(100, 30));
        tutorialButton.setBorder(new MatteBorder(0, 1, 0, 1, borderCol));
        topPannel.add(tutorialButton);

        statsButton = new JButton("Statistics");
        statsButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        statsButton.setFocusable(false);
        statsButton.setBackground(backroundCol);
        statsButton.setForeground(Color.WHITE);
        statsButton.setPreferredSize(new Dimension(100, 30));
        statsButton.setBorder(new MatteBorder(0, 1, 0, 1, borderCol));
        topPannel.add(statsButton);

        gamePanel.add(topPannel, BorderLayout.NORTH);

        // Center panel for tile grid
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 1000, 30, 1000));
        gridPanel.setBackground(backroundCol);
        gridPanel.setPreferredSize(new Dimension(280, 335));
        tiles = new JLabel[ROWS][COLS];

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

        gamePanel.add(gridPanel, BorderLayout.CENTER);

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
                        keyButtons.put(c, key);
                        break;
                    case '-':
                        key = new JButton("<");
                        key.setFont(new Font("Helvetica", Font.BOLD, 24));
                        key.setPreferredSize(new Dimension(70, 60));
                        keyButtons.put(c, key);
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

        gamePanel.add(keyboardPanel, BorderLayout.SOUTH);

        // Second panel (Tutorial)
        JPanel tutorialPanel = new JPanel(new BorderLayout());

        // Top panel for How-to-Play
        JPanel topPannel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topPannel2.setBackground(backroundCol);
        topPannel2.setBorder(BorderFactory.createLineBorder(borderCol));

        closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        closeButton.setFocusable(false);
        closeButton.setBackground(backroundCol);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorderPainted(false);
        topPannel2.add(closeButton);
        tutorialPanel.add(topPannel2, BorderLayout.NORTH);

        // Third panel (Stats)
        JPanel statsPanel = new JPanel(new BorderLayout());

        // Top panel for Stats
        JPanel topPannel3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topPannel3.setBackground(backroundCol);
        topPannel3.setBorder(BorderFactory.createLineBorder(borderCol));

        closeButton2 = new JButton("X");
        closeButton2.setPreferredSize(new Dimension(50, 50));
        closeButton2.setFont(new Font("Helvetica", Font.BOLD, 16));
        closeButton2.setFocusable(false);
        closeButton2.setBackground(backroundCol);
        closeButton2.setForeground(Color.WHITE);
        closeButton2.setBorderPainted(false);
        topPannel3.add(closeButton2);
        statsPanel.add(topPannel3, BorderLayout.NORTH);

        //Add panels to cardPanel
        cardPanel.add(tutorialPanel, "TUTORIAL");
        cardPanel.add(gamePanel, "GAME");
        cardPanel.add(statsPanel, "STATS");
        add(cardPanel);

        setVisible(true);
    }

    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }

    public void updateTileText() {
        input = currentWord;
        for (int i = 0; i < input.length(); i++) {
            setTile(attempts, i, input.charAt(i));
        }
        for (int i = 4; i >= input.length(); i--) {
            setTile(attempts, i, ' ');
        }
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
        currentWord = "";
    }

    public void setKeyColor(char c, Color background) {
        JButton key = keyButtons.get(Character.toUpperCase(c));
        if (key != null) {
            key.setBackground(background);
        }
    }

    public void registerKeyListener(KeyListener listener) {
        this.addKeyListener(listener);
    }

    public void registerKeyButtonListener(char c, ActionListener listener) {
        JButton key = keyButtons.get(Character.toUpperCase(c));
        if (key != null) {
            key.addActionListener(listener);
        }
    }

    public void registerTutorialButtonListener(ActionListener listener) {
        JButton key = tutorialButton;
        key.addActionListener(listener);
    }

    public void registerCloseButtonListener(ActionListener listener) {
        JButton key = closeButton;
        key.addActionListener(listener);
        key = closeButton2;
        key.addActionListener(listener);
    }

    public void registerStatsButtonListener(ActionListener listener) {
        JButton key = statsButton;
        key.addActionListener(listener);
    }

    @Override
    public void onFeedback(String feedback, int attempt) {
        attempts = attempt;
        feedbackRow(feedback, attempt);
    }

    @Override
    public void onModelChanged(String word) {
        currentWord = word;
    }
}
