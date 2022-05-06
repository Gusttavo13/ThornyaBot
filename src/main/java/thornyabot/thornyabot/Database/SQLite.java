package thornyabot.thornyabot.Database;

import thornyabot.thornyabot.ThornyaBot;

public class SQLite {

    private static String url = null;

    public SQLite(){
        url = "jdbc:sqlite:" + ThornyaBot.pl.getDataFolder() + "/tickets.db";
        createTableTickets();
    }

    private void createTableTickets(){
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
    }

}
