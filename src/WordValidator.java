import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class WordValidator implements FileInputReader {

    static ArrayList<String> WORDS = new ArrayList<>();
    private static final String DB_URL = "jdbc:derby:dbdata/WordleDB;create=true";

    public WordValidator() {
        initializeDatabase();
        readFile();
        // Word list found online.
    }

    @Override
    public void readFile() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            ResultSet check = stmt.executeQuery("SELECT COUNT(*) FROM Words");
            check.next();
            if (check.getInt(1) == 0) {
                FileReader s = new FileReader("./resources/words.txt");
                BufferedReader inStream = new BufferedReader(s);
                String line = inStream.readLine();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Words (word, is_answer) VALUES (?, ?)");

                while (line != null) {
                    ps.setString(1, line);
                    ps.setBoolean(2, false);
                    ps.addBatch();
                    WORDS.add(line);
                    line = inStream.readLine();
                }
                ps.executeBatch();
                inStream.close();
            } else {
                ResultSet rs = stmt.executeQuery("SELECT word FROM Words");
                while (rs.next()) {
                    WORDS.add(rs.getString("word"));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }

    boolean isValidGuessWord(String guess) {
        return WORDS.contains(guess);
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE Words (word VARCHAR(10) PRIMARY KEY, is_answer BOOLEAN)");
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }
}
