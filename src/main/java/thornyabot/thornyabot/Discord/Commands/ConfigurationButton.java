package thornyabot.thornyabot.Discord.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import thornyabot.thornyabot.Database.SQLite;
import thornyabot.thornyabot.Discord.BotManager;
import thornyabot.thornyabot.Utils.Log;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigurationButton extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (!event.getName().equals("devticket")) return; // make sure we handle the right command
        event.deferReply(false).queue();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("\uD83D\uDD10  Configurações do Ticket  \uD83D\uDD10")
                .setDescription("Clique para ativar ou desativar os tickets")
                .setColor(new Color(0xFFD500));
        event.getChannel().sendMessageEmbeds(eb.build())
                .setActionRow(Button.success("configbutton:ticket:open", "Abrir ticket")).queue();
        event.reply("Configuração do Ticket criada com sucesso").setEphemeral(true).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (event.getComponentId().equals("configbutton:ticket:open")) {
            BotManager.guild.getTextChannelById("977438348224393218").retrieveMessageById("977440520353763360").queue(message -> {
                if(message.getButtons().get(0).getId().equalsIgnoreCase("addticket:disabled")){
                    BotManager.guild.getTextChannelById("977438348224393218").editMessageComponentsById("977440520353763360", ActionRow.of(Button.success("addticket", "Abrir ticket"))).queue();
                }
            });
            event.getMessage().editMessageComponents(ActionRow.of(Button.danger("configbutton:ticket:close", "Fechar Ticket"))).queue();
            event.reply("Você abriu os tickets para a comunidade.").setEphemeral(true).queue();
        }

        if (event.getComponentId().equals("configbutton:ticket:close")) {
            BotManager.guild.getTextChannelById("977438348224393218").retrieveMessageById("977440520353763360").queue(message -> {
                if(message.getButtons().get(0).getId().equalsIgnoreCase("addticket")){
                    BotManager.guild.getTextChannelById("977438348224393218").editMessageComponentsById("977440520353763360", ActionRow.of(Button.danger("addticket:disabled", "Tickets fechado").asDisabled())).queue();
                }
            });
            event.getMessage().editMessageComponents(ActionRow.of(Button.success("configbutton:ticket:open", "Abrir Ticket"))).queue();
            event.reply("Você fechou os tickets para a comunidade.").setEphemeral(true).queue();
        }
    }
}
