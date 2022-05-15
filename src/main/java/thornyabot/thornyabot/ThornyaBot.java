package thornyabot.thornyabot;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import thornyabot.thornyabot.Commands.Tickets;
import thornyabot.thornyabot.Database.SQLite;
import thornyabot.thornyabot.Discord.BotManager;
import thornyabot.thornyabot.Utils.Config;

public final class ThornyaBot extends JavaPlugin {

    public static ThornyaBot pl = null;

    @Override
    public void onEnable() {
        pl = this;
        Config.carregarconfigs();
        new SQLite();
        new BotManager();
        registrarComandos();
    }

    @Override
    public void onDisable() {
        BotManager.getJDA().shutdownNow();
    }

    public void registrarComandos(){
        registrarComando("tickets", new Tickets());
    }

    public void registrarComando(String nome, CommandExecutor comando) {
        this.getCommand(nome).setExecutor(comando);
    }
}
