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

import java.util.*;

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
                            if(p.hasPermission("tickets.use")){
                                p.sendMessage("§a+=============================+");
                                if(event.getCurrentItem() != null){
                                    String[] ticketData = SQLite.getTicketByID(event.getCurrentItem().getItemMeta().getDisplayName().split("#")[1]).split("&&&");
                                    p.sendMessage(" §a| " + "§eSeu TicketID: §6" + ticketData[0]);
                                    if(ticketData[1].equalsIgnoreCase("Denúncia")) p.sendMessage(" §a| " + "§eJogador denunciado: §c" + ticketData[3]);
                                    p.sendMessage(" §a| ");
                                    p.sendMessage(" §a| " + "§aMensagem: §f" + ticketData[2]);
                                    if(ticketData[6].equalsIgnoreCase("1")){
                                        p.sendMessage("§a+=============================+");
                                        if(ticketData[5].equalsIgnoreCase("null")){
                                            p.sendMessage(" §a| " + "§eVisualizado por §4" + ticketData[4]);
                                        }else{
                                            p.sendMessage(" §a| " + "§eRespondido por §4" + ticketData[4]);
                                            p.sendMessage(" §a| " + "§eResposta: §f" + ticketData[5]);
                                        }
                                    }
                                }
                                p.sendMessage("§a+=============================+");
                                gui.close(p);
                            }
                        }
                    });

            lore.clear();
            gui.addItem(tickets);
        });
        GuiItem empty_panel = ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem();
        GuiItem previous_item = ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE).name(Component.text(Messages.TICKETGUI_ITEM_PREVIOUS_PAGE_TITLE)).asGuiItem(event -> {
            gui.previous();
            if (gui.getCurrentPageNum() == 1) {
                gui.updatePageItem(6, 3, empty_panel);
            }
        });
        GuiItem next_item = ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE).name(Component.text(Messages.TICKETGUI_ITEM_NEXT_PAGE_TITLE)).asGuiItem(event -> {
            gui.next();
            if (gui.getCurrentPageNum() > 1) {
                gui.updatePageItem(6, 3, previous_item);
            }
        });

        gui.setItem(6,1, empty_panel);
        gui.setItem(6,2, empty_panel);
        gui.setItem(6, 3, (gui.getCurrentPageNum() > 1 ? previous_item : empty_panel));//pre
        gui.setItem(6,4, empty_panel);
        gui.setItem(6,6, empty_panel);
        gui.setItem(6,7, (SQLite.getCountTicketsFromPlayer(p.getName()) > 45? next_item : empty_panel)); //next
        gui.setItem(6,8, empty_panel);
        gui.setItem(6,9, empty_panel);
        gui.setItem(6, 5, addTicket);
        gui.open(p);
    }
    /**
     * Criar categoria
     *
     *
     * @param category_name String
     * @param p Player
     * @param gui Gui
     * @param material Material
     * @param item_title String
     * @param messages List<String>
     * @param messages2 List<String> Optional
     * @param success_message String
     * @param cancel_message String
     * @param discord_chat Boolean
     *
     * @return GuiItem
     */
    private static GuiItem CreateCategory(String category_name, Player p, Gui gui, Material material, String item_title, List<String> messages, List<String> messages2,String success_message, String cancel_message, Boolean discord_chat){
        GuiItem item = ItemBuilder.from(material).name(Component.text(item_title)).asGuiItem(event -> {
            if(event.isLeftClick()){
                gui.close(p);
                messages.forEach(message ->{
                    p.sendMessage(message);
                });
                ChatInput.waitForPlayer(ThornyaBot.pl, p, callback1 ->{
                    if(callback1.equalsIgnoreCase("cancel") || callback1.equalsIgnoreCase(Messages.TICKETS_CANCEL_WORD)){
                        p.sendMessage(cancel_message);
                        return;
                    }
                    if(messages2.isEmpty()){
                        String ticketid = RandomUtil.randomString(12);
                        p.sendMessage(success_message.replace("{ticketid}", ticketid));
                        SQLite.createTicket(ticketid, p.getName(), category_name, callback1);
                        if(discord_chat){
                            //criar aqui o sistema do canal
                        }
                    }
                    if(messages2.size() > 0) {
                        messages2.forEach(message -> {
                            p.sendMessage(message);
                        });
                        ChatInput.waitForPlayer(ThornyaBot.pl, p, callback2 -> {
                            if (callback2.equalsIgnoreCase("cancel") || callback2.equalsIgnoreCase(Messages.TICKETS_CANCEL_WORD)) {
                                return;
                            }
                            String ticketid = RandomUtil.randomString(12);
                            p.sendMessage(success_message.replace("{ticketid}", ticketid));
                            SQLite.createTicket(ticketid, p.getName(), category_name, callback2, callback1);
                            if (discord_chat) {
                                //criar aqui o sistema do canal
                            }
                        });
                    }
                });
            }
        });
        return item;
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

        gui.setItem(3, 5, voltar);

        //Categoria de Bugs

        gui.setItem(2, 2, CreateCategory("Bugs", p, gui, Material.EMERALD,
                Messages.TICKETS_CHOOSE_TYPE_BUG_TITLE,
                Arrays.asList(Messages.TICKETS_PREFIX + Messages.TICKETS_BUGS_ASK_MESSAGE, Messages.TICKETS_CANCEL_MESSAGE),
                Collections.EMPTY_LIST, Messages.TICKETS_REPORT_SUCESS, Messages.TICKETS_REPORT_CANCELED, true));

        //Categoria de Sugestões

        gui.setItem(2, 4, CreateCategory("Sugestões", p, gui, Material.BEACON,
                Messages.TICKETS_CHOOSE_TYPE_SUGGESTION_TITLE,
                Arrays.asList(Messages.TICKETS_PREFIX + Messages.TICKETS_SUGGESTION_ASK_MESSAGE, Messages.TICKETS_CANCEL_MESSAGE),
                Collections.EMPTY_LIST, Messages.TICKETS_REPORT_SUCESS, Messages.TICKETS_REPORT_CANCELED, false));
        //Categoria de Dúvida

        gui.setItem(2, 6, CreateCategory("Dúvida", p, gui, Material.WRITABLE_BOOK,
                Messages.TICKETS_CHOOSE_TYPE_QUESTION_TITLE,
                Arrays.asList(Messages.TICKETS_PREFIX + Messages.TICKETS_QUESTION_ASK_MESSAGE, Messages.TICKETS_CANCEL_MESSAGE),
                Collections.EMPTY_LIST, Messages.TICKETS_REPORT_SUCESS, Messages.TICKETS_REPORT_CANCELED, true));

        //Categoria de Denúncia

        gui.setItem(2, 8, CreateCategory("Denúncia", p, gui, Material.DIAMOND,
                Messages.TICKETS_CHOOSE_TYPE_REPORT_TITLE,
                Arrays.asList(Messages.TICKETS_PREFIX + Messages.TICKETS_REPORT_ASK_PLAYER_NAME_MESSAGE, Messages.TICKETS_CANCEL_MESSAGE),
                Arrays.asList(Messages.TICKETS_REPORT_ASK_MESSAGE, Messages.TICKETS_CANCEL_MESSAGE),
                Messages.TICKETS_REPORT_SUCESS, Messages.TICKETS_REPORT_CANCELED, true));

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
