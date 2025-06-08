
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class WordleGame implements Game {

    private final WordProvider wordProvider;
    private final WordValidator wordValidator;
    private final ConsoleIO consoleIO;
    private final StatSaver statSaver;
    private final StreakCounter streakCounter;

        public WordleGame(WordProvider wordProvider, WordValidator wordValidator, ConsoleIO consoleIO, StatSaver statSaver, StreakCounter streakCounter) {
        this.wordProvider = wordProvider;
        this.wordValidator = wordValidator;
        this.consoleIO = consoleIO;
        this.statSaver = statSaver;
        this.streakCounter = streakCounter;
    }

    @Override
    public void startGame() {
        String targetWord = wordProvider.getRandomWord();
        int maxTries = 6;
        ArrayList<String> GUESSED = new ArrayList<>();
        consoleIO.showMessage("~~ Welcome to Wordle! ~~");
        consoleIO.showMessage("Each guess must be a valid 5-letter word.\n");
        consoleIO.showMessage("The symbol under the tiles will show how close your guess was to the word.");
        consoleIO.showMessage("V: Green (Letter is in the word and in the correct spot.)");
        consoleIO.showMessage("?: Yellow (Letter is in the word but in the wrong spot.)");
        consoleIO.showMessage("X: Grey (Letter is not in the word in any spot.)");
        consoleIO.showMessage("Please enter your guess as a valid 5-letter word (e.g., apple, stone, grain).\n");

        for (int attempt = 1; attempt <= maxTries; attempt++) {
            String guess = consoleIO.getUserInput("Enter your 5-letter guess (" + attempt + "/" + maxTries + "): ");

            if (guess.length() != 5) {
                consoleIO.showMessage("Invalid guess! Must be exactly 5 letters.");
                attempt--;
                if (attempt > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    consoleIO.showMessage("\n" + getGuessSpaced(GUESSED.get(attempt - 1)) + "\n" + getFeedback(targetWord, GUESSED.get(attempt - 1)) + "\n");
                }
                continue;
            }

            if (!WordValidator.isValidGuessWord(guess)) {
                consoleIO.showMessage("Word not in list. Try another.");
                attempt--;
                if (attempt > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    consoleIO.showMessage("\n" + getGuessSpaced(GUESSED.get(attempt - 1)) + "\n" + getFeedback(targetWord, GUESSED.get(attempt - 1)) + "\n");
                }
                continue;
            }

            if (GUESSED.contains(guess)) {
                consoleIO.showMessage("Word already guessed. Try another.");
                attempt--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                consoleIO.showMessage("\n" + getGuessSpaced(GUESSED.get(attempt - 1)) + "\n" + getFeedback(targetWord, GUESSED.get(attempt - 1)) + "\n");

                continue;
            }

            if (guess.equals(targetWord)) {
                consoleIO.showMessage(" Congratulations! You guessed the word: " + targetWord + "\n");
                statSaver.saveStats(attempt);
                streakCounter.addStreak();
                gameOver();
                return;
            } else {
                GUESSED.add(guess);
                consoleIO.showMessage("\n" + getGuessSpaced(GUESSED.get(attempt - 1)) + "\n" + getFeedback(targetWord, GUESSED.get(attempt - 1)) + "\n");
            }
        }

        consoleIO.showMessage("Game over! The correct word was: " + targetWord + "\n");
        statSaver.saveStats(7);
        streakCounter.endStreak();
        gameOver();
    }

    private String getGuessSpaced(String guess) {
        String output = "";
        for (int i = 0; i < 5; i++) {
            output += guess.charAt(i) + " ";
        }
        return output;
    }
    
    private String getFeedback(String targetWord, String guess) {
        String feedback = "";
        boolean[] used = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == targetWord.charAt(i)) {
                feedback += "V";
                used[i] = true;
            } else {
                feedback += "_";
            }
        }

        String output = "";
        for (int i = 0; i < 5; i++) {
            if (feedback.charAt(i) == 'V') {
                output += "V ";
            } else if (feedback.charAt(i) == '_') {
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (!used[j] && guess.charAt(i) == targetWord.charAt(j)) {
                        found = true;
                        used[j] = true;
                        break;
                    }
                }
                if (found) {
                    output += "? ";
                } else {
                    output += "X ";
                }
            }
        }

        return output;
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
            input = (consoleIO.getUserInput("Do you want to play again (Y) or end system (N)?"));
            if (input.length() > 1) {
                consoleIO.showMessage("Invalid input. Only input one character.");
            } else if (input.charAt(0) == 'y') {
                consoleIO.showMessage("\nStarting new game...\n");
                startGame();
                return;
            } else if (input.charAt(0) == 'n') {
                consoleIO.showMessage("Thank you for playing!");
                return;
            } else {
                consoleIO.showMessage("Invalid input. Please try again.");
            }
        }
    }

    @Override
    public void readStats() {
        statSaver.readFile();
        consoleIO.showMessage("~~Statistics:~~\n");
        consoleIO.showMessage("Games Played: " + statSaver.gamesPlayed());
        consoleIO.showMessage("Win %: " + statSaver.winPercentage());
        consoleIO.showMessage("Current Streak: " + streakCounter.getStreak());
        consoleIO.showMessage("Max Streak: " + streakCounter.getMaxStreak());
        consoleIO.showMessage("\nGuess Distribution:");
        for (int i = 1; i < 7; i++) {
            String output = i + ": ";
            for (int j = 0; j < statSaver.getGuessDist(i); j++) {
                output += "|";
            }
            output += statSaver.getGuessDist(i);
            consoleIO.showMessage(output);
        }
        consoleIO.showMessage("");
    }

}
