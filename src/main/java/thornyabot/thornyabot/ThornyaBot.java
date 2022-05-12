package thornyabot.thornyabot;

import org.bukkit.plugin.java.JavaPlugin;
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
    }

    @Override
    public void onDisable() {
        BotManager.getJDA().shutdownNow();
    }
}
