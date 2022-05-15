package thornyabot.thornyabot.Database;

import org.bukkit.Bukkit;
import thornyabot.thornyabot.ThornyaBot;

import java.sql.*;
import java.util.ArrayList;

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
            String sqlPlayer = "INSERT INTO Tickets (ticket, nickname, type, message, is_answered, player) VALUES (?, ?, ?, ?, 0, ?)";
            try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlPlayer)) {
            pstmt.setString(1, ticketID);
            pstmt.setString(2, nickname);
            pstmt.setString(3, type);
            pstmt.setString(4, message);
            pstmt.setString(5, player[0]);
                pstmt.executeUpdate();
                pstmt.close();
            }catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        }
    }

    public static String getUserIDByTicket(String ticketID){
        String userID = "";
        String sql = "SELECT nickname FROM Tickets WHERE ticket = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, ticketID);
            ResultSet rs  = pstmt.executeQuery();
            while(rs.next()){
                userID = rs.getString(1);
            }
            rs.close();
        }catch (SQLException e){

            System.out.println(e.getMessage());
        }
        return userID;
    }

    public static int getCountTicketsFromPlayer(String player){
        int tickets = 0;
        String sql = "SELECT count(nickname) FROM Tickets WHERE nickname = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, player);
            ResultSet rs  = pstmt.executeQuery();
            while(rs.next()){
                tickets = rs.getInt(1);
            }
            rs.close();
        }catch (SQLException e){

            System.out.println(e.getMessage());
        }
        return tickets;
    }

    public static ArrayList<String> getTicketsFromPlayer(String player){
        ArrayList<String> tickets = new ArrayList<String>();
        String sql = "SELECT ticket, nickname, type, staff, is_answered FROM Tickets WHERE nickname = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, player);
            ResultSet rs  = pstmt.executeQuery();
            while(rs.next()){
                tickets.add(rs.getString("ticket") + "&&&" +
                                rs.getString("nickname") + "&&&" +
                                rs.getString("type") + "&&&" +
                                rs.getString("staff") + "&&&" +
                                rs.getString("is_answered") + "&&&");
            }
            rs.close();
        }catch (SQLException e){

            System.out.println(e.getMessage());
        }
        return tickets;
    }

    public static void updateTicket(String ticketID, String staff, String answer) {
        String sql = "UPDATE Tickets SET staff = ?, answer = ?, is_answered = 1 WHERE ticket = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, staff);
        pstmt.setString(2, answer);
        pstmt.setString(3, ticketID);
        pstmt.executeUpdate();
            pstmt.close();
        }catch (SQLException e){

            System.out.println(e.getMessage());
        }

    }




}
