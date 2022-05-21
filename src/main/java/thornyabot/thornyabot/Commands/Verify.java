package thornyabot.thornyabot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import thornyabot.thornyabot.Database.Mysql;
import thornyabot.thornyabot.Database.SQLite;
import thornyabot.thornyabot.Discord.BotManager;

import java.awt.*;
import java.util.Objects;

public class Verify implements CommandExecutor {


    public static void sendMessageInformando(String user, String nickname){
        if(Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).isOnline()){
            for(int i = 0; i <= 100; i++){
                Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).sendMessage(" ");
            }
            Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).sendMessage(" ");
            Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).sendMessage("§aSua conta foi vinculada com sucesso!");
            Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).sendMessage(" ");
            Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).sendMessage("§2Minecraft: §a" + nickname);
            Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).sendMessage("§9DiscordID: §3" + user);
            Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).sendMessage(" ");
        }

    }
    @Override
    public boolean onCommand(@NotNull CommandSender snd, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if(cmd.getName().equalsIgnoreCase("verificar") || cmd.getName().equalsIgnoreCase("verify")){
            if (snd instanceof Player) {
                Player p = (Player)snd;
                if (args.length == 1) {
                    if(!Mysql.hasNickname(p.getName())){
                        if(Mysql.hasCode(args[0])){
                            Mysql.updateVerificationPlayer(p.getName(), p.getUniqueId().toString(), args[0]);
                            sendMessageInformando(Mysql.getDiscordIDFromNick(p.getName()), p.getName());
                        }else {
                            p.sendMessage("§cCódigo Inválido!");
                        }
                    }else{
                        p.sendMessage("§4Você já é verificado!");
                    }
                }else{
                    p.sendMessage("§cUse /verificar [Código]");
                }
            }else{
                Bukkit.getConsoleSender().sendMessage("§cComando permitido somente para jogadores");
            }
        }
        return false;
    }
}
