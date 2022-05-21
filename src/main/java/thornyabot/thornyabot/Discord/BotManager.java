package thornyabot.thornyabot.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;
import thornyabot.thornyabot.Discord.Commands.Tickets;
import thornyabot.thornyabot.Discord.Commands.Verify;
import thornyabot.thornyabot.Utils.Config;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class BotManager {
    private static JDA jda;
    public static Guild guild;

    public BotManager(){
        buildJDA();
        guild = getJDA().getGuildById("594364577618329620");
    }


    private void buildJDA() {
        if(jda == null) {
            String token = Config.getFile("config.yml").getString("token");
            try {
                this.jda = JDABuilder.createDefault(token)
                        .enableCache(CacheFlag.VOICE_STATE, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS, CacheFlag.CLIENT_STATUS)
                        .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
                        .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS)
                        .addEventListeners(new Tickets())
                        .addEventListeners(new Verify())
                        .setActivity(Activity.playing(Config.getFile("config.yml").getString("description")))
                        .build().awaitReady();
                registerCommands();
            } catch (LoginException | InterruptedException e) {
                Bukkit.getConsoleSender().sendMessage("Crashei na Construção do BOT *buildJDA()*");
                Bukkit.getConsoleSender().sendMessage(e.toString());
                e.printStackTrace();

            }
        }
    }

    private void registerCommands(){
        getJDA().upsertCommand("server", "Sistema de Tickets").queue();
    }

    public static JDA getJDA(){ return jda; }

}
