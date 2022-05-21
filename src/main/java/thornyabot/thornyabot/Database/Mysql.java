package thornyabot.thornyabot.Database;

import com.sun.org.apache.xpath.internal.operations.Bool;
import thornyabot.thornyabot.Utils.Config;

import java.sql.*;

public class Mysql {

    private static Connection conn;
    private static String host, database, username, password;
    private static int port;

    public Mysql (){
        loadMySQL();
    }

    public static void createVerificationUser(String user, String code){
        loadMySQL();
        String sql = "INSERT INTO verification (DiscordID, Code) VALUES (?, ?)";

        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, code);
            ps.execute();
            ps.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public static void updateVerificationPlayer(String nickname, String UUID, String code) {
        loadMySQL();
        String sql = "UPDATE verification SET Nickname = ?, UUID = ?, Code = null WHERE Code = ?";

        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, nickname);
            ps.setString(2, UUID);
            ps.setString(3, code);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static Boolean hasCodeUser(String discordID){
        loadMySQL();
        Boolean has = false;
        String sql = "SELECT COUNT(Code) FROM verification WHERE DiscordID = ?";

        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, discordID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                has = rs.getInt(1) > 0;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return has;
    }

    public static Boolean hasCode(String code){
        loadMySQL();
        Boolean has = false;
        String sql = "SELECT COUNT(Code) FROM verification WHERE Code = ?";
        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                has = rs.getInt(1) > 0;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return has;
    }


    public static Boolean hasCodePlayer(String nickname){
        loadMySQL();
        Boolean has = false;
        String sql = "SELECT COUNT(Code) FROM verification WHERE Nickname = ?";

        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
             has = rs.getInt(1) > 0;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return has;
    }

    public static Boolean hasNickname(String nickname){
        loadMySQL();
        Boolean has = false;
        String sql = "SELECT COUNT(Nickname) FROM verification WHERE Nickname = ?";

        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                has = rs.getInt(1) > 0;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return has;
    }
    public static Boolean hasDiscordID(String discordID){
        loadMySQL();
        Boolean has = false;
        String sql = "SELECT COUNT(DiscordID) FROM verification WHERE DiscordID = ?";

        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, discordID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                has = rs.getInt(1) > 0;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return has;
    }


    public static String getDiscordIDFromNick(String nickname) {
        loadMySQL();
        String discordID = null;
        String sql = "SELECT DiscordID FROM verification WHERE Nickname = ?";

        try{
            PreparedStatement ps = getConnect().prepareStatement(sql);
            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                discordID = rs.getString(1);
            }
            ps.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return discordID;
    }
















    private static void loadMySQL(){
        if(Config.getFile("config.yml").getBoolean("mysql.enable")){
            host = Config.getFile("config.yml").getString("mysql.host");
            database = Config.getFile("config.yml").getString("mysql.database");
            username = Config.getFile("config.yml").getString("mysql.username");
            port = Config.getFile("config.yml").getInt("mysql.port");
            password = Config.getFile("config.yml").getString("mysql.password");

            try {
                if (getConnect() != null && !getConnect().isClosed()) {
                    return;
                }
                Class.forName("java.sql.Driver");
                setConnect(DriverManager.getConnection("jdbc:mysql://" +
                        host + ":" + port + "/" + database +
                        "?autoReconnect=true&useSSL=false", username, password));
                System.out.println("O Mysql ligou na porta " + port);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    public static Connection getConnect() {
        return conn;
    }

    public static void setConnect(Connection connect) {
        conn = connect;
    }

    public static void closeConnect(Connection connect) throws SQLException {
        connect.close();
    }


}
