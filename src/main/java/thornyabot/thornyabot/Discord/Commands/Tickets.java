package thornyabot.thornyabot.Discord.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import thornyabot.thornyabot.Discord.BotManager;

import java.awt.*;

public class Tickets extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (!event.getName().equals("server")) return; // make sure we handle the right command

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("✅ Sistema de tickets do Thornya ✅")
                .setDescription("Fale com o nosso suporte, abra um ticket!\n\nReaja com \uD83D\uDCE9 para abrir um ticket.")
                .setColor(new Color(0x136300))
                .setFooter("Thornya Tickets - Bot", "https://minotar.net/avatar/robot");
        BotManager.guild.getTextChannelById("734945439006195735").sendMessageEmbeds(eb.build()).queue(message -> {
            message.addReaction("\uD83D\uDCE9").queue();
        });
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(!event.getMessageId().equalsIgnoreCase("971916167012835348")) return;

        Bukkit.getConsoleSender().sendMessage(event.getReactionEmote().getId());
        Bukkit.getConsoleSender().sendMessage(event.getReaction().getReactionEmote().getEmote().getName());
        Bukkit.getConsoleSender().sendMessage(event.getUser().getName());

        event.getReaction().removeReaction().complete();



        //criar função de chat e dos tickets

    }

}
