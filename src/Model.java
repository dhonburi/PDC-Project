
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class Model {

    private final WordProvider wordProvider;
    private final WordValidator wordValidator;
    private final ConsoleIO consoleIO;
    private final StatSaver statSaver;
    private final StreakCounter streakCounter;
    private ModelListener listener;

    ArrayList<String> GUESSED = new ArrayList<>();

    public Model(WordProvider wordProvider, WordValidator wordValidator, ConsoleIO consoleIO, StatSaver statSaver, StreakCounter streakCounter) {
        this.wordProvider = wordProvider;
        this.wordValidator = wordValidator;
        this.consoleIO = consoleIO;
        this.statSaver = statSaver;
        this.streakCounter = streakCounter;
    }

    public String getRandomWord() {
        return wordProvider.getRandomWord();
    }

    public void clearGuessList() {
        GUESSED.clear();
    }

    public boolean GuessListContains(String guess) {
        return GUESSED.contains(guess);
    }

    public void addGuessList(String guess) {
        GUESSED.add(guess);
    }

    public boolean isValidGuessWord(String guess) {
        return WordValidator.isValidGuessWord(guess);
    }
    
    public boolean isFiveChars(String guess) {
        return guess.length() == 5;
    }

    public String getGuessList(int index) {
        return GUESSED.get(index);
    }

    public void saveStats(int stats) {
        statSaver.saveStats(stats);
    }

    public void addStreak() {
        streakCounter.addStreak();
    }

    public void endStreak() {
        streakCounter.endStreak();
    }


    public String getGuessSpaced(String guess) {
        String output = "";
        for (int i = 0; i < 5; i++) {
            output += guess.charAt(i) + " ";
        }
        return output;
    }

    public String getFeedback(String targetWord, String guess) {
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

    public void updateStats() {
        statSaver.readFile();
    }

    public int getGamesPlayed() {
        return statSaver.gamesPlayed();
    }

    public int getWinPercentage() {
        return statSaver.winPercentage();
    }

    public int getStreak() {
        return streakCounter.getStreak();
    }

    public int getMaxStreak() {
        return streakCounter.getMaxStreak();
    }

    public int getGuessDist(int attempts) {
        return statSaver.getGuessDist(attempts);
    }

    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    private void notifyListener() {
        if (listener != null) {

        }
    }
}
