package thornyabot.thornyabot.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static thornyabot.thornyabot.ThornyaBot.pl;

public class Config {

    private static File file = null;
    private static FileConfiguration fileC = null;
    private static File translate = null;
    private static FileConfiguration fctranslate = null;

    public static void carregarconfigs(){
        criarConfig("config.yml");
        //criarConfig("staffs.yml");
    }
    private static void criarConfig(String nomedoarquivo){
        File fileVerifica = new File(pl.getDataFolder(), nomedoarquivo);
        if(!fileVerifica.exists()){ pl.saveResource(nomedoarquivo, false);}
    }
    public static FileConfiguration getFile(String nomedoarquivo){
        if(fileC == null){
            file = new File(pl.getDataFolder(), nomedoarquivo);
            fileC = YamlConfiguration.loadConfiguration(file);
        }
        return fileC;

    }

    //////////////////////////////////////////////////////////////////////////\\/\/\/\/\//\/\
    public static void reloadAll(){
        saveConfig();
        reloadConfig("config.yml");
    }
    public static void saveConfig(){

        try {
            getFile("config.yml").save(file);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static void reloadConfig(String nomedoarquivo){
        if(file == null){
            file = new File(pl.getDataFolder(), nomedoarquivo);
        }
        fileC = YamlConfiguration.loadConfiguration(file);
        if(fileC != null){
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(file);
            fileC.setDefaults(defaults);
        }
    }


}
