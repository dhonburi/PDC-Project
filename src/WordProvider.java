
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dhonl
 */
public class WordProvider implements FileInputReader {

    static ArrayList<String> ANSWERS = new ArrayList<>();
    private static final String DB_URL = "jdbc:derby:dbdata/WordleDB;create=true";
    DatabaseManager dbManager;

    public WordProvider(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        readFile();
    }

    @Override
    public void readFile() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {

            ResultSet check = stmt.executeQuery("SELECT COUNT(*) FROM Words"); // Word lists were found online.
            check.next();
            if (check.getInt(1) == 0) {
                FileReader s = new FileReader("./resources/wordanswers.txt");
                BufferedReader inStream = new BufferedReader(s);
                String line = inStream.readLine();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Words (word, is_answer) VALUES (?, ?)");

                while (line != null) {
                    ps.setString(1, line);
                    ps.setBoolean(2, true);
                    ps.addBatch();
                    ANSWERS.add(line);
                    line = inStream.readLine();
                    
                }
                ps.executeBatch();
                inStream.close();
            }

            ResultSet rs = stmt.executeQuery("SELECT word FROM Words WHERE is_answer = true");
            while (rs.next()) {
                ANSWERS.add(rs.getString("word"));
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

    private final Random random = new Random();

    public String getRandomWord() {
        return ANSWERS.get(random.nextInt(ANSWERS.size())).toLowerCase();
    }
}
