package thornyabot.thornyabot.Utils;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("all")
public interface Messages {

    static String replaceChar(String text){
        return text.replace("&", "§");
    }

    //GENERAL
    String NO_PERMISSION = replaceChar(Config.getFile("config.yml").getString("messages.no_permission"));

    //Tickets
    String TICKETS_PREFIX = replaceChar(Config.getFile("config.yml").getString("messages.tickets.prefix"));
    String TICKETS_BACK_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.back_item.back_title"));
    List<String> TICKETS_BACK_LORE = Config.getFile("config.yml").getStringList("messages.tickets.back_item.back_lore");

    String TICKETS_CANCEL_MESSAGE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.cancel_message"));
    String TICKETS_CANCEL_WORD = replaceChar(Config.getFile("config.yml").getString("messages.tickets.cancel_word"));
    //Tickets_GUI
    String TICKETGUI_MENU_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.ticketgui.menu_title"));
    String TICKETGUI_ITEM_OPEN_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.ticketgui.ticketadd_item.ticketadd_title"));
    List<String> TICKETGUI_ITEM_OPEN_LORE = Config.getFile("config.yml").getStringList("messages.tickets.ticketgui.ticketadd_item.ticketadd_lore");
    //TIckets_GUI_Item
    String TICKETGUI_ITEM_NEXT_PAGE_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.ticketgui.item_next_page.next_page_title"));
    List<String> TICKETGUI_ITEM_NEXT_PAGE_LORE = Config.getFile("config.yml").getStringList("messages.tickets.ticketgui.item_next_page.next_page_lore");
    String TICKETGUI_ITEM_PREVIOUS_PAGE_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.ticketgui.item_previous_page.previous_page_title"));
    List<String> TICKETGUI_ITEM_PREVIOUS_PAGE_LORE = Config.getFile("config.yml").getStringList("messages.tickets.ticketgui.item_previous_page.previous_page_lore");
    //Ticket_Item
    String TICKETS_ITEM_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.ticketgui.tickets_item.tickets_title"));
    List<String> TICKETS_ITEM_LORE = Config.getFile("config.yml").getStringList("messages.tickets.ticketgui.tickets_item.tickets_lore");




    //Choose_Ticket_Report_GUI
    String TICKETS_CHOOSE_TYPE_MENU_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.menu_title"));
    String TICKETS_CHOOSE_TYPE_REPORT_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.report_title"));
    String TICKETS_CHOOSE_TYPE_SUGGESTION_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.suggestion_title"));
    String TICKETS_CHOOSE_TYPE_QUESTION_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.question_title"));
    String TICKETS_CHOOSE_TYPE_BUG_TITLE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.bug_title"));
    String TICKETS_REPORT_SUCESS = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.report_sucess"));
    String TICKETS_REPORT_CANCELED = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.report_canceled"));


    //player report messages
    String TICKETS_REPORT_ASK_PLAYER_NAME_MESSAGE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.ask_report_player_name_message"));
    String TICKETS_REPORT_ASK_MESSAGE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.ask_report_message"));
    String TICKETS_SUGGESTION_ASK_MESSAGE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.ask_suggestion_message"));
    String TICKETS_BUGS_ASK_MESSAGE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.ask_bugs_message"));
    String TICKETS_QUESTION_ASK_MESSAGE = replaceChar(Config.getFile("config.yml").getString("messages.tickets.choosetypereport.ask_question_message"));



}
