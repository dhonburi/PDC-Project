
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
    DatabaseManager dbManager;

    public WordValidator(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        readFile();
    }

    @Override
    public void readFile() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {

            ResultSet check = stmt.executeQuery("SELECT COUNT(*) FROM Words WHERE is_answer = false");
            check.next();
            if (check.getInt(1) == 0) {
                FileReader s = new FileReader("./resources/words.txt"); // Word lists were found online.
                BufferedReader inStream = new BufferedReader(s);
                String line = inStream.readLine();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Words (word, is_answer) VALUES (?, ?)");
                PreparedStatement checkWord = conn.prepareStatement("SELECT COUNT(*) FROM Words WHERE word = ?");

                while (line != null) {
                    String word = line.toLowerCase().trim();
                    checkWord.setString(1, word);
                    ResultSet rs = checkWord.executeQuery();
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        ps.setString(1, word);
                        ps.setBoolean(2, false);
                        ps.addBatch();
                    }
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
}
