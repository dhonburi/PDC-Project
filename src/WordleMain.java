/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author dhonl
 */

public class WordleMain {
    public static void main(String[] args) {
        WordProvider wordProvider = new WordProvider();
        WordValidator wordValidator = new WordValidator();
        StatSaver statSaver = new StatSaver();
        StreakCounter streakCounter = new StreakCounter();
        Model game = new Model(wordProvider, wordValidator, statSaver, streakCounter);
        View view = new View();
        Controller controller = new Controller(game, view);

        controller.startGame();
        
    }
}

