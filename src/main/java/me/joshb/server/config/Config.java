package me.joshb.server.config;

import me.joshb.server.DiscordBotAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Config {

    private String fileName = "Config";

    FileConfiguration config;

    File file;

    public Config(){ }
    private static Config instance = new Config();
    public static Config getInstance(){
        return instance;
    }

    public FileConfiguration getConfig(){
        return config;
    }

    public void save(){
        try {
            ConfigUpdater.update(DiscordBotAPI.plugin, fileName + ".yml", file, Arrays.asList());
        } catch (Exception e){
            System.out.println("COULD NOT SAVE FILE: " +fileName);
            e.printStackTrace();
        }
    }

    public void reload(){
        try {
            config.load(file);
        } catch (Exception e){
            System.out.println("COULD NOT RELOAD FILE: " + fileName);
            e.printStackTrace();
        }
    }

    public void initialize(){
        if (!DiscordBotAPI.plugin.getDataFolder().exists()) {
            DiscordBotAPI.plugin.getDataFolder().mkdir();
        }

        file = new File(DiscordBotAPI.plugin.getDataFolder(), fileName + ".yml");

        if(!file.exists()){
            file.getParentFile().mkdirs();
            copy(DiscordBotAPI.plugin.getResource(fileName + ".yml"), file);
        }
        config = YamlConfiguration.loadConfiguration(file);
        save();
        reload();
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
