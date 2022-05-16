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

import java.util.ArrayList;

public class Tickets implements CommandExecutor {

    private static void OpenTicketGUI(Player p){
        // Main constructor
        Gui gui = Gui.gui()
                .title(Component.text("§cO que você deseja Reportar?"))
                .rows(3)
                .disableAllInteractions()
                .create();

        GuiItem voltar = ItemBuilder.from(Material.BARRIER).name(Component.text("§cVoltar")).asGuiItem(event -> {
            if(event.isLeftClick()){
                TicketsGUI(p);
            }
        });
        GuiItem reportplayer = ItemBuilder.from(Material.DIAMOND_SWORD).name(Component.text("§cDenunciar Jogador")).asGuiItem(event -> {
            if(event.isLeftClick()){
                p.sendMessage("Você clicou no " + event.getCurrentItem().getItemMeta().getDisplayName());
            }
        });
        GuiItem suggestion = ItemBuilder.from(Material.BEACON).name(Component.text("§cSugestão")).asGuiItem(event -> {
            if(event.isLeftClick()){
                p.sendMessage("Você clicou no " + event.getCurrentItem().getItemMeta().getDisplayName());
            }
        });
        GuiItem question = ItemBuilder.from(Material.WRITABLE_BOOK).name(Component.text("§cDúvida")).asGuiItem(event -> {
            if(event.isLeftClick()){
                p.sendMessage("Você clicou no " + event.getCurrentItem().getItemMeta().getDisplayName());
            }
        });
        GuiItem bug = ItemBuilder.from(Material.KNOWLEDGE_BOOK).name(Component.text("§cBug")).asGuiItem(event -> {
            if(event.isLeftClick()){
                p.sendMessage("Você clicou no " + event.getCurrentItem().getItemMeta().getDisplayName());
            }
        });

        gui.setItem(3, 5, voltar);
        gui.setItem(2, 2, bug);
        gui.setItem(2, 4, suggestion);
        gui.setItem(2, 6, question);
        gui.setItem(2, 8, reportplayer);
        gui.open(p);
    }
    private static void TicketsGUI(Player p){
        // Main constructor
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("§cTickets Abertos - [§f" + SQLite.getCountTicketsFromPlayer(p.getName()) + "§c]"))
                .rows(6)
                .disableAllInteractions()
                .create();
        GuiItem addTicket = ItemBuilder.from(Material.PAPER).name(Component.text("§2Abrir Ticket")).asGuiItem(event -> {
            if(event.isLeftClick()){
                OpenTicketGUI(p);
            }
        });


        SQLite.getTicketsFromPlayer(p.getName()).forEach(ticketInutil -> {

            ArrayList<Component> lore = new ArrayList<Component>();
            lore.add(Component.text(" "));
            lore.add(Component.text("§a" + ticketInutil.split("&&&")[2]));
            lore.add(Component.text(" "));
            if(ticketInutil.split("&&&")[4].equalsIgnoreCase("1")){
                lore.add(Component.text("§4STAFF: " + ticketInutil.split("&&&")[3]));
                lore.add(Component.text("§aRespondido"));
            }else{
                lore.add(Component.text("§4Não Respondido"));
            }

            GuiItem tickets = ItemBuilder.from(Material.EMERALD)
                    .name(Component.text("§eTicket #" + ticketInutil.split("&&&")[0]))
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


    @Override
    public boolean onCommand(@NotNull CommandSender snd, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if(cmd.getName().equalsIgnoreCase("tickets")){
            if(snd instanceof Player){
                Player p = (Player)snd;
                if(p.hasPermission("tickets.use")){
                    TicketsGUI(p);

                }else{
                    p.sendMessage("§cVocê não tem permissão para usar esse comando.");
                }













            }else{
                Bukkit.getConsoleSender().sendMessage("§cComando permitido somente para jogadores");
            }











        }
        return false;
    }
}
