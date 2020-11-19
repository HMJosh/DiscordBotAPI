package me.joshb.server;

import me.joshb.server.config.LinkedAccounts;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class AccountManager {

    public AccountManager(){}
    private static AccountManager instance = new AccountManager();
    public static AccountManager getInstance(){
        return instance;
    }

    public String getCode(UUID uuid){
        return getConfig().getString(uuid.toString() + ".Code");
    }

    public String getCode(String discordID){
        for(String uuid : getConfig().getKeys(false)){
            if(getConfig().getString(uuid + ".Discord-ID").equals(discordID)) {
                return getConfig().getString(uuid + ".Code");
            }
        }
        return null;
    }

    public void setDiscordID(String discordID, String code){
        for(String uuid : getConfig().getKeys(false)){
            if(getConfig().getString(uuid + ".Code").equals(code)) {
                getConfig().set(uuid + ".Discord-ID", discordID);
                LinkedAccounts.getInstance().save();
            }
        }

    }

    public String getDiscordID(UUID uuid){
        return getConfig().getString(uuid.toString() + ".Discord-ID");
    }

    public String getUUID(String discordID){
        for(String uuid : getConfig().getKeys(false)){
            return getConfig().getString(uuid + ".Discord-ID");
        }
        return null;
    }

    private FileConfiguration getConfig(){
        return LinkedAccounts.getInstance().getConfig();
    }
}