package thornyabot.thornyabot.Database;

import thornyabot.thornyabot.ThornyaBot;

import java.sql.*;

public class SQLite {

    private static String url = null;

    public SQLite(){
        url = "jdbc:sqlite:" + ThornyaBot.pl.getDataFolder() + "/tickets.db";
        createTableTickets();
    }

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void createTableTickets() {
        String sql = "Create TABLE IF NOT EXISTS Tickets (\n" +
                "       id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "       nickname string NOT NULL,\n" +
                "       type string NOT NULL,\n" +
                "       message string NOT NULL,\n" +
                "       player string,\n" +
                "       staff string,\n" +
                "       answer string,\n" +
                "       is_answered integer\n" +
                ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connect().close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
