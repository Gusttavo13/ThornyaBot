package thornyabot.thornyabot.Discord.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
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
import thornyabot.thornyabot.Utils.RandomUtil;
import thornyabot.thornyabot.Utils.TimeUnit;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;

public class Tickets extends ListenerAdapter {

    private String messageID = "";

    private String chatID = RandomUtil.randomString(8);

    public void onSelectMenuTicket(SelectMenuInteractionEvent event){
        if(event.getComponent().getId() == "ticket:type"){
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

                    Modal modalReport = Modal.create("ticketreasonplayer" + chatID, "Denúncia de Jogador")
                            .addActionRows(ActionRow.of(player), ActionRow.of(reason))
                            .build();

                    event.replyModal(modalReport).queue();
                    break;
                case "bug":
                    TextInput bug = TextInput.create("ticketchat:bug", "Descreva o que ocorreu", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Descreva o bug!")
                            .setMinLength(15)
                            .setMaxLength(1000)
                            .build();

                    Modal modalBug = Modal.create("ticketbug" + chatID, "Reportar Bugs")
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

                    Modal modalQuestion = Modal.create("ticketquestion" + chatID, "Dúvidas")
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

                    Modal modalSuggestion = Modal.create("ticketsuggestion" + chatID, "Sugestão para o Servidor")
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
        if (event.getModalId().equals("ticketreasonplayer" + chatID)) {
            String player = event.getValue("ticketchat:player").getAsString();
            String reason = event.getValue("ticketchat:reason").getAsString();

            SQLite.createTicket(chatID, event.getUser().getId(), "report-player", reason, player);

            event.reply("Obrigado pelo seu reporte!").setEphemeral(true).queue();
        }
        if (event.getModalId().equals("ticketbug" + chatID)) {
            String reason = event.getValue("ticketchat:bug").getAsString();

            SQLite.createTicket(chatID, event.getUser().getId(), "bugs", reason);

            event.reply("Obrigado pelo seu reporte!").setEphemeral(true).queue();
        }
        if (event.getModalId().equals("ticketquestion" + chatID)) {
            String reason = event.getValue("ticketchat:question").getAsString();

            SQLite.createTicket(chatID, event.getUser().getId(), "question", reason);

            event.reply("Obrigado pelo sua dúvida!").setEphemeral(true).queue();
        }

        if (event.getModalId().equals("ticketsuggestion" + chatID)) {
            String reason = event.getValue("ticketchat:suggestion").getAsString();

            SQLite.createTicket(chatID, event.getUser().getId(), "suggestion", reason);

            event.reply("Obrigado pelo sua sugestão!").setEphemeral(true).queue();
        }
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (!event.getName().equals("server")) return; // make sure we handle the right command
        event.deferReply().queue();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("✅ Sistema de tickets do Thornya ✅")
                .setDescription("Fale com o nosso suporte, abra um ticket!\n\nClique no botão para abrir um ticket.")
                .setColor(new Color(0x136300))
                .setFooter("Thornya Tickets - Bot", "https://minotar.net/avatar/robot");
        BotManager.guild.getTextChannelById("734945439006195735").sendMessageEmbeds(eb.build())
                .setActionRow(Button.primary("addticket", "Abrir ticket")).queue(message -> messageID = message.getId());
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (event.getComponentId().equals("ticketchat:close")) {
            event.getChannel().delete().queue();
        }
        if(event.getMessageId().equalsIgnoreCase(messageID)){
            if (event.getComponentId().equals("addticket")) {

                SelectMenu menu = SelectMenu.create("ticket:type")
                        .setPlaceholder("Qual o seu problema?")
                        .setMinValues(1)
                        .setMaxValues(1)
                        .addOption("Dúvidas", "questions")
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
