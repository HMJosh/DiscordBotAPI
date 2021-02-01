package me.joshb.discordbotapi.server.config;

import me.joshb.discordbotapi.server.DiscordBotAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Messages {

    private String fileName = "Messages";

    FileConfiguration config;

    File file;

    public Messages(){ }
    private static Messages instance = new Messages();
    public static Messages getInstance(){
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
            DiscordBotAPI.plugin.saveResource(fileName + ".yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        reload();
    }
}
