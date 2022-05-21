package thornyabot.thornyabot;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import thornyabot.thornyabot.Commands.Tickets;
import thornyabot.thornyabot.Commands.Verify;
import thornyabot.thornyabot.Database.Mysql;
import thornyabot.thornyabot.Database.SQLite;
import thornyabot.thornyabot.Discord.BotManager;
import thornyabot.thornyabot.Utils.Config;
import thornyabot.thornyabot.Utils.Images;
import thornyabot.thornyabot.Utils.RandomUtil;

public final class ThornyaBot extends JavaPlugin {

    public static ThornyaBot pl = null;

    @Override
    public void onEnable() {
        pl = this;
        Config.carregarconfigs();
        new Mysql();
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
        registrarComando("verificar", new Verify());
    }

    public void registrarComando(String nome, CommandExecutor comando) {
        this.getCommand(nome).setExecutor(comando);
    }

    public void registrarListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
