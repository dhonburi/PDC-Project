
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
    private final StatSaver statSaver;
    private final StreakCounter streakCounter;
    private ModelListener listener;
    private String feedback;
    private StringBuilder currentWord = new StringBuilder();
    private int attempt;

    ArrayList<String> GUESSED = new ArrayList<>();

    public Model(WordProvider wordProvider, WordValidator wordValidator, StatSaver statSaver, StreakCounter streakCounter) {
        this.wordProvider = wordProvider;
        this.wordValidator = wordValidator;
        this.statSaver = statSaver;
        this.streakCounter = streakCounter;
    }

    // GuessList Methods
    public void clearGuessList() {
        GUESSED.clear();
    }

    public boolean GuessListContains(String guess) {
        return GUESSED.contains(guess);
    }

    public void addGuessList(String guess) {
        GUESSED.add(guess);
    }

    public String getGuessList(int index) {
        return GUESSED.get(index);
    }

    // GameLogic Methods
    public String getRandomWord() {
        return wordProvider.getRandomWord();
    }

    // Word Check Methods
    public boolean isValidGuessWord(String guess) {
        return WordValidator.isValidGuessWord(guess);
    }

    public boolean isFiveChars(String guess) {
        return guess.length() == 5;
    }

    // End of Game Methods
    public void saveStats(int stats) {
        statSaver.saveStats(stats);
    }

    public void addStreak() {
        streakCounter.addStreak();
    }

    public void endStreak() {
        streakCounter.endStreak();
    }

    public void win(int attempts) {
        feedback = "VVVVV";
        attempt = attempts;
        notifyListenerFeedback();
    }

    // Get Feedback (check grey, yellow, green) Method
    public String getFeedback(String targetWord, String guess, int attempts) {
        feedback = "";
        attempt = attempts;
        boolean[] used = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == targetWord.charAt(i)) {
                feedback += "V";
                used[i] = true;
            } else {
                feedback += "_";
            }
        }

        String temp = "";
        for (int i = 0; i < 5; i++) {
            if (feedback.charAt(i) == 'V') {
                temp += "V";
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
                    temp += "?";
                } else {
                    temp += "X";
                }
            }
        }
        feedback = temp;
        notifyListenerFeedback();
        return feedback;
    }

    // Stats Methods
    public void updateStats() {
        statSaver.readFile();
        streakCounter.readFile();
        notifyListenerStats();
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

    // Typing Methods
    public void addLetter(char c) {
        if (currentWord.length() < 5) {
            currentWord.append(c);
            notifyListener();
        }
    }

    public void removeLastLetter() {
        if (currentWord.length() > 0) {
            currentWord.deleteCharAt(currentWord.length() - 1);
            notifyListener();
        }
    }

    public String getCurrentWord() {
        return currentWord.toString();
    }

    public void clearCurrentWord() {
        currentWord.setLength(0);
    }

    // ModelListener Methods for View Class
    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    private void notifyListenerFeedback() {
        if (listener != null) {
            listener.onFeedback(feedback, attempt);
        }
    }

    private void notifyListener() {
        if (listener != null) {
            listener.onModelChanged(currentWord.toString());
        }
    }

    private void notifyListenerStats() {
        if (listener != null) {
            listener.onStats(getGamesPlayed(), getWinPercentage(), getStreak(), getMaxStreak(), getGuessDist(1), getGuessDist(2), getGuessDist(3), getGuessDist(4), getGuessDist(5), getGuessDist(6));
        }
    }
}
