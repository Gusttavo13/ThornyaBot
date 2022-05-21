package thornyabot.thornyabot.Utils;

import thornyabot.thornyabot.ThornyaBot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Images {


    private static void ProcessCMD(String... cmd){
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            return;
        }
    }

    public static void createCodeImage(String code){

        String bg = ThornyaBot.pl.getDataFolder().getPath() + "\\imagem\\base.png";
        String outputCode = ThornyaBot.pl.getDataFolder().getPath() + "\\imagem\\verificationcode.png";
        String outputLetter = ThornyaBot.pl.getDataFolder().getPath() + "\\imagem\\letra.png";
        String font = "\"" + ThornyaBot.pl.getDataFolder().getPath().replace("\\", "/") + "/imagem/mine.otf\"";

        ProcessCMD("magick", "-size", "320x64", "canvas:none", "-font", font, "-pointsize", "20", "-fill", "white", "-draw", "\"text 58,48 '$code$'\"".replace("$code$", code), "\"$path_output$\"".replace("$path_output$", outputLetter));
        ProcessCMD("magick", "composite", outputLetter, bg, outputCode);

    }

}
