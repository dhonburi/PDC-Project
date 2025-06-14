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
        DatabaseManager dbManager = new DatabaseManager();
        WordProvider wordProvider = new WordProvider(dbManager);
        WordValidator wordValidator = new WordValidator(dbManager);
        StatSaver statSaver = new StatSaver(dbManager);
        StreakCounter streakCounter = new StreakCounter(dbManager);

        Model game = new Model(wordProvider, wordValidator, statSaver, streakCounter);
        View view = new View();
        Controller controller = new Controller(game, view);

        controller.startGame();

    }
}
