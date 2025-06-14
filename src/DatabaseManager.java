
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:derby:dbdata/WordleDB;create=true";

    public DatabaseManager() {
        initializeDatabaseStats();
        initializeDatabaseWords();

    }

    private void initializeDatabaseStats() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE GameStats (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, timestamp TIMESTAMP, guesses INT, won BOOLEAN, streak INT)");
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDatabaseWords() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE Words (word VARCHAR(10) PRIMARY KEY, is_answer BOOLEAN)");
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }
}