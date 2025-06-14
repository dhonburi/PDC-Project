
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author dhonl
 */
public class StatSaver {

    private static final String DB_URL = "jdbc:derby:dbdata/WordleDB;create=true";
    DatabaseManager dbManager;
    
    public StatSaver(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void saveStats(int guesses) {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            boolean win;
            if (guesses < 7) {
                win = true;
            } else {
                win = false;
            }
            ResultSet rs = stmt.executeQuery("SELECT * FROM GameStats ORDER BY id DESC FETCH FIRST ROW ONLY");
            int streak = 0;
            if (rs.next()) {
                 streak = rs.getInt("streak");
            }
            recordGuessDistribution(guesses, win, streak + 1);
            PrintWriter pw = new PrintWriter(new FileOutputStream("./resources/stats.txt", true)); // For the old stats txt file
            pw.println(guesses);
            pw.close();
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file");
        }
    }

    public void recordGuessDistribution(int guesses, boolean won, int streak) {
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement ps = conn.prepareStatement("INSERT INTO GameStats (timestamp, guesses, won, streak) VALUES (CURRENT_TIMESTAMP, ?, ?, ?)")) {

            ps.setInt(1, guesses);
            ps.setBoolean(2, won);
            ps.setInt(3, streak);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int gamesPlayed() {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM GameStats")) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public double winPercentage() {
        int wins = 0, total = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT won FROM GameStats")) {

            while (rs.next()) {
                total++;
                if (rs.getBoolean("won")) {
                    wins++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total == 0 ? 0.0 : (wins * 100.0 / total);
    }

    public int getGuessDist(int guessCount) {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM GameStats WHERE guesses = ?")) {
            ps.setInt(1, guessCount);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<Integer> getGuessDistribution() {
        ArrayList<Integer> distribution = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            distribution.add(0);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT guesses, won FROM GameStats")) {

            while (rs.next()) {
                int guesses = rs.getInt("guesses");
                boolean won = rs.getBoolean("won");
                if (!won || guesses > 6) {
                    distribution.set(6, distribution.get(6) + 1);
                } else {
                    distribution.set(guesses - 1, distribution.get(guesses - 1) + 1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distribution;
    }
}
