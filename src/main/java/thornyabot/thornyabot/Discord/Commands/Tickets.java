package thornyabot.thornyabot.Discord.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import thornyabot.thornyabot.Database.SQLite;
import thornyabot.thornyabot.Discord.BotManager;
import thornyabot.thornyabot.ThornyaBot;
import thornyabot.thornyabot.Utils.Log;
import thornyabot.thornyabot.Utils.RandomUtil;
import thornyabot.thornyabot.Utils.TimeUnit;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;

public class Tickets extends ListenerAdapter {

    private String messageID = "";

    private String chatID = "";

    @Override
    public void onSelectMenuInteraction(SelectMenuInteractionEvent event){

        if(event.getComponentId().equalsIgnoreCase("ticket:type")){
            switch (event.getValues().get(0)){
                case "report-player":
                    TextInput player = TextInput.create("ticketchat:player", "Qual jogador deseja denunciar", TextInputStyle.SHORT)
                            .setPlaceholder("Informe o nick Discord/Minecraft")
                            .setMinLength(3)
                            .setMaxLength(16) // or setRequiredRange(10, 100)
                            .build();

                    TextInput reason = TextInput.create("ticketchat:reason", "Qual o motivo da denúncia?", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Descreva o que aconteceu!")
                            .setMinLength(15)
                            .setMaxLength(1000)
                            .build();

                    Modal modalReport = Modal.create("ticketreasonplayer" + event.getChannel().getName().split("-")[1], "Denúncia de Jogador")
                            .addActionRows(ActionRow.of(player), ActionRow.of(reason))
                            .build();

                    event.replyModal(modalReport).queue();
                    break;
                case "bugs": ;
                    TextInput bug = TextInput.create("ticketchat:bugs", "Descreva o que ocorreu", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Descreva o bug!")
                            .setMinLength(15)
                            .setMaxLength(1000)
                            .build();

                    Modal modalBug = Modal.create("ticketbugs" + event.getChannel().getName().split("-")[1], "Reportar Bugs")
                            .addActionRows(ActionRow.of(bug))
                            .build();

                    event.replyModal(modalBug).queue();
                    break;
                case "questions":
                    TextInput question = TextInput.create("ticketchat:question", "Qual seria sua dúvida", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Diga qual sua dúvida!")
                            .setMinLength(15)
                            .setMaxLength(1000)
                            .build();

                    Modal modalQuestion = Modal.create("ticketquestion" + event.getChannel().getName().split("-")[1], "Dúvidas")
                            .addActionRows(ActionRow.of(question))
                            .build();

                    event.replyModal(modalQuestion).queue();
                    break;
                case "suggestion":
                    TextInput suggestion = TextInput.create("ticketchat:suggestion", "Qual sua sugestão", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Qual sua sugestão para o servidor?")
                            .setMinLength(15)
                            .setMaxLength(1000)
                            .build();

                    Modal modalSuggestion = Modal.create("ticketsuggestion" + event.getChannel().getName().split("-")[1], "Sugestão para o Servidor")
                            .addActionRows(ActionRow.of(suggestion))
                            .build();

                    event.replyModal(modalSuggestion).queue();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("ticketreasonplayer" + event.getChannel().getName().split("-")[1])) {
            String player = event.getValue("ticketchat:player").getAsString();
            String reason = event.getValue("ticketchat:reason").getAsString();

            SQLite.createTicket(event.getChannel().getName().split("-")[1], event.getUser().getId(), "Denúncia", reason, player);

            event.reply("Obrigado pelo seu reporte!").setEphemeral(true).queue();
        }
        if (event.getModalId().equals("ticketbug" + event.getChannel().getName().split("-")[1])) {
            String reason = event.getValue("ticketchat:bug").getAsString();

            SQLite.createTicket(event.getChannel().getName().split("-")[1], event.getUser().getId(), "Bugs", reason);

            event.reply("Obrigado pelo seu reporte!").setEphemeral(true).queue();
        }
        if (event.getModalId().equals("ticketquestion" + event.getChannel().getName().split("-")[1])) {
            String reason = event.getValue("ticketchat:question").getAsString();

            SQLite.createTicket(event.getChannel().getName().split("-")[1], event.getUser().getId(), "Perguntas", reason);

            event.reply("Obrigado pelo sua dúvida!").setEphemeral(true).queue();
        }

        if (event.getModalId().equals("ticketsuggestion" + event.getChannel().getName().split("-")[1])) {
            String reason = event.getValue("ticketchat:suggestion").getAsString();

            SQLite.createTicket(event.getChannel().getName().split("-")[1], event.getUser().getId(), "Sugestões", reason);

            event.reply("Obrigado pelo sua sugestão!").setEphemeral(true).queue();
        }
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (!event.getName().equals("devticket")) return; // make sure we handle the right command
        event.deferReply(false).queue();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("✅ Sistema de tickets do Thornya ✅")
                .setDescription("Fale com o nosso suporte, abra um ticket!\n\nClique no botão para abrir um ticket.")
                .setColor(new Color(0x136300))
                .setFooter("Thornya Tickets - Bot", "https://minotar.net/avatar/robot");
        BotManager.guild.getTextChannelById("977438348224393218").sendMessageEmbeds(eb.build())
                .setActionRow(Button.primary("addticket", "Abrir ticket").asDisabled()).queue(message -> messageID = message.getId());
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (event.getComponentId().equals("ticketchat:close")) {
            event.deferReply(false).queue();
            Role role = BotManager.guild.getRoleById(973727876161613854L);
            if(event.getMember().getRoles().contains(role)){
                Log.LogDiscordText("O Staff " + event.getMember().getAsMention() + " fechou o ticket " + event.getChannel().getName().split("-")[1]);
                SQLite.updateTicket(event.getChannel().getName().split("-")[1], event.getMember().getId(), "");
                event.getChannel().delete().queue();

            }else{
                event.reply("Você não pode fechar seu próprio ticket, espere um staff!").setEphemeral(true).queue();
            }
        }
        if(event.getMessageId().equalsIgnoreCase(messageID)){
            if (event.getComponentId().equals("addticket")) {

                SelectMenu menu = SelectMenu.create("ticket:type")
                        .setPlaceholder("Qual o seu problema?")
                        .setMinValues(1)
                        .setMaxValues(1)
                        .addOption("Dúvidas", "questions", "", Emoji.fromUnicode("\uD83D\uDC40"))
                        .addOption("Bugs", "bugs")
                        .addOption("Reportar Jogador", "report-player")
                        .addOption("Sugestão", "suggestion")
                        .build();

                EnumSet<Permission> allow = EnumSet.of(
                        Permission.MESSAGE_SEND,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES,
                        Permission.VIEW_CHANNEL);



                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Bem-vindo ao Suporte do Thornya")
                        .setColor(new Color(0xDEC323))
                        .setDescription("Procuramos resolver seu problema o mais rápido o possível");

                //Canal criado
                chatID = RandomUtil.randomString(12);
                BotManager.guild.getCategoryById("974126542345076836").createTextChannel("ticket-" + chatID)
                        .addPermissionOverride(event.getMember(), allow, null).queue(textChannel -> {
                            textChannel.sendMessage(event.getMember().getAsMention()).queue(message1 -> {
                                message1.replyEmbeds(eb.build()).setActionRow(Button.danger("ticketchat:close", "\uD83D\uDD12 Fechar ticket")).queue();
                            });
                            try { Thread.sleep(TimeUnit.SECOND*2);} catch(Exception e){}
                            textChannel.sendMessage("Selecione a opção.").setActionRow(menu).queue();

                });
            }
        }

    }

}
