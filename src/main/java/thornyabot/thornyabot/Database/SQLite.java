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
                "       ticket string NOT NULL,\n" +
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

    public static void createTicket(String ticketID, String nickname, String type, String message, String... player) {

        if(player == null){
            String sqlNoPlayer = "INSERT INTO Tickets (ticket, nickname, type, message, is_answered) VALUES (?, ?, ?, ?, 0)";
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sqlNoPlayer)) {
            pstmt.setString(1, ticketID);
            pstmt.setString(2, nickname);
            pstmt.setString(3, type);
            pstmt.setString(4, message);
                pstmt.executeUpdate();
                pstmt.close();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }


        }else{
            String sqlPlayer = "INSERT INTO Tickets (ticket, nickname, type, message, is_answered, player) VALUES ('', ?, ?, ?, 0, ?)";
            try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlPlayer)) {
            pstmt.setString(1, nickname);
            pstmt.setString(2, type);
            pstmt.setString(3, message);
            pstmt.setString(4, player[0]);
                pstmt.executeUpdate();
                pstmt.close();
            }catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        }
    }

    public static void updateTicket(String id, String staff, String answer) {
        String sql = "UPDATE Tickets SET staff = ?, answer = ?, is_answered = 1 WHERE id = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, staff);
        pstmt.setString(2, answer);
        pstmt.setString(3, id);
        pstmt.executeUpdate();
            pstmt.close();
        }catch (SQLException e){

            System.out.println(e.getMessage());
        }

    }




}
