package thornyabot.thornyabot.Utils;

import net.dv8tion.jda.api.entities.MessageEmbed;
import thornyabot.thornyabot.Discord.BotManager;

import java.util.Arrays;

public class Log {

    private static String idlogChannel = Config.getFile("config.yml").getString("channels.log");

    public static void LogDiscordText(String message){
        BotManager.guild.getTextChannelById(idlogChannel).sendMessage(message).queue();
    }
    public static void LogDiscordEmbed(MessageEmbed... embeds){
        BotManager.guild.getTextChannelById(idlogChannel).sendMessageEmbeds(Arrays.asList(embeds)).queue();
    }

}
