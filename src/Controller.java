
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        view.submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latestInput = view.inputField.getText().trim().toLowerCase();
                inputReady = true;
                synchronized (Controller.this) {
                    Controller.this.notify();
                }
            }
        });

        this.view.addEnterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latestInput = view.inputField.getText().trim().toLowerCase();
                inputReady = true;
                synchronized (Controller.this) {
                    Controller.this.notify();
                }
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

        for (attempt = 1; attempt <= maxTries; attempt++) {
            String guess = getUserInput("Enter your 5-letter guess (" + attempt + "/" + maxTries + "): ");

            if (!model.isFiveChars(guess)) {
                System.out.println("Invalid guess! Must be exactly 5 letters.");
                attempt--;
                if (attempt > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("\n" + model.getGuessList(attempt - 1) + "\n" + feedback + "\n");
                }
                continue;
            }

            if (!WordValidator.isValidGuessWord(guess)) {
                System.out.println("Word not in list. Try another.");
                attempt--;
                if (attempt > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("\n" + model.getGuessList(attempt - 1) + "\n" + feedback + "\n");
                }
                continue;
            }

            if (model.GuessListContains(guess)) {
                System.out.println("Word already guessed. Try another.");
                attempt--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("\n" + model.getGuessList(attempt - 1) + "\n" + feedback + "\n");

                continue;
            }

            if (guess.equals(targetWord)) {
                model.win(attempt);
                System.out.println(" Congratulations! You guessed the word: " + targetWord + "\n");
                model.saveStats(attempt);
                model.addStreak();
                gameOver();
                return;
            } else {
                model.addGuessList(guess);

                feedback = model.getFeedback(targetWord, model.getGuessList(attempt - 1), attempt);
                System.out.println("\n" + model.getGuessList(attempt - 1) + "\n" + feedback + "\n");
            }
        }

        System.out.println("Game over! The correct word was: " + targetWord + "\n");
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
