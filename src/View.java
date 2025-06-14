
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

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

    public String input;
    private int attempts;
    private String currentWord;
    public boolean typingEnabled = false;
    private Thread currentThread;

    // Stats variables
    private int gamesPlayed;
    private double winPercent;
    private int winStreak;
    private int maxStreak;
    private HashMap<Integer, Integer> distributions = new HashMap<>();
    private HashMap<Integer, JPanel> distBar = new HashMap<>();
    private HashMap<Integer, JLabel> distLabel = new HashMap<>();

    // Keyboard and tile JLabels/JButotn Hashmaps
    private JLabel[][] tiles;
    private HashMap<Character, JButton> keyButtons = new HashMap<>();
    private HashMap<Character, Integer> keyColours = new HashMap<>();

    // GUI Components
    private JPanel topPannel;
    private JButton tutorialButton;
    private JButton statsButton;
    private JButton replayButton;
    private JLabel popUplabel;

    private JButton closeButton;
    private JButton closeButton2;
    private JPanel ReplayPanel;
    private JButton replayButton2;

    private JLabel gamesPlayedNumber;
    private JLabel percentNumber;
    private JLabel streakNumber;
    private JLabel maxNumber;

    // Colours
    Color backGroundCol = new Color(18, 18, 19);
    Color borderCol = new Color(58, 58, 60);
    Color keysCol = new Color(129, 131, 132);
    Color green = new Color(106, 170, 100);
    Color yellow = new Color(201, 180, 88);
    Color gray = new Color(58, 58, 60);
    Color popUpCol = new Color(248, 248, 248);

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public View() {
        // JFrame setup
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
        // Setup the Card Panels
        setupTutorialPanel();
        setupGamePanel();
        setupStatsPanel();

        add(cardPanel);
        setVisible(true);
    }

    private void setupGamePanel() {
        // First panel (Game)
        JPanel gamePanel = new JPanel(new BorderLayout());

        // Top panel for How-to-Play & Stats Buttons
        topPannel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        topPannel.setBackground(backGroundCol);
        topPannel.setBorder(BorderFactory.createLineBorder(borderCol));

        // Tutorial Card Button
        tutorialButton = makeTopButton("How to Play", 120);
        topPannel.add(tutorialButton);

        // Stat Card Button
        statsButton = makeTopButton("Statistics", 100);
        topPannel.add(statsButton);

        // Reset game Button
        replayButton = makeTopButton("Replay", 70);

        gamePanel.add(topPannel, BorderLayout.NORTH);

        // Center panel for Announcement / Label
        JPanel CenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        CenterPanel.setBackground(backGroundCol);

        // PopUp Label for announcements ("Not enough letters", "Congratuations")
        JPanel popUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1000, 0));
        popUpPanel.setBackground(backGroundCol);
        popUpPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        popUplabel = makeStyledLabel("test", 14);
        popUplabel.setOpaque(true);
        popUplabel.setBackground(backGroundCol);
        popUplabel.setForeground(backGroundCol);
        popUplabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        popUpPanel.add(popUplabel);

        CenterPanel.add(popUpPanel, BorderLayout.CENTER);

        // Second Center panel for tile grid
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 1000, 30, 1000));
        gridPanel.setBackground(backGroundCol);
        gridPanel.setPreferredSize(new Dimension(280, 400));
        tiles = new JLabel[ROWS][COLS];

        // Creating each letter "tile" and adding it to a hashmap for future editing
        for (int i = 0; i < ROWS; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            rowPanel.setBackground(backGroundCol);
            for (int j = 0; j < COLS; j++) {
                JLabel tile = new JLabel(" ", SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setFont(new Font("Helvetica", Font.BOLD, 24));
                tile.setPreferredSize(new Dimension(50, 50));
                tile.setBackground(backGroundCol);
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
        keyboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        keyboardPanel.setBackground(backGroundCol);

        String[] rows = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "+ZXCVBNM-"
        };

        // Creating Keyboard Row by Row, using a Flow Layout, and again adding to a
        // hashmap for future editing
        for (String row : rows) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 1));
            rowPanel.setBackground(backGroundCol);
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
                key.setCursor(new Cursor(Cursor.HAND_CURSOR));
                rowPanel.add(key);
            }
            keyboardPanel.add(rowPanel);
        }

        gamePanel.add(keyboardPanel, BorderLayout.SOUTH);
        cardPanel.add(gamePanel, "GAME");
    }

    private void setupTutorialPanel() {
        // Second panel (Tutorial)
        JPanel tutorialPanel = new JPanel(new BorderLayout());

        // Top panel for Tutorial (Just close button top right)
        JPanel tutorialTopPannel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        tutorialTopPannel.setBackground(backGroundCol);

        closeButton = makeCloseButton();
        tutorialTopPannel.add(closeButton);
        tutorialPanel.add(tutorialTopPannel, BorderLayout.NORTH);

        // Center Panel for Tutorial ( Two panels allow for centering content on any
        // display size, while keeping text left aligned )
        JPanel tutorialMainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tutorialMainPanel.setBackground(backGroundCol);

        JPanel tutorialCenterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        tutorialCenterPanel.setPreferredSize(new Dimension(600, 800));
        tutorialCenterPanel.setBackground(backGroundCol);

        // Tutorial Title Label
        JLabel tutorialTitle = makeTitleLabel("How to Play", 32);
        tutorialTitle.setBorder(BorderFactory.createEmptyBorder(-10, 10, 5, 1000));
        tutorialCenterPanel.add(tutorialTitle, BorderLayout.CENTER);

        // Subtitle Label
        JLabel tutorialSubtitle = makeStyledPlainLabel("Guess the Wordle in 6 tries.", 24);
        tutorialSubtitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 1000));
        tutorialCenterPanel.add(tutorialSubtitle, BorderLayout.CENTER);

        // Bullet Label
        JLabel tutorialBullet = makeStyledPlainLabel(
                "<html><ul><li>Each guess must be a valid 5-letter word.</li></ul></html>", 17);
        tutorialBullet.setBorder(BorderFactory.createEmptyBorder(-5, -10, 0, 1000));
        tutorialCenterPanel.add(tutorialBullet, BorderLayout.CENTER);

        JLabel tutorialBullet2 = makeStyledPlainLabel(
                "<html><ul><li>The color of the tiles will change to show how close your<br>guess was to the word.</li></ul></html>",
                17);
        tutorialBullet2.setBorder(BorderFactory.createEmptyBorder(-15, -10, 0, 1000));
        tutorialCenterPanel.add(tutorialBullet2, BorderLayout.CENTER);

        JLabel tutorialBullet3 = makeStyledPlainLabel(
                "<html><ul><li>Use either the onscreen keyboard, or use your device's<br>keyboard. Enter to sumbit guess.</li></ul></html>",
                17);
        tutorialBullet3.setBorder(BorderFactory.createEmptyBorder(-15, -10, 0, 1000));
        tutorialCenterPanel.add(tutorialBullet3, BorderLayout.CENTER);

        // Examples Title
        JLabel tutorialExamplesTitle = makeStyledLabel("Examples", 20);
        tutorialExamplesTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 1000));
        tutorialCenterPanel.add(tutorialExamplesTitle, BorderLayout.CENTER);

        // Example Grid (Modified the Game Card code)
        String[] words = { "CRANE", "ABOUT", "HOUSE" };
        String[] caption = { "<html><b><em>C</em></b> is in the word and in the correct spot.</html>",
                "<html><b><em>B</em></b> is in the word but in the wrong spot.</html>",
                "<html><b><em>S</em></b> is not in the word in any spot.</html>" };
        for (int i = 0; i < 3; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            rowPanel.setBackground(backGroundCol);
            rowPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 1000));
            for (int j = 0; j < COLS; j++) {
                JLabel tile = new JLabel(String.valueOf(words[i].charAt(j)), SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setFont(new Font("Helvetica", Font.BOLD, 28));
                tile.setPreferredSize(new Dimension(40, 40));
                tile.setBackground(backGroundCol);
                if (j == 0 && i == 0) {
                    tile.setBackground(green);
                } else if (j == 1 && i == 1) {
                    tile.setBackground(yellow);
                } else if (j == 3 && i == 2) {
                    tile.setBackground(gray);
                }
                tile.setForeground(Color.WHITE);
                tile.setBorder(BorderFactory.createLineBorder(borderCol));
                rowPanel.add(tile);
            }
            tutorialCenterPanel.add(rowPanel);

            // Under Example Text
            JLabel tutorialExamplesText = makeStyledPlainLabel(caption[i], 17);
            tutorialExamplesText.setBorder(BorderFactory.createEmptyBorder(5, 25, 10, 1000));
            tutorialCenterPanel.add(tutorialExamplesText, BorderLayout.CENTER);
        }

        // Closing Label
        JLabel tutorialText = makeStyledPlainLabel(
                "<html>Press <b><em>'X'</em></b> on the top right of the screen or the <b><em>'Esc'</em></b> key <br>to close this Tutorial Menu and start playing!</html>",
                17);
        tutorialText.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 1000));
        tutorialCenterPanel.add(tutorialText, BorderLayout.CENTER);

        tutorialMainPanel.add(tutorialCenterPanel);
        tutorialPanel.add(tutorialMainPanel, BorderLayout.CENTER);

        cardPanel.add(tutorialPanel, "TUTORIAL");
    }

    private void setupStatsPanel() {
        // Third panel (Stats)
        JPanel statsPanel = new JPanel(new BorderLayout());

        // Top panel for Stats ( Just for the close button top right )
        JPanel statsTopPannel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        statsTopPannel.setBackground(backGroundCol);

        closeButton2 = makeCloseButton();
        statsTopPannel.add(closeButton2);
        statsPanel.add(statsTopPannel, BorderLayout.NORTH);

        // Center Panel for Stats
        JPanel statsMainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        statsMainPanel.setBackground(backGroundCol);

        JPanel statsCenterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        statsCenterPanel.setPreferredSize(new Dimension(600, 800));
        statsCenterPanel.setBackground(backGroundCol);

        // Stats Title Label
        JLabel statsTitle = makeTitleLabel("Statistics", 32);
        statsTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 1000));
        statsCenterPanel.add(statsTitle, BorderLayout.CENTER);

        // Inner panel to create margin
        JPanel statsInnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsInnerPanel.setBackground(backGroundCol);

        // Panel for played games stat
        gamesPlayedNumber = makeStyledPlainLabel(Integer.toString(gamesPlayed), 64);
        JPanel playedPanel = makeStatPanel(gamesPlayedNumber, "Played");
        statsInnerPanel.add(playedPanel, BorderLayout.CENTER);

        // Panel for win percentage stat
        percentNumber = makeStyledPlainLabel(String.format("%.1f", winPercent), 64);
        JPanel percentPanel = makeStatPanel(percentNumber, "Win %");
        statsInnerPanel.add(percentPanel, BorderLayout.CENTER);

        // Panel for current streak stat
        streakNumber = makeStyledPlainLabel(Integer.toString(winStreak), 64);
        JPanel streakPanel = makeStatPanel(streakNumber, "<html><center>Current<br>Streak</center></html>");
        statsInnerPanel.add(streakPanel, BorderLayout.CENTER);

        // Panel for maximum streak stat
        maxNumber = makeStyledPlainLabel(Integer.toString(winStreak), 64);
        JPanel maxPanel = makeStatPanel(maxNumber, "<html><center>Max<br>Streak</center></html>");
        statsInnerPanel.add(maxPanel, BorderLayout.CENTER);

        statsCenterPanel.add(statsInnerPanel, BorderLayout.CENTER);

        // Guess Distribution panel with margin
        JPanel guessDistPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        guessDistPanel.setPreferredSize(new Dimension(9999, 250));
        guessDistPanel.setBackground(backGroundCol);

        // Distribution Title Panel
        JPanel guessDistTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        guessDistTitlePanel.setBackground(backGroundCol);
        JLabel guessDistTitle = makeStyledLabel("GUESS DISTRIBUTION", 20);
        guessDistTitlePanel.add(guessDistTitle, BorderLayout.CENTER);
        guessDistPanel.add(guessDistTitlePanel, BorderLayout.CENTER);

        JPanel breakLine5 = new JPanel();
        breakLine5.setPreferredSize(new Dimension(9999, 0));
        breakLine5.setOpaque(false);
        guessDistPanel.add(breakLine5);

        // Distribution Panel
        for (int i = 1; i < 7; i++) {
            JPanel DistPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            DistPanel.setBackground(backGroundCol);
            JLabel guessDistTitleNum = makeStyledLabel(Integer.toString(i), 16);
            DistPanel.add(guessDistTitleNum, BorderLayout.CENTER);

            JPanel DistBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            distBar.put(i, DistBar);
            DistBar.setPreferredSize(new Dimension(distributions.get(i) * 10 + 30, 20));
            DistBar.setBackground(gray);

            JLabel guessDistNumber = makeStyledLabel(Integer.toString(distributions.get(i)), 16);
            distLabel.put(i, guessDistNumber);
            DistBar.add(guessDistNumber, BorderLayout.CENTER);

            DistPanel.add(DistBar, BorderLayout.CENTER);

            JPanel breakLineDist = new JPanel();
            breakLineDist.setPreferredSize(new Dimension(9999, 0));
            breakLineDist.setOpaque(false);
            DistPanel.add(breakLineDist);

            guessDistPanel.add(DistPanel, BorderLayout.CENTER);
        }

        statsCenterPanel.add(guessDistPanel, BorderLayout.CENTER);

        // Replay Button Panel
        ReplayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        ReplayPanel.setPreferredSize(new Dimension(500, 200));
        ReplayPanel.setBackground(backGroundCol);

        replayButton2 = new JButton("Play Again?");
        replayButton2.setFont(new Font("Helvetica", Font.BOLD, 16));
        replayButton2.setFocusable(false);
        replayButton2.setBackground(backGroundCol);
        replayButton2.setForeground(Color.WHITE);
        replayButton2.setPreferredSize(new Dimension(120, 60));
        replayButton2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        replayButton2.setBorder(BorderFactory.createLineBorder(borderCol));

        statsCenterPanel.add(ReplayPanel, BorderLayout.CENTER);

        statsMainPanel.add(statsCenterPanel, BorderLayout.CENTER);
        statsPanel.add(statsMainPanel, BorderLayout.CENTER);
        cardPanel.add(statsPanel, "STATS");
    }

    // Methods for Creating repeating Labels, Buttons, etc.
    public JLabel makeTitleLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        label.setForeground(Color.WHITE);
        return label;
    }

    public JLabel makeStyledLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Helvetica", Font.BOLD, size));
        label.setForeground(Color.WHITE);
        return label;
    }

    public JLabel makeStyledPlainLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Helvetica", Font.PLAIN, size));
        label.setForeground(Color.WHITE);
        return label;
    }

    public JButton makeTopButton(String string, int width) {
        JButton button = new JButton(string);
        button.setFont(new Font("Helvetica", Font.BOLD, 16));
        button.setFocusable(false);
        button.setBackground(backGroundCol);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(width, 30));
        button.setBorder(new MatteBorder(0, 1, 0, 1, borderCol));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public JButton makeCloseButton() {
        JButton button = new JButton("X");
        button.setPreferredSize(new Dimension(80, 50));
        button.setFont(new Font("Helvetica", Font.PLAIN, 28));
        button.setFocusable(false);
        button.setBackground(backGroundCol);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public JPanel makeStatPanel(JLabel number, String subtitle) {
        // Panel stats
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setPreferredSize(new Dimension(105, 100));
        panel.setBackground(backGroundCol);

        number.setPreferredSize(new Dimension(120, 60));
        number.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(number, BorderLayout.CENTER);

        JPanel breakLine = new JPanel();
        breakLine.setPreferredSize(new Dimension(9999, 0));
        breakLine.setOpaque(false);
        panel.add(breakLine);

        JLabel maxTitle = makeStyledPlainLabel(subtitle, 16);
        panel.add(maxTitle, BorderLayout.CENTER);

        return panel;
    }

    // CardLayout Methods
    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
        typingEnabled = name.equals("GAME");
    }

    // Game Reset / Replay Methods
    public void resetAttempts() {
        attempts = 0;
    }

    public void hideReplay() {
        topPannel.remove(replayButton);
        ReplayPanel.remove(replayButton2);
        topPannel.revalidate();
        topPannel.repaint();
    }

    public void showReplay() {
        topPannel.add(replayButton);
        ReplayPanel.add(replayButton2, BorderLayout.CENTER);
        topPannel.revalidate();
        topPannel.repaint();
    }

    // Tile Methods
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
                    setTile(attempt - 1, i, Color.RED); // only if error
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

    public void resetBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                setTile(i, j, backGroundCol);
                setTile(i, j, ' ');
            }
        }
    }

    // Keyboard Methods
    public void setKeyColor(char c, Color background) {
        JButton key = keyButtons.get(Character.toUpperCase(c));
        if (key != null) {
            key.setBackground(background);
        }
    }

    public void resetKeys() {
        for (char c = 'A'; c <= 'Z'; c++) {
            setKeyColor(c, keysCol);
            keyColours.put(c, 0);
        }
    }

    // Pop up Methods
    public void updatePopUp(String message) {
        popUplabel.setText(message);

        // Stop any previous animation thread
        if (currentThread != null && currentThread.isAlive()) {
            currentThread.interrupt();
            popUplabel.setBackground(backGroundCol);
            popUplabel.setForeground(backGroundCol);
        }

        // Start a new animation thread (used ChatGPT to help with)
        currentThread = new Thread(() -> {
            try {
                popUplabel.setBackground(popUpCol);
                popUplabel.setForeground(Color.BLACK);
                Thread.sleep(1500); // initial pause

                for (int i = 248; i > 18; i -= 2) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }

                    Color fadeColor = new Color(i, i, i);
                    SwingUtilities.invokeLater(() -> popUplabel.setBackground(fadeColor));

                    Thread.sleep(1);
                }

                // Hide the popup after fading
                SwingUtilities.invokeLater(() -> {
                    popUplabel.setBackground(backGroundCol);
                    popUplabel.setForeground(backGroundCol);
                });

            } catch (InterruptedException e) {
                // Gracefully end the thread
                Thread.currentThread().interrupt();
            }
        });

        currentThread.start();
    }

    public void holdPopUp(String message) { // For the final answer when incorrect
        popUplabel.setText(message);
        popUplabel.setBackground(popUpCol);
        popUplabel.setForeground(Color.BLACK);
    }

    public void hidePopUp() { // For when resetting game
        popUplabel.setBackground(backGroundCol);
        popUplabel.setForeground(backGroundCol);
    }

    // Stats Page Methods
    public void updateStats() {
        gamesPlayedNumber.setText(Integer.toString(gamesPlayed));
        percentNumber.setText(String.format("%.1f", winPercent));
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
            popUplabel.setBackground(backGroundCol);
            popUplabel.setForeground(backGroundCol);
        }
        int largestDist = 0;
        for (int i = 1; i <= 7; i++) {
            if (largestDist < distributions.get(i)) {
                largestDist = distributions.get(i);
            }
        }
        if (largestDist == 0) {
            largestDist = 1;
        }
        int finalMultiplier = 300 / largestDist;

        // Start a new animation thread (copied the popup label's code)
        currentThread = new Thread(() -> {
            try {
                for (int i = 0; i <= 50; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }

                    // ChatGpt used to make the animation
                    double t = i / 50.0; // progress 0 to 1
                    double easeOut = 1 - Math.pow(1 - t, 3);
                    double multiplier = (double) (easeOut * finalMultiplier);

                    for (int j = 1; j <= 7; j++) {
                        int barNum = j;
                        SwingUtilities.invokeLater(() -> {
                            distBar.get(barNum).setPreferredSize(
                                    new Dimension((int) (distributions.get(barNum) * multiplier) + 30, 20));
                            distBar.get(barNum).revalidate();
                            distBar.get(barNum).repaint();
                        });

                    }

                    Thread.sleep(10);
                }

                SwingUtilities.invokeLater(() -> {
                    for (int i = 1; i <= 7; i++) {
                        distBar.get(i).setPreferredSize(new Dimension(distributions.get(i) * finalMultiplier + 30, 20));
                    }
                });

            } catch (InterruptedException e) {
                // Gracefully end the thread
                Thread.currentThread().interrupt();
            }
        });

        currentThread.start();
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

    public void registerReplayButtonListener(ActionListener listener) {
        JButton key = replayButton;
        key.addActionListener(listener);
        key = replayButton2;
        key.addActionListener(listener);
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
    public void onStats(int played, double percent, int streak, int max, int dist1, int dist2, int dist3, int dist4,
            int dist5, int dist6) {
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
