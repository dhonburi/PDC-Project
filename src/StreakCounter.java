
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 *
 * @author dhonl
 */
public class StreakCounter {

    private static final String DB_URL = "jdbc:derby:dbdata/WordleDB;create=true";
    DatabaseManager dbManager;
    private int currentStreak = 0;

    public StreakCounter(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addStreak() {
        currentStreak++;
    }

    public void endStreak() {
        currentStreak = 0;
    }

    public int getStreak() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT streak FROM GameStats ORDER BY id DESC FETCH FIRST ROW ONLY")) {

            if (rs.next()) {
                return rs.getInt("streak");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentStreak;
    }

    public int getMaxStreak() {
        int maxStreak = 0, tempStreak = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT won FROM GameStats ORDER BY id")) {

            while (rs.next()) {
                if (rs.getBoolean("won")) {
                    tempStreak++;
                    if (tempStreak > maxStreak) {
                        maxStreak = tempStreak;
                    }
                } else {
                    tempStreak = 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maxStreak;
    }

    public void readFile() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {

            ResultSet check = stmt.executeQuery("SELECT COUNT(*) FROM GameStats");
            check.next();
            if (check.getInt(1) == 0) {
                FileReader s = new FileReader("./resources/stats.txt");
                BufferedReader inStream1 = new BufferedReader(s);
                String line = inStream1.readLine();

                PreparedStatement ps = conn.prepareStatement("INSERT INTO GameStats (timestamp, guesses, won, streak) VALUES (CURRENT_TIMESTAMP, ?, ?, ?)");
                int streak = 0;
                while (line != null) {
                    int number = Integer.parseInt(line);
                    ps.setInt(1, number);
                    if (number != 7) {
                        streak++;
                        ps.setBoolean(2, true);
                        ps.setInt(3, streak);
                    } else {
                        streak = 0;
                        ps.setBoolean(2, false);
                        ps.setInt(3, 0);
                    }

                    ps.addBatch();
                    line = inStream1.readLine();
                }
                ps.executeBatch();
                inStream1.close();
            }

            ResultSet rs = stmt.executeQuery("SELECT * FROM GameStats ORDER BY id DESC FETCH FIRST ROW ONLY");
            if (rs.next()) {
                currentStreak = rs.getInt("streak");
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
}
