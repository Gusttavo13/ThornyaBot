package thornyabot.thornyabot.Commands;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import thornyabot.thornyabot.Database.SQLite;
import thornyabot.thornyabot.ThornyaBot;
import thornyabot.thornyabot.Utils.ChatManager.ChatInput;
import thornyabot.thornyabot.Utils.Config;
import thornyabot.thornyabot.Utils.Messages;
import thornyabot.thornyabot.Utils.RandomUtil;

import java.util.ArrayList;
import java.util.Objects;

public class Tickets implements CommandExecutor {

    /**
     * Tela inicial dos Tickets
     *
     * @param p Player
     *
     */
    private static void TicketsGUI(Player p){
        // Main constructor
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(Messages.TICKETGUI_MENU_TITLE.replace("$count$", String.valueOf(SQLite.getCountTicketsFromPlayer(p.getName())))))
                .rows(6)
                .disableAllInteractions()
                .create();
        GuiItem addTicket = ItemBuilder.from(Material.PAPER).name(Component.text("§2Abrir Ticket")).asGuiItem(event -> {
            if(event.isLeftClick()){
                ChooseTypeReport(p);
            }
        });


        SQLite.getTicketsFromPlayer(p.getName()).forEach(ticketInutil -> {

            ArrayList<Component> lore = new ArrayList<Component>();

            Messages.TICKETS_ITEM_LORE.forEach(loreConfig -> {
                lore.add(Component.text(loreConfig
                        .replaceAll("&", "§")
                        .replace("{type}", ticketInutil.split("&&&")[2])
                        .replace("{staff_answer}",
                                Objects.requireNonNull(ticketInutil.split("&&&")[4].equalsIgnoreCase("1") ?
                                        Config.getFile("config.yml").getString("messages.tickets.ticketgui.tickets_item.tickets_staff_answer")
                                                .replaceAll("&", "§")
                                                .replace("$staff$", ticketInutil.split("&&&")[3]) : ""))
                        .replace("{answer}",
                                Objects.requireNonNull(ticketInutil.split("&&&")[4].equalsIgnoreCase("1") ?
                                        Config.getFile("config.yml").getString("messages.tickets.ticketgui.tickets_item.tickets_answer")
                                                .replace("&", "§")
                                        :
                                        Config.getFile("config.yml").getString("messages.tickets.ticketgui.tickets_item.tickets_no_answer")
                                                .replace("&", "§")
                                )
                        )
                ));

            });
            GuiItem tickets = ItemBuilder.from(Material.EMERALD)
                    .name(Component.text(Messages.TICKETS_ITEM_TITLE.replace("{ticketid}", ticketInutil.split("&&&")[0])))
                    .lore(lore)
                    .asGuiItem(event -> {
                        if(event.isLeftClick()){
                            p.sendMessage("Você clicou no " + event.getCurrentItem().getItemMeta().getDisplayName());
                        }
                    });

            lore.clear();
            gui.addItem(tickets);
        });
        gui.setItem(6, 5, addTicket);
        gui.open(p);
    }

    /**
     * Escolher o tipo de ticket
     *
     * @param p Player
     *
     */
    private static void ChooseTypeReport(Player p){
        // Main constructor
        Gui gui = Gui.gui()
                .title(Component.text(Messages.TICKETS_CHOOSE_TYPE_MENU_TITLE))
                .rows(3)
                .disableAllInteractions()
                .create();

        GuiItem voltar = ItemBuilder.from(Material.BARRIER).name(Component.text(Messages.TICKETS_BACK_TITLE)).asGuiItem(event -> {
            if(event.isLeftClick()){
                TicketsGUI(p);
            }
        });
        GuiItem reportplayer = ItemBuilder.from(Material.DIAMOND_SWORD).name(Component.text(Messages.TICKETS_CHOOSE_TYPE_REPORT_TITLE)).asGuiItem(event -> {
            if(event.isLeftClick()){
                gui.close(p);
                p.sendMessage(Messages.TICKETS_PREFIX + Messages.TICKETS_REPORT_ASK_PLAYER_NAME_MESSAGE);
                p.sendMessage(Messages.TICKETS_CANCEL_MESSAGE);
                ChatInput.waitForPlayer(ThornyaBot.pl, p, callbackPlayer ->{
                    if(callbackPlayer.equalsIgnoreCase("cancel") || callbackPlayer.equalsIgnoreCase(Messages.TICKETS_CANCEL_WORD)){
                        p.sendMessage(Messages.TICKETS_REPORT_CANCELED);
                        return;
                    }
                    p.sendMessage(Messages.TICKETS_REPORT_ASK_MESSAGE);
                    p.sendMessage(Messages.TICKETS_CANCEL_MESSAGE);
                    ChatInput.waitForPlayer(ThornyaBot.pl, p, callbackMessage ->{
                        if(callbackMessage.equalsIgnoreCase("cancel") || callbackMessage.equalsIgnoreCase(Messages.TICKETS_CANCEL_WORD)){
                            return;
                        }
                        String ticketid = RandomUtil.randomString(12);
                        p.sendMessage(Messages.TICKETS_REPORT_SUCESS.replace("{ticketid}", ticketid));
                        SQLite.createTicket(ticketid, p.getName(), "Denúncia", callbackMessage, callbackPlayer);
                    });


                });
            }
        });
        GuiItem suggestion = ItemBuilder.from(Material.BEACON).name(Component.text(Messages.TICKETS_CHOOSE_TYPE_SUGGESTION_TITLE)).asGuiItem(event -> {
            if(event.isLeftClick()){
                gui.close(p);
                p.sendMessage(Messages.TICKETS_PREFIX + Messages.TICKETS_SUGGESTION_ASK_MESSAGE);
                p.sendMessage(Messages.TICKETS_CANCEL_MESSAGE);
                ChatInput.waitForPlayer(ThornyaBot.pl, p, callback -> {
                    if (callback.equalsIgnoreCase("cancel") || callback.equalsIgnoreCase(Messages.TICKETS_CANCEL_WORD)) {
                        return;
                    }
                    String ticketid = RandomUtil.randomString(12);
                    p.sendMessage(Messages.TICKETS_REPORT_SUCESS.replace("{ticketid}", ticketid));
                    SQLite.createTicket(ticketid, p.getName(), "Sugestões", callback);
                });
            }
        });
        GuiItem question = ItemBuilder.from(Material.WRITABLE_BOOK).name(Component.text(Messages.TICKETS_CHOOSE_TYPE_QUESTION_TITLE)).asGuiItem(event -> {
            if(event.isLeftClick()){
                gui.close(p);
                p.sendMessage(Messages.TICKETS_PREFIX + Messages.TICKETS_QUESTION_ASK_MESSAGE);
                p.sendMessage(Messages.TICKETS_CANCEL_MESSAGE);
                ChatInput.waitForPlayer(ThornyaBot.pl, p, callback -> {
                    if (callback.equalsIgnoreCase("cancel") || callback.equalsIgnoreCase(Messages.TICKETS_CANCEL_WORD)) {
                        return;
                    }
                    String ticketid = RandomUtil.randomString(12);
                    p.sendMessage(Messages.TICKETS_REPORT_SUCESS.replace("{ticketid}", ticketid));
                    SQLite.createTicket(ticketid, p.getName(), "Sugestões", callback);
                });
            }
        });
        GuiItem bug = ItemBuilder.from(Material.KNOWLEDGE_BOOK).name(Component.text(Messages.TICKETS_CHOOSE_TYPE_BUG_TITLE)).asGuiItem(event -> {
            if(event.isLeftClick()){
                gui.close(p);
                p.sendMessage(Messages.TICKETS_PREFIX + Messages.TICKETS_BUGS_ASK_MESSAGE);
                p.sendMessage(Messages.TICKETS_CANCEL_MESSAGE);
                ChatInput.waitForPlayer(ThornyaBot.pl, p, callback -> {
                    if (callback.equalsIgnoreCase("cancel") || callback.equalsIgnoreCase(Messages.TICKETS_CANCEL_WORD)) {
                        return;
                    }
                    String ticketid = RandomUtil.randomString(12);
                    p.sendMessage(Messages.TICKETS_REPORT_SUCESS.replace("{ticketid}", ticketid));
                    SQLite.createTicket(ticketid, p.getName(), "Sugestões", callback);
                });
            }
        });

        gui.setItem(3, 5, voltar);
        gui.setItem(2, 2, bug);
        gui.setItem(2, 4, suggestion);
        gui.setItem(2, 6, question);
        gui.setItem(2, 8, reportplayer);
        gui.open(p);
    }



    @Override
    public boolean onCommand(@NotNull CommandSender snd, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if(cmd.getName().equalsIgnoreCase("tickets")){
            if(snd instanceof Player){
                Player p = (Player)snd;
                if(p.hasPermission("tickets.use")){
                    TicketsGUI(p);

                }else{
                    p.sendMessage(Messages.NO_PERMISSION);
                }
            }else{
                Bukkit.getConsoleSender().sendMessage("§cComando permitido somente para jogadores");
            }











        }
        return false;
    }
}
