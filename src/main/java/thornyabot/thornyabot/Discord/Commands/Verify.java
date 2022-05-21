package thornyabot.thornyabot.Discord.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import thornyabot.thornyabot.Database.Mysql;
import thornyabot.thornyabot.Discord.BotManager;
import thornyabot.thornyabot.ThornyaBot;
import thornyabot.thornyabot.Utils.Images;
import thornyabot.thornyabot.Utils.RandomUtil;

import java.awt.*;
import java.io.File;
import java.util.Objects;

public class Verify extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if (!event.getName().equals("devverificar")) return;
        // make sure we handle the right command
        event.deferReply(false).queue();
        embedVerification();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (event.getComponentId().equals("queueverification")) {
            if(Mysql.hasDiscordID(event.getUser().getId())){
                event.reply("Você já possui um código no seu privado.").setEphemeral(true).queue();
            }else {
                String code = RandomUtil.randomString(12);
                Mysql.createVerificationUser(event.getMember().getId(), code);
                Images.createCodeImage(code);
                embedVerificationUser(event.getUser(), code);
                event.reply("Enviamos seu código no privado!").setEphemeral(true).queue();
            }
        }
    }


    public static void embedVerificationUser(User user, String code){
        String path = ThornyaBot.pl.getDataFolder().getPath() + "\\imagem\\verificationcode.png";
        File file = new File(path);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Thornya Space - Verificação na Federação Galactica")
                .setDescription("\nUse esse código dentro do servidor!\n\n\n`/verificar ["+ code +"]`\n\n")
                .setColor(new Color(0x36013F))
                .setFooter("Código de Verificação - Thornya Space", null)
                .setImage("attachment://verificationcode.png");
        user.openPrivateChannel().queue(embed -> {
            embed.sendFile(file, "verificationcode.png").setEmbeds(eb.build()).queue();
        });
    }

    public static void embedVerification(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("\uD83C\uDF0C Verificação da Federação Galáctica \uD83C\uDF0C")
                .setDescription("\nFaça sua verificação na federação Galáctica\n\nClique no botão para iniciar a verificação\n\nVocê receberá uma mensagem privada!")
                .setColor(new Color(0x36013F))
                .setFooter("Federação Espacial", null);
        BotManager.guild.getTextChannelById("977438266041171999").sendMessageEmbeds(eb.build())
                .setActionRow(Button.success("queueverification", "Iniciar verificação").asDisabled()).queue();
    }





}
