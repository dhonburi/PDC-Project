/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dhonl
 */
public class GameLoop implements Runnable {

    Model model;
    View view;
    Controller controller;
    
    String targetWord;

    public GameLoop(Model model, View view, Controller controller) {
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void run() {
        targetWord = model.getRandomWord();

        model.clearGuessList();
        model.clearCurrentWord();
        view.hideReplay();
        view.resetAttempts();
        view.resetBoard();
        view.resetKeys();
        view.hidePopUp();
        int maxTries = 6;
        for (int attempt = 1; attempt <= maxTries; attempt++) {
            String guess = controller.getUserInput().toLowerCase();

            if (!model.isFiveChars(guess)) {
                view.updatePopUp("Not enough letters");
                attempt--;
                continue;
            } else {
            }

            if (!model.isValidGuessWord(guess)) {
                view.updatePopUp("Not in word list");
                attempt--;
                continue;
            }

            if (model.GuessListContains(guess)) {
                view.updatePopUp("Word already guessed");
                attempt--;
                continue;
            }

            if (guess.equals(targetWord)) {
                model.win(attempt);
                view.updatePopUp("Congratulations! You win!");
                model.saveStats(attempt);
                model.addStreak();
                controller.gameOver();
                return;
            } else {
                model.addGuessList(guess);

                model.getFeedback(targetWord, model.getGuessList(attempt - 1), attempt);
                model.clearCurrentWord();
            }
        }

        view.holdPopUp(targetWord.toUpperCase());
        model.saveStats(7);
        model.endStreak();
        controller.gameOver();
    }

}
