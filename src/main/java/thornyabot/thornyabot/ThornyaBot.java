package thornyabot.thornyabot;

import org.bukkit.plugin.java.JavaPlugin;
import thornyabot.thornyabot.Utils.Config;

public final class ThornyaBot extends JavaPlugin {

    public static ThornyaBot pl = null;

    @Override
    public void onEnable() {
        pl = this;
        Config.carregarconfigs();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
