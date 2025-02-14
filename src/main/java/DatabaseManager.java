import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:/Users/retfirer/IdeaProjects/telegram-bot/src/main/resources/consultations.db";
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    //  System.out.println(DB_URL);
    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS consultations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "username TEXT, " +
                    "consultation_date TEXT NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addConsultation(long userId, String username, String date) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO consultations(user_id, username, consultation_date) VALUES(?,?,?)"))
        {
            pstmt.setLong(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, date);
            pstmt.executeUpdate();
            pstmt.close();
        }
    }

    public static String getUserConsultations(long userId) throws SQLException {
        StringBuilder sb = new StringBuilder();
        // System.out.println( "❌ 11 "+userId);
        try (Connection conn = DriverManager.getConnection(DB_URL);
           //  log.info("❌ 12 ", userId);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT consultation_date, created_at FROM consultations WHERE user_id = ? ORDER BY created_at DESC")


             )

        {//System.out.println( "❌ 12 "+userId);
                     //"SELECT consultation_date,created_at  FROM consultations  ORDER BY created_at DESC")) {
         //   log.info( "❌ 13 "+userId);
            pstmt.setLong(1, userId);
         //   log.info( "❌ 14 "+userId);
            ResultSet rs = pstmt.executeQuery();
            //pstmt.close();
        //    log.info( "❌ 15 "+userId);
            while (rs.next()) {
                sb.append("📅 Консультация: ").append(rs.getString("consultation_date"))
                        .append("\n⏰ Записано: ").append(rs.getString("created_at")).append("\n\n");
            }
        //    log.info( "❌ 16 "+userId);

        }
        return sb.length() > 0 ? sb.toString() : "🔍 У вас нет активных записей";
    }
}
