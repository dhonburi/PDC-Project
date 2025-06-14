import java.sql.*;

/**
 *
 * @author dhonl
 */
public class StreakCounter {

    private static final String DB_URL = "jdbc:derby:dbdata/WordleDB;create=true";
    private int currentStreak = 0;

    public StreakCounter() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS GameStats (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, timestamp TIMESTAMP, guesses INT, won BOOLEAN, streak INT)");

        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }

    public void addStreak() {
        currentStreak++;
    }

    public void endStreak() {
        currentStreak = 0;
    }

    public int getStreak() {
        return currentStreak;
    }

    public int getMaxStreak() {
        int maxStreak = 0, tempStreak = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT won FROM GameStats ORDER BY id")) {

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

    public void saveStreak(int guesses, boolean won, int streak) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO GameStats (timestamp, guesses, won, streak) VALUES (CURRENT_TIMESTAMP, ?, ?, ?)")) {

            ps.setInt(1, guesses);
            ps.setBoolean(2, won);
            ps.setInt(3, streak);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        // Placeholder for compatibility
    }

    public int readStreak() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT streak FROM GameStats ORDER BY id DESC FETCH FIRST ROW ONLY")) {

            if (rs.next()) {
                return rs.getInt("streak");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int readMaxStreak() {
        return getMaxStreak();
    }
} 
