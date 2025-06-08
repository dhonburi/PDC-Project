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
        ConsoleIO consoleIO = new ConsoleIO();
        StatSaver statSaver = new StatSaver();
        StreakCounter streakCounter = new StreakCounter();
        WordleGame game = new WordleGame(wordProvider, wordValidator, consoleIO, statSaver, streakCounter);

        game.startGame();
        
    }
}

