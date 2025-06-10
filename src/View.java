
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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
    private String currentWord;
    private Thread currentThread;
    public boolean typingEnabled = true;

    private int gamesPlayed;
    private int winPercent;
    private int winStreak;
    private int maxStreak;
    private HashMap<Integer, Integer> distributions = new HashMap<>();
    private HashMap<Integer, JPanel> distBar = new HashMap<>();
    private HashMap<Integer, JLabel> distLabel = new HashMap<>();

    private HashMap<Character, JButton> keyButtons = new HashMap<>();
    private HashMap<Character, Integer> keyColours = new HashMap<>();

    private JButton tutorialButton;
    private JButton statsButton;
    private JButton closeButton;
    private JButton closeButton2;
    private JLabel popUplabel;

    private JLabel gamesPlayedNumber;
    private JLabel percentNumber;
    private JLabel streakNumber;
    private JLabel maxNumber;

    Color backroundCol = new Color(18, 18, 19);
    Color borderCol = new Color(58, 58, 60);
    Color keysCol = new Color(129, 131, 132);
    Color green = new Color(106, 170, 100);
    Color yellow = new Color(201, 180, 88);
    Color gray = new Color(58, 58, 60);
    Color popUpCol = new Color(248, 248, 248);

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

        // Initialise the Stats
        for (int i = 1; i <= 7; i++) {
            distributions.put(i, 0);
            distBar.put(i, new JPanel());
            distLabel.put(i, new JLabel());

        }

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
        tutorialButton.setPreferredSize(new Dimension(120, 30));
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

        // Center panel for Announcement / Label
        JPanel CenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        CenterPanel.setBackground(backroundCol);

        JPanel popUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1000, 0));
        popUpPanel.setBackground(backroundCol);
        popUpPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        popUplabel = new JLabel("test", SwingConstants.CENTER);
        popUplabel.setOpaque(true);
        popUplabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        popUplabel.setBackground(backroundCol);
        popUplabel.setForeground(backroundCol);
        popUplabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        popUpPanel.add(popUplabel);

        CenterPanel.add(popUpPanel, BorderLayout.CENTER);
        // Second Center panel for tile grid
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 1000, 30, 1000));
        gridPanel.setBackground(backroundCol);
        gridPanel.setPreferredSize(new Dimension(280, 400));
        tiles = new JLabel[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            rowPanel.setBackground(backroundCol);
            for (int j = 0; j < COLS; j++) {
                JLabel tile = new JLabel("", SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setFont(new Font("Helvetica", Font.BOLD, 24));
                tile.setPreferredSize(new Dimension(50, 50));
                tile.setBackground(backroundCol);
                tile.setForeground(Color.WHITE);
                tile.setBorder(BorderFactory.createLineBorder(borderCol));
                tiles[i][j] = tile;
                rowPanel.add(tile);
            }
            gridPanel.add(rowPanel);
        }

        CenterPanel.add(gridPanel, BorderLayout.CENTER);
        gamePanel.add(CenterPanel, BorderLayout.CENTER);

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
        JPanel tutorialTopPannel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tutorialTopPannel.setBackground(backroundCol);
        tutorialTopPannel.setBorder(BorderFactory.createLineBorder(borderCol));

        closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        closeButton.setFocusable(false);
        closeButton.setBackground(backroundCol);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorderPainted(false);
        tutorialTopPannel.add(closeButton);
        tutorialPanel.add(tutorialTopPannel, BorderLayout.NORTH);

        // Third panel (Stats)
        JPanel statsPanel = new JPanel(new BorderLayout());

        // Top panel for Stats
        JPanel statsTopPannel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        statsTopPannel.setBackground(backroundCol);
        statsTopPannel.setBorder(BorderFactory.createLineBorder(borderCol));

        closeButton2 = new JButton("X");
        closeButton2.setPreferredSize(new Dimension(50, 50));
        closeButton2.setFont(new Font("Helvetica", Font.BOLD, 16));
        closeButton2.setFocusable(false);
        closeButton2.setBackground(backroundCol);
        closeButton2.setForeground(Color.WHITE);
        closeButton2.setBorderPainted(false);
        statsTopPannel.add(closeButton2);
        statsPanel.add(statsTopPannel, BorderLayout.NORTH);

        // Center Panel for Stats
        JPanel statsCenterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        statsCenterPanel.setBackground(backroundCol);

        // Stats Title Panel
        JPanel statsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statsTitlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 1000));
        statsTitlePanel.setBackground(backroundCol);
        JLabel statsTitle = new JLabel("Statistics");
        statsTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        statsTitle.setForeground(Color.WHITE);
        statsTitlePanel.add(statsTitle, BorderLayout.CENTER);
        statsCenterPanel.add(statsTitlePanel, BorderLayout.CENTER);

        // Inner panel to create margin
        JPanel statsInnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsInnerPanel.setBackground(backroundCol);

        // Panel for played games stat
        JPanel playedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        playedPanel.setPreferredSize(new Dimension(100, 100));
        playedPanel.setBackground(backroundCol);

        gamesPlayedNumber = new JLabel(Integer.toString(gamesPlayed));
        gamesPlayedNumber.setFont(new Font("Helvetica", Font.PLAIN, 64));
        gamesPlayedNumber.setForeground(Color.WHITE);
        gamesPlayedNumber.setPreferredSize(new Dimension(120, 60));
        gamesPlayedNumber.setHorizontalAlignment(SwingConstants.CENTER);
        playedPanel.add(gamesPlayedNumber, BorderLayout.CENTER);

        JPanel breakLine = new JPanel();
        breakLine.setPreferredSize(new Dimension(9999, 0));
        breakLine.setOpaque(false);
        playedPanel.add(breakLine);

        JLabel gamesPlayedTitle = new JLabel("Played");
        gamesPlayedTitle.setFont(new Font("Helvetica", Font.PLAIN, 16));
        gamesPlayedTitle.setForeground(Color.WHITE);
        playedPanel.add(gamesPlayedTitle, BorderLayout.CENTER);

        statsInnerPanel.add(playedPanel, BorderLayout.CENTER);

        // Panel for win percentage stat
        JPanel percentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        percentPanel.setPreferredSize(new Dimension(100, 100));
        percentPanel.setBackground(backroundCol);

        percentNumber = new JLabel(Integer.toString(winPercent));
        percentNumber.setFont(new Font("Helvetica", Font.PLAIN, 64));
        percentNumber.setForeground(Color.WHITE);
        percentNumber.setPreferredSize(new Dimension(120, 60));
        percentNumber.setHorizontalAlignment(SwingConstants.CENTER);
        percentPanel.add(percentNumber, BorderLayout.CENTER);

        JPanel breakLine2 = new JPanel();
        breakLine2.setPreferredSize(new Dimension(9999, 0));
        breakLine2.setOpaque(false);
        percentPanel.add(breakLine2);

        JLabel percentTitle = new JLabel("Win %");
        percentTitle.setFont(new Font("Helvetica", Font.PLAIN, 16));
        percentTitle.setForeground(Color.WHITE);
        percentPanel.add(percentTitle, BorderLayout.CENTER);

        statsInnerPanel.add(percentPanel, BorderLayout.CENTER);

        // Panel for current streak stat
        JPanel streakPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        streakPanel.setPreferredSize(new Dimension(100, 100));
        streakPanel.setBackground(backroundCol);

        streakNumber = new JLabel(Integer.toString(winStreak));
        streakNumber.setFont(new Font("Helvetica", Font.PLAIN, 64));
        streakNumber.setForeground(Color.WHITE);
        streakNumber.setPreferredSize(new Dimension(120, 60));
        streakNumber.setHorizontalAlignment(SwingConstants.CENTER);
        streakPanel.add(streakNumber, BorderLayout.CENTER);

        JPanel breakLine3 = new JPanel();
        breakLine3.setPreferredSize(new Dimension(9999, 0));
        breakLine3.setOpaque(false);
        streakPanel.add(breakLine3);

        JLabel streakTitle = new JLabel("<html><center>Current<br>Streak</center></html>"); // had to use html because could not find any solutiion to warp the label, \n doesnt work.
        streakTitle.setFont(new Font("Helvetica", Font.PLAIN, 16));
        streakTitle.setForeground(Color.WHITE);
        streakPanel.add(streakTitle, BorderLayout.CENTER);

        statsInnerPanel.add(streakPanel, BorderLayout.CENTER);

        // Panel for maximum streak stat
        JPanel maxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        maxPanel.setPreferredSize(new Dimension(100, 100));
        maxPanel.setBackground(backroundCol);

        maxNumber = new JLabel(Integer.toString(maxStreak));
        maxNumber.setFont(new Font("Helvetica", Font.PLAIN, 64));
        maxNumber.setForeground(Color.WHITE);
        maxNumber.setPreferredSize(new Dimension(120, 60));
        maxNumber.setHorizontalAlignment(SwingConstants.CENTER);
        maxPanel.add(maxNumber, BorderLayout.CENTER);

        JPanel breakLine4 = new JPanel();
        breakLine4.setPreferredSize(new Dimension(9999, 0));
        breakLine4.setOpaque(false);
        maxPanel.add(breakLine4);

        JLabel maxTitle = new JLabel("<html><center>Max<br>Streak</center></html>");
        maxTitle.setFont(new Font("Helvetica", Font.PLAIN, 16));
        maxTitle.setForeground(Color.WHITE);
        maxPanel.add(maxTitle, BorderLayout.CENTER);

        statsInnerPanel.add(maxPanel, BorderLayout.CENTER);

        statsCenterPanel.add(statsInnerPanel, BorderLayout.CENTER);

        // Guess Distribution panel with margin
        JPanel guessDistPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        guessDistPanel.setPreferredSize(new Dimension(500, 1000));
        guessDistPanel.setBackground(backroundCol);

        // Title Panel
        JPanel guessDistTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        guessDistTitlePanel.setBackground(backroundCol);
        JLabel guessDistTitle = new JLabel("GUESS DISTRIBUTION");
        guessDistTitle.setFont(new Font("Helvetica", Font.BOLD, 20));
        guessDistTitle.setForeground(Color.WHITE);
        guessDistTitlePanel.add(guessDistTitle, BorderLayout.CENTER);
        guessDistPanel.add(guessDistTitlePanel, BorderLayout.CENTER);

        JPanel breakLine5 = new JPanel();
        breakLine5.setPreferredSize(new Dimension(9999, 0));
        breakLine5.setOpaque(false);
        guessDistPanel.add(breakLine5);

        // Distribution  Panel
        for (int i = 1; i < 7; i++) {
            JPanel DistPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            DistPanel.setBackground(backroundCol);
            JLabel guessDistTitleNum = new JLabel(Integer.toString(i));
            guessDistTitleNum.setFont(new Font("Helvetica", Font.BOLD, 16));
            guessDistTitleNum.setForeground(Color.WHITE);
            DistPanel.add(guessDistTitleNum, BorderLayout.CENTER);

            JPanel DistBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            distBar.put(i, DistBar);
            DistBar.setPreferredSize(new Dimension(distributions.get(i) * 10 + 30, 20));
            DistBar.setBackground(gray);

            JLabel guessDistNumber = new JLabel(Integer.toString(distributions.get(i)));
            distLabel.put(i, guessDistNumber);
            guessDistNumber.setFont(new Font("Helvetica", Font.BOLD, 16));
            guessDistNumber.setForeground(Color.WHITE);
            DistBar.add(guessDistNumber, BorderLayout.CENTER);

            DistPanel.add(DistBar, BorderLayout.CENTER);

            JPanel breakLineDist = new JPanel();
            breakLineDist.setPreferredSize(new Dimension(9999, 0));
            breakLineDist.setOpaque(false);
            DistPanel.add(breakLineDist);

            guessDistPanel.add(DistPanel, BorderLayout.CENTER);
        }

        statsCenterPanel.add(guessDistPanel, BorderLayout.CENTER);
        statsPanel.add(statsCenterPanel, BorderLayout.CENTER);

        //Add panels to cardPanel
        cardPanel.add(gamePanel, "GAME");
        cardPanel.add(statsPanel, "STATS");
        cardPanel.add(tutorialPanel, "TUTORIAL");
        

        add(cardPanel);

        setVisible(true);
    }

    // CardLayout Methods
    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
        if (name.equals("GAME")) {
            typingEnabled = true;
        } else {
            typingEnabled = false;
        }
    }

    // Tile & Keyboard Methods
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
        typingEnabled = false;
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
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        currentWord = "";
        typingEnabled = true;
    }

    public void setKeyColor(char c, Color background) {
        JButton key = keyButtons.get(Character.toUpperCase(c));
        if (key != null) {
            key.setBackground(background);
        }
    }

    // Registering Action Listeners & Key Listeners
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

    
    // Pop up Methods
    public void updatePopUp(String message) {
        popUplabel.setText(message);
        popUplabel.setBackground(popUpCol);
        popUplabel.setForeground(Color.BLACK);

        // Stop any previous animation thread
        if (currentThread != null && currentThread.isAlive()) {
            currentThread.interrupt();
        }

        // Start a new animation thread (used ChatGPT to help with)
        currentThread = new Thread(() -> {
            try {
                Thread.sleep(1500); // initial pause

                for (int i = 248; i > 18; i--) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }

                    Color fadeColor = new Color(i, i, i);
                    SwingUtilities.invokeLater(() -> popUplabel.setBackground(fadeColor));

                    Thread.sleep(1);
                }

                // Hide the popup after fading
                SwingUtilities.invokeLater(() -> {
                    popUplabel.setBackground(backroundCol);
                    popUplabel.setForeground(backroundCol);
                });

            } catch (InterruptedException e) {
                // Gracefully end the thread
                Thread.currentThread().interrupt();
            }
        });

        currentThread.start();
    }
    
    // For the final answer when incorrect
    public void holdPopUp(String message) {
        popUplabel.setText(message);
        popUplabel.setBackground(popUpCol);
        popUplabel.setForeground(Color.BLACK);
    }
    
    // Stats Page Methods
    public void updateStats() {
        gamesPlayedNumber.setText(Integer.toString(gamesPlayed));
        percentNumber.setText(Integer.toString(winPercent));
        streakNumber.setText(Integer.toString(winStreak));
        maxNumber.setText(Integer.toString(maxStreak));
        for (int i = 1; i <= 7; i++) {
            distLabel.get(i).setText(Integer.toString(distributions.get(i)));
        }
        distBarAnimation();
    }

    public void distBarAnimation() {
        // Stop any previous animation thread
        if (currentThread != null && currentThread.isAlive()) {
            currentThread.interrupt();
        }

        // Start a new animation thread (copied the popup label's code)
        currentThread = new Thread(() -> {
            try {
                for (int i = 0; i <= 50; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }

                    // ChatGpt
                    double t = i / 50.0;  // progress 0 to 1
                    double easeOut = 1 - Math.pow(1 - t, 3);
                    double multiplier = (double) (easeOut * 10);

                    for (int j = 1; j <= 7; j++) {
                        int barNum = j;
                        SwingUtilities.invokeLater(() -> {
                            distBar.get(barNum).setPreferredSize(new Dimension((int) (distributions.get(barNum) * multiplier) + 30, 20));
                            distBar.get(barNum).revalidate();
                            distBar.get(barNum).repaint();
                        });

                    }

                    Thread.sleep(10);
                }

                SwingUtilities.invokeLater(() -> {
                    for (int i = 1; i <= 7; i++) {
                        distBar.get(i).setPreferredSize(new Dimension(distributions.get(i) * 10 + 30, 20));
                    }
                });

            } catch (InterruptedException e) {
                // Gracefully end the thread
                Thread.currentThread().interrupt();
            }
        });

        currentThread.start();
    }

    // Model Listener Methods
    @Override
    public void onFeedback(String feedback, int attempt) {
        attempts = attempt;
        feedbackRow(feedback, attempt);
    }

    @Override
    public void onModelChanged(String word) {
        currentWord = word;
    }

    @Override
    public void onStats(int played, int percent, int streak, int max, int dist1, int dist2, int dist3, int dist4, int dist5, int dist6) {
        gamesPlayed = played;
        winPercent = percent;
        winStreak = streak;
        maxStreak = max;
        distributions.put(1, dist1);
        distributions.put(2, dist2);
        distributions.put(3, dist3);
        distributions.put(4, dist4);
        distributions.put(5, dist5);
        distributions.put(6, dist6);
    }

}
