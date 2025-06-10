
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class Controller implements Game {

    private Model model;
    private View view;

    private String latestInput = "";
    private boolean inputReady = false;

    private String feedback;
    private int attempt;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        model.addListener(view);
        model.updateStats();

        view.registerKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (view.typingEnabled) {
                    char c = e.getKeyChar();
                    if (Character.isLetter(c)) {
                        c = Character.toUpperCase(c);
                        model.addLetter(c);
                        view.updateTileText();
                    } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        model.removeLastLetter();
                        view.updateTileText();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        latestInput = model.getCurrentWord();
                        inputReady = true;
                        synchronized (Controller.this) {
                            Controller.this.notify();
                        }
                    }
                }
            }
        });

        for (char c = 'A'; c <= 'Z'; c++) {
            final char keyChar = c;
            view.registerKeyButtonListener(c, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (view.typingEnabled) {
                        model.addLetter(keyChar);
                        view.updateTileText();
                    }
                }
            });
        }

        view.registerKeyButtonListener('-', new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.typingEnabled) {
                    model.removeLastLetter();
                    view.updateTileText();
                }
            }
        });

        view.registerKeyButtonListener('+', new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.typingEnabled) {
                    latestInput = model.getCurrentWord();
                    inputReady = true;
                    synchronized (Controller.this) {
                        Controller.this.notify();
                    }
                }
            }
        });

        view.registerTutorialButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showCard("TUTORIAL");
            }
        });

        view.registerCloseButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showCard("GAME");
            }
        });

        view.registerStatsButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.updateStats();
                view.updateStats();
                view.showCard("STATS");
            }
        });
    }

    public String getUserInput(String prompt) {
        System.out.print(prompt);
        inputReady = false;
        try {
            synchronized (this) {
                while (!inputReady) {
                    this.wait();
                }
            }
        } catch (InterruptedException e) {
        }
        return latestInput;
    }

    @Override
    public void startGame() {
        String targetWord = model.getRandomWord();
        int maxTries = 6;
        model.clearGuessList();
        System.out.println("~~ Welcome to Wordle! ~~");
        System.out.println("Each guess must be a valid 5-letter word.\n");
        System.out.println("The symbol under the tiles will show how close your guess was to the word.");
        System.out.println("V: Green (Letter is in the word and in the correct spot.)");
        System.out.println("?: Yellow (Letter is in the word but in the wrong spot.)");
        System.out.println("X: Grey (Letter is not in the word in any spot.)");
        System.out.println("Please enter your guess as a valid 5-letter word (e.g., apple, stone, grain).\n");

        Thread currentThread;

        for (attempt = 1; attempt <= maxTries; attempt++) {
            String guess = getUserInput("Enter your 5-letter guess (" + attempt + "/" + maxTries + "): ").toLowerCase();

            if (!model.isFiveChars(guess)) {
                view.updatePopUp("Must be exactly 5 letters.");
                attempt--;
                continue;
            } else {
            }

            if (!WordValidator.isValidGuessWord(guess)) {
                view.updatePopUp("Word not in list.");
                attempt--;
                continue;
            }

            if (model.GuessListContains(guess)) {
                view.updatePopUp("Word already guessed.");
                attempt--;
                continue;
            }

            if (guess.equals(targetWord)) {
                model.win(attempt);
                view.updatePopUp("Congratulations! You guessed the word!");
                model.saveStats(attempt);
                model.addStreak();
                gameOver();
                return;
            } else {
                model.addGuessList(guess);

                feedback = model.getFeedback(targetWord, model.getGuessList(attempt - 1), attempt);
                model.clearCurrentWord();
            }
        }

        view.updatePopUp(targetWord.toUpperCase());
        model.saveStats(7);
        model.endStreak();
        gameOver();

    }

    public void gameOver() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        readStats();

        String input = "";
        while (!input.equals("n")) {
            input = (getUserInput("Do you want to play again (Y) or end system (N)?"));
            if (input.length() > 1) {
                System.out.println("Invalid input. Only input one character.");
            } else if (input.charAt(0) == 'y') {
                System.out.println("\nStarting new game...\n");
                startGame();
                return;
            } else if (input.charAt(0) == 'n') {
                System.out.println("Thank you for playing!");
                return;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    @Override
    public void readStats() {
        model.updateStats();
        System.out.println("~~Statistics:~~\n");
        System.out.println("Games Played: " + model.getGamesPlayed());
        System.out.println("Win %: " + model.getWinPercentage());
        System.out.println("Current Streak: " + model.getStreak());
        System.out.println("Max Streak: " + model.getMaxStreak());
        System.out.println("\nGuess Distribution:");
        for (int i = 1; i < 7; i++) {
            String output = i + ": ";
            for (int j = 0; j < model.getGuessDist(i); j++) {
                output += "|";
            }
            output += model.getGuessDist(i);
            System.out.println(output);
        }
        System.out.println("");
    }
}
